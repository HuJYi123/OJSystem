package com.ayi.ayiojbackendmodel.model.dto.question;

import lombok.Data;

/**
 * className:JudgeCase
 * Package:com.ayi.ayioj.model.dto.question
 * Description: 题目用例对象
 *
 * @Date: 2023/10/21 16:53
 * @Author:hjy
 */
@Data
public class JudgeCase {
    /**
     * 输入用例
     */
    private String input;

    /**
     * 输出用例
     */
    private String output;
}
