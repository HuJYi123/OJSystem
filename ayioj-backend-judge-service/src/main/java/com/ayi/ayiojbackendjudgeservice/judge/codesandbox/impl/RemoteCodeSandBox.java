package com.ayi.ayiojbackendjudgeservice.judge.codesandbox.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.ayi.ayiojbackendcommom.exception.BusinessException;
import com.ayi.ayiojbackendjudgeservice.judge.codesandbox.CodeSandBox;
import com.ayi.ayiojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.ayi.ayiojbackendmodel.model.codesandbox.ExecuteCodeResponse;

/**
 * className:CodeSandBoxImpl
 * Package:com.ayi.ayioj.judge.codesandbox
 * Description: 远程代码沙箱（实际调用接口的沙箱）
 *
 * @Date: 2023/12/12 23:57
 * @Author:hjy
 */
public class RemoteCodeSandBox implements CodeSandBox {

    /**
     * 定义鉴权请求头和密钥
     */
    private static final String AUTH_HEADER = "Authorization";
    private static final String AUTH_KEY = "123456";
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        String url = "http://192.168.93.130:8103/api/execute/executeCode";
        String jsonStr = JSONUtil.toJsonStr(executeCodeRequest);
        String response = HttpUtil.createPost(url).
                header(AUTH_HEADER, AUTH_KEY).
                body(jsonStr).
                execute().
                body();
        if (StrUtil.isEmpty(response)) {
            throw new BusinessException(500, "远程代码沙箱调用失败");
        }
        return JSONUtil.toBean(response, ExecuteCodeResponse.class);
    }
}
