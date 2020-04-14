package com.dkm.problem.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author qf
 * @date 2020/4/10
 * @vesion 1.0
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("tb_problem")
public class Problem extends Model<Problem> {

   private Long id;

   /**
    * 问题
    */
   private String problemName;

   /**
    * 答案A
    */
   private String answerA;

   /**
    * 答案B
    */
   private String answerB;

   /**
    * 答案C
    */
   private String answerC;

   /**
    * 答案D
    */
   private String answerD;

   /**
    * 正确答案
    */
   private String answer;
}
