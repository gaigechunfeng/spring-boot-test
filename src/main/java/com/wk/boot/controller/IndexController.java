package com.wk.boot.controller;

import com.wk.boot.model.User;
import com.wk.boot.util.Msg;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by gaige on 2017/4/10.
 */
@RestController
public class IndexController {

//    @RequestMapping("/error")
//    public Msg error(){
//
//        return Msg.ERROR;
//    }

    @RequestMapping("/login")
    public Msg login(User user) {

        System.out.println(user);
        return Msg.SUCCESS;
    }
}
