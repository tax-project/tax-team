package com.dkm.qrCode.controller;

import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.jwt.contain.LocalUser;
import com.dkm.jwt.entity.UserLoginQuery;
import com.dkm.jwt.islogin.CheckToken;
import com.dkm.qrCode.QrCode;
import com.dkm.qrCode.entity.Result;
import com.dkm.utils.StringUtils;
import com.dkm.voucher.entity.bo.IdVo;
import com.dkm.voucher.entity.vo.VoucherQrCodeVo;
import com.dkm.voucher.service.IVoucherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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

   @Value("${qrCode.restaurant}")
   private String restaurantQrCode;

   /**
    * 超市
    */
   @Value("${qrCode.suQrUrl}")
   private String suQrUrl;

   @Value("${qrCode.supermarket}")
   private String supermarketQrCode;

   /**
    * 建材
    */
   @Value("${qrCode.buQrUrl}")
   private String buQrUrl;

   @Value("${qrCode.buildingMaterial}")
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

   private final String ZENG_NAME = "曾小厨餐厅";

   private final String YU_NAME = "渔乐圈餐厅";

   private final String CHENG_NAME = "成福记餐厅";

   @Autowired
   private IVoucherService voucherService;

   @Autowired
   private LocalUser localUser;

   @ApiOperation(value = "餐厅二维码(选择优惠卷)", notes = "餐厅二维码(选择优惠卷)")
   @ApiImplicitParam(name = "restaurantName", value = "餐厅名称", required = true, dataType = "String", paramType = "path")
   @GetMapping("/restaurantQrCode")
   @CrossOrigin
   @CheckToken
   public Result restaurantQrCode (@RequestParam("restaurantName") String restaurantName) {

      if (StringUtils.isBlank(restaurantName)) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }

      if (!ZENG_NAME.equals(restaurantName) && !YU_NAME.equals(restaurantName) && !CHENG_NAME.equals(restaurantName)) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR);
      }

      UserLoginQuery user = localUser.getUser("user");

      VoucherQrCodeVo vo = new VoucherQrCodeVo();
      vo.setUserId(user.getId());
      //消费者进行使用
      vo.setQrCodeUrl(reQrUrl);
      vo.setTypeName(restaurantName);
      vo.setTypeMoney(30.0);

      IdVo idVo = voucherService.insertVoucher(vo);

      if (idVo == null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "您已领了三次");
      }

      //餐厅的二维码
      String url = restaurantQrCode + "?id=" + idVo.getId() + "&typeName=" +vo.getTypeName() + "&typeMoney=" +vo.getTypeMoney() + "&openId=" +user.getWxOpenId();
      //操作员扫描
      Result result = new Result();
      result.setUrl(url);
      return result;
   }

   @ApiOperation(value = "超市二维码(选择优惠卷)", notes = "超市二维码(选择优惠卷)")
   @GetMapping("/supermarketQrCode")
   @CrossOrigin
   @CheckToken
   public Result supermarketQrCode () {

      UserLoginQuery user = localUser.getUser("user");

      VoucherQrCodeVo vo = new VoucherQrCodeVo();
      vo.setUserId(user.getId());
      vo.setQrCodeUrl(suQrUrl);
      vo.setTypeName("超市");
      vo.setTypeMoney(10.0);

      IdVo idVo = voucherService.insertVoucher(vo);

      if (idVo == null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "您已领了三次");
      }

      //超市
      String url = supermarketQrCode + "?id=" + idVo.getId() + "&typeName=" +vo.getTypeName() + "&typeMoney=" +vo.getTypeMoney() + "&openId=" +user.getWxOpenId();

      //超市二维码
      Result result = new Result();
      result.setUrl(url);
      return result;
   }

   @ApiOperation(value = "建材二维码(选择优惠卷)", notes = "建材二维码(选择优惠卷)")
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

      IdVo idVo = voucherService.insertVoucher(vo);

      if (idVo == null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "请勿频繁操作");
      }

      String url = buildingMaterialQrCode + "?id=" + idVo.getId() + "&typeName=" +vo.getTypeName() + "&typeMoney=" +vo.getTypeMoney() + "&openId=" +user.getWxOpenId();

      //建材二维码
      qrCode.qrCode(url,response);
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


   @ApiOperation(value = "餐厅二维码(用于已有优惠卷查看)", notes = "餐厅二维码(用于已有优惠卷查看)")
   @ApiImplicitParam(name = "id", value = "凭证id", required = true, dataType = "Long", paramType = "path")
   @GetMapping("/restaurant")
   @CrossOrigin
   @CheckToken
   public void restaurant (HttpServletResponse response,@RequestParam("id") Long id) {

      UserLoginQuery user = localUser.getUser("user");
      //餐厅的二维码
      String url = restaurantQrCode + "?id=" + id + "&typeName=餐厅&typeMoney=30.0"+ "&openId=" +user.getWxOpenId();
      //操作员扫描
      qrCode.qrCode(url,response);
   }

   @ApiOperation(value = "超市二维码(用于已有优惠卷查看)", notes = "超市二维码(用于已有优惠卷查看)")
   @ApiImplicitParam(name = "id", value = "凭证id", required = true, dataType = "Long", paramType = "path")
   @GetMapping("/supermarket")
   @CrossOrigin
   @CheckToken
   public void supermarket (HttpServletResponse response,@RequestParam("id") Long id) {

      UserLoginQuery user = localUser.getUser("user");
      //超市的二维码
      String url = supermarketQrCode + "?id=" + id + "&typeName=超市&typeMoney=10.0" + "&openId=" +user.getWxOpenId();

      //超市二维码
      qrCode.qrCode(url,response);
   }

   @ApiOperation(value = "建材二维码(用于已有优惠卷查看)", notes = "建材二维码(用于已有优惠卷查看)")
   @ApiImplicitParam(name = "id", value = "凭证id", required = true, dataType = "Long", paramType = "path")
   @GetMapping("/buildingMaterial")
   @CrossOrigin
   @CheckToken
   public void buildingMaterial (HttpServletResponse response,@RequestParam("id") Long id) {

      UserLoginQuery user = localUser.getUser("user");

      //建材的二维码
      String url = buildingMaterialQrCode + "?id=" + id + "&typeName=建材&typeMoney=30.0" + "&openId=" +user.getWxOpenId();

      //建材二维码
      qrCode.qrCode(url,response);
   }
}
