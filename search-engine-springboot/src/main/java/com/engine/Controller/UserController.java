package com.engine.Controller;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.engine.Service.UserService;
import com.engine.common.R;
import com.engine.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    /*
        {
            "userName":"tjs",
            "password":"654321"
        }
     */
    public R<Map<String,String>> Login(@RequestBody User user){

        String userName = user.getUserName();
        String password = user.getPassword();
        Map<String, String> map = userService.login(userName, password);
        return R.success(map);
    }

    //权限登录之后的
    @GetMapping("/logout")
    public R<String> Logout(){
        boolean logout = userService.logout();
        return logout?R.success("登出成功"):R.error(500,"登出失败");
    }

    @PostMapping("/regist")
    /*
    {
        "userName":"tjs",
        "nickName":"luckydog",
        "password":"654321",
        "email":"123121@qq.com",
        "sex":"1",
        "phonenumber":"2312321312"
    }
     */
    public R<String> Regist(@RequestBody User user) {
        System.out.println(user);
        String userName = user.getUserName();
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);
        User one = userService.getOne(queryWrapper);
        if(!Objects.isNull(one)){
            return R.error(500,"用户名已存在");
        }
        userService.saveUser(user);

        return R.success("注册成功！");
    }
}
