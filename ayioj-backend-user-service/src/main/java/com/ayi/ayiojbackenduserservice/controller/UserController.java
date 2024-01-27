package com.ayi.ayiojbackenduserservice.controller;

import cn.hutool.core.util.StrUtil;
import com.ayi.ayiojbackendcommom.common.BaseResponse;
import com.ayi.ayiojbackendcommom.common.DeleteRequest;
import com.ayi.ayiojbackendcommom.common.ErrorCode;
import com.ayi.ayiojbackendcommom.common.ResultUtils;
import com.ayi.ayiojbackendcommom.common.shiro.JwtUtil;
import com.ayi.ayiojbackendcommom.exception.BusinessException;
import com.ayi.ayiojbackendcommom.exception.ThrowUtils;
import com.ayi.ayiojbackenduserservice.mapper.RoleMapper;
import com.ayi.ayiojbackendmodel.model.dto.user.*;
import com.ayi.ayiojbackendmodel.model.entity.User;
import com.ayi.ayiojbackendmodel.model.entity.UserRole;
import com.ayi.ayiojbackendmodel.model.vo.LoginUserVO;
import com.ayi.ayiojbackendmodel.model.vo.ManageUserVO;
import com.ayi.ayiojbackendmodel.model.vo.UserVO;
import com.ayi.ayiojbackenduserservice.service.SysRoleService;
import com.ayi.ayiojbackenduserservice.service.UserRoleService;
import com.ayi.ayiojbackenduserservice.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * className:UserControllera
 * Package:com.ayi.ayioj.controller
 * Description: TODO
 *
 * @Date: 2023/10/23 10:04
 * @Author:hjy
 */
@Api(tags = "用户")
@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleMapper roleMapper;
    /**
     * 登录
     * @param dto
     * @param request
     * @return 返回登录信息
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> login(@RequestBody UserLoginDTO dto, HttpServletRequest request){
        if (dto == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LoginUserVO loginUserVO = userService.login(dto, request);

        return ResultUtils.success(loginUserVO);
    }

    /**
     * 注册
     * @param dto
     * @param request
     * @return 返回注册用户的id
     */
    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody UserRegisterDTO dto, HttpServletRequest request) {
        if (dto == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = userService.register(dto, request);
        return ResultUtils.success(id);
    }

    /**
     * 用户注销
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> logout(HttpServletRequest request){
        if (null == request){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean aBoolean = userService.logout(request);
        return ResultUtils.success(aBoolean);
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getUserInfo(HttpServletRequest request){
        if (null == request){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        String token = JwtUtil.getToken(request);
        String username = JwtUtil.getUsername(token);
        if (StrUtil.isEmpty(username)) {
            return ResultUtils.success(loginUserVO);
        }
        User user = userService.getUserByName(username);
        if (user == null){
            return ResultUtils.success(loginUserVO);
        }
        BeanUtils.copyProperties(user, loginUserVO);
        String roleName = roleMapper.getRoleName(user.getId());
        loginUserVO.setUserRole(roleName);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 分页查询用户列表
     * @param dto
     * @return
     */
    @PostMapping("/getList")
    public BaseResponse<Page<UserVO>> getList(@RequestBody UserQueryDTO dto) {
        if (dto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(userService.getList(dto));
    }

    /**
     * 分页展示用户列表
     * @param userQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
//    @RequiresRoles("admin")
    public BaseResponse<Page<ManageUserVO>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest,
                                                           HttpServletRequest request) {
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        Page<ManageUserVO> userPageVO = new Page<>(userPage.getCurrent(), userPage.getSize());
        List<User> records = userPage.getRecords();
        List<ManageUserVO> collect = records.stream().map(item -> {
            ManageUserVO manageUserVO = new ManageUserVO();
            BeanUtils.copyProperties(item, manageUserVO);
            String roleName = roleMapper.getRoleName(item.getId());
            manageUserVO.setUserRole(roleName);
            return manageUserVO;
        }).collect(Collectors.toList());
        userPageVO.setRecords(collect);
        userPageVO.setTotal(userPage.getTotal());
        return ResultUtils.success(userPageVO);
    }

    /**
     * 更新用户信息
     * @param userUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @RequiresRoles("admin")
    @Transactional
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest,
                                            HttpServletRequest request) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        // 修改用户信息
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 修改用户对应的角色
        if (userUpdateRequest.getUserRole() != null) {
            Long roleId = sysRoleService.getIdByName(userUpdateRequest.getUserRole());
            Long userId = userUpdateRequest.getId();
            // 先删再加
            List<Long> list = new ArrayList<>();
            list.add(userId);
            userRoleService.deleteByUserIds(userId);
            UserRole userRole = new UserRole();
            userRole.setRoleId(roleId).setUserId(userId);
            Boolean uUserRole = userRoleService.addUserRole(userRole);
            ThrowUtils.throwIf(!uUserRole, ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(true);
    }

    /**
     * 重置密码
     * @param userResetPwdDTO
     * @param request
     * @return
     */
    @PostMapping("/updatePwd")
    @RequiresRoles("admin")
    @Transactional
    public BaseResponse<Boolean> updateUserPwd(@RequestBody UserResetPwdDTO userResetPwdDTO,
                                            HttpServletRequest request) {
        if (userResetPwdDTO == null || userResetPwdDTO.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean b = userService.resetPwd(userResetPwdDTO);
        return ResultUtils.success(b);
    }

    @PostMapping("/delete")
    @RequiresRoles("admin")
    @Transactional
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        Boolean b1 = userRoleService.deleteByUserIds(deleteRequest.getId());
        return ResultUtils.success(b || b1);
    }
}
