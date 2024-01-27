package com.example.springboot_02;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.example.springboot_02.model.ExecuteCodeRequest;
import com.example.springboot_02.model.ExecuteCodeResponse;
import com.example.springboot_02.model.ExecuteMessage;
import com.example.springboot_02.model.JudgeInfo;
import com.example.springboot_02.utils.ProcessUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * className:JavaNativeCodeSandBox
 * Package:com.example.springboot_02
 * Description: 使用java原生编译代码且执行
 *
 * @Date: 2023/12/14 0:02
 * @Author:hjy
 */
public class JavaNativeCodeSandbox implements CodeSandbox {

    private final String GLOBAL_CODE_DIR_NAME = "tmpCode";

    private final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    private final long TIME_OUT = 5000L;

    private static final String SECURITY_MANAGER_PATH = "D:\\OJSystem\\ayi-code-snadbox\\src\\main\\resources\\security";

    private static final String SECURITY_MANAGE_CLASS_NAME = "MySecurityManager";
    /** 黑名单，防止用户输入幽灵代码，进行非法操作*/
    private static final List<String> BLACK_LIST = Arrays.asList("exec", "Files");

    /**
     * Hutool字典树工具类，可以用更少的空间存储更多的敏感词汇，实现更高效的敏感词查找
     */
    private static final WordTree WORD_TREE;

    static {
        WORD_TREE = new WordTree();
        WORD_TREE.addWords(BLACK_LIST);
    }

    public static void main(String[] args) {
        JavaNativeCodeSandbox javaNativeCodeSandBox = new JavaNativeCodeSandbox();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setInputList(Arrays.asList("1 5", " 1 3"));
        // 读取claapath资源下的main代码
        String code = ResourceUtil.readStr("testCode/simpleComputeArgs/Main.java", StandardCharsets.UTF_8);
        executeCodeRequest.setCode(code);
        executeCodeRequest.setLanguage("java");
        System.setProperty("file.encoding", "UTF-8");
        ExecuteCodeResponse executeCodeResponse = javaNativeCodeSandBox.executeCode(executeCodeRequest);
        System.out.println(executeCodeResponse);
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        // 使用安全管理器来管理允许用户提交的代码里面的哪些操作，安全管理器不应该加在这个地方，因为会限制我们自己写的代码，应该限制的是用户提交的代码
//        System.setSecurityManager(new MySecurityManager());
        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();
        // 使用Hutool工具包进行敏感词过滤，检验代码中是否有黑名单代码
//        WordTree wordTree = new WordTree();
//        wordTree.addWords(BLACK_LIST);
        FoundWord foundWord = WORD_TREE.matchWord(code);
        if (foundWord != null) {
            System.out.println("非法代码：" + foundWord.getFoundWord());
            return null;
        }

        // 1、把用户代码保存为文件
        // 获取项目当前目录
        String userDir = System.getProperty("user.dir");
        // 创建tmpCode文件夹路径
        String globalCodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;

        // 判断全局代码目录是否存在，没有则新建
        if (!FileUtil.exist(globalCodePathName)) {
            FileUtil.mkdir(globalCodePathName);
        }

        // 用户代码放到tmpCode文件夹下，并且存到到各自的文件夹进行隔离
        // 把用户的代码隔离存放
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + GLOBAL_JAVA_CLASS_NAME;
        // 把代码以utf-8编码写到文件中，返回文件对象
        File file = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);

        // 2、得到class文件，获取class文件绝对路径，拼接获取命令行代码
        String compileCmd = String.format("javac -encoding utf-8 %s", file.getAbsolutePath());
        // 执行编译命令
        try {
            Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(compileProcess, "编译");
            System.out.println(executeMessage);
        } catch (Exception e) {
           return tryExceptionAngResponse(e);
        }
        // 3、执行代码，得到输出结果，使用for循环执行多个输入用例
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String inputArgs : inputList) {
            // 在JVM层面指定每个进程执行的最大占用内存为256m，超过这个内存程序就会停止，防止错误程序卡崩系统，防止内存溢出。-Xmx(最大堆空间大小) -Xms(初始堆空间大小)
//            String runCmd = String.format("java -Xmx256m -Dfile-encoding=UTF-8 -cp %s Main %s", userCodeParentPath, inputArgs);
            // 实现在执行用户代码时在命令行设置安全管理器
            String runCmd = String.format("java -Xmx256m -Dfile-encoding=UTF-8 -cp %s;%s -Djava.security.manage=%s Main %s", userCodeParentPath, SECURITY_MANAGER_PATH, SECURITY_MANAGE_CLASS_NAME, inputArgs);
            try {
                Process runProcess = Runtime.getRuntime().exec(runCmd);
                // 解决程序执行超时问题
                new Thread(() -> {
                    try {
                        // 当超过定义的超时时间时，杀死执行程序
                        Thread.sleep(TIME_OUT);
                        // 当该程序还在运行，还在运行说明超过我们分配的程序运行执行实践，立即终止
                        if (runProcess.isAlive()) {
                            runProcess.destroy();
                        }
                    }catch (Exception e) {
                        tryExceptionAngResponse(e);
                        e.printStackTrace();
                    }
                }).start();
                ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(runProcess, "运行");
                executeMessageList.add(executeMessage);
                System.out.println(executeMessage);
            } catch (Exception e) {
                tryExceptionAngResponse(e);
            }
        }
        // 4、处理输出结果
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        List<String> outputList = new ArrayList<>();
        // 取用时最大值，用于判断是否超时
        long maxTime = 0;
        for (ExecuteMessage executeMessage : executeMessageList) {
            String errorMessage = executeMessage.getErrorMessage();
            // 如果有错误信息，说明程序执行出错，结束遍历
            if (StrUtil.isNotBlank(errorMessage)) {
                executeCodeResponse.setMessage(errorMessage);
                executeCodeResponse.setStatus(3);
                break;
            }
            outputList.add(executeMessage.getMessage());
            Long time = executeMessage.getTime();
            if (time != null) {
                maxTime = Math.max(maxTime, time);
            }
        }
        if (outputList.size() == executeMessageList.size()) {
            executeCodeResponse.setStatus(1);
        }
        executeCodeResponse.setOutputList(outputList);
        JudgeInfo judgeInfo = new JudgeInfo();
//        judgeInfo.setMessage();
//        judgeInfo.setMemory();
        judgeInfo.setTime(maxTime);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        // 清理文件，释放空间
        if (file.getParentFile() != null) {
            boolean del = FileUtil.del(userCodeParentPath);
            System.out.println("删除：" + (del ? "成功" : "失败"));
        }
        return executeCodeResponse;
    }

    /**
     * 获取错误响应
     * 处理错误信息，提升程序健壮性
     * @param e
     * @return
     */
    private ExecuteCodeResponse tryExceptionAngResponse(Throwable e) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setMessage(e.getMessage());
        // 表示代码沙箱错误
        executeCodeResponse.setStatus(2);
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        return executeCodeResponse;

    }
}
