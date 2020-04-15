package com.dkm.problem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.problem.dao.ProblemMapper;
import com.dkm.problem.entity.Problem;
import com.dkm.problem.entity.vo.ProblemVo;
import com.dkm.problem.service.IProblemService;
import com.dkm.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author qf
 * @date 2020/4/10
 * @vesion 1.0
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements IProblemService {

   @Autowired
   private IdGenerator idGenerator;

   /**
    * 增加或修改问题
    * @param vo
    */
   @Override
   public void insertProblem(ProblemVo vo) {
      Problem problem = new Problem();
      if (vo.getId() == null) {
         //增加
         problem.setId(idGenerator.getNumberId());
         problem.setProblemName(vo.getProblemName());
         problem.setAnswerA(vo.getAnswerA());
         problem.setAnswerB(vo.getAnswerB());
         problem.setAnswerC(vo.getAnswerC());
         problem.setAnswerD(vo.getAnswerD());
         problem.setAnswer(vo.getAnswer());

         int insert = baseMapper.insert(problem);

         if (insert <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "增加问题失败");
         }
      }

      //修改
      if (vo.getId() != null) {
         problem.setId(vo.getId());
         problem.setProblemName(vo.getProblemName());
         problem.setAnswerA(vo.getAnswerA());
         problem.setAnswerB(vo.getAnswerB());
         problem.setAnswerC(vo.getAnswerC());
         problem.setAnswerD(vo.getAnswerD());
         problem.setAnswer(vo.getAnswer());

         int update = baseMapper.updateById(problem);

         if (update <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "修改问题失败");
         }
      }
   }

   @Override
   public void deleteProblem(Long id) {

      int deleteById = baseMapper.deleteById(id);

      if (deleteById <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "删除失败");
      }
   }

   @Override
   public List<Problem> listProblem() {
      //查询所有，随机抽取5条记录
      List<Problem> list = baseMapper.selectList(null);
      if (list.size() <= 5) {
         return list;
      }

      //大于5条则随机抽取5条
      Set<Integer> set = getList(list.size());

      Set<Problem> result = new HashSet<>();
      for (int i = 0; i <= list.size()-1; i++) {
         for (Integer integer : set) {
            result.add(list.get(integer));
         }
      }

      //将set转List
      List<Problem> resultList = new ArrayList<>();
      resultList.addAll(result);
      return resultList;
   }

   @Override
   public List<Problem> allListProblem() {
      return baseMapper.selectList(null);
   }


   public Set<Integer> getList (Integer size) {
      Set<Integer> set = getSet(size);
      if (set.size() < 5) {
         while (true) {
            Set<Integer> set1 = getSet(size);
            if (set1.size() == 5) {
               return set1;
            }
         }
      }
      return set;
   }

   public Set<Integer> getSet (Integer size) {
      Set<Integer> set = new HashSet<Integer>();
      for (int i = 0; i <= 4; i++) {
         int random = (int) (Math.random()*size);
         set.add(random);
      }
      return set;
   }
}
