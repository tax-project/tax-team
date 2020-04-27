package com.dkm.problem.service;

import com.dkm.problem.entity.Problem;
import com.dkm.problem.entity.vo.ProblemVo;

import java.util.List;

/**
 * @author qf
 * @date 2020/4/10
 * @vesion 1.0
 **/
public interface IProblemService {

   /**
    * 增加问题
    * 或修改问题
    * @param vo
    */
   void insertProblem (ProblemVo vo);

   /**
    * 删除问题
    * @param id
    */
   void deleteProblem (Long id);

   /**
    * 随机返回10条数据
    * @return
    */
   List<Problem> listProblem ();

   /**
    * 从10条数据中返回5条数据答题
    * @return
    */
   List<Problem> listAnswerProblem ();

   /**
    * 返回全部的题目
    * @return 题目集合
    */
   List<Problem> allListProblem();

}
