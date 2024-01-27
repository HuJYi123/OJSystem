package com.ayi.ayiojbackendmodel.model.dto.questionSubmit;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * className:QuestionSubmitAddDTO
 * Package:com.ayi.ayioj.model.dto.questionSubmit
 * Description: TODO
 *
 * @Date: 2023/10/21 17:29
 * @Author:hjy
 */
@Data
public class QuestionSubmitAddDTO {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 判题信息（JSON对象）
     */
    private String judgeInfo;

    /**
     * 判题状态（0-待判题、1-判题中、2-判题成功、3-判题失败）
     */
    private Integer status;

    /**
     * 题目id
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
     * 是否删除
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
