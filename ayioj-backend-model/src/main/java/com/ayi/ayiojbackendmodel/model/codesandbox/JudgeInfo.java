package com.ayi.ayiojbackendmodel.model.codesandbox;

import lombok.Data;

/**
 * className:JudgeInfo
 * Package:com.ayi.ayioj.model.dto.questionSubmit
 * Description: TODO
 *
 * @Date: 2023/10/21 17:01
 * @Author:hjy
 */
@Data
public class JudgeInfo {
    /**
     * 程序执行信息
     */
    private String message;

    /**
     * 消耗内存
     */
    private Long memory;

    /**
     * 消耗时间(kb)
     */
    private Long time;
}
