package com.ayi.ayiojbackendquestionservice.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ayi.ayiojbackendcommom.constant.CommonConstant;
import com.ayi.ayiojbackendcommom.exception.BusinessException;
import com.ayi.ayiojbackendmodel.model.codesandbox.JudgeInfo;
import com.ayi.ayiojbackendcommom.utils.SqlUtils;
import com.ayi.ayiojbackendmodel.model.dto.questionSubmit.QuestionSubmitAddDTO;
import com.ayi.ayiojbackendmodel.model.dto.questionSubmit.QuestionSubmitQueryDTO;
import com.ayi.ayiojbackendmodel.model.entity.Question;
import com.ayi.ayiojbackendmodel.model.entity.QuestionSubmit;
import com.ayi.ayiojbackendmodel.model.entity.User;
import com.ayi.ayiojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.ayi.ayiojbackendmodel.model.enums.QuestionSubmitLanguageEnum;
import com.ayi.ayiojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.ayi.ayiojbackendmodel.model.vo.QuestionSubmitVO;
import com.ayi.ayiojbackendquestionservice.mapper.QuestionSubmitMapper;
import com.ayi.ayiojbackendquestionservice.rabbitmq.MyMessageProducer;
import com.ayi.ayiojbackendquestionservice.service.QuestionService;
import com.ayi.ayiojbackendquestionservice.service.QuestionSubmitService;
import com.ayi.ayiojbackendserviceclient.service.JudgeFeignClient;
import com.ayi.ayiojbackendserviceclient.service.UserFeignClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.ayi.ayiojbackendcommom.common.ErrorCode.*;

/**
 * @author Administrator
 * @description 针对表【question_submit(题目提交)】的数据库操作Service实现
 * @createDate 2023-10-21 16:21:47
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {

    @Autowired
    private UserFeignClient userService;

    @Autowired
    private QuestionService questionService;

    @Resource
    private MyMessageProducer myMessageProducer;

    @Autowired
    /**
     *懒加载
     */
    @Lazy
    private JudgeFeignClient judgeService;

    @Override
    public long doQuestionSubmit(QuestionSubmitAddDTO dto, HttpServletRequest request) {
        if (questionService.getQuestion(dto.getQuestionId()) == null) {
            throw new BusinessException(NOT_FOUND_ERROR, "题目不存在");
        }
        QuestionSubmitLanguageEnum[] values = QuestionSubmitLanguageEnum.values();
        Set<String> collect = Arrays.stream(values).map(item -> {
            return item.getValue();
        }).collect(Collectors.toSet());
        if (dto.getLanguage() == null || !collect.contains(dto.getLanguage())) {
            throw new BusinessException(PARAMS_ERROR, "编程语言不存在！");
        }
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setCode(dto.getCode());
        questionSubmit.setLanguage(dto.getLanguage());
        questionSubmit.setJudgeInfo(dto.getJudgeInfo());
        questionSubmit.setQuestionId(dto.getQuestionId());
        questionSubmit.setCreateTime(new Date());
        questionSubmit.setUpdateTime(new Date());
        questionSubmit.setIsDelete(0);
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WINTING.getValue());
        questionSubmit.setUserId(userService.getLoginUser(request).getId());
        boolean save = this.save(questionSubmit);
        if (!save) {
            throw new BusinessException(SYSTEM_ERROR, "提交代码失败！！！");
        }
        Question question = questionService.getById(dto.getQuestionId());
        question.setSubmitNum(question.getSubmitNum() + 1);
        boolean update = questionService.updateById(question);
        if (!update) {
            throw new BusinessException(SYSTEM_ERROR, "更新题目失败！！！");
        }
        Long questionSubmitId = questionSubmit.getId();
        // 使用消息消费异步执行
        myMessageProducer.sendMessage("code_exchange", "my_routingKey", String.valueOf(questionSubmitId));
        // 提交成功后异步执行判题服务
//        CompletableFuture.runAsync(() -> {
//            judgeService.doJudge(questionSubmitId);
//        });
        return questionSubmitId;
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        // 脱敏：仅本人和管理员能看见自己（提交 userId 和登录用户 id 不同）提交的代码
        long userId = loginUser.getId();
        // 处理脱敏
        if (userId != questionSubmit.getUserId() && !userService.isAdmin(loginUser)) {
            questionSubmitVO.setCode(null);
        }
        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getListQuestionSubmitVO(Page<QuestionSubmit> questionSubmit, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmit.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmit.getCurrent(), questionSubmit.getSize(), questionSubmit.getTotal());
        if (CollectionUtils.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream()
                .map(questionSubmit1 -> {
                    QuestionSubmitVO questionSubmitVO = getQuestionSubmitVO(questionSubmit1, loginUser);
                    Integer status = questionSubmit1.getStatus();
                    String judgeInfoJson = questionSubmit1.getJudgeInfo();
                    if (StrUtil.isNotBlank(judgeInfoJson)) {
                        JudgeInfo judgeInfo = JSONUtil.toBean(judgeInfoJson, JudgeInfo.class);

                        String message = judgeInfo.getMessage();
                        JudgeInfoMessageEnum enumByValue = JudgeInfoMessageEnum.getEnumByValue(message);
                        questionSubmitVO.setMessage(enumByValue.getText());
                        questionSubmitVO.setMemory((judgeInfo.getMemory()) + "kb");
                        questionSubmitVO.setTime(judgeInfo.getTime() + "s");
                    }
                    switch (status) {
                        case 0:
                            questionSubmitVO.setStatus("待判题");
                            break;
                        case 1:
                            questionSubmitVO.setStatus("判题中");
                            break;
                        case 2:
                            questionSubmitVO.setStatus("判题成功");
                            break;
                        case 3:
                            questionSubmitVO.setStatus("判题失败");
                            break;
                        default:
                            questionSubmitVO.setStatus("未知");
                    }
                    return questionSubmitVO;
                })
                .collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }

    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryDTO questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}




