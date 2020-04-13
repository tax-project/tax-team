package com.dkm.qrCode.controller;

import com.dkm.jwt.contain.LocalUser;
import com.dkm.jwt.entity.UserLoginQuery;
import com.dkm.jwt.islogin.CheckToken;
import com.dkm.qrCode.QrCode;
import com.dkm.voucher.entity.vo.VoucherQrCodeVo;
import com.dkm.voucher.service.IVoucherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author qf
 * @date 2020/4/11
 * @vesion 1.0
 **/
@Api(tags = "二维码生成API")
@RestController
@RequestMapping("/v1/qrCode")
public class QrCodeController {

   @Autowired
   private QrCode qrCode;

   /**
    * 餐厅
    */
   @Value("${qrCode.reQrUrl}")
   private String reQrUrl;

   @Value("${qrCode.restaurantQrCode}")
   private String restaurantQrCode;

   /**
    * 超市
    */
   @Value("${qrCode.suQrUrl}")
   private String suQrUrl;

   @Value("${qrCode.supermarketQrCode}")
   private String supermarketQrCode;

   /**
    * 建材
    */
   @Value("${qrCode.buQrUrl}")
   private String buQrUrl;

   @Value("${qrCode.buildingMaterialQrCode}")
   private String buildingMaterialQrCode;

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

   @Autowired
   private IVoucherService voucherService;

   @Autowired
   private LocalUser localUser;

   @ApiOperation(value = "餐厅二维码", notes = "餐厅二维码")
   @GetMapping("/restaurantQrCode")
   @CrossOrigin
   @CheckToken
   public void restaurantQrCode (HttpServletResponse response) {

      UserLoginQuery user = localUser.getUser("user");

      VoucherQrCodeVo vo = new VoucherQrCodeVo();
      vo.setUserId(user.getId());
      //消费者进行使用
      vo.setQrCodeUrl(reQrUrl);
      vo.setTypeName("餐厅");
      vo.setTypeMoney(20.0);

      voucherService.insertVoucher(vo);

      //餐厅的二维码
      //操作员扫描
      qrCode.qrCode(restaurantQrCode,response);
   }

   @ApiOperation(value = "超市二维码", notes = "超市二维码")
   @GetMapping("/supermarketQrCode")
   @CrossOrigin
   @CheckToken
   public void supermarketQrCode (HttpServletResponse response) {

      UserLoginQuery user = localUser.getUser("user");

      VoucherQrCodeVo vo = new VoucherQrCodeVo();
      vo.setUserId(user.getId());
      vo.setQrCodeUrl(suQrUrl);
      vo.setTypeName("超市");
      vo.setTypeMoney(10.0);

      voucherService.insertVoucher(vo);

      //超市二维码
      qrCode.qrCode(supermarketQrCode,response);
   }

   @ApiOperation(value = "建材二维码", notes = "建材二维码")
   @GetMapping("/buildingMaterialQrCode")
   @CrossOrigin
   @CheckToken
   public void buildingMaterialQrCode (HttpServletResponse response) {

      UserLoginQuery user = localUser.getUser("user");

      VoucherQrCodeVo vo = new VoucherQrCodeVo();
      vo.setUserId(user.getId());
      vo.setQrCodeUrl(buQrUrl);
      vo.setTypeName("建材");
      vo.setTypeMoney(30.0);

      voucherService.insertVoucher(vo);

      //建材二维码
      qrCode.qrCode(buildingMaterialQrCode,response);
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
