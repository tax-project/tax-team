package com.dkm.count.entity.bo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: HuangJie
 * @Date: 2020/4/15 9:06
 * @Version: 1.0V
 */
@Data
public class ExcelBO {

    private Long id;
    private Long userId;
    private String userNickName;
    private String typeName;
    private String ticketUrl;
    private Double payMoney;
    private String  payTime;
    private Long taxUserId;
    private String taxNickName;
    private String taxUserName;

}
