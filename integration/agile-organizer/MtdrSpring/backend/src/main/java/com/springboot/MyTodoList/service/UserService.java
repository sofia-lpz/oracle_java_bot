package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.User;
import com.springboot.MyTodoList.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        return users;
    }

    public ResponseEntity<User> getUserById(int id) {
        Optional<User> userById = userRepository.findById(id);
        if (userById.isPresent()) {
            return new ResponseEntity<>(userById.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<User> getUserByName(String name) {
        Optional<User> userByName = userRepository.findByName(name);
        if (userByName.isPresent()) {
            return new ResponseEntity<>(userByName.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public User addUser(User newUser) {
        return userRepository.save(newUser);
    }

    public boolean deleteUser(int id) {
        try {
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public User updateUser(int id, User user2update) {
        Optional<User> dbUser = userRepository.findById(id);
        if (dbUser.isPresent()) {
            User user = dbUser.get();
            user.setID(id);
            // user.setNumber(user2update.getNumber()); // Removed
            user.setPassword(user2update.getPassword());
            return userRepository.save(user);
        } else {
            return null;
        }
    }

    // @Override
    // public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
    //     User user = userRepository.findByPhone_number(phoneNumber)
    //             .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + phoneNumber));
    //     return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), new ArrayList<>());
    // }

    // public boolean userExists(String phoneNumber) {
    //     return userRepository.findByPhone_number(phoneNumber).isPresent();
    // }
}