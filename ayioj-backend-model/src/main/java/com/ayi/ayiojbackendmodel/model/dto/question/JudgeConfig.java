package com.ayi.ayiojbackendmodel.model.dto.question;

import lombok.Data;

/**
 * className:judgeConfig
 * Package:com.ayi.ayioj.model.dto.question
 * Description: 判题配置对象
 *
 * @Date: 2023/10/21 16:54
 * @Author:hjy
 */
@Data
public class JudgeConfig {

    /**
     * 时间限制（单位：ms）
     */
    private Long timeLimit;

    /**
     * 内存限制（单位：kb）
     */
    private Long memoryLimit;

    /**
     * 堆栈限制（单位：kb）
     */
    private Long stackLimit;
}
