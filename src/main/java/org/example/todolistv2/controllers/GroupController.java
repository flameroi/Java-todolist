package org.example.todolistv2.controllers;

import org.example.todolistv2.entity.Group;
import org.example.todolistv2.exceptions.BadRequestException;
import org.example.todolistv2.exceptions.NotFoundOwnerException;
import org.example.todolistv2.exceptions.NotFoundObjectException;
import org.example.todolistv2.services.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

public class GroupController {
    GroupService groupServices;

    @RequestMapping(method = GET, value = "/users/{userId}/groups")
    @ResponseBody
    public List<Group> found(@PathVariable String userId) {
        return groupServices.found(userId);
    }

    @RequestMapping(method = GET, value = "/users/{userId}/groups/{groupId}")
    @ResponseBody
    public Group found(@PathVariable String userId,
                       @PathVariable String groupId) {
        return groupServices.found(userId, groupId);
    }

    @RequestMapping(method = POST, value = "/users/{userId}/groups")
    @ResponseBody
    public Group create(@PathVariable String userId,
                        @RequestBody Group group) {
        return groupServices.create(group, userId);
    }

    @RequestMapping(method = PUT, value = "/users/{userId}/groups/{groupId}")
    @ResponseBody
    public Group update(@PathVariable String userId,
                        @PathVariable String groupId,
                        @RequestBody Group groupUpdater) {
        return groupServices.update(userId, groupId, groupUpdater);
    }

    @RequestMapping(method = DELETE, value = "/users/{userId}/groups/{groupId}")
    @ResponseBody
    public Group remove(@PathVariable String userId,
                        @PathVariable String groupId) {
        return groupServices.remove(groupId);
    }

    @ExceptionHandler
    public ResponseEntity<String> exceptionHandler(Exception exception, WebRequest request) {
        if (exception instanceof NotFoundObjectException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (exception instanceof NotFoundOwnerException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (exception instanceof BadRequestException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
