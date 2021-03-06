package com.dkm.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dkm.admin.entity.UserAdmin;
import com.dkm.admin.service.IUserAdminService;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.jwt.contain.LocalUser;
import com.dkm.jwt.entity.UserLoginQuery;
import com.dkm.user.dao.UserMapper;
import com.dkm.user.entity.User;
import com.dkm.user.entity.bo.UserBO;
import com.dkm.user.entity.vo.UserVO;
import com.dkm.user.service.IUserService;
import com.dkm.user.utils.WeChatUtil;
import com.dkm.user.utils.bo.WeChatUtilBO;
import com.dkm.utils.IdGenerator;
import com.dkm.utils.ShaUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: HuangJie
 * @Date: 2020/4/10 16:38
 * @Version: 1.0V
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements IUserService {

    private final static Integer ADMIN_NUM = 3;

    @Autowired
    private WeChatUtil weChatUtil;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private LocalUser localUser;

    @Autowired
    private IUserAdminService userAdminService;
    @Override
    public UserBO bindUserInformation(String code, Integer status) {
        try {
            WeChatUtilBO weChatUtilBO = weChatUtil.codeToUserInfo(code);
            User user = new User();
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
            System.out.println("count:"+count);
            if (count>0){
                //用户已经存在，做用户信息更新
                UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("wx_open_id",user.getWxOpenId());
                int update = userMapper.update(user, updateWrapper);
                if (update!=1){
                    throw new ApplicationException(CodeType.SERVICE_ERROR, "用户信息更新失败");
                }
                QueryWrapper<User> wrapper = new QueryWrapper<>();
                wrapper.eq("wx_open_id",user.getWxOpenId());
                user = userMapper.selectOne(wrapper);

            }else {
                //用户不存在，做用户信息添加
                user.setId(idGenerator.getNumberId());
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

    @Override
    public List<UserVO> allOperator() {
        UserLoginQuery localUserUser = localUser.getUser("user");
        Integer roleStatus = localUserUser.getRoleStatus();
        if (!roleStatus.equals(ADMIN_NUM)){
            throw new ApplicationException(CodeType.SERVICE_ERROR, "您的权限不够");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_status",2);
        List<User> users = userMapper.selectList(queryWrapper);
        if (users!=null){
            return users.stream().map(user -> {
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(user, userVO);
                return userVO;
            }).collect(Collectors.toList());
        }
        return new ArrayList<UserVO>();
    }

    @Override
    public Boolean operationPermission(Long id,Integer status) {
        UserLoginQuery localUserUser = localUser.getUser("user");
        Integer roleStatus = localUserUser.getRoleStatus();
        if (!roleStatus.equals(ADMIN_NUM)){
            throw new ApplicationException(CodeType.SERVICE_ERROR, "您的权限不够");
        }
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",user.getId());
        int update = userMapper.update(user, updateWrapper);
        return update == 1;
    }

    @Override
    public UserBO bindUserAdminInformation(String code, Integer status, String iphone, String password) {

        //先判断该手机号是否为管理员手机
        UserAdmin userAdmin = userAdminService.queryInfo(iphone);

        if (userAdmin == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "您没有管理员登录权限");
        }

        if (userAdmin.getStatus() == 1) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "该账号被冻结，请联系管理员解冻");
        }

        String s = userAdmin.getPassword();
        String word = ShaUtils.getSha1(password);
        if (!word.equals(s)) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "您输入的密码错误");
        }

        try {
            WeChatUtilBO weChatUtilBO = weChatUtil.codeToUserInfo(code);
            User user = new User();
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
            System.out.println("count:"+count);
            if (count>0){
                //用户已经存在，做用户信息更新
                UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("wx_open_id",user.getWxOpenId());
                int update = userMapper.update(user, updateWrapper);
                if (update!=1){
                    throw new ApplicationException(CodeType.SERVICE_ERROR, "用户信息更新失败");
                }
                QueryWrapper<User> wrapper = new QueryWrapper<>();
                wrapper.eq("wx_open_id",user.getWxOpenId());
                user = userMapper.selectOne(wrapper);

            }else {
                //用户不存在，做用户信息添加
                user.setId(idGenerator.getNumberId());
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

    /**
     * 修改时间和次数
     * @param date
     */
    @Override
    public void updateTime(LocalDateTime date, Long userId) {
        User user = new User();
        user.setId(userId);
        user.setUpdateTime(LocalDateTime.now());
        int i = userMapper.updateById(user);

        if (i <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"修改失败");
        }

        //领卷次数加一
        Integer integer = userMapper.increaseOne(userId);
        if (integer!=1){
            throw new ApplicationException(CodeType.SERVICE_ERROR, "操作异常，请重试");
        }
    }

    /**
     * 查询操作员的操作权限
     * @param id
     * @return
     */
    @Override
    public Integer queryOptionStatus(Long id) {
        User user = userMapper.selectById(id);
        return user.getStatus();
    }
}
