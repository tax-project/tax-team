package com.dkm.answer.controller;

import com.alibaba.fastjson.JSONObject;
import com.dkm.answer.service.IAnswerService;
import com.dkm.user.utils.BodyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: HuangJie
 * @Date: 2020/4/11 13:55
 * @Version: 1.0V
 */
@Api(tags = "答题接口")
@RestController
@RequestMapping("/answer")
public class AnswerController {

    @Autowired
    private IAnswerService answerService;

    @ApiOperation(value = "答题提交",notes = "以answer为键值，传递一个对象集合，内包括题目编号，用户编号，所选答案编号",code = 200,produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "problemId",value = "题目编号",dataType = "Long",required = true, paramType = "body"),
            @ApiImplicitParam(name = "userId",value = "用户编号",dataType = "Long",required = true, paramType = "body"),
            @ApiImplicitParam(name = "answer",value = "所选答案编号",dataType = "String",required = true, paramType = "body")
    })
    @PostMapping("/submit")
    @CrossOrigin
    public Boolean submitAnswer(HttpServletRequest request){
        JSONObject json = BodyUtils.bodyJson(request);
        String answer = json.getString("answer");

        return answerService.submitAnswer(answer);
    }
}
