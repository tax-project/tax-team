package com.dkm.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.dkm.jwt.islogin.CheckToken;
import com.dkm.user.entity.bo.UserBO;
import com.dkm.user.entity.vo.UserVO;
import com.dkm.user.service.IUserService;
import com.dkm.user.token.CreateToken;
import com.dkm.user.utils.BodyUtils;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private CreateToken createToken;

    /**
     * 消费者登录接口
     * @param request 请求体
     * @return 返回用户信息实体类
     */
    @ApiOperation(value = "消费者用户绑定",notes = "传入消费者用户微信的code码",code = 200,produces = "application/json")
    @ApiImplicitParam(name = "code",value = "微信code码",dataType = "String",required = true,paramType = "body")
    @PostMapping("/consumers")
    @CrossOrigin
    public Map<String, Object> consumersUser(HttpServletRequest request){
        JSONObject json = BodyUtils.bodyJson(request);
        String code = json.getString("code");
        UserBO userBO = userService.bindUserInformation(code, 1);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userBO,userVO);
        String token = createToken.getToken(userBO);
        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        map.put("data",userVO);
        return map;
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
    public Map<String, Object> operatorUser(HttpServletRequest request){
        JSONObject json = BodyUtils.bodyJson(request);
        String code = json.getString("code");
        UserBO userBO = userService.bindUserInformation(code, 2);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userBO,userVO);
        String token = createToken.getToken(userBO);
        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        map.put("data",userVO);
        return map;
    }

    /**
     * 操作员登录接口
     * @param request 请求体
     * @return 返回用户信息实体类
     */
    @ApiOperation(value = "管理员用户绑定",notes = "传入操作员用户微信的code码",code = 200,produces = "application/json")
    @ApiImplicitParam(name = "code",value = "微信code码",dataType = "String",required = true,paramType = "body")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "data",response = UserVO.class)
    })
    @PostMapping("/admin")
    @CrossOrigin
    public Map<String, Object> adminUser(HttpServletRequest request){
        JSONObject json = BodyUtils.bodyJson(request);
        String code = json.getString("code");
        UserBO userBO = userService.bindUserInformation(code, 3);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userBO,userVO);
        String token = createToken.getToken(userBO);
        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        map.put("data",userVO);
        return map;
    }
    @ApiOperation(value = "获取所有的操作员",notes = "在HTTP头中携带TOKEN",code = 200,produces = "application/json")
    @ApiImplicitParam(name = "token",value = "用户Token，存放在HTTP的头部",required = true,dataType = "String",paramType = "header")
    @GetMapping("/all/operator")
    @CrossOrigin
    @CheckToken
    public List<UserVO> allOperator(){
        return userService.allOperator();
    }

    @ApiOperation(value = "设置操作员权限",notes = "传入操作员ID",code = 200,produces = "application/json")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "token",value = "用户Token，存放在HTTP的头部",required = true,dataType = "String",paramType = "header"),
        @ApiImplicitParam(name = "id",value = "操作员ID",dataType = "Long",required = true,paramType = "body"),
        @ApiImplicitParam(name = "status",value = "操作员状态，0代表无权限，1代表有权限",dataType = "Integer",required = true,paramType = "body")
    })
    @PostMapping("/operator/permission")
    @CrossOrigin
    @CheckToken
    public Boolean operationPermission(HttpServletRequest request){
        JSONObject json = BodyUtils.bodyJson(request);
        Long id = json.getLong("id");
        Integer status = json.getInteger("status");
        return userService.operationPermission(id,status);
    }

}