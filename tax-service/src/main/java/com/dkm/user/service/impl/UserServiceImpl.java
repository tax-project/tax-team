package com.dkm.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: HuangJie
 * @Date: 2020/4/10 16:38
 * @Version: 1.0V
 */
@Service
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
                System.out.println("更新："+user.toString());

            }else {
                //用户不存在，做用户信息添加
                user.setId(idGenerator.getNumberId());
                user.setStatus(0);
                user.setUpdateMuch(0);
                int insert = userMapper.insert(user);
                if (insert!=1){
                    throw new ApplicationException(CodeType.SERVICE_ERROR, "用户信息添加失败");
                }
                System.out.println("新增："+user.toString());
            }

            UserBO userBO = new UserBO();
            BeanUtils.copyProperties(user,userBO);
            return userBO;
        } catch (IOException e) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "用户信息绑定失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(rollbackFor = Exception.class)
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
