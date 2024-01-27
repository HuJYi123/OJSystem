package com.ayi.ayiojbackendmodel.model.dto.question;

import lombok.Data;

import java.util.List;

/**
 * className:QuestionInfosDTO
 * Package:com.ayi.ayioj.model.dto.question
 * Description: TODO
 *
 * @Date: 2024/1/19 15:50
 * @Author:hjy
 */
@Data
public class QuestionInfosDTO {
    private Long count;

    private List<String> judgeInfo;
}
