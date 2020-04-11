package com.dkm.answer.service;

/**
 * @Author: HuangJie
 * @Date: 2020/4/11 14:05
 * @Version: 1.0V
 */
public interface IAnswerService {

    /**
     * 提交答题试题
     * @param answer 试题JSON字符串
     * @return 是否存储成功
     */
    Boolean submitAnswer(String answer);

}
