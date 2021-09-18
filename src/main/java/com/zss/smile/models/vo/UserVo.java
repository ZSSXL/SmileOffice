package com.zss.smile.models.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author ZSS
 * @date 2021/9/6 16:22
 * @desc 用户传输对象
 */
@Data
public class UserVo implements Serializable {

    @NotEmpty
    private String username;

    @NotEmpty
    private String loginName;

    @NotEmpty
    private String password;
}
