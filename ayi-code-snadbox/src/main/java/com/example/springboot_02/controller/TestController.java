package com.example.springboot_02.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * className:TestController
 * Package:com.example.springboot_02.controller
 * Description: TODO
 *
 * @Date: 2023/12/13 23:10
 * @Author:hjy
 */
@RestController
@RequestMapping("/")
public class TestController {
    @GetMapping("/health")
    public String checkHealth() {
        return "ok";
    }
}
