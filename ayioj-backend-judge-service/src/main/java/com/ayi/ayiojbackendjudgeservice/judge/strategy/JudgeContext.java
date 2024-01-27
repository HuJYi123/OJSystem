package com.ayi.ayiojbackendjudgeservice.judge.strategy;

import com.ayi.ayiojbackendmodel.model.codesandbox.JudgeInfo;
import com.ayi.ayiojbackendmodel.model.dto.question.JudgeCase;
import com.ayi.ayiojbackendmodel.model.entity.Question;
import com.ayi.ayiojbackendmodel.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * className:JudgeContext
 * Package:com.ayi.ayioj.judge.strategy
 * Description: 上下文（用于定义在判题策略中传递的参数）
 *
 * @Date: 2023/12/13 1:58
 * @Author:hjy
 */

@Data
public class JudgeContext {
    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;

}
