package com.dkm.count.controller;

import com.dkm.count.entity.bo.CountBO;
import com.dkm.jwt.islogin.CheckToken;
import com.dkm.voucher.entity.Voucher;
import com.dkm.voucher.service.IVoucherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: HuangJie
 * @Date: 2020/4/13 9:31
 * @Version: 1.0V
 */
@Api(tags = "报表接口")
@RestController
@RequestMapping("/count")
public class CountController {

    @Autowired
    private IVoucherService voucherService;


    @ApiOperation(value = "获取所有的支付记录",notes = "HTTP头部携带Token",httpMethod = "GET")
    @ApiImplicitParam(name = "token",value = "用户Token，存放在HTTP的头部",required = true,dataType = "String",paramType = "header")
    @GetMapping("/all/voucher")
    @CheckToken
    @CrossOrigin
    public List<Voucher> listAllVoucher(){
        return voucherService.listAllVoucher();
    }

    @ApiOperation(value = "获取参与人数，已经发放金额等参数",notes = "HTTP头部携带Token，得到参与人数，已经发放金额，剩余金额，参与人数中超市，餐厅，建材，分别占多少",httpMethod = "GET")
    @ApiImplicitParam(name = "token",value = "用户Token，存放在HTTP的头部",required = true,dataType = "String",paramType = "header")
    @GetMapping("/payment")
    @CheckToken
    @CrossOrigin
    public CountBO paymentOverview(){
        return voucherService.paymentOverview();
    }

}
