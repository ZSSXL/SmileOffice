package com.zss.smile.repository;

import com.zss.smile.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author ZSS
 * @date 2021/9/6 9:45
 * @desc 用户信息持久化
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * 通过登录名获取用户信息
     *
     * @param loginName 登录名
     * @return user
     */
    Optional<User> findByLoginName(String loginName);

    /**
     * 通过登录名和密码获取用户信息
     *
     * @param loginName 登录名
     * @param password  密码
     * @return user
     */
    Optional<User> findByLoginNameAndPassword(String loginName, String password);
}
