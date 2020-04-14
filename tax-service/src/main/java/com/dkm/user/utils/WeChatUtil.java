package com.dkm.user.utils;

import com.alibaba.fastjson.JSONObject;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.user.utils.bo.WeChatUtilBO;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: HuangJie
 * @Date: 2020/3/23 16:31
 * @Version: 1.0V
 */
@Component
public class WeChatUtil {

    private static final String APP_ID = "appid";
    private static final String SECRET = "secret";
    private static final String CODE = "code";
    private static final String GRANT_TYPE = "grant_type";
    private static final String AUTHORIZATION_CODE = "authorization_code";
    private static final String OPEN_ID = "openid";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String OK = "ok";
    private static final String UTF = "utf-8";
    private static final String ISO = "iso8859-1";

    @Value("${weChat.access.token.url}")
    private String weChatAccessTokenUrl;
    @Value("${weChat.access.token.confirmation.url}")
    private String weChatAccessTokenConfirmationUrl;
    @Value("${weChat.user.info.url}")
    private String weChatUserInfoUrl;
    @Value("${weChat.app.id}")
    private String weChatAppId;
    @Value("${weChat.secret}")
    private String weChatSecret;

    public WeChatUtilBO codeToUserInfo(String code) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //使用code换取accessToken和openId码
        HttpGet httpGet = new HttpGet(String.format("%s?%s=%s&%s=%s&%s=%s&%s=%s", weChatAccessTokenUrl, APP_ID, weChatAppId, SECRET, weChatSecret, CODE, code, GRANT_TYPE, AUTHORIZATION_CODE));
        CloseableHttpResponse responseToken = httpClient.execute(httpGet);
        String responseTokenStr = EntityUtils.toString(responseToken.getEntity());
        String accessToken = JSONObject.parseObject(responseTokenStr).getString("access_token");
        String openId = JSONObject.parseObject(responseTokenStr).getString("openid");
        //判断当前的accessToken码是否有效
        HttpGet httpGet1 = new HttpGet(weChatAccessTokenConfirmationUrl+"?"+ACCESS_TOKEN+"="+accessToken+"&"+OPEN_ID+"="+openId);
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet1);
        //ok表示当前accessToken状态码有效
        String returnState = JSONObject.parseObject(EntityUtils.toString(httpResponse.getEntity())).getString("errmsg");
        if (OK.equals(returnState)) {
            HttpGet httpGet2 = new HttpGet(String.format("%s?%s=%s&%s=%s", weChatUserInfoUrl, ACCESS_TOKEN, accessToken, OPEN_ID, openId));
            CloseableHttpResponse userInfoResponse = httpClient.execute(httpGet2);
            String userInfoResponseStrIso = EntityUtils.toString(userInfoResponse.getEntity());
            //http请求响应的参数以ISO8859-1进行的编码，需进行转换
            String userInfoResponseStrUtf = new String(userInfoResponseStrIso.getBytes(ISO), UTF);
            //获取授权得到的值
            JSONObject userInfoJson = JSONObject.parseObject(userInfoResponseStrUtf);
            WeChatUtilBO weChatUtilBO = new WeChatUtilBO();
            weChatUtilBO.setDkmWeChatUserOpenId(userInfoJson.getString("openid"));
            weChatUtilBO.setDkmWeChatUserNickName(userInfoJson.getString("nickname"));
            weChatUtilBO.setDkmWeChatUserSex(userInfoJson.getString("sex"));
            weChatUtilBO.setDkmWeChatUserProvince(userInfoJson.getString("province"));
            weChatUtilBO.setDkmWeChatUserCity(userInfoJson.getString("city"));
            weChatUtilBO.setDkmWeChatUserCountry(userInfoJson.getString("country"));
            weChatUtilBO.setDkmWeChatUserHeadImgUrl(userInfoJson.getString("headimgurl"));
            weChatUtilBO.setDkmWeChatUserPrivilege(userInfoJson.getString("privilege"));
            weChatUtilBO.setDkmWeChatUserUnionId(userInfoJson.getString("unionid"));
            return weChatUtilBO;
        }else {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"accessToken状态码失效，请重试");
        }
    }
}
