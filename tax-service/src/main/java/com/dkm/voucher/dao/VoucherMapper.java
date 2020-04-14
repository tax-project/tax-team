package com.dkm.voucher.dao;

import com.dkm.IBaseMapper.IBaseMapper;
import com.dkm.count.entity.bo.CountBO;
import com.dkm.voucher.entity.Voucher;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author qf
 * @date 2020/4/10
 * @vesion 1.0
 **/
@Component
public interface VoucherMapper extends IBaseMapper<Voucher> {

    /**
     * 获取总金额
     * @return 金额
     */
    @Select("select sum(pay_money)issued_money, count(distinct user_id)issued_user_much from tb_voucher")
    CountBO paymentOverview();

    /**
     * 获取超市参与人数
     * @return 人数
     */
    @Select("select count(distinct user_id) from tb_voucher where type_money = 10")
    Integer supermarketMuch();
    /**
     * 获取餐厅参与人数
     * @return 人数
     */
    @Select("select count(distinct user_id) from tb_voucher where type_money = 20")
    Integer restaurantMuch();
    /**
     * 获取建材参与人数
     * @return 人数
     */
    @Select("select count(distinct user_id) from tb_voucher where type_money = 30")
    Integer buildMuch();
}
