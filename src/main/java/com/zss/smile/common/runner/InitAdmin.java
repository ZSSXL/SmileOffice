package com.zss.smile.common.runner;

import com.zss.smile.models.entities.User;
import com.zss.smile.service.UserService;
import com.zss.smile.util.DateUtil;
import com.zss.smile.util.EncryptionUtil;
import com.zss.smile.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author ZSS
 * @date 2021/9/6 15:21
 * @desc 初始化管理员账号
 */
@Slf4j
@Component
public class InitAdmin implements CommandLineRunner {

    private final UserService userService;

    private final static String ADMIN_LOGIN_NAME = "admin";

    private final static String ADMIN_PASSWORD = "Kk6neiid";

    @Autowired
    public InitAdmin(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        Boolean exist = userService.isExistInDb(ADMIN_LOGIN_NAME);
        if (exist){
            log.info("The initial administrator already exists");
        } else {
            User admin = User.builder()
                    .userId(IdUtil.getId())
                    .username(ADMIN_LOGIN_NAME)
                    .loginName(ADMIN_LOGIN_NAME)
                    .password(EncryptionUtil.encrypt(ADMIN_PASSWORD))
                    .createTime(DateUtil.currentTimestamp())
                    .build();
            try {
                userService.createUser(admin);
            } catch (Exception e){
                log.error("Init admin error [{}]", e.getMessage());
            }
        }
    }
}
