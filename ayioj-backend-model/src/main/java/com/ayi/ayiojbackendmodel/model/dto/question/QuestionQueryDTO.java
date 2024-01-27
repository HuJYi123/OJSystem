package com.ayi.ayiojbackendmodel.model.dto.question;

import com.ayi.ayiojbackendcommom.common.PageRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * className:QuestionQueryDTO
 * Package:com.ayi.ayioj.model.dto.question
 * Description: TODO
 *
 * @Date: 2023/10/21 16:40
 * @Author:hjy
 */
@Data
public class QuestionQueryDTO extends PageRequest implements Serializable {
    /**
     * id
     */
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
     * 创建用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}
