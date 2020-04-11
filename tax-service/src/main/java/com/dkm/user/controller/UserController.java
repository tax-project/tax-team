package com.dkm.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.dkm.user.entity.bo.UserBO;
import com.dkm.user.entity.vo.UserVO;
import com.dkm.user.service.IUserService;
import com.dkm.user.utils.BodyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: HuangJie
 * @Date: 2020/4/10 16:29
 * @Version: 1.0V
 */
@Api(tags = "用户信息绑定接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 消费者登录接口
     * @param request 请求体
     * @return 返回用户信息实体类
     */
    @ApiOperation(value = "消费者用户绑定",notes = "传入消费者用户微信的code码",code = 200,produces = "application/json")
    @ApiImplicitParam(name = "code",value = "微信code码",dataType = "String",required = true,paramType = "body")
    @PostMapping("/consumers")
    @CrossOrigin
    public UserVO consumersUser(HttpServletRequest request){
        JSONObject json = BodyUtils.bodyJson(request);
        String code = json.getString("code");
        UserBO userBO = userService.bindUserInformation(code, 1);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userBO,userVO);
        return userVO;
    }

    /**
     * 操作员登录接口
     * @param request 请求体
     * @return 返回用户信息实体类
     */
    @ApiOperation(value = "操作员用户绑定",notes = "传入操作员用户微信的code码",code = 200,produces = "application/json")
    @ApiImplicitParam(name = "code",value = "微信code码",dataType = "String",required = true,paramType = "body")
    @PostMapping("/operator")
    @CrossOrigin
    public UserVO operatorUser(HttpServletRequest request){
        JSONObject json = BodyUtils.bodyJson(request);
        String code = json.getString("code");
        UserBO userBO = userService.bindUserInformation(code, 2);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userBO,userVO);
        return userVO;
    }

    /**
     * 操作员登录接口
     * @param request 请求体
     * @return 返回用户信息实体类
     */
    @ApiOperation(value = "管理员用户绑定",notes = "传入操作员用户微信的code码",code = 200,produces = "application/json")
    @ApiImplicitParam(name = "code",value = "微信code码",dataType = "String",required = true,paramType = "body")
    @PostMapping("/admin")
    @CrossOrigin
    public UserVO adminUser(HttpServletRequest request){
        JSONObject json = BodyUtils.bodyJson(request);
        String code = json.getString("code");
        UserBO userBO = userService.bindUserInformation(code, 2);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userBO,userVO);
        return userVO;
    }
}