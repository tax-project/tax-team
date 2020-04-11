package com.dkm.pay.wxPay.controller;

import com.dkm.pay.wxPay.utils.HttpRequest;
import com.dkm.pay.wxPay.utils.WxUtils;
import com.dkm.utils.IdGenerator;
import com.dkm.voucher.entity.bo.ResultBo;
import com.github.wxpay.sdk.WXPayUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qf
 * @date 2020/4/11
 * @vesion 1.0
 **/
@Slf4j
@Api(tags = "微信企业向个人发红包API")
@RestController
@RequestMapping("/v1/wxPay")
public class WxPayController {

   @Value("${wx.pay}")
   private String wxUrl;

   @Value("${wx.appId}")
   private String appId;

   @Value("${wx.mchId}")
   private String mchId;

   @Value("${wx.paterNerKey}")
   private String paterNerKey;

   @Autowired
   private IdGenerator idGenerator;


   @ApiOperation(value = "微信企业给个人发红包", notes = "微信企业给个人发红包")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "price", value = "支付金钱", required = true, dataType = "Double", paramType = "path"),
         @ApiImplicitParam(name = "openId", value = "openId", required = true, dataType = "String", paramType = "path"),
   })
   @GetMapping("/appPay")
   @CrossOrigin
   public Map<String, String> orders(@RequestParam("openId") String openId,
                                     @RequestParam("price") Double price,
                                     HttpServletRequest request) {

      try {

         // 拼接统一下单地址参数
         Map<String, String> paraMap = new HashMap<String, String>();
         // 获取请求ip地址
         String ip = request.getHeader("x-forwarded-for");
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
         }
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
         }
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
         }
         if (ip.indexOf(",") != -1) {
            String[] ips = ip.split(",");
            ip = ips[0].trim();
         }

         paraMap.put("mch_appid", appId);
         // 商户ID
         paraMap.put("mchid", mchId);
         paraMap.put("nonce_str", WXPayUtil.generateNonceStr());
         paraMap.put("partner_trade_no", idGenerator.getOrderCode());
         paraMap.put("openid", openId);
         // 支付金额，单位分
         //元转分
         Integer money = WxUtils.changeY2F(price);
         paraMap.put("amount", String.valueOf(money));
         paraMap.put("check_name", "NO_CHECK");
         paraMap.put("desc", "红包");
         // 将所有参数(map)转xml格式
         String xml = WXPayUtil.generateSignedXml(paraMap, paterNerKey);

         String xmlStr = HttpRequest.httpsRequest(wxUrl, "POST", xml);

         // 以下内容是返回前端页面的json数据
         Map<String,String> resultMap = new HashMap<>();
         //将返回数据XML转为Map格式
         Map<String, String> result = WXPayUtil.xmlToMap(xmlStr);

         if ("SUCCESS".equals(result.get("return_code")) && "SUCCESS".equals(result.get("result_code")) ) {
            resultMap.put("订单号", result.get("partner_trade_no"));
            resultMap.put("微信付款单号", result.get("payment_no"));
            resultMap.put("付款时间",result.get("payment_time"));
            resultMap.put("status","0");
            return resultMap;
         } else {
            resultMap.put("return_msg",result.get("return_msg"));
            resultMap.put("status","1");
            return resultMap;
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   @GetMapping("/get")
   public Object get() {
      ResultBo bo = new ResultBo();
      bo.setResult("ok");
      return bo;
   }
}
