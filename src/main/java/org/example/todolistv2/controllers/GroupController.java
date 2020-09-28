package org.example.todolistv2.controllers;

import org.example.todolistv2.entity.Group;
import org.example.todolistv2.services.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.*;

public class GroupController {
    GroupService groupServices;

    @RequestMapping(method = GET, value = "/users/{userId}/groups")
    @ResponseBody
    public ResponseEntity<?> found(@PathVariable(value = "userId") String userId) {
        return groupServices.found(userId);
    }

    @RequestMapping(method = GET, value = "/users/{userId}/groups/{groupId}")
    @ResponseBody
    public ResponseEntity<?> found(@PathVariable(value = "userId") String userId,
                                   @PathVariable(value = "groupId") String groupId) {
        return groupServices.found(userId, groupId);
    }

    @RequestMapping(method = POST, value = "/users/{userId}/groups")
    @ResponseBody
    public ResponseEntity<?> create(@PathVariable(value = "userId") String userId,
                                    @RequestBody Group group) {
        return groupServices.create(group, userId);
    }

    @RequestMapping(method = PUT, value = "/users/{userId}/groups/{groupId}")
    @ResponseBody
    public ResponseEntity<?> update(@PathVariable(value = "userId") String userId,
                                    @PathVariable(value = "groupId") String groupId,
                                    @RequestBody Group groupUpdater) {
        return groupServices.update(userId, groupId, groupUpdater);
    }

    @RequestMapping(method = DELETE, value = "/users/{userId}/groups/{groupId}")
    @ResponseBody
    public ResponseEntity<?> remove(@PathVariable(value = "userId") String userId,
                                    @PathVariable(value = "groupId") String groupId) {
        return groupServices.remove(groupId);
    }


}
