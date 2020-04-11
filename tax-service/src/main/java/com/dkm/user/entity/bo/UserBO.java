package com.dkm.user.entity.bo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: HuangJie
 * @Date: 2020/4/11 9:51
 * @Version: 1.0V
 */
@Data
public class UserBO {
    /**
     * 用户ID
     */
    private Long id;
    /**
     * 用户微信openid
     */
    private String wxOpenId;
    /**
     * 用户微信昵称
     */
    private String wxNickName;
    /**
     * 用户微信头像地址
     */
    private String wxHeadImgUrl;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色级别
     */
    private Integer roleStatus;
    /**
     * 答题提交时间
     */
    private LocalDateTime updateTime;
    /**
     *  答题提交次数
     */
    private Integer updateMuch;
    /**
     * 操作员认证
     */
    private Integer status;
}
