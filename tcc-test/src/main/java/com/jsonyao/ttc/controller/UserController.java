package com.jsonyao.ttc.controller;

import com.jsonyao.ttc.db142.model.User;
import com.jsonyao.ttc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分布式接口幂等性: 用户测试服务前端控制器
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 查询所有用户
     * @return
     */
    @RequestMapping("userList")
    public String userList(ModelMap map){
        List<User> users = userService.getAllUsers();
        map.addAttribute("users", users);
        return "user/user-list";
    }

    @RequestMapping("delUser")
    @ResponseBody
    public Map<String,Object> delUser(@RequestParam Integer userId){
        int result = userService.delUser(userId);
        Map<String,Object> map = new HashMap<>();
        map.put("status",result);
        return map;
    }
}
