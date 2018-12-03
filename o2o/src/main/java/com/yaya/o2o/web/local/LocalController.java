package com.yaya.o2o.web.local;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/local")
public class LocalController {
    //绑定账号页路由
    @RequestMapping(value = "/accountbind", method = RequestMethod.GET)
    private String accountbind() {
        return "local/accountbind";
    }

    //修改密码页路由
    @RequestMapping(value = "/changepwd", method = RequestMethod.GET)
    private String changepsw() {
        return "local/changepwd";
    }

    //登录页路由
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    private String login() {
        return "local/login";
    }

    //注册页路由
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    private String register() {
        return "local/register";
    }
}
