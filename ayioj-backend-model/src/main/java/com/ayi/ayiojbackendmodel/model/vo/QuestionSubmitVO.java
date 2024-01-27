package com.ayi.ayiojbackendmodel.model.vo;

import cn.hutool.core.bean.BeanUtil;
import com.ayi.ayiojbackendmodel.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * className:QuestionSubmitVO
 * Package:com.ayi.ayioj.model.vo
 * Description: TODO
 *
 * @Date: 2023/10/21 17:12
 * @Author:hjy
 */
@Data
public class QuestionSubmitVO {
    /**
     * id
     */
    private Long id;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

//    /**
//     * 判题信息
//     */
//    private String judgeInfo;

    /**
     * 判题结果
     */
    private String message;

    /**
     * 消耗内存
     */
    private String memory;

    /**
     * 消耗时间
     */
    private String time;


    /**
     * 判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）
     */
    private String status;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 提交用户信息
     */
    private UserVO userVO;

    /**
     * 对应题目信息
     */
    private QuestionVO questionVO;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    /**
     * 包装类转对象
     * @param questionSubmitVO
     * @return
     */
//    public static QuestionSubmit voToObj(QuestionSubmitVO questionSubmitVO) {
//        if (questionSubmitVO == null) {
//            return null;
//        }
//        QuestionSubmit questionSubmit = new QuestionSubmit();
//        BeanUtil.copyProperties(questionSubmitVO, questionSubmit);
//        String judgeInfo1 = questionSubmitVO.getJudgeInfo();
//        if (judgeInfo1 != null) {
//            questionSubmit.setJudgeInfo(judgeInfo1);
//        }
//        return questionSubmit;
//    }

    /**
     * 对象转VO对象（脱敏）
     * @param questionSubmit
     * @return
     */
    public static QuestionSubmitVO objToVo(QuestionSubmit questionSubmit) {
        if (questionSubmit == null) {
            return null;
        }
        QuestionSubmitVO questionVO = new QuestionSubmitVO();
        BeanUtil.copyProperties(questionSubmit, questionVO);
//        String judgeInfo1 = questionSubmit.getJudgeInfo();
//        if (judgeInfo1 != null) {
//            questionVO.setJudgeInfo(judgeInfo1);
//        }
        return questionVO;
    }

}
