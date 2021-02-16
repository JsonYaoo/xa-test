package com.jsonyao.ttc.service;

import com.jsonyao.ttc.db142.dao.UserMapper;
import com.jsonyao.ttc.db142.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分布式接口幂等性: 用户测试服务
 */
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 查询所有用户
     * @return
     */
    public List<User> getAllUsers() {
        return userMapper.selectAllUsers();
    }
}
