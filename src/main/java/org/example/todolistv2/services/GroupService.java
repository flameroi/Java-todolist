package org.example.todolistv2.services;

import org.example.todolistv2.entity.Group;
import org.example.todolistv2.mongotemplates.GroupRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class GroupService {
    GroupRepository groupRepository;
    UserService userServices;
    ItemService itemServices;

    public ResponseEntity<?> create(Group newGroup, String userId) {
        if (userServices.exist(userId) && newGroup != null) {
            return ResponseEntity.ok(newGroup);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<?> update(String userId, String groupId, Group updGroup) {
        if (updGroup != null) {
            Group oldGroup = groupRepository.findGroupById(groupId);
            if (oldGroup != null) {
                if (updGroup.getUserId() != null && userId != updGroup.getUserId()) {
                    if (userServices.exist(updGroup.getUserId())) {
                        oldGroup.setUserId(updGroup.getUserId());
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                    }
                }
                if (updGroup.getName() != null) {
                    oldGroup.setName(updGroup.getName());
                }
                return ResponseEntity.ok(oldGroup);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<?> remove(String groupId) {
        Group dropedGroup = groupRepository.findGroupById(groupId);
        if (dropedGroup != null) {
            groupRepository.delete(dropedGroup);
            return ResponseEntity.ok(dropedGroup);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<?> found() {
        List<Group> groupList = groupRepository.findAll();
        return ResponseEntity.ok(groupList);
    }

    public ResponseEntity<?> found(String userId) {
        List<Group> groupList = groupRepository.findGroupByUserId(userId);
        if (groupList != null) {
            return ResponseEntity.ok(groupList);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<?> found(String userId, String groupId) {
        Group group = groupRepository.findGroupById(groupId);
        if (userServices.exist(userId)) {
            if (group != null) {
                return ResponseEntity.ok(group);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    void removeByUserId(String groupId) {
        List<Group> dropedGroups = groupRepository.findGroupByUserId(groupId);
        if (dropedGroups != null) {
            for (Group group : dropedGroups) {
                itemServices.removeByGroupId(group.getId());
            }
            groupRepository.deleteAll(dropedGroups);
        }
    }

    boolean exist(String group_id) {
        if (groupRepository.findGroupById(group_id) == null) {
            return false;
        } else {
            return true;
        }
    }
}
