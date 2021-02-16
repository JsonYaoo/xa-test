package com.jsonyao.ttc.service;

import com.jsonyao.ttc.db142.dao.UserMapper;
import com.jsonyao.ttc.db142.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 分布式接口幂等性: 用户测试服务
 */
@Service
@Slf4j
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private CuratorFramework curatorClient;

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

    /**
     * 根据UserId更新User(有更新次数有版本号)
     * @param user
     * @return
     */
    public int updateUser3(User user) {
        return userMapper.updateUser2(user);
    }

    /**
     * 插入用户: 根据唯一业务单号创建分布式锁保证幂等性 => 有唯一业务单号: 比如username
     * @param user
     */
    public int insertUser(User user) throws Exception {
        // 根据唯一业务单号创建分布式锁
        InterProcessMutex lock = new InterProcessMutex(curatorClient, "/" + user.getUsername());

        // 获取分布式锁, 只有获取到分布式锁的才执行插入, 且插入后不释放锁, 让其自动过期, 这个主要是防止短时间内抖动的点击, 而要想长期保证username唯一需要插入前做业务校验
        boolean isLock = lock.acquire(30, TimeUnit.SECONDS);
        if(isLock){
            return userMapper.insertSelective(user);
        }

        return 0;
    }
}
