package com.zss.smile.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * 1. secret 加密密钥
 * 2. expire 过期时间
 * 3. issuer 签发者
 * 4. subject 面向的用户
 *
 * @author ZSS
 * @date 2020/7/10 16:35
 * @desc Token 工具类
 */
@Getter
@Setter
@SuppressWarnings("unused")
public class TokenUtil {

    private static String issuer;
    private static String secret;
    private static String subject;

    /**
     * 有效期 - 30天
     */
    private static final long EXPIRATION_DATE = 1000L * 60L * 60L * 24L * 15L;
    
    static {
        Properties properties = new Properties();
        InputStream resourceAsStream = TokenUtil.class
                .getClassLoader()
                .getResourceAsStream("config/jwt-config.properties");
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        issuer = properties.getProperty("jwt.issuer");
        secret = properties.getProperty("jwt.secret-key");
        subject = properties.getProperty("jwt.subject");
    }

    /**
     * 生成Token
     *
     * @param map 自定义键值对
     * @return String
     */
    public static String createToken(Map<String, String> map) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTCreator.Builder builder = JWT.create()
                .withIssuer(issuer)
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_DATE));

        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.withClaim(entry.getKey(), entry.getValue());
        }
        return builder.sign(algorithm);
    }

    /**
     * 通过token获取其中的key对应的值
     *
     * @param token 认证的token
     * @param key   对应的键
     * @return claim对象
     */
    public static Claim getClaim(String token, String key) throws JWTVerificationException {
        DecodedJWT jwt = getDecoded(token);
        return jwt.getClaim(key);
    }

    /**
     * 验证Token是否过期失效
     *
     * @param token token
     * @return boolean
     */
    public static boolean isValid(String token) {
        if (StringUtils.isEmpty(token)) {
            return false;
        } else {
            try {
                getDecoded(token);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    /**
     * 获取 decoded
     *
     * @param token token串
     * @return DecodedJWT
     */
    private static DecodedJWT getDecoded(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

}
