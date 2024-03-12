package com.devrezaur.main;

import com.devrezaur.main.UserNotFoundException;
import com.devrezaur.main.controller.UserController2;
import com.devrezaur.main.model.User2;
import com.devrezaur.main.repository.UserRepository2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private UserRepository2 userRepository;

    @InjectMocks
    private UserController2 userController;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        // Arrange
        List<User2> users = Arrays.asList(new User2(1L, "John", "Doe", "john@example.com"));
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User2> result = userController.getAllUsers();

        // Assert
        assertEquals(users, result);
    }

    @Test
    public void testGetUserById() {
        // Arrange
        Long userId = 1L;
        User2 user = new User2(userId, "John", "Doe", "john@example.com");
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

        // Act
        User2 result = userController.getUserById(userId);

        // Assert
        assertEquals(user, result);
    }

    @Test
    public void testDeleteUser() {
        // Arrange
        Long userId = 1L;

        // Act
        String result = userController.deleteUser(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
        assertEquals("User with ID 1 deleted successfully", result);
    }

    @Test
    public void testCreateUser() {
        // Arrange
        User2 user = new User2(1L, "John", "Doe", "john@example.com");

        // Act
        String result = userController.createUser(user);

        // Assert
        verify(userRepository, times(1)).save(user);
        assertEquals("User created successfully", result);
    }

    @Test
    public void testUpdateUser() {
        // Arrange
        Long userId = 1L;
        User2 existingUser = new User2(userId, "John", "Doe", "john@example.com");
        User2 updatedUserDetails = new User2(userId, "Jane", "Doe", "jane@example.com");

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(existingUser));

        // Act
        String result = userController.updateUser(userId, updatedUserDetails);

        // Assert
        verify(userRepository, times(1)).save(existingUser);
        assertEquals("User updated successfully", result);
        assertEquals(updatedUserDetails.getFirstName(), existingUser.getFirstName());
        assertEquals(updatedUserDetails.getLastName(), existingUser.getLastName());
        assertEquals(updatedUserDetails.getEmail(), existingUser.getEmail());
    }

    @Test(expected = UserNotFoundException.class)
    public void testUpdateUserNotFound() {
        // Arrange
        Long userId = 1L;
        User2 updatedUserDetails = new User2(userId, "Jane", "Doe", "jane@example.com");

        // Act
        userController.updateUser(userId, updatedUserDetails);

        // Assert (Expecting UserNotFoundException)
    }
}
