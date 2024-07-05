package project.test.xface.mapper;


import org.apache.ibatis.annotations.Mapper;
import project.test.xface.entity.pojo.GroupVoucher;
import project.test.xface.entity.pojo.SeckillVoucher;
import project.test.xface.entity.pojo.Voucher;
import project.test.xface.entity.pojo.VoucherOrder;

@Mapper
public interface VoucherMapper {
    boolean addVoucher(Voucher voucher);

    boolean addSeckillVoucher(SeckillVoucher seckillVoucher);

    boolean addGroupVoucher(GroupVoucher groupVoucher);

    void addVoucherOrder(VoucherOrder voucherOrder);

    boolean updateSecKill();

    int checkIfExistOrder(VoucherOrder voucherOrder);
}
