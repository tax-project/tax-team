package com.dkm.answer.entity.bo;

import lombok.Data;

/**
 * @Author: HuangJie
 * @Date: 2020/4/11 14:01
 * @Version: 1.0V
 */
@Data
public class AnswerBO {
    /**
     * 答题提交编号
     */
    private Long id;
    /**
     * 所答题目编号
     */
    private Long problemId;
    /**
     * 答题用户编号
     */
    private Long userId;
    /**
     * 所选答案
     */
    private String answer;
}
