package com.dkm.pay.wxPay.entity;

import lombok.Data;

/**
 * @author qf
 * @date 2020/4/11
 * @vesion 1.0
 **/
@Data
public class WxResultVo {

   private int code;

   private String msg;

   private WxTokenVo data;
}
