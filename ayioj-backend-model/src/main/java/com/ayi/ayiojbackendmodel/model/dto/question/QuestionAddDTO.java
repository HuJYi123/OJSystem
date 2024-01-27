package com.ayi.ayiojbackendmodel.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * className:QuestionAddDTO
 * Package:com.ayi.ayioj.model.dto.question
 * Description: TODO
 *
 * @Date: 2023/10/21 16:38
 * @Author:hjy
 */
@Data
public class QuestionAddDTO implements Serializable {

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 题目答案
     */
    private String answer;


    /**
     * 判题用例
     */
    private List<JudgeCase> judgeCase;

    /**
     * 判题配置
     */
    private JudgeConfig judgeConfig;

    private static final long serialVersionUID = 1L;
}
