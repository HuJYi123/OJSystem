package com.ayi.ayiojbackendquestionservice.service;

import com.ayi.ayiojbackendmodel.model.dto.question.QuestionAddDTO;
import com.ayi.ayiojbackendmodel.model.dto.question.QuestionQueryDTO;
import com.ayi.ayiojbackendmodel.model.dto.question.QuestionUpdateDTO;
import com.ayi.ayiojbackendmodel.model.entity.Question;
import com.ayi.ayiojbackendmodel.model.vo.QuestionInfosVO;
import com.ayi.ayiojbackendmodel.model.vo.QuestionVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author Administrator
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2023-10-21 16:19:27
*/
public interface QuestionService extends IService<Question> {

    Boolean add(QuestionAddDTO dto, HttpServletRequest request);

    Page<QuestionVO> listQuestionVO(QuestionQueryDTO dto);

    QuestionVO getQuestionVO(Long id);

    Page<Question> listQuestion(QuestionQueryDTO dto);

    Question getQuestion(Long id);

    Boolean deleteByIds(Long ids, HttpServletRequest request);

    Boolean updateQuession(QuestionUpdateDTO dto, HttpServletRequest request);

    void validQuestion(Question question, boolean add);

    QueryWrapper<Question> getQueryWrapper(QuestionQueryDTO QuestionQueryDTO);

    QuestionVO getQuestionVO(Question question, HttpServletRequest request);

    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);

    QuestionInfosVO getInfos(HttpServletRequest request);
}
