package com.wk.boot.test;

import com.wk.boot.client.RestClient;
import com.wk.boot.model.User;
import com.wk.boot.service.IUserService;
import com.wk.boot.util.Util;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gaige on 2017/4/7.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private IUserService userService;

    @Test
    public void testSaveUser() {

        for (int i = 0; i < 10; i++) {
            System.out.println(userService.save(new User("name" + i, "username" + i)));
        }

        System.out.println(userService.findByUsername("username5"));
    }


}
