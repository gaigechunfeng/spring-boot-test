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
 * Created by gaige on 2017/4/10.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RestClientTest {

    @Autowired
    private IUserService userService;

    @Autowired
    private RestClient restClient;

    @Test
    public void testLogin() {

        addUser();

        Map<String, String> params = new HashMap<>();
        params.put("username", "gaige");
        params.put("password", "gaige");

        Map<String, String> header = new HashMap<>();
        header.put("Referer", "http://localhost:8888/api/login");
        User user = restClient.post("http://localhost:8888/api/login", params, header, User.class);
        System.out.println(user);
    }

    private void addUser() {

        userService.save(new User("gaige","gaige","gaigechunfeng"));
    }

    @Test
    public void testRestClient() {

        //ADD
        Map<String, String> newUserMap = new HashMap<>();
        newUserMap.put("username", "321321");
        newUserMap.put("name", "gaige");
        User user = restClient.post("http://localhost:8888/users", newUserMap, null, User.class);

        List<User> users = restClient.getForList("http://localhost:8888/users", User.class);
        assert !Util.isEmpty(users);

        //Find One
        User user2 = restClient.get("http://localhost:8888/users/{0}", User.class, user.getId());

        assert user2 != null;

        //Delete
        restClient.delete("http://localhost:8888/users/{0}", user.getId());

        //Find One
        User user3 = restClient.get("http://localhost:8888/users/{0}", User.class, user.getId());
        assert user3 == null;

    }
}
