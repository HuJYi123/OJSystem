package com.ayi.ayiojbackendquestionservice.service;

import com.ayi.ayiojbackendmodel.model.dto.questionSubmit.QuestionSubmitAddDTO;
import com.ayi.ayiojbackendmodel.model.dto.questionSubmit.QuestionSubmitQueryDTO;
import com.ayi.ayiojbackendmodel.model.entity.QuestionSubmit;
import com.ayi.ayiojbackendmodel.model.entity.User;
import com.ayi.ayiojbackendmodel.model.vo.QuestionSubmitVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author Administrator
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2023-10-21 16:21:47
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    long doQuestionSubmit(QuestionSubmitAddDTO dto, HttpServletRequest request);

    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User user);

    Page<QuestionSubmitVO> getListQuestionSubmitVO(Page<QuestionSubmit> questionSubmit, User user);

    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryDTO questionSubmitQueryRequest);
}
