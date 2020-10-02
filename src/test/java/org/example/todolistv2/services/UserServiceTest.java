package org.example.todolistv2.services;

import org.example.todolistv2.entity.User;
import org.example.todolistv2.exceptions.BadRequestException;
import org.example.todolistv2.mongotemplates.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private GroupService groupServices;

    @DisplayName("UserCreateTest")
    @Test
    void createTest() {
        User user = new User();
        user.setFullName("Josh");

        Assert.assertEquals(user, userService.create(user));
    }

    @DisplayName("UserCreateFailTest ╯°□°）╯")
    @Test
    public void createFailTest() {
        User user = null;
        Assert.assertThrows(BadRequestException.class, () -> userService.create(user));
    }

    //@Test

}