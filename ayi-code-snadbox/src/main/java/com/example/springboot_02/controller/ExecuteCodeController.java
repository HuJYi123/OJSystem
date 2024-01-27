package com.example.springboot_02.controller;

import com.example.springboot_02.JavaCodeSandboxTemplate;
import com.example.springboot_02.JavaDockerCodeSandbox;
import com.example.springboot_02.JavaNativeCodeSandbox;
import com.example.springboot_02.model.ExecuteCodeRequest;
import com.example.springboot_02.model.ExecuteCodeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * className:ExecuteCodeController
 * Package:com.example.springboot_02.controller
 * Description: TODO
 *
 * @Date: 2023/12/26 22:22
 * @Author:hjy
 */
@RestController
@RequestMapping("/execute")
public class ExecuteCodeController {
    /**
     * 定义鉴权请求头和密钥
     */
    private static final String AUTH_HEADER = "Authorization";
    private static final String AUTH_KEY = "123456";

    @Autowired
    private JavaDockerCodeSandbox javaDockerCodeSandbox;


    @PostMapping("/executeCode")
    ExecuteCodeResponse executeCode(@RequestBody ExecuteCodeRequest executeCodeRequest, HttpServletRequest request, HttpServletResponse response) {
        if (executeCodeRequest == null) {
            throw new RuntimeException("参数为空");
        }
        String authHeader = request.getHeader(AUTH_HEADER);
        if (! authHeader.equals(AUTH_KEY)) {
            response.setStatus(401);
            return null;
        }
        switch (executeCodeRequest.getLanguage()) {
            case "java":
                return javaDockerCodeSandbox.executeCode(executeCodeRequest);
//                return new JavaNativeCodeSandbox().executeCode(executeCodeRequest);
//            case "python":
//                return new PythonDockerCodeSandBox().executeCode(executeCodeRequest);
//            case "c":
//                return new CDockerCodeSandBox().executeCode(executeCodeRequest);
//            case "c++":
//                return new CppDockerCodeSandBox().executeCode(executeCodeRequest);
//            case "go":
            default:
                return javaDockerCodeSandbox.executeCode(executeCodeRequest);
        }
    }
}
