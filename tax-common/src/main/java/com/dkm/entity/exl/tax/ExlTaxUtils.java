package com.dkm.entity.exl.tax;

import lombok.Data;

/**
 * @author qf
 * @date 2020/5/4
 * @vesion 1.0
 **/
@Data
public class ExlTaxUtils {

   /**
    * 超市金额
    */
   private Integer supperMoney;

   /**
    * 曾小厨餐厅金额
    */
   private Integer zRestaurantMoney;

   /**
    * 成福记餐厅金额
    */
   private Integer cRestaurantMoney;

   /**
    * 渔乐圈餐厅金额
    */
   private Integer yRestaurantMoney;

   /**
    * 总金额
    */
   private Integer allMoney;

   /**
    * 截止时间
    */
   private String endTime;
}
