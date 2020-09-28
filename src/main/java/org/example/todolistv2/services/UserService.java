package org.example.todolistv2.services;

import org.example.todolistv2.entity.User;
import org.example.todolistv2.mongotemplates.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class UserService {
    @Autowired
    UserRepository userRepository;
    GroupService groupServices;

    public ResponseEntity<?> create(User newUser) {
        if (newUser != null) {
            return ResponseEntity.ok(newUser);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<?> update(String userId, User updUser) {
        if (updUser != null) {
            User oldUser = userRepository.findUserById(userId);
            if (oldUser != null) {
                if (updUser.getId() != null && userId != updUser.getId()) {
                    oldUser.setId(updUser.getId());
                }
                if (updUser.getFullName() != null) {
                    oldUser.setFullName(updUser.getFullName());
                }
                return ResponseEntity.ok(oldUser);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<?> remove(String userId) {
        User removeUser = userRepository.findUserById(userId);
        if (removeUser != null) {
            groupServices.removeByUserId(userId);
            userRepository.delete(removeUser);
            return ResponseEntity.ok(removeUser);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<?> found() {
        List<User> userList = userRepository.findAll();
        if (userList != null) {
            return ResponseEntity.ok(userList);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    public ResponseEntity<?> found(String userId) {
        User userList = userRepository.findUserById(userId);
        if (userList != null) {
            return ResponseEntity.ok(userList);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    boolean exist(String user_id) {
        if (userRepository.findUserById(user_id) == null) {
            return false;
        } else {
            return true;
        }
    }
}
