package com.dkm.user.utils.bo;

import lombok.Data;

/**
 * @Author: HuangJie
 * @Date: 2020/3/24 9:23
 * @Version: 1.0V
 */
@Data
public class WeChatUtilBO {
    /**
     * 典卡姆的微信普通用户标识ID，对当前开发者账户唯一
     */
    private String dkmWeChatUserOpenId;
    /**
     * 典卡姆的微信用户昵称
     */
    private String dkmWeChatUserNickName;
    /**
     * 典卡姆的微信用户性别
     */
    private String dkmWeChatUserSex;
    /**
     * 典卡姆的微信用户所在省份
     */
    private String dkmWeChatUserProvince;
    /**
     * 典卡姆的微信用户所在市
     */
    private String dkmWeChatUserCity;
    /**
     * 典卡姆的微信用户所在国家
     */
    private String dkmWeChatUserCountry;
    /**
     * 典卡姆的微信用户头像地址
     */
    private String dkmWeChatUserHeadImgUrl;
    /**
     * 用户特权信息时JSON数组
     */
    private String dkmWeChatUserPrivilege;
    /**
     * 典卡姆的微信用户同意标识，针对一个微信开发平台账号下的应用
     */
    private String dkmWeChatUserUnionId;
}
