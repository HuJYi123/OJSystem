package com.ayi.ayiojbackendmodel.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * className:QuestionSubmitStatusEnum
 * Package:com.ayi.ayioj.model.enums
 * Description: TODO
 *
 * @Date: 2023/11/5 23:02
 * @Author:hjy
 */
public enum QuestionSubmitStatusEnum {

    WINTING("待判题", 0),

    EUNNING("判题中", 1),

    SUCCEED("判题成功", 2),

    FAILED("判题失败", 3)
    ;
    private final String test;

    private final Integer value;

    public String getTest() {
        return test;
    }

    public Integer getValue() {
        return value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static QuestionSubmitStatusEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (QuestionSubmitStatusEnum anEnum : QuestionSubmitStatusEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    QuestionSubmitStatusEnum(String test, Integer value) {
        this.test = test;
        this.value = value;
    }

}
