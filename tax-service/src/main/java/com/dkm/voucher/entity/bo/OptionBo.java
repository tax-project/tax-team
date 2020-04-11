package com.dkm.voucher.entity.bo;

import lombok.Data;

/**
 * @author qf
 * @date 2020/4/11
 * @vesion 1.0
 **/
@Data
public class OptionBo {

   /**
    * 操作人用户Id
    */
   private Long optionUserId;

   /**
    * 小票url
    */
   private String ticketUrl;

   /**
    * 操作人名字
    */
   private String optionUser;

   /**
    * 凭证id
    */
   private Long id;
}
