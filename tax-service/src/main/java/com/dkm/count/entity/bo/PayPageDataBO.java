package com.dkm.count.entity.bo;

import lombok.Data;

/**
 * @Author: HuangJie
 * @Date: 2020/4/17 11:32
 * @Version: 1.0V
 */
@Data
public class PayPageDataBO {

    private String userNickName;
    private String taxNickName;
    private String payTime;
    private Double payMoney;
    private String imageUrl;
}
