package com.wk.boot.controller;

import com.wk.boot.model.User;
import com.wk.boot.service.IUserService;
import com.wk.boot.util.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by gaige on 2017/4/7.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private IUserService userService;

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Msg getUsers() {

        return Msg.success(userService.listAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Msg detail(@PathVariable("id") Long id) {

        return Msg.success(userService.findById(id));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Msg delete(@PathVariable("id") Long id) {

        userService.removeById(id);

        return Msg.SUCCESS;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Msg save(User user) {

        return Msg.success(userService.save(user));
    }

}
