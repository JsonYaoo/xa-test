package com.jsonyao.ttc.service;

import com.jsonyao.ttc.db142.dao.UserMapper;
import com.jsonyao.ttc.db142.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分布式接口幂等性: 用户测试服务
 */
@Service
@Slf4j
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

    /**
     * 根据UserId删除用户: 根据唯一业务号删除可以保证幂等, 最好删除前先查询一下
     * @param userId
     * @return
     */
    public int delUser(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user!=null){
            log.info("用户存在，用户为："+userId);
            return userMapper.deleteByPrimaryKey(userId);
        }
        log.info("用户不存在存在，用户为："+userId);
        return 0;
    }

    /**
     * 根据UserId查询用户
     * @param userId
     * @return
     */
    public User selectById(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    /**
     * 根据UserId更新User(无更新次数无版本号)
     * @param user
     * @return
     */
    public int updateUser1(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 根据UserId更新User(有更新次数无版本号)
     * @param user
     * @return
     */
    public int updateUser2(User user) {
        return userMapper.updateUser1(user);
    }
}
