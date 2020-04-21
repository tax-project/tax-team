package com.dkm.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dkm.admin.entity.UserAdmin;
import com.dkm.admin.entity.vo.PageAdmin;

/**
 * @author qf
 * @date 2020/4/21
 * @vesion 1.0
 **/
public interface IUserAdminService {

   /**
    * 增加
    * @param userAdmin
    */
   void addUserAdmin (UserAdmin userAdmin);

   /**
    * 停用或启用
    * @param id
    * @param status
    */
   void stopUserAdmin (Long id, Integer status);

   /**
    *  分页展示所有管理员手机号
    * @param pageAdmin
    * @return
    */
   Page<UserAdmin> listAllAdminInfo (PageAdmin pageAdmin);

   /**
    * 根据手机号查询是否有管理员登录权限
    * @param iphone
    * @return
    */
   UserAdmin queryInfo (String iphone);
}
