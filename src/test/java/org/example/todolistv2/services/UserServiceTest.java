package org.example.todolistv2.services;

import org.example.todolistv2.entity.User;
import org.example.todolistv2.exceptions.BadRequestException;
import org.example.todolistv2.exceptions.NotFoundObjectException;
import org.example.todolistv2.mongotemplates.UserRepository;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private GroupService groupServices;


    @DisplayName("UserCreate")
    @Test
    void create() {
        User user = new User();
        user.setFullName("Josh");
        assertTrue(userService.create(user));
        verify(userRepository, times(1)).insert(any(User.class));
    }

    @DisplayName("Создание null объекта")
    void createTestWithNullUserObject() {
        User user = new User();
        assertThrows(BadRequestException.class, () -> userService.create(null));
    }

    @DisplayName("Создание объекта без заданного имени")
    void createWithNullName() {
        User user = new User();
        assertThrows(BadRequestException.class, () -> userService.create(user));
    }

    @DisplayName("UserUpdate")
    @Test
    void update() {
        String preparedUserId = "randomUserId";
        String preparedUserFullName = "justName";

        User preparedMockUser = new User();
        preparedMockUser.setId(preparedUserId);
        preparedMockUser.setFullName(preparedUserFullName);

        User preparedSentUser = new User();

        when(userRepository.findUserById(anyString())).thenReturn(null);
        when(userRepository.findUserById(preparedUserId)).thenReturn(preparedMockUser);
        when(groupServices.notExist(anyString())).thenReturn(true);


        assertThrows(BadRequestException.class, () -> userService.update(preparedUserId, null));
        assertThrows(BadRequestException.class, () -> userService.update(preparedUserId, preparedSentUser));
        preparedSentUser.setFullName("NotJustName");
        assertThrows(NotFoundObjectException.class, () -> userService.update("IdNotEqualPreparedUserId", preparedSentUser));

        assertTrue(userService.update(preparedUserId, preparedSentUser));


        verify(userRepository, times(1)).save(any(User.class));
    }

    @DisplayName("UserRemove")
    @Test
    void remove() {
        String preparedUserId = "randomUserId";
        User preparedMockUser = new User();
        preparedMockUser.setId(preparedUserId);

        when(userRepository.findUserById(anyString())).thenReturn(null);
        when(userRepository.findUserById(preparedUserId)).thenReturn(preparedMockUser);

        assertThrows(NotFoundObjectException.class, () -> userService.remove("123"));
        assertThrows(NotFoundObjectException.class, () -> userService.remove(null));
        assertTrue(userService.remove(preparedUserId));

        verify(userRepository, times(1)).delete(any());
    }

    @DisplayName("UserFind")
    @Test
    void find() {
        String preparedUserId = "randomUserId";
        User preparedMockUser = new User();
        preparedMockUser.setId(preparedUserId);
        List<User> notEmptyUsersList = new ArrayList<>();
        notEmptyUsersList.add(preparedMockUser);
        List<User> emptyUserList = new ArrayList<>();

        when(userRepository.findAll()).thenReturn(emptyUserList);
        assertThrows(NotFoundObjectException.class, () -> userService.find());
        when(userRepository.findAll()).thenReturn(notEmptyUsersList);
        assertEquals(notEmptyUsersList, userService.find());
    }

    @DisplayName("UserGetInfo")
    @Test
    void getInfo() {
        //Init
        String preparedUserId = "randomUserId";
        User preparedMockUser = new User();
        preparedMockUser.setId(preparedUserId);

        when(userRepository.findUserById(anyString())).thenReturn(null);
        when(userRepository.findUserById(preparedUserId)).thenReturn(preparedMockUser);

        assertEquals(preparedMockUser, userService.getInfo(preparedUserId));
        assertThrows(NotFoundObjectException.class, () -> userService.getInfo("123"));
        assertThrows(NotFoundObjectException.class, () -> userService.getInfo(null));
    }
}