package com.dkm.answer.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dkm.answer.dao.AnswerMapper;
import com.dkm.answer.entity.Answer;
import com.dkm.answer.service.IAnswerService;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.user.dao.UserMapper;
import com.dkm.user.entity.User;
import com.dkm.utils.DateUtil;
import com.dkm.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: HuangJie
 * @Date: 2020/4/11 14:05
 * @Version: 1.0V
 */
@Service
public class AnswerServiceImpl implements IAnswerService {
    @Autowired
    private AnswerMapper answerMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IdGenerator idGenerator;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean submitAnswer(String answer) {



        Long userId = null;
        JSONArray array = JSONArray.parseArray(answer);
        int arraySize = array.size();
        for (int i = 0; i<arraySize; i++){
            JSONObject json = array.getJSONObject(i);
            Long problemId = json.getLong("problemId");
            userId = json.getLong("userId");
            String answerNumber = json.getString("answer");
            Answer answerOne = new Answer();
            answerOne.setId(idGenerator.getNumberId());
            answerOne.setProblemId(problemId);
            answerOne.setUserId(userId);
            answerOne.setAnswer(answerNumber);
            int insert = answerMapper.insert(answerOne);
            if (insert!=1){
                throw new ApplicationException(CodeType.SERVICE_ERROR, "答题数据存储失败");
            }
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",userId);
        User user = userMapper.selectOne(queryWrapper);
        if (user!=null){
            Integer updateMuch = user.getUpdateMuch();
            LocalDateTime updateTime = user.getUpdateTime();

            if (updateTime!=null){
                //最后答题时间
                String formatDate = DateUtil.formatDate(updateTime);
                //现在时间
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String nowTime = format.format(new Date());
                if (nowTime.equals(formatDate)){
                    throw new ApplicationException(CodeType.SERVICE_ERROR, "今天已经领取过优惠卷了，请明天再试");
                }
            }

            if (updateMuch>=3){
                throw new ApplicationException(CodeType.SERVICE_ERROR, "您的领取次数已达上限，谢谢参与");
            }

        }
        return true;
    }
}
