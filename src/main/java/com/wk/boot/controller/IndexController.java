package com.wk.boot.controller;

import com.wk.boot.service.IUserService;
import com.wk.boot.util.Msg;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by gaige on 2017/4/10.
 */
@RestController
public class IndexController {

    private IUserService userService;

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Msg login(HttpServletRequest request) {

        Object loginExceptionName = request.getAttribute("shiroLoginFailure");
        if (loginExceptionName != null) {
            switch (loginExceptionName.toString()) {
                case "org.apache.shiro.authc.IncorrectCredentialsException":
                    throw new AuthenticationException("密码错误");
                case "org.apache.shiro.authc.UnknownAccountException":
                    throw new AuthenticationException("不存在该用户");
                default:
                    throw new AuthenticationException("登录错误");
            }
        }
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        return Msg.success(userService.findByUsername(username));
    }
}
