package project.test.xface.controller.voucher;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.GroupVoucher;
import project.test.xface.entity.pojo.SeckillVoucher;
import project.test.xface.entity.pojo.Voucher;
import project.test.xface.service.VoucherService;

/**
 * 大致分为三种
 * 0：普通优惠券，跟随shop显示
 * 1：秒杀券，跟随shop显示
 * 2：群内券，只有群内的人可以看到
 * 增 删 改 查
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class VoucherController {

   @Autowired
   private VoucherService voucherServcie;

    /**
     * 新增普通优惠券
     */
    @PostMapping("/voucher")
    public Result addVoucher(@RequestBody Voucher voucher){
       return voucherServcie.addVoucher(voucher);
    }
    /**
     * 新增秒杀券
     */
    @PostMapping("/seckillVoucher")
    public Result addSeckillVoucher(@RequestBody Voucher voucher){
            return voucherServcie.addSeckillVoucher(voucher);
    }
    /**
     * 新增群内券
     */
    @PostMapping("/groupVoucher")
    public Result addGroupVoucher(@RequestBody Voucher voucher){
        return voucherServcie.addGroupVoucher(voucher);
    }
    /**
     * 查看shop券，包括1，2
     */
    @GetMapping("/vouchers")
    public Result listVouchers(Long shopId){
        return voucherServcie.listVouchers(shopId);
    }
    /**
     * 查看群内券
     */
    @GetMapping("/groupVoucher")
    public Result listGroupVouchers(Long groupId){
        return voucherServcie.listGroupVouchers(groupId);
    }
    /**
     *秒杀券下单
     */
    @PostMapping("/seckillVoucher/{id}")
    public Result seckillVoucher(@PathVariable("id") Long id){
        return voucherServcie.seckillVoucher(id);
    }


}
