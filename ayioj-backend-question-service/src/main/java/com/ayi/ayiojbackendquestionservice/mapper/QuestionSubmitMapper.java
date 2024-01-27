package com.ayi.ayiojbackendquestionservice.mapper;

import com.ayi.ayiojbackendmodel.model.entity.QuestionSubmit;
import com.ayi.ayiojbackendmodel.model.vo.QuestionSubmitTestVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author Administrator
* @description 针对表【question_submit(题目提交)】的数据库操作Mapper
* @createDate 2023-10-21 16:21:46
* @Entity com.ayi.ayioj.model.entity.QuestionSubmit
*/
public interface QuestionSubmitMapper extends BaseMapper<QuestionSubmit> {

    List<QuestionSubmitTestVO> getQuestionSubmit();
}




