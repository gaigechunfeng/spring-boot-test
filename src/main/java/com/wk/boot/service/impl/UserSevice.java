package com.wk.boot.service.impl;

import com.wk.boot.dao.IUserRepository;
import com.wk.boot.model.User;
import com.wk.boot.service.IUserService;
import com.wk.boot.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by gaige on 2017/4/7.
 */
@Service
public class UserSevice implements IUserService {

    private IUserRepository userRepository;

    @Autowired
    public void setUserRepository(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> listAll() {
//        return baseDAO.listAll(User.class);
        return (List<User>) userRepository.findAll();
    }

    @Override
    public User save(User user) {

        user.setPassword(Util.md5(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {

        List<User> users = (List<User>) userRepository.findByUsername(username);
        if (!Util.isEmpty(users)) {
            return users.get(0);
        }
        return null;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findOne(id);
    }

    @Override
    public void removeById(Long id) {
        userRepository.delete(id);
    }
}
