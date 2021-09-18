package com.zss.smile.common.aspect;

import com.zss.smile.common.SmileConstant;
import com.zss.smile.common.response.ResponseCode;
import com.zss.smile.common.response.ServerResponse;
import com.zss.smile.helpers.DocumentManager;
import com.zss.smile.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ZSS
 * @date 2020/3/16 20:22
 * @desc 身份校验拦截
 */
@Slf4j
@Aspect
@Component
public class PermissionAspect {

    @Around(value = "@annotation(com.zss.smile.common.anno.RequiredPermission)")
    public Object aroundPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取RequestHeader
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (sra != null) {
            HttpServletRequest request = sra.getRequest();
            DocumentManager.init(request);

            String token = request.getHeader(SmileConstant.SMILE_TOKEN);
            if (TokenUtil.isValid(token)) {
                return joinPoint.proceed();
            } else {
                return ServerResponse.error(ResponseCode.NEED_LOGIN.getCode(),
                        "Token已过期，请重新登录");
            }
        } else {
            log.error("权限校验拦截失败");
            return ServerResponse.error("权限校验拦截失败，请重新发送请求");
        }
    }
}
