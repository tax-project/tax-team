package com.dkm.voucher.entity.vo;

import lombok.Data;

/**
 * @author qf
 * @date 2020/4/11
 * @vesion 1.0
 **/
@Data
public class VoucherReturnQrCodeVo {

   private Long id;

   /**
    * 二维码状态
    * 0--未使用
    * 1--已使用
    */
   private Integer qrCodeStatus;

   /**
    * 优惠卷名字
    * * 超市
    * * 建材
    * * 餐厅
    */
   private String typeName;

   /**
    * 优惠卷金额
    */
   private Double typeMoney;
}
