package com.wk.boot.test;

import com.wk.boot.client.Navigator;
import com.wk.boot.model.User;
import com.wk.boot.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gaige on 2017/4/10.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RestClientTest {

    @Autowired
    private IUserService userService;

    @Autowired
    private Navigator.WebPage webPage;

    @Test
    public void testClientWithLogin() {

        addUser();

        Map<String, String> params = new HashMap<>();
        params.put("username", "gaige");
        params.put("password", "gaige");

        //login POST
        User user = webPage.post("http://localhost:8888/api/login", params, User.class);
        System.out.println("user::" + user);

        Long id = user.getId();

        //find one GET
        user = webPage.get("http://localhost:8888/api/users/{0}", null, User.class, id);
        System.out.println("user2::" + user);

        //delete one DELETE
        webPage.delete("http://localhost:8888/api/users/{0}", null, null, id);

        user = webPage.get("http://localhost:8888/api/users/{0}", null, User.class, id);
        System.out.println("user3::" + user);

        webPage.close();

        user = webPage.get("http://localhost:8888/api/users/{0}", null, User.class, id);
        System.out.println("user4::" + user);
    }

    private void addUser() {

        userService.save(new User("gaige", "gaige", "gaigechunfeng"));
    }

}
