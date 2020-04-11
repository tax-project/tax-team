package com.dkm.user.service;

import com.dkm.user.entity.bo.UserBO;
import com.dkm.user.entity.vo.UserVO;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 查询操作员的操作权限
     * @param id
     * @return
     */
    Integer queryOptionStatus (Long id);


    /**
     * 获取所有操作员
     * @return 设置操作员
     */
    List<UserVO> allOperator();

    /**
     * 设置操作员权限
     * @param id 操作员ID
     * @param status 操作员操作状态
     * @return 返回设置结果
     */
    Boolean operationPermission(Long id,Integer status);

}
