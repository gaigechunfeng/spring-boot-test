package com.wk.boot.service;

import com.wk.boot.model.User;

import java.util.List;

/**
 * Created by gaige on 2017/4/7.
 */
public interface IUserService {
    List<User> listAll();

    void save(User user);

    User findByUsername(String username);

    User findById(Long id);

    void removeById(Long id);
}
