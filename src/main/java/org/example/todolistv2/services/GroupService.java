package org.example.todolistv2.services;

import org.example.todolistv2.entity.Group;
import org.example.todolistv2.exceptions.NotFoundOwnerException;
import org.example.todolistv2.exceptions.NotFoundObjectException;
import org.example.todolistv2.exceptions.BadRequestException;
import org.example.todolistv2.mongotemplates.GroupRepository;

import java.util.List;

public class GroupService {
    GroupRepository groupRepository;
    UserService userServices;
    ItemService itemServices;

    public Group create(Group newGroup, String userId) {
        if (!userServices.exist(userId) && newGroup == null) {
            throw new NotFoundOwnerException();
        }
        if (newGroup == null) {
            throw new BadRequestException();
        }
        return newGroup;
    }

    public Group update(String userId, String groupId, Group updGroup) {
        if (updGroup != null) {
            throw new BadRequestException();
        }
        Group oldGroup = groupRepository.findGroupById(groupId);
        if (oldGroup != null) {
            throw new NotFoundObjectException();
        }
        if (updGroup.getUserId() != null && !userId.equals(updGroup.getUserId())) {
            if (userServices.exist(updGroup.getUserId())) {
                throw new BadRequestException();
            }
            oldGroup.setUserId(updGroup.getUserId());
        }
        if (updGroup.getName() != null) {
            oldGroup.setName(updGroup.getName());
        }
        return oldGroup;
    }

    public Group remove(String groupId) {
        Group removingGroup = groupRepository.findGroupById(groupId);
        if (removingGroup == null) {
            throw new NotFoundObjectException();
        }
        groupRepository.delete(removingGroup);
        return removingGroup;
    }

    public List<Group> found() {
        List<Group> groupList = groupRepository.findAll();
        return groupList;
    }

    public List<Group> found(String userId) {
        List<Group> groupList = groupRepository.findGroupByUserId(userId);
        if (groupList == null) {
            throw new NotFoundObjectException();
        }
        return groupList;
    }

    public Group found(String userId, String groupId) {
        Group group = groupRepository.findGroupById(groupId);
        if (!userServices.exist(userId)) {
            throw new NotFoundOwnerException();
        }
        if (group == null) {
            throw new NotFoundObjectException();
        }
        return group;
    }

    void removeByUserId(String groupId) {
        List<Group> removeGroups = groupRepository.findGroupByUserId(groupId);
        if (removeGroups != null) {
            for (Group group : removeGroups) {
                itemServices.removeByGroupId(group.getId());
            }
            groupRepository.deleteAll(removeGroups);
        }
    }

    boolean exist(String group_id) {
        return groupRepository.findGroupById(group_id) == null;
    }
}

