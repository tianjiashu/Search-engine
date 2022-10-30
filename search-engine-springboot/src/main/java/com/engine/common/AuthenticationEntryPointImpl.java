package com.engine.common;

import com.alibaba.fastjson.JSON;
import com.engine.utils.WebUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        R<Object> result = R.error(403, "认证失败请重新登录");
        String jsonString = JSON.toJSONString(result);
        WebUtils.renderString(httpServletResponse,jsonString);
    }
}
