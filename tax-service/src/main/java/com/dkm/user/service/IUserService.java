package com.dkm.user.service;

import com.dkm.user.entity.bo.UserBO;

/**
 * @Author: HuangJie
 * @Date: 2020/4/10 16:38
 * @Version: 1.0V
 */
public interface IUserService {

    /**
     * 绑定用户资料
     * @param code 微信用户的Code
     * @param status 角色的级别：消费者，操作员，管理员
     * @return 是否绑定的用户信息
     */
    UserBO bindUserInformation(String code, Integer status);

}
