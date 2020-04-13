package com.dkm.voucher.service;

import com.dkm.voucher.entity.Voucher;
import com.dkm.voucher.entity.bo.OptionBo;
import com.dkm.voucher.entity.vo.VoucherQrCodeVo;
import com.dkm.voucher.entity.vo.VoucherReturnQrCodeVo;
import java.util.List;

/**
 * 凭证
 * @author qf
 * @date 2020/4/10
 * @vesion 1.0
 **/
public interface IVoucherService {

   /**
    * 增加二维码信息
    * @param vo
    */
   void insertVoucher (VoucherQrCodeVo vo);

   /**
    * 根据用户id返回展示所有消费者的二维码信息
    * @param userId
    * @return
    */
   List<VoucherReturnQrCodeVo> listAllQrCode (Long userId);

   /**
    * 操作员上传凭证
    * @param bo
    */
   void uploadVoucher (OptionBo bo);

   /**
    * 支付报表
    * @return 获得所有的支付记录
    */
   List<Voucher> listAllVoucher();
}
