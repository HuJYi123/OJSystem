package com.ayi.ayiojbackendjudgeservice.judge.codesandbox.impl;

import com.ayi.ayiojbackendjudgeservice.judge.codesandbox.CodeSandBox;
import com.ayi.ayiojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.ayi.ayiojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.ayi.ayiojbackendmodel.model.codesandbox.JudgeInfo;
import com.ayi.ayiojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.ayi.ayiojbackendmodel.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * className:CodeSandBoxImpl
 * Package:com.ayi.ayioj.judge.codesandbox
 * Description: 示例代码沙箱（仅用于跑通业务流程）
 *
 * @Date: 2023/12/12 23:57
 * @Author:hjy
 */
public class ExampleCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
