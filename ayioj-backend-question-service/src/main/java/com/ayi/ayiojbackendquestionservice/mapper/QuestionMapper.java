package com.ayi.ayiojbackendquestionservice.mapper;

import com.ayi.ayiojbackendmodel.model.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Administrator
* @description 针对表【question(题目)】的数据库操作Mapper
* @createDate 2023-10-21 16:19:27
* @Entity com.ayi.ayioj.model.entity.Question
*/
public interface QuestionMapper extends BaseMapper<Question> {

    int selectQuestionByUserId(long id);

//    QuestionInfosDTO getQuestionInfos(Long userId);
}




