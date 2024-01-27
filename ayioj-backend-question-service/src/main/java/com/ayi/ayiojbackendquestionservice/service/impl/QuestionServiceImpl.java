package com.ayi.ayiojbackendquestionservice.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ayi.ayiojbackendcommom.common.CommomUtils;
import com.ayi.ayiojbackendcommom.common.ErrorCode;
import com.ayi.ayiojbackendcommom.common.shiro.JwtUtil;
import com.ayi.ayiojbackendcommom.constant.CommonConstant;
import com.ayi.ayiojbackendcommom.exception.BusinessException;
import com.ayi.ayiojbackendcommom.exception.ThrowUtils;
import com.ayi.ayiojbackendcommom.utils.RedisKeyUtil;
import com.ayi.ayiojbackendcommom.utils.SqlUtils;
import com.ayi.ayiojbackendmodel.model.codesandbox.JudgeInfo;
import com.ayi.ayiojbackendmodel.model.dto.question.*;
import com.ayi.ayiojbackendmodel.model.entity.Question;
import com.ayi.ayiojbackendmodel.model.entity.QuestionSubmit;
import com.ayi.ayiojbackendmodel.model.entity.User;
import com.ayi.ayiojbackendmodel.model.vo.QuestionInfosVO;
import com.ayi.ayiojbackendmodel.model.vo.QuestionVO;
import com.ayi.ayiojbackendmodel.model.vo.UserVO;
import com.ayi.ayiojbackendquestionservice.mapper.QuestionMapper;
import com.ayi.ayiojbackendquestionservice.mapper.QuestionSubmitMapper;
import com.ayi.ayiojbackendquestionservice.service.QuestionService;
import com.ayi.ayiojbackendserviceclient.service.UserFeignClient;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


/**
 * @author Administrator
 * @description 针对表【question(题目)】的数据库操作Service实现
 * @createDate 2023-10-21 16:19:27
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Autowired
    private UserFeignClient userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionSubmitMapper questionSubmitMapper;

    private  final static Gson GSON = new Gson();

    @Override
    public Boolean add(QuestionAddDTO dto, HttpServletRequest request) {
        Question question = new Question();
        question.setTitle(dto.getTitle());
        question.setContent(dto.getContent());
        List<String> tags = dto.getTags();
        if(tags != null) {
            question.setTags(GSON.toJson(tags));
        }
        List<JudgeCase> judgeCase = dto.getJudgeCase();
        if (judgeCase != null) {
            question.setJudgeCase(GSON.toJson(judgeCase));
        }
        JudgeConfig judgeConfig = dto.getJudgeConfig();
        if (judgeConfig != null) {
            question.setJudgeConfig(GSON.toJson(judgeConfig));
        }
        question.setAnswer(dto.getAnswer());
        question.setCreateTime(new Date());
        question.setUpdateTime(new Date());
        String username = JwtUtil.getUsername(JwtUtil.getToken(request));
        User user = userService.getUserByName(username);
        question.setUserId(user.getId());
        boolean save = this.save(question);
        if (save) {
            String jsonStr = JSONUtil.toJsonStr(question);
            String key = RedisKeyUtil.getKey(CommonConstant.PREFIX_QUESTION, question.getId());
            stringRedisTemplate.opsForValue().set(key, jsonStr);
        }
        return save;
    }

    @Override
    public Page<QuestionVO> listQuestionVO(QuestionQueryDTO dto) {
        CommomUtils.chenkPage(dto);
        Long current = dto.getCurrent();
        Long pageSize = dto.getPageSize();
        Page<Question> page = new Page<>(current, pageSize);
//        LambdaQueryWrapper<Question> query = new LambdaQueryWrapper<>();
//        query.like(StringUtils.isNotBlank(dto.getTitle()), Question::getTitle, dto.getTitle());
        page = this.baseMapper.selectPage(page, getQueryWrapper(dto));
        List<Question> records = page.getRecords();
        Page<QuestionVO> pageVO = new Page<>(current, pageSize, page.getTotal());
        List<QuestionVO> recordsVO = new ArrayList<>();
        records.stream().forEach(e -> {
            QuestionVO questionVO = new QuestionVO();
            BeanUtils.copyProperties(e, questionVO);
            if (StrUtil.isNotBlank(e.getTags())) {
                List<String> tagList = JSONUtil.toList(e.getTags(), String.class);
                questionVO.setTags(tagList);
            }
            recordsVO.add(questionVO);
        });
        pageVO.setRecords(recordsVO);
        return pageVO;
    }

    @Override
    public QuestionVO getQuestionVO(Long id) {
        Question question = this.getById(id);
        if (question == null) {
            return null;
        }
        QuestionVO questionVO = new QuestionVO();
        BeanUtils.copyProperties(question, questionVO);
        return questionVO;
    }

    @Override
    public QuestionVO getQuestionVO(Question question, HttpServletRequest request) {
        QuestionVO questionVO = QuestionVO.objToVo(question);
        // 1. 关联查询用户信息
        Long userId = question.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        questionVO.setUserVO(userVO);
        return questionVO;
    }

    @Override
    public Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request) {
        List<Question> questionList = questionPage.getRecords();
        Page<QuestionVO> questionVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(), questionPage.getTotal());
        if (CollectionUtils.isEmpty(questionList)) {
            return questionVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionList.stream().map(Question::getUserId).collect(Collectors.toSet());
        /**
         * 根据用户ID集合查询所有用户信息，并使用Map进行按用户id分组
         * id为key,value为List<User>，这里的List<User>只有一个元素
         */
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        List<QuestionVO> questionVOList = questionList.stream().map(question -> {
            QuestionVO questionVO = QuestionVO.objToVo(question);
            Long userId = question.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionVO.setUserVO(userService.getUserVO(user));
            return questionVO;
        }).collect(Collectors.toList());
        questionVOPage.setRecords(questionVOList);
        return questionVOPage;
    }

    @Override
    public QuestionInfosVO getInfos(HttpServletRequest request) {
        String token = JwtUtil.getToken(request);
        if (StrUtil.isBlank(token)) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户未登录");
        }
        String username = JwtUtil.getUsername(token);
        if (StrUtil.isBlank(username)) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户未登录");
        }
        User user = userService.getUserByName(username);
        if (user == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户不存在");
        }
        Long userId = user.getId();
        QuestionInfosVO questionInfosVO = new QuestionInfosVO();
        // 获取创建题目的数量
        int createNum = baseMapper.selectQuestionByUserId(userId);
        // 获取题目提交的信息
        LambdaQueryWrapper<QuestionSubmit> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(QuestionSubmit::getUserId, userId);
        List<QuestionSubmit> questionSubmits = questionSubmitMapper.selectList(lambdaQueryWrapper);
        AtomicInteger acceptNum = new AtomicInteger();
        AtomicInteger failNum = new AtomicInteger();
        AtomicInteger nums = new AtomicInteger();
        if (questionSubmits != null) {
            questionSubmits.stream().forEach(item -> {
                String judgeInfoStr = item.getJudgeInfo();
                JudgeInfo judgeInfo = JSONUtil.toBean(judgeInfoStr, JudgeInfo.class);
//                JudgeInfo judgeInfo = BeanUtil.toBean(judgeInfoStr, JudgeInfo.class);
                if (judgeInfo != null) {
                    nums.getAndIncrement();
                    String message = judgeInfo.getMessage();
                    if (!message.equals("Accepted")) {
                        failNum.getAndIncrement();
                    } else {
                        acceptNum.getAndIncrement();
                    }
                }
            });
        }
        questionInfosVO.setCreateNum(createNum);
        questionInfosVO.setAcceptNum(acceptNum.get());
        questionInfosVO.setFailNum(failNum.get());
        questionInfosVO.setNums(nums.get());
        return questionInfosVO;
    }

    @Override
    public Page<Question> listQuestion(QuestionQueryDTO dto) {
        CommomUtils.chenkPage(dto);
        Long current = dto.getCurrent();
        Long pageSize = dto.getPageSize();
        Page<Question> page = new Page<>(current, pageSize);
        LambdaQueryWrapper<Question> query = new LambdaQueryWrapper<>();
        query.like(StringUtils.isNotBlank(dto.getTitle()), Question::getTitle, dto.getTitle());
        page = this.baseMapper.selectPage(page, getQueryWrapper(dto));
        return page;
    }

    @Override
    public Question getQuestion(Long id) {
        Question question = this.getById(id);
        if (null == question) {
            return null;
        }
        return question;
    }

    @Override
    public Boolean deleteByIds(Long ids,HttpServletRequest request) {
        Question oldQuestion = this.getById(ids);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        /**
         * 本人和管理员才可以删除
         */
        if (!oldQuestion.getUserId().equals(userService.getLoginUser(request).getId()) && userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return this.removeById(ids);
    }

    @Override
    public Boolean updateQuession(QuestionUpdateDTO dto, HttpServletRequest request) {
        Question question = new Question();
        BeanUtils.copyProperties(dto, question);
        List<String> tags = dto.getTags();
        if (null != tags) {
            question.setTags(GSON.toJson(tags));
        }
        JudgeConfig judgeConfig = dto.getJudgeConfig();
        if (null != judgeConfig) {
            question.setJudgeConfig(GSON.toJson(judgeConfig));
        }
        List<JudgeCase> judgeCass = dto.getJudgeCass();
        if (null != judgeCass) {
            question.setJudgeCase(GSON.toJson(judgeCass));
        }
        this.validQuestion(question, false);
        long id = dto.getId();
        // 判断是否存在
        Question oldQuestion = getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        return this.updateById(question);
    }

    /**
     * 校验题目是否合法
     * @param question
     * @param add
     */
    @Override
    public void validQuestion(Question question, boolean add) {
        if (question == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String title = question.getTitle();
        String content = question.getContent();
        String tags = question.getTags();
        String answer = question.getAnswer();
        String judgeCase = question.getJudgeCase();
        String judgeConfig = question.getJudgeConfig();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(title, content, tags), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(title) && title.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(content) && content.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
        if (StringUtils.isNotBlank(answer) && answer.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "答案过长");
        }
        if (StringUtils.isNotBlank(judgeCase) && judgeCase.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "判题用例过长");
        }
        if (StringUtils.isNotBlank(judgeConfig) && judgeConfig.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "判题配置过长");
        }
    }

    @Override
    public QueryWrapper<Question> getQueryWrapper(QuestionQueryDTO questionQueryRequest) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        if (questionQueryRequest == null) {
            return queryWrapper;
        }
        Long id = questionQueryRequest.getId();
        String title = questionQueryRequest.getTitle();
        String content = questionQueryRequest.getContent();
        List<String> tags = questionQueryRequest.getTags();
        String answer = questionQueryRequest.getAnswer();
        Long userId = questionQueryRequest.getUserId();
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.like(StringUtils.isNotBlank(answer), "answer", answer);
        if (CollectionUtils.isNotEmpty(tags)) {
            for (String tag : tags) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}




