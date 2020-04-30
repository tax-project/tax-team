package com.dkm.problem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.jwt.contain.LocalUser;
import com.dkm.jwt.entity.UserLoginQuery;
import com.dkm.problem.dao.ProblemMapper;
import com.dkm.problem.entity.Problem;
import com.dkm.problem.entity.vo.ProblemVo;
import com.dkm.problem.service.IProblemService;
import com.dkm.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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

   @Autowired
   private LocalUser localUser;

   private Map<Long,List<Long>> longListMap = new ConcurrentHashMap<>();

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

      UserLoginQuery user = localUser.getUser("user");

      //查询所有，随机抽取10条记录
      List<Problem> list = baseMapper.selectList(null);
      if (list.size() <= 10) {
         return list;
      }

      //大于10条则随机抽取10条
      Set<Integer> set = getList(list.size(),10);

      Set<Problem> result = new HashSet<>();
      for (int i = 0; i <= list.size()-1; i++) {
         for (Integer integer : set) {
            result.add(list.get(integer));
         }
      }

      //将生成的10位随机数据存入map中
      List<Long> answerList = new ArrayList<>();
      for (Problem problem : result) {
         answerList.add(problem.getId());
      }
      longListMap.put(user.getId(),answerList);

      //将set转List
      List<Problem> resultList = new ArrayList<>();
      resultList.addAll(result);
      return resultList;
   }

   /**
    * 从10条数据中返回5条数据答题
    * @return
    */
   @Override
   public List<Problem> listAnswerProblem() {
      UserLoginQuery user = localUser.getUser("user");

      //拿到所有数据的Id
      List<Long> list = longListMap.get(user.getId());

      if (list.size() == 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "您还未学习~");
      }

      List<Problem> problems = baseMapper.selectBatchIds(list);

      //从10条数据随机取5条
      Set<Integer> set = getList(10, 5);

      Set<Problem> result = new HashSet<>();
      for (int i = 0; i <= list.size()-1; i++) {
         for (Integer integer : set) {
            result.add(problems.get(integer));
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


   public Set<Integer> getList (Integer size, Integer count) {
      Set<Integer> set = getSet(size,count);
      if (set.size() < count) {
         while (true) {
            Set<Integer> set1 = getSet(size,count);
            if (set1.size() == count) {
               return set1;
            }

            if (set1.size() > count) {
               throw new ApplicationException(CodeType.SERVICE_ERROR, "获取题目失败");
            }
         }
      }
      return set;
   }

   public Set<Integer> getSet (Integer size, Integer count) {
      Set<Integer> set = new HashSet<Integer>();
      for (int i = 0; i < count; i++) {
         int random = (int) (Math.random()*size);
         set.add(random);
      }
      return set;
   }
}
