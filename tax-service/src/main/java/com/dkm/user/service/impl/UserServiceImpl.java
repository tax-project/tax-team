package com.dkm.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.user.dao.UserMapper;
import com.dkm.user.entity.User;
import com.dkm.user.entity.bo.UserBO;
import com.dkm.user.service.IUserService;
import com.dkm.user.utils.WeChatUtil;
import com.dkm.user.utils.bo.WeChatUtilBO;
import com.dkm.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @Author: HuangJie
 * @Date: 2020/4/10 16:38
 * @Version: 1.0V
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private WeChatUtil weChatUtil;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IdGenerator idGenerator;
    @Override
    public UserBO bindUserInformation(String code, Integer status) {
        try {
            WeChatUtilBO weChatUtilBO = weChatUtil.codeToUserInfo(code);
            User user = new User();
            user.setId(idGenerator.getNumberId());
            user.setWxOpenId(weChatUtilBO.getDkmWeChatUserOpenId());
            user.setWxNickName(weChatUtilBO.getDkmWeChatUserNickName());
            user.setWxHeadImgUrl(weChatUtilBO.getDkmWeChatUserHeadImgUrl());
            switch (status){
                case 1:
                    //消费者
                    user.setRoleName("消费者");
                    user.setRoleStatus(1);
                    break;
                case 2:
                    //操作员
                    user.setRoleName("操作员");
                    user.setRoleStatus(2);
                    break;
                case 3:
                    //管理员
                    user.setRoleName("管理员");
                    user.setRoleStatus(3);
                    break;
                default:
                    //错误操作
                    break;
            }
            //查询该微信openID用户是否存在
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("wx_open_id",user.getWxOpenId());
            Integer count = userMapper.selectCount(queryWrapper);
            if (count>0){
                //用户已经存在，做用户信息更新
                UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("wx_open_id",user.getWxOpenId());
                int update = userMapper.update(user, updateWrapper);
                if (update!=1){
                    throw new ApplicationException(CodeType.SERVICE_ERROR, "用户信息更新失败");
                }
            }else {
                //用户不存在，做用户信息添加
                user.setStatus(0);
                user.setUpdateMuch(0);
                int insert = userMapper.insert(user);
                if (insert!=1){
                    throw new ApplicationException(CodeType.SERVICE_ERROR, "用户信息添加失败");
                }
            }

            UserBO userBO = new UserBO();
            BeanUtils.copyProperties(user,userBO);
            return userBO;
        } catch (IOException e) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "用户信息绑定失败");
        }
    }
}
