package com.dkm.voucher.dao;

import com.dkm.IBaseMapper.IBaseMapper;
import com.dkm.count.entity.bo.CountBO;
import com.dkm.voucher.entity.Voucher;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.ArrayList;
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
    @Select("select count(distinct user_id) from tb_voucher where type_name = '超市'")
    Integer supermarketMuch();
    /**
     * 曾小厨餐厅
     * @return 人数
     */
    @Select("select count(distinct user_id) from tb_voucher where type_name = '曾小厨餐厅'")
    Integer restaurantMuch();
    /**
     * 渔乐圈餐厅
     * @return 人数
     */
    @Select("select count(distinct user_id) from tb_voucher where type_name = '渔乐圈餐厅'")
    Integer buildMuch();

    /**
     * 成福记餐厅
     * @return 人数
     */
    @Select("select count(distinct user_id) from tb_voucher where type_name = '成福记餐厅'")
    Integer intoRestaurant();

    /**
     * 获取分页之后的支付数据
     * @param startPage 开始的条例
     * @param pageMuch 拿到多少页
     * @return 数据
     */
    @Select("select * from tb_voucher where qr_code_status = 1 limit #{startPage},#{pageMuch}")
    ArrayList<Voucher> payPageData(@Param("startPage") Integer startPage, @Param("pageMuch") Integer pageMuch);
}
