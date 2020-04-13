package com.dkm.user.token;

import com.dkm.jwt.entity.UserLoginQuery;
import com.dkm.user.entity.bo.UserBO;
import com.dkm.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author qf
 * @date 2020/4/13
 * @vesion 1.0
 **/
@Component
public class CreateToken {

   @Autowired
   private JwtUtil jwtUtil;


   public String getToken (UserBO bo) {
      UserLoginQuery query = new UserLoginQuery();
      query.setId(bo.getId());
      query.setWxOpenId(bo.getWxOpenId());
      query.setRoleStatus(bo.getRoleStatus());
      query.setWxNickName(bo.getWxNickName());
      //24小时过期时间
      return jwtUtil.createJWT(1000 * 60 * 60 * 24L, query);
   }



}
