package com.dkm.problem.entity.vo;

import lombok.Data;

/**
 * @author qf
 * @date 2020/4/10
 * @vesion 1.0
 **/
@Data
public class ProblemVo {

   private Long id;

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
}
