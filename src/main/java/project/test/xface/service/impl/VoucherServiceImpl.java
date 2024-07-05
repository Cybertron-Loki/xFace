package project.test.xface.service.impl;

import cn.hutool.json.JSONUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.GroupVoucher;
import project.test.xface.entity.pojo.SeckillVoucher;
import project.test.xface.entity.pojo.Voucher;
import project.test.xface.entity.pojo.VoucherOrder;
import project.test.xface.mapper.VoucherMapper;
import project.test.xface.service.VoucherService;
import project.test.xface.utils.RedisWorker;
import project.test.xface.utils.SimpleRedisLock;
import project.test.xface.utils.UserHolder;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static project.test.xface.common.RedisConstant.*;
import static project.test.xface.rabbit.VoucherOrderConfig.VOUCHERORDER_EXCHANGE;
import static project.test.xface.rabbit.VoucherOrderConfig.VOUCHERORDER_QUEUE;


@Service
public class VoucherServiceImpl implements VoucherService {

    @Autowired
    private VoucherMapper voucherMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedisWorker redisWorker;
    @Resource
    private RabbitTemplate rabbitTemplate;


    @Override
    public Result addVoucher(Voucher voucher) {
        if (voucher == null) return Result.fail("不能为空");
        Long id = redisWorker.nextId(Voucher_ID_KEY);
        voucher.setId(id);
        boolean isSuccess = voucherMapper.addVoucher(voucher);
        if (isSuccess) {
            return Result.success();
        } else {
            return Result.fail("新增失败");
        }
    }

    @Transactional
    @Override
    public Result addSeckillVoucher(Voucher voucher) {
        if (voucher == null) return Result.fail("不能为空");
        Long id = redisWorker.nextId(Voucher_ID_KEY);
        voucher.setId(id);
        boolean isSuccess = voucherMapper.addVoucher(voucher);
        if (isSuccess) {
            SeckillVoucher seckillVoucher = new SeckillVoucher();
            seckillVoucher.setVoucherId(voucher.getId());
            seckillVoucher.setStock(voucher.getStock());
            seckillVoucher.setBeginTime(voucher.getBeginTime());
            seckillVoucher.setEndTime(voucher.getEndTime());
            isSuccess = voucherMapper.addSeckillVoucher(seckillVoucher);
            if (isSuccess){
                //存到缓存
                stringRedisTemplate.opsForValue().set(CACHE_SECKILLVOUCHER+voucher.getId(),voucher.getStock().toString());
                return Result.success();
        }}
        return Result.fail("新增失败");
    }

    @Override
    public Result addGroupVoucher(Voucher voucher) {
        if (voucher == null) return Result.fail("不能为空");
        Long id = redisWorker.nextId(Voucher_ID_KEY);
        voucher.setId(id);
        boolean isSuccess = voucherMapper.addVoucher(voucher);
        if (isSuccess) {
            GroupVoucher groupVoucher = new GroupVoucher();
            groupVoucher.setVoucherId(voucher.getId());
            groupVoucher.setStock(voucher.getStock());
            groupVoucher.setBeginTime(voucher.getBeginTime());
            groupVoucher.setEndTime(voucher.getEndTime());
            groupVoucher.setCreatorId(voucher.getCreatorId());
            isSuccess = voucherMapper.addGroupVoucher(groupVoucher);
            if (isSuccess){
                //存到缓存
                stringRedisTemplate.opsForValue().set(CACHE_GROUPVOUCHER+voucher.getId(),voucher.getStock().toString());
                return Result.success();
            }}
        return Result.fail("新增失败");
    }

    @Override
    public Result listVouchers(Long shopId) {
        return null;
    }

    @Override
    public Result listGroupVouchers(Long groupId) {
        return null;
    }

    /**
     * 异步实现秒杀
     */

//    private VoucherService proxy;//定义代理对象，提前定义后面会用到
    //注入脚本
    private static final DefaultRedisScript<Long> SECKILL_VOUCHER;
    static {
        SECKILL_VOUCHER = new DefaultRedisScript<>();
        SECKILL_VOUCHER.setLocation(new ClassPathResource("seckill.lua"));
        SECKILL_VOUCHER.setResultType(Long.class);
    }
    //创建阻塞队列  这个阻塞队列特点：当一个线程尝试从队列获取元素的时候，如果没有元素该线程阻塞，直到队列中有元素才会被唤醒获取
//    private BlockingQueue<VoucherOrder> orderTasks = new ArrayBlockingQueue<>(1024 * 1024);//初始化阻塞队列的大小



    @Override
    public Result seckillVoucher(Long voucherId) { //使用lua脚本
        //获取用户
        Long userId = UserHolder.getUser().getId();
        //1.执行lua脚本
        Long result = stringRedisTemplate.execute(
                SECKILL_VOUCHER,
                Collections.emptyList(), //这里是key数组，没有key，就传的一个空集合
                voucherId.toString(), userId.toString()
        );
        //2.判断结果是0
        int r = result.intValue();//Long型转为int型，便于下面比较
        if (r != 0){
            //2.1 不为0，代表没有购买资格
            return  Result.fail(r == 1?"优惠券已售罄":"不能重复购买");

        }

        //2.2 为0，有购买资格，把下单信息保存到阻塞队列中
        //7.创建订单   向订单表新增一条数据，除默认字段，其他字段的值需要set
        VoucherOrder voucherOrder = new VoucherOrder();
        //7.1订单id
        long orderId = redisWorker.nextId("order");
        voucherOrder.setId(orderId);
        //7.2用户id
        voucherOrder.setUserId(userId);
        //7.3代金券id
        voucherOrder.setVoucherId(voucherId);
//        //放入阻塞对列中
//        orderTasks.add(voucherOrder);
        //放到rabbitmq
        rabbitTemplate.convertAndSend(VOUCHERORDER_EXCHANGE,"order", JSONUtil.toJsonStr(voucherOrder));
        //获取代理对象
//        proxy = (VoucherService) AopContext.currentProxy();
        //3.返回订单id
        return Result.success(orderId);
    }



//    //创建线程池
//    private static final ExecutorService SECKILL_ORDER_EXECUTOR = Executors.newSingleThreadExecutor();
//    //利用spring提供的注解，在类初始化完毕后立即执行线程任务
//    @PostConstruct
//    private void init(){
//        SECKILL_ORDER_EXECUTOR.submit(new VoucherOrderHandler());
//    }
    //创建线程任务，内部类方式
//    private class VoucherOrderHandler implements Runnable{
//
//        @Override
//        public void run() {
//            //1.获取队列中的订单信息
//            try {
////                VoucherOrder voucherOrder = orderTasks.take();
//                //2.创建订单，这是调之前那个创建订单的方法，需要稍作改动
//                handleVoucherOrder(voucherOrder);
//            } catch (Exception e) {
////                log.info("异常信息:",e);
//            }
//        }
//    }
//    private void handleVoucherOrder(VoucherOrder voucherOrder) {
//        Long userId = voucherOrder.getUserId();
//        //创建锁对象
//        SimpleRedisLock lock = new SimpleRedisLock(stringRedisTemplate, "order:" + userId);
//        //获取锁
//        boolean isLock = lock.tryLock(1200);
//        //判断是否获取锁成功
//        if (!isLock){
////            log.error("您已购买过该商品，不能重复购买");
//        }
//        try {
//            proxy.createVoucherOrder(voucherOrder);//使用代理对象，最后用于提交事务
//        } catch (IllegalStateException e) {
//            throw new RuntimeException(e);
//        } finally {
//            lock.unlock();//释放锁
//        }
//    }
    @Transactional
    public void createVoucherOrder(VoucherOrder voucherOrder){
//        Long voucherId = voucherOrder.getVoucherId();
//        //5.一人一单
//        Long userId = voucherOrder.getId();
        //5.1查询订单
//        int count = query().eq("user_id", userId).eq("voucher_id", voucherId).count();
        int count=voucherMapper.checkIfExistOrder(voucherOrder);
        //5.2判断是否存在
        if (count > 0){
//            log.error("您已经购买过了");
        }
        //6.扣减库存
        SeckillVoucher seckillVoucher = new SeckillVoucher();
        seckillVoucher.setStock(seckillVoucher.getStock()-1);
        boolean success = voucherMapper.updateSecKill();
//                .setSql("stock = stock - 1")//set stock = stock -1
//                .eq("voucher_id",voucherId).gt("stock",0) //where id = ? and stock > 0
//                .update();
        if (!success){
//           log.info("库存不足！");
        }
        voucherMapper.addVoucherOrder(voucherOrder);
    }





}
