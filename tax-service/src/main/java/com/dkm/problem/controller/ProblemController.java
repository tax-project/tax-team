package com.dkm.problem.controller;

import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.jwt.islogin.CheckToken;
import com.dkm.problem.entity.Problem;
import com.dkm.problem.entity.vo.ProblemVo;
import com.dkm.problem.service.IProblemService;
import com.dkm.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qf
 * @date 2020/4/11
 * @vesion 1.0
 **/
@Api(tags = "问题Api")
@RestController
@RequestMapping("/v1/problem")
public class ProblemController {

   @Autowired
   private IProblemService problemService;


   @ApiOperation(value = "增加问题或修改问题", notes = "增加问题或修改问题")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "id", value = "(传null为增加,传id则为修改)", required = false, dataType = "Long", paramType = "path"),
         @ApiImplicitParam(name = "problemName", value = "问题", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "answerA", value = "答案A", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "answerB", value = "答案B", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "answerC", value = "答案C", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "answerD", value = "答案D", required = true, dataType = "String", paramType = "path"),
   })
   @PostMapping("/insertProblem")
   @CrossOrigin
   @CheckToken
   public void insertProblem (@RequestBody ProblemVo vo) {

      if (StringUtils.isBlank(vo.getAnswerA()) || StringUtils.isBlank(vo.getAnswerB()) || StringUtils.isBlank(vo.getAnswerC())
      || StringUtils.isBlank(vo.getAnswerD()) || StringUtils.isBlank(vo.getProblemName())) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }

      problemService.insertProblem(vo);

   }


   @ApiOperation(value = "删除问题", notes = "删除问题")
   @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path")
   @GetMapping("/deleteProblem")
   @CrossOrigin
   @CheckToken
   public void deleteProblem (@RequestParam("id") Long id) {

      if (id == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }

      problemService.deleteProblem(id);

   }



   @ApiOperation(value = "随机返回5条数据", notes = "随机返回5条数据")
   @GetMapping("/listProblem")
   @CrossOrigin
   @CheckToken
   public List<Problem> listProblem () {
      return problemService.listProblem ();
   }


}
