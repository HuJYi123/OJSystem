package com.ayi.ayiojbackendjudgeservice.judge;

import com.ayi.ayiojbackendmodel.model.codesandbox.JudgeInfo;
import com.ayi.ayiojbackendjudgeservice.judge.strategy.DefaultJudgeStrategyImpl;
import com.ayi.ayiojbackendjudgeservice.judge.strategy.JavaLanguageJudgeStrategyImpl;
import com.ayi.ayiojbackendjudgeservice.judge.strategy.JudgeContext;
import com.ayi.ayiojbackendjudgeservice.judge.strategy.JudgeStrategy;
import com.ayi.ayiojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * className:JudgeManage
 * Package:com.ayi.ayioj.judge
 * Description: 判题管理（简化调用）
 *
 * @Date: 2023/12/13 2:24
 * @Author:hjy
 */
@Service
public class JudgeManage {
    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategyImpl();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategyImpl();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
