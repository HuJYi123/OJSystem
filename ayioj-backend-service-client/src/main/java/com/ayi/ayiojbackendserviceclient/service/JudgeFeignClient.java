package com.ayi.ayiojbackendserviceclient.service;

import com.ayi.ayiojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * className:JudgeService
 * Package:com.ayi.ayioj.judge
 * Description: 判题服务
 *
 * @Date: 2023/12/13 0:42
 * @Author:hjy
 */
@FeignClient(name = "ayioj-backend-judge-service", path = "/api/judge")
public interface JudgeFeignClient {

    @PostMapping("/inner/doJudge")
    QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId);
}
