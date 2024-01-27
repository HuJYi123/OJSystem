package com.ayi.ayiojbackendmodel.model.codesandbox;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * className:ExecuteCodeRequest
 * Package:com.ayi.ayioj.judge.codesandbox.model
 * Description: 代码沙箱执行返回
 *
 * @Date: 2023/12/12 23:46
 * @Author:hjy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExecuteCodeResponse {

    /**
     * 输出用例
     */
    private List<String> outputList;

    /**
     * 执行信息
     */
    private String message;

    /**
     * 执行状态
     */
    private Integer status;

    /**
     * 判题信息
     */
    private JudgeInfo judgeInfo;

}
