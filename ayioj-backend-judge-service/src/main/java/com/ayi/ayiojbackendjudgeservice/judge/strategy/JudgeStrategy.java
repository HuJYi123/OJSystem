package com.ayi.ayiojbackendjudgeservice.judge.strategy;

import com.ayi.ayiojbackendmodel.model.codesandbox.JudgeInfo;

/**
 * className:JudgeStrategy
 * Package:com.ayi.ayioj.judge.strategy
 * Description: 判题策略
 *              根据提交代码的语言进行判题策略的选择，因为不同语言执行时间不同，需要使用不同策略进行题目的判断
 *
 * @Date: 2023/12/13 1:57
 * @Author:hjy
 */
public interface JudgeStrategy {
    JudgeInfo doJudge(JudgeContext judgeContext);
}
