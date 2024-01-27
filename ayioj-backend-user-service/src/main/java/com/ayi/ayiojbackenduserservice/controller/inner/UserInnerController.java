package com.ayi.ayiojbackenduserservice.controller.inner;

import com.ayi.ayiojbackendmodel.model.entity.User;
import com.ayi.ayiojbackenduserservice.mapper.RoleMapper;
import com.ayi.ayiojbackenduserservice.service.UserService;
import com.ayi.ayiojbackenduserservice.shiro.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * className:UserInnerController
 * Package:com.ayi.ayiojbackenduserservice.controller.inner
 * Description: TODO
 *
 * @Date: 2024/1/25 11:02
 * @Author:hjy
 */

@RestController
@RequestMapping("/inner")
public class UserInnerController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleMapper roleMapper;
    @GetMapping("/getUserByName")
    User getUserByName(@RequestParam("userName") String username) {
        return userService.getUserByName(username);
    }

    @GetMapping("/getById/")
    User getById(@RequestParam("userId") Long userId) {
        return userService.getById(userId);
    }

    @PostMapping("/listByIds")
    List<User> listByIds(@RequestBody Collection<Long> userIdSet) {
        return userService.listByIds(userIdSet);
    }

    @GetMapping("/getRoleName")
    String getRoleName(@RequestParam("id") Long id) {
        return roleMapper.getRoleName(id);
    }

    @GetMapping("/getUserNameByToken")
    String getUserNameByToken(@RequestParam("token") String token) {
        return JwtUtil.getUsername(token);
    }
}
