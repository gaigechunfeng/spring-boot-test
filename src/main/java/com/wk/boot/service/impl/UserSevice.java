package com.wk.boot.service.impl;

import com.wk.boot.dao.BaseDAO;
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

    //    private IUserRepository userRepository;
    private BaseDAO baseDAO;

    //    @Autowired
//    public void setUserRepository(IUserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    @Autowired
    public void setBaseDAO(BaseDAO baseDAO) {
        this.baseDAO = baseDAO;
    }

    @Override
    public List<User> listAll() {
//        return baseDAO.listAll(User.class);
        return baseDAO.findAll(User.class);
    }

    @Override
    public void save(User user) {

        if (user.getId() == 0L) {
            user.setPwd(Util.encodePwd(user.getPwd()));
        }
//        return userRepository.save(user);
        baseDAO.save(user);
    }

    @Override
    public User findByUsername(String username) {

//        List<User> users = (List<User>) userRepository.findByUsername(username);
        List<User> users = baseDAO.find("select * from dcs_user where wid=?", User.class, username);
        if (!Util.isEmpty(users)) {
            return users.get(0);
        }
        return null;
    }

    @Override
    public User findById(Long id) {
//        return userRepository.findOne(id);
        return baseDAO.findById(User.class, id);
    }

    @Override
    public void removeById(Long id) {
//        userRepository.delete(id);
        baseDAO.deleteById(User.class, id);
    }
}
