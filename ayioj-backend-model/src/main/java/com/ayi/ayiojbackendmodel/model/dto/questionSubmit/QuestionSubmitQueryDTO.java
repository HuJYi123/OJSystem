package com.ayi.ayiojbackendmodel.model.dto.questionSubmit;

import com.ayi.ayiojbackendcommom.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * className:QuestionSubmitAddDTO
 * Package:com.ayi.ayioj.model.dto.questionSubmit
 * Description: TODO
 *
 * @Date: 2023/10/21 17:29
 * @Author:hjy
 */
@Data
public class QuestionSubmitQueryDTO extends PageRequest implements Serializable {
    /**
     * 编程语言
     */
    private String language;

    /**
     * 提交状态
     */
    private Integer status;

    /**
     * 题目 id
     */
    private Long questionId;


    /**
     * 用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}
