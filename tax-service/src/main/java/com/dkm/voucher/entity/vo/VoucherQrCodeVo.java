package com.dkm.voucher.entity.vo;

import lombok.Data;

/**
 * @author qf
 * @date 2020/4/11
 * @vesion 1.0
 **/
@Data
public class VoucherQrCodeVo {

   /**
    * 用户Id
    */
   private Long userId;

   /**
    * 二维码地址
    */
   private String qrCodeUrl;

   /**
    * 优惠卷名称
    * 超市
    * 建材
    * 餐厅
    */
   private String typeName;

   /**
    * 优惠卷金额
    */
   private Double typeMoney;
}
