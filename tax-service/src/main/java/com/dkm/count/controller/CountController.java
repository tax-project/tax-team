package com.dkm.count.controller;

import com.alibaba.fastjson.JSONObject;
import com.dkm.count.entity.bo.CountBO;
import com.dkm.count.entity.bo.ExcelBO;
import com.dkm.count.entity.bo.PayPageBO;
import com.dkm.count.entity.bo.PayPageDataBO;
import com.dkm.jwt.islogin.CheckToken;
import com.dkm.user.utils.BodyUtils;
import com.dkm.utils.ExcelUtils;
import com.dkm.utils.FilesUtils;
import com.dkm.voucher.service.IVoucherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public List<ExcelBO> listAllVoucher(){
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

    @ApiOperation(value = "导出支付记录的Excel",notes = "HTTP头部携带Token",httpMethod = "GET")
    @ApiImplicitParam(name = "token",value = "用户Token，存放在HTTP的头部",required = true,dataType = "String",paramType = "header")
    @GetMapping("/export/excel")
    @CrossOrigin
    public void exportExcel(HttpServletResponse response){
        HSSFWorkbook sheets = voucherService.exportExcel();
        String fileName = "有奖竞答活动支付统计.xls";
        String fileStorePath = "exl";
        String path = FilesUtils.getPath(fileName,fileStorePath);
        ExcelUtils.outFile(sheets,path,response);
    }


    @ApiOperation(value = "获取分页之后的数据详情",notes = "HTTP头部携带Token",httpMethod = "GET")
    @ApiImplicitParam(name = "token",value = "用户Token，存放在HTTP的头部",required = true,dataType = "String",paramType = "header")
    @PostMapping("/pay/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "第几页",required = true,dataType = "Integer",paramType = "body"),
            @ApiImplicitParam(name = "pageMuch",value = "每一页显示的数目",required = true,dataType = "Integer",paramType = "body")
    })
    @CheckToken
    @CrossOrigin
    public PayPageBO payPageData(HttpServletRequest request){
        JSONObject json = BodyUtils.bodyJson(request);
        Integer page = json.getInteger("page");
        Integer pageMuch = json.getInteger("pageMuch");
        return voucherService.payPageData(page,pageMuch);
    }
}
