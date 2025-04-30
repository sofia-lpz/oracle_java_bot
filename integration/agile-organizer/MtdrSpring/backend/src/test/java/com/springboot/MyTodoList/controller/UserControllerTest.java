package com.springboot.MyTodoList.controller;

import com.springboot.MyTodoList.controller.UserController;
import com.springboot.MyTodoList.model.User;
import com.springboot.MyTodoList.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {
    
    @Mock
    private UserService userService;
    
    @InjectMocks
    private UserController userController;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testGetAllUsers() {
        // Arrange
        User user1 = new User();
        user1.setID(1);
        user1.setName("John Doe");
        
        User user2 = new User();
        user2.setID(2);
        user2.setName("Jane Doe");
        
        List<User> expectedUsers = Arrays.asList(user1, user2);
        
        when(userService.findAll()).thenReturn(expectedUsers);
        
        // Act
        List<User> actualUsers = userController.getAllUsers();
        
        // Assert
        assertEquals(expectedUsers, actualUsers);
        verify(userService, times(1)).findAll();
    }
    
    @Test
    public void testGetUserById_Success() {
        // Arrange
        int userId = 1;
        User expectedUser = new User();
        expectedUser.setID(userId);
        expectedUser.setName("John Doe");
        
        when(userService.getUserById(userId)).thenReturn(expectedUser);
        
        // Act
        ResponseEntity<User> response = userController.getUserById(userId);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUser, response.getBody());
        verify(userService, times(1)).getUserById(userId);
    }
    
    @Test
    public void testGetUserById_NotFound() {
        // Arrange
        int userId = 99;
        when(userService.getUserById(userId)).thenThrow(new RuntimeException("User not found"));
        
        // Act
        ResponseEntity<User> response = userController.getUserById(userId);
        
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, times(1)).getUserById(userId);
    }
    
    @Test
    public void testAddUser_Success() throws Exception {
        // Arrange
        User newUser = new User();
        newUser.setName("New User");
        newUser.setPhoneNumber("1234567890");
        
        User savedUser = new User();
        savedUser.setID(3);
        savedUser.setName("New User");
        savedUser.setPhoneNumber("1234567890");
        
        when(userService.addUser(newUser)).thenReturn(savedUser);
        
        // Act
        ResponseEntity response = userController.addUser(newUser);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).addUser(newUser);
        assertEquals("3", response.getHeaders().get("location").get(0));
    }
    
    @Test
    public void testUpdateUser_Success() {
        // Arrange
        int userId = 1;
        User userToUpdate = new User();
        userToUpdate.setID(userId);
        userToUpdate.setName("Updated Name");
        
        User updatedUser = new User();
        updatedUser.setID(userId);
        updatedUser.setName("Updated Name");
        
        when(userService.updateUser(userId, userToUpdate)).thenReturn(updatedUser);
        
        // Act
        ResponseEntity response = userController.updateUser(userToUpdate, userId);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
        verify(userService, times(1)).updateUser(userId, userToUpdate);
    }
    
    @Test
    public void testUpdateUser_NotFound() {
        /*
        // Arrange
        int userId = 99;
        User userToUpdate = new User();
        userToUpdate.setID(userId);
        
        when(userService.updateUser(userId, userToUpdate)).thenReturn(null);
        
        // Act
        ResponseEntity response = userController.updateUser(userToUpdate, userId);
        
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, times(1)).updateUser(userId, userToUpdate);
        */
    }
    
    @Test
    public void testDeleteUser_Success() {
        // Arrange
        int userId = 1;
        when(userService.deleteUser(userId)).thenReturn(true);
        
        // Act
        ResponseEntity<Boolean> response = userController.deleteUser(userId);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
        verify(userService, times(1)).deleteUser(userId);
    }
    
    @Test
    public void testDeleteUser_NotFound() {
        /*
        // Arrange
        int userId = 99;
        when(userService.deleteUser(userId)).thenReturn(false);
        
        // Act
        ResponseEntity<Boolean> response = userController.deleteUser(userId);
        
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(false, response.getBody());
        verify(userService, times(1)).deleteUser(userId);
        */
    }
}