package com.ayi.ayiojbackendmodel.model.dto.user;

import lombok.Data;

/**
 * className:UserRegisterDTO
 * Package:com.ayi.ayioj.model.dto.user
 * Description: TODO
 *
 * @Date: 2023/10/23 11:37
 * @Author:hjy
 */
@Data
public class UserRegisterDTO {
    private String userAccount;

    private String userPassword;

    private String checkPassword;
}
