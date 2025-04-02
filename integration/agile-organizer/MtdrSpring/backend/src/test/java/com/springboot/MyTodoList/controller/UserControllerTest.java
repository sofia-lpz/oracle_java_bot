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
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User();
        User user2 = new User();
        List<User> userList = Arrays.asList(user1, user2);

        when(userService.findAll()).thenReturn(userList);

        List<User> result = userController.getAllUsers();
        assertEquals(2, result.size());
        verify(userService, times(1)).findAll();
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        when(userService.getUserById(1)).thenReturn(new ResponseEntity<>(user, HttpStatus.OK));

        ResponseEntity<User> result = userController.getUserById(1);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(userService, times(1)).getUserById(1);
    }

    @Test
    public void testGetUserByIdNotFound() {
        when(userService.getUserById(1)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        ResponseEntity<User> result = userController.getUserById(1);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(userService, times(1)).getUserById(1);
    }

    @Test
    public void testAddUser() throws Exception {
        User user = new User();
        when(userService.addUser(user)).thenReturn(user);

        ResponseEntity result = userController.addUser(user);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(userService, times(1)).addUser(user);
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        when(userService.updateUser(1, user)).thenReturn(user);

        ResponseEntity result = userController.updateUser(user, 1);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(userService, times(1)).updateUser(1, user);
    }

    @Test
    public void testUpdateUserNotFound() {
        User user = new User();
        when(userService.updateUser(1, user)).thenReturn(null);

        ResponseEntity result = userController.updateUser(user, 1);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(userService, times(1)).updateUser(1, user);
    }

    @Test
    public void testDeleteUser() {
        when(userService.deleteUser(1)).thenReturn(true);

        ResponseEntity<Boolean> result = userController.deleteUser(1);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(true, result.getBody());
        verify(userService, times(1)).deleteUser(1);
    }

    @Test
    public void testDeleteUserNotFound() {
        when(userService.deleteUser(1)).thenReturn(false);

        ResponseEntity<Boolean> result = userController.deleteUser(1);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals(false, result.getBody());
        verify(userService, times(1)).deleteUser(1);
    }

    @Test
    public void testNonAdminCannotCreateAdminUser() throws Exception {
        User newAdminUser = new User();
        newAdminUser.setName("New Admin");
        newAdminUser.setPassword("password123");
        newAdminUser.setRole(Role.ADMIN.toString());

        when(userService.addUser(newAdminUser))
                .thenThrow(new AccessDeniedException("Only admins can create admin users"));

        Exception exception = assertThrows(AccessDeniedException.class, () -> {
            userController.addUser(newAdminUser);
        });

        assertEquals("Only admins can create admin users", exception.getMessage());
        verify(userService, times(1)).addUser(newAdminUser);
    }

    @Test
    public void testPasswordNotReturnedInGetUser() {
        User user = new User();
        user.setID(1);
        user.setName("Test User");
        user.setPassword("encrypted_password");

        when(userService.getUserById(1)).thenReturn(new ResponseEntity<>(user, HttpStatus.OK));

        ResponseEntity<User> result = userController.getUserById(1);
        assertEquals(HttpStatus.OK, result.getStatusCode());

        assertNull(result.getBody().getPassword());
        verify(userService, times(1)).getUserById(1);
    }
}