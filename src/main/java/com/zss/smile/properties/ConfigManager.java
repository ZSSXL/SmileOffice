package com.zss.smile.properties;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author ZSS
 * @date 2021/9/1 14:16
 * @desc 配置管理
 */
@SuppressWarnings("unused")
public class ConfigManager {

    private static Properties properties;

    static {
        init();
    }


    private static void init() {
        try {
            properties = new Properties();
            InputStream stream = ConfigManager.class
                    .getResourceAsStream("config/settings.properties");
            properties.load(stream);
        } catch (Exception ex) {
            properties = null;
        }
    }

    public static String getProperty(String name) {
        if (properties == null) {
            return "";
        }

        String property = properties.getProperty(name);

        return property == null ? "" : property;
    }
}
