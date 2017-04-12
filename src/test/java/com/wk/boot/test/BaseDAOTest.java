package com.wk.boot.test;

import com.wk.boot.dao.BaseDAO;
import com.wk.boot.model.User;
import com.wk.boot.util.Util;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 005689 on 2017/4/12.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseDAOTest {

    private BaseDAO baseDAO;

    @Autowired
    public void setBaseDAO(BaseDAO baseDAO) {
        this.baseDAO = baseDAO;
    }

    @Test
    public void testTransaction(){

        List<User> users = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            User user = new User();

            user.setWid("test" + (i + 1));
            user.setPwd("test");
            user.setUui(Util.uuid());
            users.add(user);
        }

        baseDAO.save(users);

    }
}
