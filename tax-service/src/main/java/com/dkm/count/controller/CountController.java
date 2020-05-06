package com.dkm.count.controller;

import com.alibaba.fastjson.JSONObject;
import com.dkm.count.entity.bo.CountBO;
import com.dkm.count.entity.bo.ExcelBO;
import com.dkm.count.entity.bo.PayPageBO;
import com.dkm.entity.exl.tax.ExlTaxUtils;
import com.dkm.entity.exl.tax.TaxOutInfoExl;
import com.dkm.jwt.islogin.CheckToken;
import com.dkm.user.utils.BodyUtils;
import com.dkm.utils.DateUtil;
import com.dkm.utils.ExcelUtils;
import com.dkm.utils.FilesUtils;
import com.dkm.voucher.service.IVoucherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @ApiOperation(value = "获取分页之后的数据详情",notes = "HTTP头部携带Token",httpMethod = "POST")
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


    @CrossOrigin
    @GetMapping("/exl")
    public void orderExcel(HttpServletResponse response) {

        //查询出需要导出的数据
        List<TaxOutInfoExl> list = voucherService.listAll();

        Integer zCount = 0;
        Integer cCount = 0;
        Integer yCount = 0;
        Integer sCount = 0;

        //创建报表体
        List<List<String>> body = new ArrayList<>();
        for (TaxOutInfoExl query : list) {

            sCount += query.getSupper();
            zCount += query.getZRestaurant();
            cCount += query.getCRestaurant();
            yCount += query.getYRestaurant();

            List<String> bodyValue = new ArrayList<>();
            bodyValue.add(query.getDate());
            bodyValue.add(String.valueOf(query.getSupper()));
            bodyValue.add(String.valueOf(query.getCRestaurant()));
            bodyValue.add(String.valueOf(query.getZRestaurant()));
            bodyValue.add(String.valueOf(query.getYRestaurant()));
            //将数据添加到报表体中
            body.add(bodyValue);
        }
        ExlTaxUtils contentExl = new ExlTaxUtils();
        LocalDateTime now = LocalDateTime.now();
        String date = DateUtil.formatDateTime(now);
        contentExl.setEndTime(date);

        Integer supperMoney = sCount * 10;
        Integer zMoney = zCount * 30;
        Integer cMoney = cCount * 30;
        Integer yMoney = yCount * 30;
        Integer allPrice = supperMoney + zMoney + cMoney + yMoney;

        contentExl.setSupperMoney(supperMoney);
        contentExl.setCRestaurantMoney(cMoney);
        contentExl.setYRestaurantMoney(yMoney);
        contentExl.setZRestaurantMoney(zMoney);
        contentExl.setAllMoney(allPrice);


        String fileName = "税务局优惠卷活动数据统计.xls";
        Workbook excel = ExcelUtils.exp2Excel(body,contentExl);
        String fileStorePath = "exl";
        String path = FilesUtils.getPath(fileName,fileStorePath);
        ExcelUtils.outFile2(excel,path,response);
    }
}
