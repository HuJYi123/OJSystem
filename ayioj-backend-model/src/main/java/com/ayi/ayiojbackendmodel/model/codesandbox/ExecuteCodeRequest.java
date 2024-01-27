package com.ayi.ayiojbackendmodel.model.codesandbox;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * className:ExecuteCodeRequest
 * Package:com.ayi.ayioj.judge.codesandbox.model
 * Description: 代码沙箱执行请求参数
 *
 * @Date: 2023/12/12 23:46
 * @Author:hjy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExecuteCodeRequest {

    /**
     * 输入用例
     */
    private List<String> inputList;

    private String code;

    private String language;
}
