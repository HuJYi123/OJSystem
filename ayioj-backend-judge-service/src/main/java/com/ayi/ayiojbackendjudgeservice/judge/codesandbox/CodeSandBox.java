package com.ayi.ayiojbackendjudgeservice.judge.codesandbox;

import com.ayi.ayiojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.ayi.ayiojbackendmodel.model.codesandbox.ExecuteCodeResponse;

/**
 * className:CodeSandBox
 * Package:com.ayi.ayioj.judge.codesandbox
 * Description: 代码沙箱执行接口
 *
 * @Date: 2023/12/12 23:45
 * @Author:hjy
 */
public interface CodeSandBox {

    /**
     * 执行代码
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
