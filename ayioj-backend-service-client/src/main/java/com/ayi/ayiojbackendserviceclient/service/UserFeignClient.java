package com.ayi.ayiojbackendserviceclient.service;

import com.ayi.ayiojbackendcommom.common.BaseResponse;
import com.ayi.ayiojbackendcommom.common.DeleteRequest;
import com.ayi.ayiojbackendcommom.common.ErrorCode;
import com.ayi.ayiojbackendcommom.common.shiro.JwtUtil;
import com.ayi.ayiojbackendcommom.exception.BusinessException;
import com.ayi.ayiojbackendmodel.model.dto.user.*;
import com.ayi.ayiojbackendmodel.model.entity.User;
import com.ayi.ayiojbackendmodel.model.enums.UserRoleEnum;
import com.ayi.ayiojbackendmodel.model.vo.LoginUserVO;
import com.ayi.ayiojbackendmodel.model.vo.ManageUserVO;
import com.ayi.ayiojbackendmodel.model.vo.UserVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * className:UserService
 * Package:com.ayi.ayiojbackendmodel.service
 * Description: TODO
 *
 * @Date: 2023/10/23 10:25
 * @Author:hjy
 */
@FeignClient(name = "ayioj-backend-user-service", path = "/api/user")
public interface UserFeignClient {

//    @PostMapping("/login")
//    BaseResponse<LoginUserVO> login(@RequestBody UserLoginDTO dto, HttpServletRequest request);
//
//    @PostMapping("/register")
//    BaseResponse<Long> register(@RequestBody UserRegisterDTO dto, HttpServletRequest request);
//
//    @PostMapping("/logout")
//    BaseResponse<Boolean> logout(HttpServletRequest request);
//
//    @GetMapping("/get/login")
//    BaseResponse<LoginUserVO> getUserInfo(HttpServletRequest request);
//
//    @PostMapping("/getList")
//    BaseResponse<Page<UserVO>> getList(@RequestBody UserQueryDTO dto);
//
//
//    @PostMapping("/list/page")
//    BaseResponse<Page<ManageUserVO>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest,
//                                                           HttpServletRequest request);
//
//    @PostMapping("/update")
//    BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest,
//                                            HttpServletRequest request);
//
//    @PostMapping("/updatePwd")
//    BaseResponse<Boolean> updateUserPwd(@RequestBody UserResetPwdDTO userResetPwdDTO,
//                                               HttpServletRequest request);
//
//    @PostMapping("/delete")
//    BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request);




    @GetMapping("/inner/getUserByName")
    User getUserByName(@RequestParam("userName") String username);



    @GetMapping("/inner/getById")
    User getById(@RequestParam("userId") Long userId);

    @PostMapping("/inner/listByIds")
    List<User> listByIds(@RequestBody Collection<Long> userIdSet);

    @GetMapping("/inner/getRoleName")
    String getRoleName(@RequestParam("id") Long id);

    default User getLoginUser(HttpServletRequest request) {
        String token = JwtUtil.getToken(request);
        String username = getUserNameByToken(token);
        User user = getUserByName(username);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return user;
    }

    default Boolean isAdmin(User user) {
        String roleName = getRoleName(user.getId());
        return "admin".equals(roleName);
    }
    default boolean isAdmin(HttpServletRequest request) {
        String token = JwtUtil.getToken(request);
        String username = JwtUtil.getUsername(token);
        User user = getUserByName(username);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        String roleName = getRoleName(user.getId());
        return "admin".equals(roleName);
    }
    default UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @GetMapping("/inner/getUserNameByToken")
    String getUserNameByToken(@RequestParam("token") String token);
}
