package com.dkm.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkm.admin.dao.UserAdminMapper;
import com.dkm.admin.entity.UserAdmin;
import com.dkm.admin.entity.vo.PageAdmin;
import com.dkm.admin.service.IUserAdminService;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.utils.IdGenerator;
import com.dkm.utils.ShaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qf
 * @date 2020/4/21
 * @vesion 1.0
 **/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserAdminServiceImpl extends ServiceImpl<UserAdminMapper, UserAdmin> implements IUserAdminService {

   @Autowired
   private IdGenerator idGenerator;

   @Override
   public synchronized void addUserAdmin(UserAdmin userAdmin) {

      if (userAdmin.getId() == null) {

         LambdaQueryWrapper<UserAdmin> wrapper = new LambdaQueryWrapper<UserAdmin>()
               .eq(UserAdmin::getIphone,userAdmin.getIphone());
         UserAdmin userAdmin1 = baseMapper.selectOne(wrapper);

         if (userAdmin1 != null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "该手机号已经添加过了");
         }

         String password = userAdmin.getPassword();
         userAdmin.setId(idGenerator.getNumberId());
         userAdmin.setPassword(ShaUtils.getSha1(password));
         int insert = baseMapper.insert(userAdmin);

         if (insert <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "添加失败");
         }
         return;
      }

      String password = userAdmin.getPassword();
      userAdmin.setId(userAdmin.getId());
      userAdmin.setPassword(ShaUtils.getSha1(password));
      int update = baseMapper.updateById(userAdmin);

      if (update <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "修改密码失败");
      }

   }

   @Override
   public void stopUserAdmin(Long id, Integer status) {
      UserAdmin userAdmin = new UserAdmin();
      userAdmin.setId(id);
      userAdmin.setStatus(status);
      int i = baseMapper.updateById(userAdmin);

      if (i <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "修改失败");
      }
   }

   @Override
   public Page<UserAdmin> listAllAdminInfo(PageAdmin pageAdmin) {
      Page<UserAdmin> page = new Page<>();
      page.setCurrent(pageAdmin.getCurrent());
      page.setSize(pageAdmin.getSize());

      baseMapper.selectPage(page, null);
      return page;
   }

   @Override
   public UserAdmin queryInfo(String iphone) {
      LambdaQueryWrapper<UserAdmin> wrapper = new LambdaQueryWrapper<UserAdmin>()
            .eq(UserAdmin::getIphone,iphone);
      return baseMapper.selectOne(wrapper);
   }
}
