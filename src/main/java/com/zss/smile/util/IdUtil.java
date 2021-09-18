package com.zss.smile.util;

import java.util.UUID;

/**
 * @author ZSS
 * @date 2021/9/6 15:28
 * @desc 获取随机Id
 */
public class IdUtil {

    /**
     * 获取 UUID
     *
     * @return String
     */
    public static String getId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
