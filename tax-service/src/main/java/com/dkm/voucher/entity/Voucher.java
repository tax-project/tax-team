package com.dkm.voucher.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author qf
 * @date 2020/4/10
 * @vesion 1.0
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("tb_voucher")
public class Voucher extends Model<Voucher> {

   private Long id;

   /**
    * 用户Id
    */
   private Long userId;

   /**
    * 二维码地址
    */
   private String qrCodeUrl;

   /**
    * 二维码状态
    * 0--未使用
    * 1--已使用
    */
   private Integer qrCodeStatus;

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

   /**
    * 小票地址
    */
   private String ticketUrl;

   /**
    * 上传时间
    */
   private LocalDateTime dateTime;

   /**
    * 操作人id
    */
   private Long updateUserId;

   /**
    * 操作人
    */
   private String updateUser;

   /**
    * 支付金额
    */
   private Double payMoney;

   /**
    * 支付时间
    */
   private LocalDateTime payTime;
}
