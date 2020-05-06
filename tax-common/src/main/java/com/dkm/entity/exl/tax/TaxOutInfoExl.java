package com.dkm.entity.exl.tax;

import lombok.Data;

/**
 * @author qf
 * @date 2020/5/4
 * @vesion 1.0
 **/
@Data
public class TaxOutInfoExl {

   /**
    * 日期
    */
   private String date;

   /**
    * 超市
    */
   private Integer supper;

   /**
    * 曾小厨餐厅
    */
   private Integer zRestaurant;

   /**
    * 成福记餐厅
    */
   private Integer cRestaurant;

   /**
    * 渔乐圈餐厅
    */
   private Integer yRestaurant;
}
