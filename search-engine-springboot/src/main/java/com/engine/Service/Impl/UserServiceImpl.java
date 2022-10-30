package com.engine.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.engine.common.SnowflakeManager;
import com.engine.normalMapper.FavoritesMapper;
import com.engine.normalMapper.UserMapper;
import com.engine.Service.UserService;
import com.engine.common.CustomException;
import com.engine.domain.LoginUser;
import com.engine.domain.User;
import com.engine.utils.JwtUtil;
import com.engine.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SnowflakeManager snowflakeManager;

    @Autowired
    private FavoritesMapper favoritesMapper;

    @Override
    public Map<String,String> login(String username, String password){
        //登录认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){//认证不通过
            log.error("认证不通过");
            throw new CustomException("用户名或密码错误");
        }
        //认证通过
        LoginUser loginuser = (LoginUser) authenticate.getPrincipal();

        //生成token
        String userId = loginuser.getUser().getId().toString();
        String token = JwtUtil.createJWT(userId);
        Map<String,String> map = new HashMap<>(5);
        map.put("token",token);

        //将通过校验的authenticate存入redis  过期时间设为7天
        redisCache.setCacheObject("login:"+userId,loginuser,7, TimeUnit.DAYS);

        return map;
    }


    @Override
    public boolean logout(){
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        User user = loginUser.getUser();
        String Userid = user.getId().toString();
        redisCache.deleteObject("login:"+Userid);
        return true;
    }

    @Override
    @Transactional
    public void saveUser(User user){
        long user_id = 0;
        try {
            user_id = snowflakeManager.nextValue();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("发生未知错误");
        }
        user.setId(user_id);
        String password = user.getPassword();
        password = passwordEncoder.encode(password);
        user.setPassword(password);
        this.save(user);
        favoritesMapper.createFavoriteTable(user_id);
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        favoritesMapper.AddContentsForFavoritesName(String.valueOf(user_id),"default",ids);
    }
}
