package com.zss.smile.models.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author ZSS
 * @date 2021/9/18 15:15
 * @desc 修改密码实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyVo implements Serializable {

    @NotEmpty
    private String oldPassword;

    @NotEmpty
    private String newPassword;
}
