package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.User;
import com.springboot.MyTodoList.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        return users;
    }


    public User getUserById(int id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User getUserByName(String name) {
        return userRepository.findByName(name)
            .orElseThrow(() -> new RuntimeException("User not found with name: " + name));
    }

    public User getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow(() -> new RuntimeException("User not found with phone number: " + phoneNumber));
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
            user.setPhoneNumber(user2update.getPhoneNumber());
            user.setPassword(user2update.getPassword());
            return userRepository.save(user);
        } else {
            return null;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + phoneNumber));
    }

    public boolean userExists(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).isPresent();
    }
}