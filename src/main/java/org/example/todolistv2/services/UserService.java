package org.example.todolistv2.services;

import org.example.todolistv2.entity.User;
import org.example.todolistv2.exceptions.BadRequestException;
import org.example.todolistv2.exceptions.NotFoundObjectException;
import org.example.todolistv2.mongotemplates.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class UserService {
    @Autowired
    UserRepository userRepository;
    GroupService groupServices;

    public User create(User newUser) {
        if (newUser == null) {
            throw new BadRequestException();
        }
        return newUser;
    }

    public User update(String userId, User newUserInfo) {
        if (newUserInfo == null) {
            throw new BadRequestException();
        }
        User oldUser = userRepository.findUserById(userId);
        if (oldUser == null) {
            throw new NotFoundObjectException();
        }
        if (newUserInfo.getFullName() != null) {
            oldUser.setFullName(newUserInfo.getFullName());
        }
        return oldUser;
    }

    public User remove(String userId) {
        User removeUser = userRepository.findUserById(userId);
        if (removeUser == null) {
            throw new NotFoundObjectException();
        }
        groupServices.removeByUserId(userId);
        userRepository.delete(removeUser);
        return removeUser;
    }

    public List<User> found() {
        List<User> userList = userRepository.findAll();
        if (userList.isEmpty()) {
            throw new NotFoundObjectException();
        }
        return userList;
    }

    public User getInfo(String userId) {
        User returnUser = userRepository.findUserById(userId);
        if (returnUser == null) {
            throw new NotFoundObjectException();
        }
        return returnUser;
    }


    boolean exist(String user_id) {
        return userRepository.findUserById(user_id) != null;
    }
}
