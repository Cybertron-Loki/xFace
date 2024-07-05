package project.test.xface.service;

import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Voucher;
import project.test.xface.entity.pojo.VoucherOrder;

public interface VoucherService {
    Result addVoucher(Voucher voucher);

    Result addSeckillVoucher(Voucher seckillVoucher);

    Result addGroupVoucher(Voucher groupVoucher);

    Result listVouchers(Long shopId);

    Result listGroupVouchers(Long groupId);

    Result seckillVoucher(Long id);

    void createVoucherOrder(VoucherOrder voucherOrder);
}
