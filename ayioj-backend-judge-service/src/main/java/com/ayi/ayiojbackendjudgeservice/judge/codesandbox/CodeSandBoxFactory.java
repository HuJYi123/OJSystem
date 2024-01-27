package com.ayi.ayiojbackendjudgeservice.judge.codesandbox;

import cn.hutool.core.util.StrUtil;
import com.ayi.ayiojbackendjudgeservice.judge.codesandbox.impl.ExampleCodeSandBox;
import com.ayi.ayiojbackendjudgeservice.judge.codesandbox.impl.RemoteCodeSandBox;
import com.ayi.ayiojbackendjudgeservice.judge.codesandbox.impl.ThirdPartyCodeSandBox;

/**
 * className:CodeSandBoxFactory
 * Package:com.ayi.ayioj.judge.codesandbox
 * Description: 代码沙箱工厂（根据字符串参数创建指定的代码沙箱实例）
 * 静态工厂模式
 *
 * @Date: 2023/12/13 0:10
 * @Author:hjy
 */
public class CodeSandBoxFactory {

    public static CodeSandBox createCodeSandBox(String codeSandBoxType) {
        if (StrUtil.isEmpty(codeSandBoxType)) {
            return null;
        }
        switch (codeSandBoxType) {
            case "example":
                return new ExampleCodeSandBox();
            case "remote":
                return new RemoteCodeSandBox();
            case "thirdParty":
                return new ThirdPartyCodeSandBox();
            default:
//                return new ExampleCodeSandBox();
                return null;
        }
    }
}
