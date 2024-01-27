package com.ayi.ayiojbackendmodel.model.dto.question;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * className:QuestionEditDTO
 * Package:com.ayi.ayioj.model.dto.question
 * Description: 普通用户用
 *
 * @Date: 2023/10/21 16:39
 * @Author:hjy
 */
@Data
public class QuestionEditDTO implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

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
    private List<JudgeCase> judgeCass;

    /**
     * 判题配置
     */
    private JudgeConfig judgeConfig;

    private static final long serialVersionUID = 1L;
}
