package com.ayi.ayiojbackendjudgeservice.judge;

import com.ayi.ayiojbackendmodel.model.entity.QuestionSubmit;

/**
 * className:JudgeService
 * Package:com.ayi.ayioj.judge
 * Description: 判题服务
 *
 * @Date: 2023/12/13 0:42
 * @Author:hjy
 */
public interface JudgeService {

    QuestionSubmit doJudge(long questionSubmitId);
}
