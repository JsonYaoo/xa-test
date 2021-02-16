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

    /**
     * 根据UserId删除用户: 根据唯一业务号删除可以保证幂等, 最好删除前先查询一下
     * @param userId
     * @return
     */
    @RequestMapping("delUser")
    @ResponseBody
    public Map<String,Object> delUser(@RequestParam Integer userId){
        int result = userService.delUser(userId);
        Map<String,Object> map = new HashMap<>();
        map.put("status",result);
        return map;
    }

    /**
     * 根据UserId查询用户
     * @param userId
     * @return
     */
    @RequestMapping("userDetail")
    public String userDetail(@RequestParam  Integer userId,ModelMap map){
        User user = userService.selectById(userId);
        map.addAttribute("user",user);
        return "user/user-detail";
    }

    @RequestMapping("updateUser")
    public String updateUser(User user,String token) throws Exception {
        userService.updateUser(user);

//        Thread.sleep(5000);
//
//        if (user.getId() !=null){
//            System.out.println("更新用户");
//            userService.updateUser(user);
//        }else {
//            if (tokenSet.contains(token)){
//                System.out.println("添加用户");
//                userService.insertUser(user,token);
//            }else {
//                throw new Exception("token 不存在");
//            }
//        }
        return "redirect:/user/userList";
    }
}
