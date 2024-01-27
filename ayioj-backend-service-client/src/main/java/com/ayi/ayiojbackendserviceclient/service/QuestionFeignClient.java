package com.ayi.ayiojbackendserviceclient.service;

import com.ayi.ayiojbackendcommom.common.BaseResponse;
import com.ayi.ayiojbackendmodel.model.dto.question.QuestionAddDTO;
import com.ayi.ayiojbackendmodel.model.dto.question.QuestionDeteleDTO;
import com.ayi.ayiojbackendmodel.model.dto.question.QuestionQueryDTO;
import com.ayi.ayiojbackendmodel.model.dto.question.QuestionUpdateDTO;
import com.ayi.ayiojbackendmodel.model.dto.questionSubmit.QuestionSubmitAddDTO;
import com.ayi.ayiojbackendmodel.model.dto.questionSubmit.QuestionSubmitQueryDTO;
import com.ayi.ayiojbackendmodel.model.entity.Question;
import com.ayi.ayiojbackendmodel.model.entity.QuestionSubmit;
import com.ayi.ayiojbackendmodel.model.vo.QuestionInfosVO;
import com.ayi.ayiojbackendmodel.model.vo.QuestionSubmitVO;
import com.ayi.ayiojbackendmodel.model.vo.QuestionVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
* @author Administrator
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2023-10-21 16:19:27
*/
@FeignClient(value = "ayioj-backend-question-service", path = "/api/question")
public interface QuestionFeignClient{

//    @PostMapping("/add")
//    BaseResponse<Boolean> add(@RequestBody QuestionAddDTO dto, HttpServletRequest request);
//
//    @PostMapping("/update")
//    BaseResponse<Boolean> update(@RequestBody QuestionUpdateDTO dto, HttpServletRequest request);
//
//    @PostMapping("/list/page")
//    BaseResponse<Page<Question>> listQuestion(@RequestBody QuestionQueryDTO dto);
//
//    @GetMapping("/get")
//    BaseResponse<Question> OneQuestion(Long id);
//
//    @PostMapping("/list/page/vo")
//    BaseResponse<Page<QuestionVO>> listQuestionVO(@RequestBody QuestionQueryDTO dto);
//
//    @GetMapping("/get/vo")
//    BaseResponse<QuestionVO> getQuestionVOById(long id, HttpServletRequest request);
//
//    @PostMapping("/delete")
//    BaseResponse<Boolean> deleteByIds(@RequestBody QuestionDeteleDTO dto, HttpServletRequest request);
//
//    @PostMapping("/question_submit/list/page")
//    BaseResponse<Page<QuestionSubmitVO>> listQueryQuestionSubmit(@RequestBody QuestionSubmitQueryDTO dto, HttpServletRequest request);
//
//    @PostMapping("/question_submit/do")
//    BaseResponse<Long> addQuestionSubmit(@RequestBody QuestionSubmitAddDTO dto, HttpServletRequest request);
//
//    @GetMapping("/getInfos")
//    BaseResponse<QuestionInfosVO> getInfos(HttpServletRequest request);
//
//
    @GetMapping("/inner/getQuestionSubmitById")
    QuestionSubmit getQuestionSubmitById(long questionSubmitId);

    @PostMapping("/inner/updateBysubmitId")
    boolean updateBysubmitId(@RequestBody QuestionSubmit questionSubmitUpdate);

    @GetMapping("/inner/getById")
    Question getById(@RequestParam("questionId") Long questionId);

    @PostMapping("/inner/updateById")
    boolean updateById(@RequestBody Question question);
}
