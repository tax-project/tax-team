package com.dkm.count.entity.bo;

import lombok.Data;

/**
 * @Author: HuangJie
 * @Date: 2020/4/14 9:29
 * @Version: 1.0V
 */
@Data
public class CountBO {
    /**
     * 已发放金额
     */
    private Double issuedMoney;
    /**
     * 剩余金额
     */
    private Double remainMoney;
    /**
     * 参与人数
     */
    private Integer issuedUserMuch;
    /**
     * 超市人数
     */
    private Integer supermarketMuch;
    /**
     * 餐厅人数
     */
    private Integer restaurantMuch;
    /**
     * 建材人数
     */
    private Integer buildMuch;
}
