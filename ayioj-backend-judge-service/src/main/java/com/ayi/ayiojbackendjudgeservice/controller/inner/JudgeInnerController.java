package com.ayi.ayiojbackendjudgeservice.controller.inner;

import com.ayi.ayiojbackendjudgeservice.judge.JudgeService;
import com.ayi.ayiojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * className:JudgeInnerController
 * Package:com.ayi.ayiojbackendjudgeservice.controller.inner
 * Description: TODO
 *
 * @Date: 2024/1/25 11:24
 * @Author:hjy
 */
@RestController
@RequestMapping
public class JudgeInnerController {

    @Autowired
    private JudgeService judgeService;
    @PostMapping("/inner/doJudge")
    QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId) {
        return judgeService.doJudge(questionSubmitId);
    }
}
