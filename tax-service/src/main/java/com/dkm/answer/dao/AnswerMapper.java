package com.dkm.answer.dao;

import com.dkm.IBaseMapper.IBaseMapper;
import com.dkm.answer.entity.Answer;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @Author: HuangJie
 * @Date: 2020/4/11 14:04
 * @Version: 1.0V
 */
@Mapper
@Component
public interface AnswerMapper extends IBaseMapper<Answer> {

}
