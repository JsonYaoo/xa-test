package com.jsonyao.ttc.controller;

import com.jsonyao.ttc.db142.model.User;
import com.jsonyao.ttc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 分布式接口幂等性: 用户测试服务前端控制器
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("userList")
    public String userList(ModelMap map){
        List<User> users = userService.getAllUsers();
        map.addAttribute("users", users);
        return "user/user-list";
    }
}
