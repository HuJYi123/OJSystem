package com.ayi.ayiojbackenduserservice.service.impl;

import cn.sylinx.redis.springboot.RedisCommand;
import com.ayi.ayiojbackendcommom.common.CommomUtils;
import com.ayi.ayiojbackendcommom.common.ErrorCode;
import com.ayi.ayiojbackendcommom.common.shiro.JwtUtil;
import com.ayi.ayiojbackendcommom.constant.CommonConstant;
import com.ayi.ayiojbackendcommom.exception.BusinessException;
import com.ayi.ayiojbackenduserservice.mapper.RoleMapper;
import com.ayi.ayiojbackenduserservice.mapper.UserMapper;
import com.ayi.ayiojbackendmodel.model.dto.user.*;
import com.ayi.ayiojbackendmodel.model.entity.User;
import com.ayi.ayiojbackendmodel.model.enums.UserRoleEnum;
import com.ayi.ayiojbackendmodel.model.vo.LoginUserVO;
import com.ayi.ayiojbackendmodel.model.vo.UserVO;
import com.ayi.ayiojbackenduserservice.service.UserService;
import com.ayi.ayiojbackendcommom.utils.RedisKeyUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * className:UserServiceImpl
 * Package:com.ayi.ayioj.service.impl
 * Description: TODO
 *
 * @Date: 2023/10/23 10:28
 * @Author:hjy
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    /**
     * 加密盐值
     */
    private static final String SALT = "ayioj";

    @Autowired
    private RedisCommand redisCommand;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RoleMapper roleMapper;


    /**
     * 登录
     * @param dto
     * @param request
     * @return
     */
    @Override
    public LoginUserVO login(UserLoginDTO dto, HttpServletRequest request) {
        String userAccount = dto.getUserAccount();
        String userPassword = dto.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4 || userAccount.length() > 20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度不符合要求");
        }
        if (userPassword.length() < 6 || userPassword.length() > 20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度不符合要求");
        }
        /**
         * 密码加密
         */
        String encryptPwd = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
        query.eq(User::getUserAccount, userAccount);
        query.eq(User::getUserPassword, encryptPwd);
        User user = this.baseMapper.selectOne(query);
        if (user == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不存在或密码错误");
        }
        /**
         * 把用户存入session中，方便下次验证身份
         * 也可使用token(JWT)进行验证
         */
        String password = user.getUserPassword();
        // 获取token
        String token = JwtUtil.sign(userAccount, password);
        String key = RedisKeyUtil.assemblyServiceKey(CommonConstant.PREFIX_USER_TOKEN, token);
//        redisCommand.set(key, token);
        redisTemplate.opsForValue().set(key, token);
        // 设置超时时间
//        redisCommand.expire(key, (int) JwtUtil.EXPIRE_TIME / 1000);
        redisTemplate.expire(key, Duration.ofSeconds(JwtUtil.EXPIRE_TIME));
//        redisTemplate.expire(key, Duration.ofSeconds(JwtUtil.EXPIRE_TIME));

//        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, user);
        LoginUserVO loginUserVO = copeProperty(user);
        loginUserVO.setToken(token);
        String roleName = roleMapper.getRoleName(user.getId());
        loginUserVO.setUserRole(roleName);
        return loginUserVO;
    }

    /**
     * 注册
     * @param dto
     * @param request
     * @return
     */
    @Override
    public Long register(UserRegisterDTO dto, HttpServletRequest request) {
        String userAccount = dto.getUserAccount();
        String userPassword = dto.getUserPassword();
        String checkPassword = dto.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空！");
        }
        if (userAccount.length() < 4 || userAccount.length() > 20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度不符合要求");
        }
        if (userPassword.length() < 6 || userPassword.length() > 20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度不符合要求");
        }
        if (!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码不一致");
        }
        /**
         * 使用加锁方式防止同个用户重复注册
         */
        synchronized(userAccount.intern()) {
            LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
            query.eq(User::getUserAccount, userAccount);
            Long count = this.baseMapper.selectCount(query);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号已存在");
            }
            String encryptPwd = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            User user = new User();
            user.setUserAccount(userAccount).setUserPassword(encryptPwd);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "注册失败");
            }
            return user.getId();
        }
    }

    @Override
    public Boolean logout(HttpServletRequest request) {
//
//        if (request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE) == null){
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户未登录");
//        }
//        // 移除session中的用户信息
//        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
//        return true;
        // 用户退出逻辑
        Subject subject = SecurityUtils.getSubject();
        subject.logout();

        String token = request.getHeader(CommonConstant.X_ACCESS_TOKEN);
        String key = RedisKeyUtil.assemblyServiceKey(CommonConstant.PREFIX_USER_TOKEN, token);
        redisCommand.del(key);
        return true;
    }

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    @Override
    public LoginUserVO getUserInfo(HttpServletRequest request) {
        String token = JwtUtil.getToken(request);
        String username = JwtUtil.getUsername(token);
        User user = getUserByName(username);
//        User user = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (user == null || user.getId() == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户未登录");
        }
        Long id = user.getId();
        user = this.getById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return copeProperty(user);
    }

    @Override
    public Page<UserVO> getList(UserQueryDTO dto) {
        CommomUtils.chenkPage(dto);
        Long current = dto.getCurrent();
        Long pageSize = dto.getPageSize();
        Page<User> page = new Page<>(current, pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(dto.getUserName()) && dto.getUserName() != null, User::getUserName, dto.getUserName());
        page = this.baseMapper.selectPage(page, wrapper);
        Page<UserVO> userVOPage = new Page<>(current, pageSize, page.getTotal());
        List<UserVO> list = new ArrayList<>();
        page.getRecords().forEach(e -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(e, userVO);
            list.add(userVO);
        });
        userVOPage.setRecords(list);
        return userVOPage;
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        String token = JwtUtil.getToken(request);
        String username = JwtUtil.getUsername(token);
        User user = getUserByName(username);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return user;
    }

    @Override
    public Boolean isAdmin(HttpServletRequest request) {
        String token = JwtUtil.getToken(request);
        String username = JwtUtil.getUsername(token);
        User user = getUserByName(username);
        return user != null && UserRoleEnum.ADMIN.getValue().equals(roleMapper.getRoleName(user.getId()));
    }

    @Override
    public Boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(roleMapper.getRoleName(user.getId()));
    }

    @Override
    public User getUserByName(String username) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUserAccount, username);
        User user = this.baseMapper.selectOne(userLambdaQueryWrapper);
        return user;
    }

    /**
     * 复制属性
     * @param user
     * @return
     */
    private LoginUserVO copeProperty(User user) {
        if (user == null){
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (userQueryRequest == null) {
            return queryWrapper;
        }
        String userAccount = userQueryRequest.getUserAccount();
        String userName = userQueryRequest.getUserName();

        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(userAccount), "userAccount", userAccount);
        queryWrapper.like(StringUtils.isNotBlank(userName), "userName", userName);
        queryWrapper.eq("isDelete", false);
        return queryWrapper;
    }

    @Override
    public Boolean resetPwd(UserResetPwdDTO userResetPwdDTO) {
        Long id = userResetPwdDTO.getId();
        String userPassword = userResetPwdDTO.getUserPassword();
        String checkPassword = userResetPwdDTO.getCheckPassword();
        if (StringUtils.isAnyBlank(id.toString(), userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空！");
        }
        if (userPassword.length() < 6 || userPassword.length() > 20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度不符合要求");
        }
        if (!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码不一致");
        }
        String encryptPwd = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        User user = new User();
        user.setId(id).setUserPassword(encryptPwd);
        User user1 = getById(id);
        if (user1 == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在！");
        }
        boolean b = this.updateById(user);
        return b;
    }
}
