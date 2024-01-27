package com.ayi.ayiojbackendmodel.model.vo;

import com.ayi.ayiojbackendmodel.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * className:QuestionSubmitTestVO
 * Package:com.ayi.ayioj.model.vo
 * Description: TODO
 *
 * @Date: 2024/1/7 16:12
 * @Author:hjy
 */
@Data
public class QuestionSubmitTestVO {


    private String title;


    private List<QuestionSubmit> questionSubmitList;
}
