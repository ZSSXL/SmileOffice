package com.zss.smile.service.impl;

import com.zss.smile.models.entities.User;
import com.zss.smile.repository.UserRepository;
import com.zss.smile.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author ZSS
 * @date 2021/9/6 9:48
 * @desc 服务层接口方法实现
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Boolean isExistInDb(String loginName) {
        User user = userRepository.findByLoginName(loginName).orElse(null);
        return user != null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User login(String loginName) {
        return userRepository.findByLoginName(loginName).orElse(null);
    }

    @Override
    public User getUserById(String userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
