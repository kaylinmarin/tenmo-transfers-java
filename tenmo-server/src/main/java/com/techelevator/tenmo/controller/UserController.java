package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }
    @RequestMapping(method = RequestMethod.GET)
    public List<User> getAllUsers() {
        return userDao.getUsers();
    }

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable int id) {
        return userDao.getUserById(id);
    }

}
