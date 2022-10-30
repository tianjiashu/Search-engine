package com.engine.common;

import com.engine.utils.JwtUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class ParseTokenAOP {


    @Around("@annotation(com.engine.common.ParseToken)")
    public Object ParseToken(ProceedingJoinPoint pjp){
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("token");
        String userId = "";
        try {
            userId = JwtUtil.parseJWT(token).getSubject();
        } catch (Exception e) {
            throw new CustomException("未知错误");
        }
        Object[] args = pjp.getArgs();
        args[args.length-1] = userId;

        Object proceed = null;
        try {
            proceed = pjp.proceed(args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return proceed;
    }
}
