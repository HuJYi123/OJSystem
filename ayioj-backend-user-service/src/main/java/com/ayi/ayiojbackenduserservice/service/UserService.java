package com.ayi.ayiojbackenduserservice.service;

import com.ayi.ayiojbackendmodel.model.dto.user.*;
import com.ayi.ayiojbackendmodel.model.entity.User;
import com.ayi.ayiojbackendmodel.model.vo.LoginUserVO;
import com.ayi.ayiojbackendmodel.model.vo.UserVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * className:UserService
 * Package:com.ayi.ayioj.service
 * Description: TODO
 *
 * @Date: 2023/10/23 10:25
 * @Author:hjy
 */
public interface UserService extends IService<User> {
    LoginUserVO login(UserLoginDTO dto, HttpServletRequest request);

    Long register(UserRegisterDTO dto, HttpServletRequest request);

    Boolean logout(HttpServletRequest request);

    LoginUserVO getUserInfo(HttpServletRequest request);


    Page<UserVO> getList(UserQueryDTO dto);

    User getLoginUser(HttpServletRequest request);

    Boolean isAdmin(HttpServletRequest request);

    Boolean isAdmin(User user);

    User getUserByName(String username);

    public UserVO getUserVO(User user);

    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    Boolean resetPwd(UserResetPwdDTO userResetPwdDTO);
}
