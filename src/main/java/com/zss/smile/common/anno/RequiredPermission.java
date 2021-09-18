package com.zss.smile.common.anno;

import java.lang.annotation.*;

/**
 * @author ZSS
 * @date 2020/3/16 20:20
 * @desc 自定义注解 身份校验
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RequiredPermission {
}
