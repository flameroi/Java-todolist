package org.example.todolistv2.services;

import org.example.todolistv2.entity.User;
import org.example.todolistv2.exceptions.BadRequestException;
import org.example.todolistv2.exceptions.NotFoundObjectException;
import org.example.todolistv2.mongotemplates.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupService groupServices;

    public Object create(User newUser) {
        if (newUser == null || newUser.getId() != null || exist(newUser.getId())) {
            throw new BadRequestException();
        }
        userRepository.insert(newUser);
        return null;
    }

    public void update(String userId, User newUserInfo) {
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
    }

    public void remove(String userId) {
        User removeUser = userRepository.findUserById(userId);
        if (removeUser == null) {
            throw new NotFoundObjectException();
        }
        groupServices.removeByUserId(userId);
        userRepository.delete(removeUser);
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
