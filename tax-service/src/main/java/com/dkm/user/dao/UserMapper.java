package com.dkm.user.dao;

import com.dkm.IBaseMapper.IBaseMapper;
import com.dkm.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @Author: HuangJie
 * @Date: 2020/4/10 16:51
 * @Version: 1.0V
 */
@Mapper
@Component
public interface UserMapper extends IBaseMapper<User> {
}
