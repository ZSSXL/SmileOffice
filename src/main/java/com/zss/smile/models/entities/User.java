package com.zss.smile.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author ZSS
 * @date 2021/9/3 17:00
 * @desc 用户实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "smile_user")
@Table(appliesTo = "smile_user", comment = "用户")
public class User implements Serializable {

    /**
     * 用户信息
     */
    @Id
    @Column(name = "user_id", columnDefinition = "varchar(255)")
    private String userId;

    @Column(name = "username", nullable = false, columnDefinition = "varchar(255)")
    private String username;

    @Column(name = "login_name", nullable = false, columnDefinition = "varchar(255)")
    private String loginName;

    @Column(name = "password", nullable = false, columnDefinition = "varchar(255)")
    private String password;

    @Column(name = "create_time", nullable = false, columnDefinition = "varchar(20)")
    private String createTime;
}
