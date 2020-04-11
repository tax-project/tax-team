package com.dkm.pay.wxPay.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.httpclient.HttpClientUtils;
import com.dkm.httpclient.HttpResult;
import com.dkm.pay.wxPay.entity.ResultVo;
import com.dkm.pay.wxPay.entity.WxLoginVo;
import com.dkm.pay.wxPay.entity.WxResultVo;
import com.dkm.pay.wxPay.entity.WxTokenVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @author qf
 * @date 2020/4/11
 * @vesion 1.0
 **/
@Api(tags = "微信企业向个人发红包API")
@RestController
@RequestMapping("/v1/wxPay")
public class WxPayController {

   @Autowired
   private HttpClientUtils apiService;

   @Value("${wx.userName}")
   private String userName;

   @Value("${wx.passWord}")
   private String passWord;

   @Value("${wx.authLogin}")
   private String authLogin;

   @Value("${wx.payToPerson}")
   private String payToPerson;

   @ApiOperation(value = "微信企业给个人发红包", notes = "微信企业给个人发红包")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "price", value = "支付金钱", required = true, dataType = "Double", paramType = "path"),
         @ApiImplicitParam(name = "openId", value = "openId", required = true, dataType = "String", paramType = "path"),
   })
   @GetMapping("/person")
   @CrossOrigin
   public Object orders(@RequestParam("openId") String openId,
                        @RequestParam("price") Double price) {


      WxLoginVo vo = new WxLoginVo();
      vo.setAuthUserKey(userName);
      vo.setAuthPassword(passWord);
      String jsonString = JSON.toJSONString(vo);

      HttpResult httpResponse;
      try {
         httpResponse = apiService.doPost(authLogin, jsonString);
      } catch (Exception e) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "调用支付登录接口失败");
      }

      if (httpResponse.getCode() != 200) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, httpResponse.getBody());
      }

      WxResultVo wxResultVo = JSONObject.parseObject(httpResponse.getBody(), WxResultVo.class);

      if (wxResultVo.getCode() != 0) {
         throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,wxResultVo.getMsg());
      }

      WxTokenVo data = wxResultVo.getData();
      String token = data.getToken();
      //调支付接口
      String url = payToPerson + "?price=" + price + "&openId=" +openId;

      HttpResult result = apiService.get(url, token);

      if (result.getCode() != 200) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, result.getBody());
      }

      ResultVo resultVo = JSONObject.parseObject(result.getBody(), ResultVo.class);

      if (resultVo.getCode() != 0) {
         throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,resultVo.getMsg());
      }

      return resultVo.getData();

   }

}
