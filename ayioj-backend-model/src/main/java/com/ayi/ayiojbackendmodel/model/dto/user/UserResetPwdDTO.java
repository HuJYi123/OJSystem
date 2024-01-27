package com.ayi.ayiojbackendmodel.model.dto.user;

import lombok.Data;

/**
 * className:UserReseartPwdDTO
 * Package:com.ayi.ayioj.model.dto.user
 * Description: TODO
 *
 * @Date: 2024/1/19 9:37
 * @Author:hjy
 */
@Data
public class UserResetPwdDTO {
    private Long id;

    private String userPassword;

    private String checkPassword;
}
