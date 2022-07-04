package com.zss.smile.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ZSS
 * @date 2022/6/29 14:48
 * @desc minio配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    private String url;

    private String accessKey;

    private String secretKey;
}
