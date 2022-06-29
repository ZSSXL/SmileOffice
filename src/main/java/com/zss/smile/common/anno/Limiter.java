package com.zss.smile.common.anno;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author zhoushs@dist.com.cn
 * @date 2021/11/12 14:01
 * @desc 请求限流注解
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Limiter {

    /**
     * 资源的key,唯一
     * 作用：不同的接口，不同的流量控制
     */
    String key() default "";

    /**
     * 最多的访问限制次数
     */
    double permitsPerSecond();

    /**
     * 获取令牌的最大等待时间
     */
    long timeout();

    /**
     * 获取令牌最大等待时间,单位(例:分钟/秒/毫秒) 默认:毫秒
     */
    TimeUnit timeunit() default TimeUnit.MILLISECONDS;

    /**
     * 得不到令牌的提示语
     */
    String msg() default "系统繁忙,请等候再试......";
}
