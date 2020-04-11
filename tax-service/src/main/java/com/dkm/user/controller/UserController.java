package com.dkm.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.dkm.user.entity.bo.UserBO;
import com.dkm.user.entity.vo.UserVO;
import com.dkm.user.service.IUserService;
import com.dkm.user.utils.BodyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: HuangJie
 * @Date: 2020/4/10 16:29
 * @Version: 1.0V
 */
@Api(tags = "用户信息接口")
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
        UserBO userBO = userService.bindUserInformation(code, 3);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userBO,userVO);
        return userVO;
    }
    @ApiOperation(value = "获取所有的操作员",notes = "无",code = 200,produces = "application/json")
    @GetMapping("/all/operator")
    @CrossOrigin
    public List<UserVO> allOperator(){
        return userService.allOperator();
    }

    @ApiOperation(value = "设置操作员权限",notes = "传入操作员ID",code = 200,produces = "application/json")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id",value = "操作员ID",dataType = "Long",required = true,paramType = "body"),
        @ApiImplicitParam(name = "status",value = "操作员状态，0代表无权限，1代表有权限",dataType = "Integer",required = true,paramType = "body")
    })
    @PostMapping("/operator/permission")
    @CrossOrigin
    public Boolean operationPermission(HttpServletRequest request){
        JSONObject json = BodyUtils.bodyJson(request);
        Long id = json.getLong("id");
        Integer status = json.getInteger("status");
        return userService.operationPermission(id,status);
    }
}