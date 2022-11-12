package com.example.firstspringbootsecurityapp.service;



import com.example.firstspringbootsecurityapp.config.WebSecurityConfig;
import com.example.firstspringbootsecurityapp.dao.UserDao;
import com.example.firstspringbootsecurityapp.exception_handling.BadDataException;
import com.example.firstspringbootsecurityapp.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service
@Transactional

public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserDao userDao;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, @NonNull @Lazy PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public User getUserById(long id) {
        return userDao.getUserById(id);
    }

    public void add(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.add(user);
    }

    public void update(Long id, User newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        userDao.update(id, newUser);
    }

    public void delete(long id) {
        userDao.delete(id);
    }

    public List<User> getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> user = userDao.getUserByUsername(username);

        if (user.isEmpty()) {
            throw new BadDataException("Wrong username or password. Try again");
        }
        return user.get(0);
    }
}
