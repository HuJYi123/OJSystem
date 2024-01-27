package com.ayi.ayiojbackendjudgeservice.judge.codesandbox;

import com.ayi.ayiojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.ayi.ayiojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * className:CodeSandBoxProxy
 * Package:com.ayi.ayioj.judge.codesandbox
 * Description: 代理类，做增强（静态代理）
 *
 * @Date: 2023/12/13 0:30
 * @Author:hjy
 */
@Slf4j
public class CodeSandBoxProxy implements CodeSandBox{

    private final CodeSandBox codeSandBox;

    public CodeSandBoxProxy(CodeSandBox codeSandBox) {
        this.codeSandBox = codeSandBox;
    }
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求信息：" + executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);
        log.info("代码沙箱相应信息：" + executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
