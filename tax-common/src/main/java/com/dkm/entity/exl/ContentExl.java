package com.dkm.entity.exl;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @author qf
 * @date 2020/1/13
 * @vesion 1.0
 **/
@Data
public class ContentExl {

   /**
    * 成人，了哦人，小孩，幼儿
    * 人数的字符串
    */
   private String number;

   /**
    * 总人数
    */
   private Integer allNumber;

   /**
    * 团费收入
    */
   private Double incomeMoney;

   /**
    * 代收金额
    */
   private Double msPrice;

   /**
    * 其他收入
    */
   private Double otherPrice;

   /**
    * 合计收入
    */
   private Double allPrice;

   /**
    * 利润
    */
   private String setPrice;

   /**
    * 报表人
    */
   private String guideName;

   /**
    * 财务人
    */
   private String financeName;

   /**
    * 支出信息
    */
   private List<OutInfoExl> list;

   /**
    * 日期
    */
   private LocalDate date;



}
