package com.example.springboot_02.utils;

import com.example.springboot_02.model.ExecuteMessage;
import org.springframework.util.StopWatch;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * className:ExecuteUtils
 * Package:com.example.springboot_02.utils
 * Description: TODO
 *
 * @Date: 2023/12/14 21:36
 * @Author:hjy
 */
public class ProcessUtils {

    public static ExecuteMessage runProcessAndGetMessage(Process runProcess, String opName) {
        ExecuteMessage executeMessage = new ExecuteMessage();
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            //返回编译结果
            int exitValue = runProcess.waitFor();
            executeMessage.setExitValue(exitValue);
            // 正常退出
            if (exitValue == 0) {
                System.out.println(opName + "成功");
                // 分批获取进程控制台正常输出信息
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream(), "UTF-8"));
                StringBuilder compileOutputBuilder = new StringBuilder();
                // 逐行读取
                String compileOutputLine;
                while ((compileOutputLine = bufferedReader.readLine()) != null) {
                    compileOutputBuilder.append(compileOutputLine);
                }
                executeMessage.setMessage(compileOutputBuilder.toString());
            } else {
                System.out.println(opName + "失败， 错误码：" + exitValue);
                // 分批获取进程正常输出
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream(), "UTF-8"));
                StringBuilder compileOutputBuilder = new StringBuilder();
                // 逐行读取
                String compileOutputLine;
                while ((compileOutputLine = bufferedReader.readLine()) != null) {
                    compileOutputBuilder.append(compileOutputLine);
                }
                // 分批获取进程错误输出
                BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream(), "UTF-8"));
                StringBuilder errorCompileOutputBuilder = new StringBuilder();
                // 逐行读取
                String errorCompileOutputLine;
                while ((errorCompileOutputLine = errorBufferedReader.readLine()) != null) {
                    errorCompileOutputBuilder.append(errorCompileOutputLine);
                }
                executeMessage.setMessage(compileOutputBuilder.toString());
                executeMessage.setErrorMessage(errorCompileOutputBuilder.toString());
            }
            stopWatch.stop();
            executeMessage.setTime(stopWatch.getLastTaskTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return executeMessage;
    }
}
