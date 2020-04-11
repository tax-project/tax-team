package com.dkm.qrCode.controller;

import com.dkm.qrCode.QrCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author qf
 * @date 2020/4/11
 * @vesion 1.0
 **/
@Slf4j
@Api(tags = "二维码生成API")
@RestController
@RequestMapping("/v1/qrCode")
public class QrCodeController {

   @Autowired
   private QrCode qrCode;

   /**
    * 餐厅
    */
   @Value("${qrCode.restaurant}")
   private String restaurant;

   /**
    * 超市
    */
   @Value("${qrCode.supermarket}")
   private String supermarket;

   /**
    * 建材
    */
   @Value("${qrCode.buildingMaterial}")
   private String buildingMaterial;

   /**
    * 管理员
    */
   @Value("${qrCode.admin}")
   private String admin;

   /**
    * 操作员
    */
   @Value("${qrCode.operator}")
   private String operator;

   /**
    * 用户
    */
   @Value("${qrCode.user}")
   private String user;

   @ApiOperation(value = "餐厅二维码", notes = "餐厅二维码")
   @GetMapping("/restaurantQrCode")
   @CrossOrigin
   public void restaurantQrCode (HttpServletResponse response) {

      //餐厅的二维码
      qrCode.qrCode(restaurant,response);
   }

   @ApiOperation(value = "超市二维码", notes = "超市二维码")
   @GetMapping("/supermarketQrCode")
   @CrossOrigin
   public void supermarketQrCode (HttpServletResponse response) {

      //超市二维码
      qrCode.qrCode(supermarket,response);
   }

   @ApiOperation(value = "建材二维码", notes = "建材二维码")
   @GetMapping("/buildingMaterialQrCode")
   @CrossOrigin
   public void buildingMaterialQrCode (HttpServletResponse response) {

      //建材二维码
      qrCode.qrCode(buildingMaterial,response);
   }

   @ApiOperation(value = "管理人员二维码", notes = "管理人员二维码")
   @GetMapping("/adminQrCode")
   @CrossOrigin
   public void adminQrCode (HttpServletResponse response) {

      //管理人员二维码
      qrCode.qrCode(admin,response);
   }

   @ApiOperation(value = "操作员二维码", notes = "操作员二维码")
   @GetMapping("/operatorQrCode")
   @CrossOrigin
   public void operatorQrCode (HttpServletResponse response) {

      //操作员二维码
      qrCode.qrCode(operator,response);
   }


   @ApiOperation(value = "用户二维码", notes = "用户二维码")
   @GetMapping("/userQrCode")
   @CrossOrigin
   public void userQrCode (HttpServletResponse response) {

      //用户二维码
      qrCode.qrCode(user,response);
   }
}
