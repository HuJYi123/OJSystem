package com.ayi.ayiojbackendjudgeservice.judge;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.ayi.ayiojbackendcommom.common.ErrorCode;
import com.ayi.ayiojbackendcommom.exception.BusinessException;
import com.ayi.ayiojbackendjudgeservice.judge.codesandbox.CodeSandBox;
import com.ayi.ayiojbackendjudgeservice.judge.codesandbox.CodeSandBoxFactory;
import com.ayi.ayiojbackendjudgeservice.judge.codesandbox.CodeSandBoxProxy;
import com.ayi.ayiojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.ayi.ayiojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.ayi.ayiojbackendmodel.model.codesandbox.JudgeInfo;
import com.ayi.ayiojbackendjudgeservice.judge.strategy.JudgeContext;
import com.ayi.ayiojbackendmodel.model.dto.question.JudgeCase;
import com.ayi.ayiojbackendmodel.model.entity.Question;
import com.ayi.ayiojbackendmodel.model.entity.QuestionSubmit;
import com.ayi.ayiojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.ayi.ayiojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.ayi.ayiojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * className:JudgeServiceImpl
 * Package:com.ayi.ayioj.judge
 * Description: 判题服务实现
 *
 * @Date: 2023/12/13 1:02
 * @Author:hjy
 */

@Service
public class JudgeServiceImpl implements JudgeService {

    @Autowired
    private QuestionFeignClient questionService;

//    @Autowired
//    private QuestionSubmitFeignClient questionSubmitService;

    @Value("${codesandbox.type:example}")
    private String type;

    @Autowired
    private JudgeManage judgeManage;
    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        // 1、传入题目的提交 id，获取到对应的题目、提交信息 (包含代码、编程语言等)
        QuestionSubmit questionSubmit = questionService.getQuestionSubmitById(questionSubmitId);
        if (null == questionSubmit) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目提交不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        if (null == question) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        // 2、如果题目提交状态不为等待中，就不用重复执行了
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WINTING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中！");
        }
        // 3、状态为待判题则进行判题，首先更改判题(题目提交)的状态为“判题中”，防止重复执行，也能让用户即时看到状态
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.EUNNING.getValue());
        boolean b = questionService.updateBysubmitId(questionSubmitUpdate);
        if (!b) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新题目状态异常");
        }
        // 4、开始调用沙箱执行代码，获取到执行结果
        // 4.1 获取对应的代码沙箱
        CodeSandBox codeSandbox = CodeSandBoxFactory.createCodeSandBox(type);
        // 获取沙箱代理实例
        codeSandbox = new CodeSandBoxProxy(codeSandbox);
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        // 获取输入用例
        String judgeCaseStr = question.getJudgeCase();
        JSONArray jsonArray = JSONUtil.parseArray(judgeCaseStr);
        List<JudgeCase> judgeCaseList = JSONUtil.toList(jsonArray, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        // 封装代码沙箱执行代码请求参数
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        // 使用代码沙箱执行代码并返回执行结果
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        // 判题用例的输出用例
        List<String> outputList = executeCodeResponse.getOutputList();

        // 5、根据沙箱的执行结果，设置题目的判题状态和信息
        // 5.1 根据判题策略（是默认判题还是使用java语言判题，还是使用js语言判题....）
        JudgeContext judgeContext = new JudgeContext();
        // 设置代码运行信息
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        // 题目配置的输入案例
        judgeContext.setInputList(inputList);
        // 设置运行代码的输出结果
        judgeContext.setOutputList(outputList);
        // 设置运行代码的输出结果
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);
        // 根据不同判题策略进行代码执行结果的判断
        JudgeInfo judgeInfo = judgeManage.doJudge(judgeContext);
        // 6）修改数据库中的判题结果
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        b = questionService.updateBysubmitId(questionSubmitUpdate);
        if (!b) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        // 答案通过则回填题目提交数量
        String enumByValue = JudgeInfoMessageEnum.getEnumByValue(judgeInfo.getMessage()).getText();
        if (enumByValue.equals("成功")) {
            question.setAcceptedNum(question.getAcceptedNum() + 1);
            boolean updateQuestion = questionService.updateById(question);
            if (!updateQuestion) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目提交数量更新错误");
            }
        }
        QuestionSubmit questionSubmitResult = questionService.getQuestionSubmitById(questionId);
        return questionSubmitResult;
    }
}
