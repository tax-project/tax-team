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
    * 随机返回5条数据
    * @return
    */
   List<Problem> listProblem ();

}
