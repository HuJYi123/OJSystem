package com.ayi.ayiojbackendquestionservice.controller.inner;

import com.ayi.ayiojbackendmodel.model.entity.Question;
import com.ayi.ayiojbackendmodel.model.entity.QuestionSubmit;
import com.ayi.ayiojbackendquestionservice.service.QuestionService;
import com.ayi.ayiojbackendquestionservice.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * className:QuestionInnerController
 * Package:com.ayi.ayiojbackendquestionservice.controller.inner
 * Description: TODO
 *
 * @Date: 2024/1/25 11:19
 * @Author:hjy
 */
@RestController
@RequestMapping("/inner")
public class QuestionInnerController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionSubmitService questionSubmitService;

    @GetMapping("/getById")
    Question getById(@RequestParam("questionId") Long questionId) {
        return questionService.getById(questionId);
    }

    @PostMapping("/updateById")
    boolean updateById(@RequestBody Question question) {
        boolean b = questionService.updateById(question);
        return b;
    }

    @GetMapping("/getQuestionSubmitById")
    QuestionSubmit getQuestionSubmitById(long questionSubmitId) {
        return questionSubmitService.getById(questionSubmitId);
    }

    @PostMapping("/updateBysubmitId")
    boolean updateBysubmitId(@RequestBody QuestionSubmit questionSubmitUpdate) {
        return questionSubmitService.updateById(questionSubmitUpdate);
    }
}
