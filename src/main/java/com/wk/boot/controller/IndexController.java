package com.wk.boot.controller;

import com.wk.boot.constant.Error;
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

    @RequestMapping(value = "/login")
    public Msg login(HttpServletRequest request) {

        Object loginExceptionName = request.getAttribute("shiroLoginFailure");
        if (loginExceptionName != null) {
            switch (loginExceptionName.toString()) {
                case "org.apache.shiro.authc.IncorrectCredentialsException":
                    throw new AuthenticationException(Error.PASSWORD_ERROR.name());
                case "org.apache.shiro.authc.UnknownAccountException":
                    throw new AuthenticationException(Error.UNKNOW_ACCOUNT.name());
                default:
                    throw new AuthenticationException(Error.LOGIN_ERROR.name());
            }
        }
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        if (username == null) {
            throw new AuthenticationException(Error.NO_LOGIN.name());
        }
        return Msg.success(userService.findByUsername(username));
    }
}
