package com.dkm.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dkm.admin.entity.UserAdmin;
import com.dkm.admin.entity.vo.PageAdmin;
import com.dkm.admin.entity.vo.ResultVo;
import com.dkm.admin.service.IUserAdminService;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.jwt.islogin.CheckToken;
import com.dkm.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author qf
 * @date 2020/4/21
 * @vesion 1.0
 **/
@Api(tags = "用户管理员的管理API")
@Slf4j
@RestController
@RequestMapping("/v1/admin")
public class UserAdminController {

   @Autowired
   private IUserAdminService userAdminService;

   @ApiOperation(value = "增加管理员手机号",notes = "增加管理员手机号",code = 200,produces = "application/json")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "status",value = "(0-正常  1-停用)",dataType = "int",required = true,paramType = "body"),
         @ApiImplicitParam(name = "name",value = "管理员名字",dataType = "String",required = true,paramType = "body"),
         @ApiImplicitParam(name = "iphone",value = "手机号",dataType = "String",required = true,paramType = "body"),
         @ApiImplicitParam(name = "password",value = "密码",dataType = "String",required = true,paramType = "body")
   })
   @PostMapping("/insert")
   @CrossOrigin
   @CheckToken
   public ResultVo addUserAdmin (@RequestBody UserAdmin userAdmin) {
      if (userAdmin.getStatus() == null || StringUtils.isBlank(userAdmin.getIphone())
            || StringUtils.isBlank(userAdmin.getName()) || StringUtils.isBlank(userAdmin.getPassword())) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }
      userAdminService.addUserAdmin(userAdmin);
      ResultVo vo = new ResultVo();
      vo.setResult("ok");
      return vo;
   }

   @ApiOperation(value = "停用或启用",notes = "停用或启用",code = 200,produces = "application/json")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "status",value = "(0-正常  1-停用)",dataType = "int",required = true,paramType = "body"),
         @ApiImplicitParam(name = "id",value = "id",dataType = "Long",required = true,paramType = "body")
   })
   @GetMapping("/stopUserAdmin")
   @CrossOrigin
   @CheckToken
   public ResultVo stopUserAdmin (@RequestParam("id") Long id, @RequestParam("status") Integer status) {
      if (id == null || status == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }
      userAdminService.stopUserAdmin(id,status);
      ResultVo vo = new ResultVo();
      vo.setResult("ok");
      return vo;
   }

   @ApiOperation(value = "分页展示所有手机号",notes = "分页展示所有手机号",code = 200,produces = "application/json")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "current",value = "当前页",dataType = "int",required = true,paramType = "body"),
         @ApiImplicitParam(name = "size",value = "每页显示条数",dataType = "int",required = true,paramType = "body")
   })
   @GetMapping("/listAllAdminInfo")
   @CrossOrigin
   @CheckToken
   public Page<UserAdmin> listAllAdminInfo (@RequestParam("current") Integer current, @RequestParam("size") Integer size) {
      if (current == null || size == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }
      PageAdmin admin = new PageAdmin();
      admin.setCurrent(current);
      admin.setSize(size);
      return userAdminService.listAllAdminInfo(admin);
   }
}
