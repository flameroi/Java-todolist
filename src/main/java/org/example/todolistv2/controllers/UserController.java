package org.example.todolistv2.controllers;

import org.example.todolistv2.entity.User;
import org.example.todolistv2.exceptions.NotFoundObjectException;
import org.example.todolistv2.exceptions.NotFoundOwnerException;
import org.example.todolistv2.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

public class UserController {
    UserService userServices;

    @RequestMapping(method = GET, value = "/users")
    @ResponseBody
    public List<User> userList() {
        return userServices.found();
    }

    @RequestMapping(method = GET, value = "/users/{userId}")
    @ResponseBody
    public User userInfo(@PathVariable String userId) {
        return userServices.getInfo(userId);
    }


    @RequestMapping(method = POST, value = "/users")
    @ResponseBody
    public User userCreator(@RequestBody User user) {
        return userServices.create(user);
    }

    @RequestMapping(method = PUT, value = "/users/{userId}")
    @ResponseBody
    public User userUpdate(@RequestBody User uploadUser,
                           @PathVariable String userId) {
        return userServices.update(userId, uploadUser);
    }

    @RequestMapping(method = DELETE, value = "/users/{userId}")
    @ResponseBody
    public User userDelete(@PathVariable String userId) {
        return userServices.remove(userId);
    }

    @ExceptionHandler
    public ResponseEntity<String> exceptionHandler(Exception exception, WebRequest request) {
        if (exception instanceof NotFoundObjectException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (exception instanceof NotFoundOwnerException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
