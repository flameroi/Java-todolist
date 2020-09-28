package org.example.todolistv2.controllers;

import org.example.todolistv2.entity.User;
import org.example.todolistv2.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.*;

public class UserController {
    UserService userServices;

    @RequestMapping(method = GET, value = "/users")
    @ResponseBody
    public ResponseEntity<?> userList() {
        return userServices.found();
    }

    @RequestMapping(method = GET, value = "/users/{userId}")
    @ResponseBody
    public ResponseEntity<?> userList(@PathVariable(value = "userId") String userId) {
        return userServices.found(userId);
    }


    @RequestMapping(method = POST, value = "/users")
    @ResponseBody
    public ResponseEntity<?> userGetter(@RequestBody User user) {
        return userServices.create(user);
    }

    @RequestMapping(method = PUT, value = "/users/{userId}")
    @ResponseBody
    public ResponseEntity<?> userUpdate(@RequestBody User uploadUser,
                                        @PathVariable(value = "userId") String userId) {
        return userServices.update(userId, uploadUser);
    }

    @RequestMapping(method = DELETE, value = "/users/{userId}")
    @ResponseBody
    public ResponseEntity<?> userDelete(@PathVariable(value = "userId") String userId) {
        return userServices.remove(userId);
    }

}
