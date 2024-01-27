package com.ayi.ayiojbackendquestionservice.controller;


import com.ayi.ayiojbackendcommom.common.BaseResponse;
import com.ayi.ayiojbackendcommom.common.ErrorCode;
import com.ayi.ayiojbackendcommom.common.ResultUtils;
import com.ayi.ayiojbackendcommom.common.shiro.JwtUtil;
import com.ayi.ayiojbackendcommom.constant.CommonConstant;
import com.ayi.ayiojbackendcommom.exception.BusinessException;
import com.ayi.ayiojbackendcommom.utils.RedisCacheUtils;
import com.ayi.ayiojbackendmodel.model.dto.question.QuestionAddDTO;
import com.ayi.ayiojbackendmodel.model.dto.question.QuestionDeteleDTO;
import com.ayi.ayiojbackendmodel.model.dto.question.QuestionQueryDTO;
import com.ayi.ayiojbackendmodel.model.dto.question.QuestionUpdateDTO;
import com.ayi.ayiojbackendmodel.model.dto.questionSubmit.QuestionSubmitAddDTO;
import com.ayi.ayiojbackendmodel.model.dto.questionSubmit.QuestionSubmitQueryDTO;
import com.ayi.ayiojbackendmodel.model.entity.Question;
import com.ayi.ayiojbackendmodel.model.entity.QuestionSubmit;
import com.ayi.ayiojbackendmodel.model.entity.User;
import com.ayi.ayiojbackendmodel.model.vo.QuestionInfosVO;
import com.ayi.ayiojbackendmodel.model.vo.QuestionSubmitVO;
import com.ayi.ayiojbackendmodel.model.vo.QuestionVO;
import com.ayi.ayiojbackendquestionservice.service.QuestionService;
import com.ayi.ayiojbackendquestionservice.service.QuestionSubmitService;
import com.ayi.ayiojbackendserviceclient.service.UserFeignClient;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

import static com.ayi.ayiojbackendcommom.utils.RedisConstants.CACHE_SHOP_TTL;


/**
 * className:QuestionController
 * Package:com.ayi.ayiojbackendmodel.controller
 * Description: TODO
 *
 * @Date: 2023/10/21 16:27
 * @Author:hjy
 */
@RestController
@RequestMapping
public class QuestionController {
    @Autowired
    @Lazy
    private QuestionService questionService;

    @Autowired
    @Lazy
    private QuestionSubmitService questionSubmitService;

    @Autowired
    @Lazy
    private UserFeignClient userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    @Lazy
    private RedisCacheUtils cacheUtils;

    /**
     * 新增题目
     *
     * @param dto
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Boolean> add(@RequestBody QuestionAddDTO dto, HttpServletRequest request) {
        if (dto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(questionService.add(dto, request));
    }

    /**
     * 更新题目
     *
     * @param dto
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> update(@RequestBody QuestionUpdateDTO dto, HttpServletRequest request) {
        if (dto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(questionService.updateQuession(dto, request));
    }


    /**
     * 获取题目分页数据（管理员界面）
     *
     * @param dto
     * @return 返回题目全部信息
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<Question>> listQuestion(@RequestBody QuestionQueryDTO dto) {
        if (dto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(questionService.listQuestion(dto));
    }

    /**
     * 根据id获取问题
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<Question> OneQuestion(Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(questionService.getQuestion(id));
    }

    /**
     * 获取题目分页数据（用户界面）
     *
     * @param dto
     * @return 返回题目部分信息
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listQuestionVO(@RequestBody QuestionQueryDTO dto) {
        if (dto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(questionService.listQuestionVO(dto));
    }


    /**
     * 根据题目id获取题目信息
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<QuestionVO> getQuestionVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 使用缓存
        Question question = cacheUtils.queryWithPassThrough(CommonConstant.PREFIX_QUESTION, id, Question.class, id1 -> questionService.getById(id), CACHE_SHOP_TTL, TimeUnit.MINUTES);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(questionService.getQuestionVO(question, request));
    }
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteByIds(@RequestBody QuestionDeteleDTO dto, HttpServletRequest request) {
        if (dto == null) {
            return null;
        }
        return ResultUtils.success(questionService.deleteByIds(dto.getId(), request));
    }

    @PostMapping("/question_submit/list/page")
    public BaseResponse<Page<QuestionSubmitVO>> listQueryQuestionSubmit(@RequestBody QuestionSubmitQueryDTO dto, HttpServletRequest request) {
        long current = dto.getCurrent();
        long pageSize = dto.getPageSize();
        String token = JwtUtil.getToken(request);
        String username = userService.getUserNameByToken(token);
//        String username = JwtUtil.getUsername(token);
        User user = userService.getUserByName(username);
        dto.setUserId(user.getId());
        Page<QuestionSubmit> page = questionSubmitService.page(new Page(current, pageSize), questionSubmitService.getQueryWrapper(dto));
        User loginUser = userService.getLoginUser(request);
        Page<QuestionSubmitVO> listQuestionSubmitVO = questionSubmitService.getListQuestionSubmitVO(page, loginUser);
        return ResultUtils.success(listQuestionSubmitVO);
    }
    @PostMapping("/question_submit/do")
    public BaseResponse<Long> addQuestionSubmit(@RequestBody QuestionSubmitAddDTO dto, HttpServletRequest request) {
        if (dto == null || dto.getQuestionId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(questionSubmitService.doQuestionSubmit(dto, request));
    }

    /**
     * 获取题目分析信息，用于展示个人主页
     */
    @GetMapping("/getInfos")
    public BaseResponse<QuestionInfosVO> getInfos(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionInfosVO questionInfosVO = questionService.getInfos(request);
        return ResultUtils.success(questionInfosVO);
    }
}
