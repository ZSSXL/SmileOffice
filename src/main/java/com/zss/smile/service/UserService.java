package com.zss.smile.service;

import com.zss.smile.models.entities.User;

/**
 * @author ZSS
 * @date 2021/9/6 9:47
 * @desc 用户服务层接口
 */
public interface UserService {

    /**
     * 通过loginName判断用户是否存在
     *
     * @param loginName 登录名
     * @return boolean
     */
    Boolean isExistInDb(String loginName);

    /**
     * 创建用户
     *
     * @param user 用户信息
     */
    void createUser(User user);

    /**
     * 登录
     * @param loginName 登录名
     * @return  user
     */
    User login(String loginName);

    /**
     * 通过Id获取用户信息
     * @param userId 用户Id
     * @return user
     */
    User getUserById(String userId);
}
