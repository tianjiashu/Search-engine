package com.engine.Filter;

import com.engine.common.CustomException;
import com.engine.domain.LoginUser;
import com.engine.utils.JwtUtil;
import com.engine.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        if(Objects.isNull(token)||token.length()==0){
            filterChain.doFilter(request,response);
            return;
        }
        //解析token
        String userId = "";
        try {
            userId = JwtUtil.parseJWT(token).getSubject();
        } catch (Exception e) {
            throw new CustomException("token不合法");
        }
        //根据userId查redis
        LoginUser loginUser = redisCache.getCacheObject("login:" + userId);
        if(Objects.isNull(loginUser)){
            throw new CustomException("用户未登录！");
        }
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(loginUser,null,null));
        filterChain.doFilter(request,response);
    }
}
