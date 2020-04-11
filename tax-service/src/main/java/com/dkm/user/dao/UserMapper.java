package com.dkm.user.dao;

import com.dkm.IBaseMapper.IBaseMapper;
import com.dkm.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

/**
 * @Author: HuangJie
 * @Date: 2020/4/10 16:51
 * @Version: 1.0V
 */
@Mapper
@Component
public interface UserMapper extends IBaseMapper<User> {
    /**
     * 设置用户的答题次数加一
     * @param userId 用户ID
     * @return 是否成功
     */
    @Update("update tb_user set update_much=update_much+1 where id = #{userId}")
    Integer increaseOne(Long userId);
}
