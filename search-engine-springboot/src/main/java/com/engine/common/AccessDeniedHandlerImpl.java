package com.engine.common;

import com.alibaba.fastjson.JSON;
import com.engine.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        R<Object> result = R.error(HttpStatus.FORBIDDEN.value(), "权限不足");
        String jsonString = JSON.toJSONString(result);
        WebUtils.renderString(httpServletResponse,jsonString);
    }
}
