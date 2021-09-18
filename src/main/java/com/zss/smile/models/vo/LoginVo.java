package com.zss.smile.models.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author ZSS
 * @date 2021/9/6 15:39
 * @desc 登录实体
 */
@Data
public class LoginVo implements Serializable {

    /**
     * 登录名
     */
    @NotEmpty
    private String loginName;

    /**
     * 密码
     */
    @NotEmpty
    private String password;
}
