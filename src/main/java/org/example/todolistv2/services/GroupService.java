package org.example.todolistv2.services;

import org.example.todolistv2.entity.Group;
import org.example.todolistv2.exceptions.NoAccessException;
import org.example.todolistv2.exceptions.NotFoundOwnerException;
import org.example.todolistv2.exceptions.NotFoundObjectException;
import org.example.todolistv2.exceptions.BadRequestException;
import org.example.todolistv2.mongotemplates.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserService userServices;
    @Autowired
    private ItemService itemServices;

    public void create(Group newGroup, String userId) {
        if (!userServices.exist(userId) && newGroup == null) {
            throw new NotFoundOwnerException();
        }
        if (newGroup == null) {
            throw new BadRequestException();
        }
        groupRepository.insert(newGroup);
    }

    public void update(String userId, String groupId, Group updGroup) {
        if (updGroup == null) {
            throw new BadRequestException();
        }
        Group oldGroup = groupRepository.findGroupById(groupId);
        if (oldGroup == null) {
            throw new NotFoundObjectException();
        }
        if (updGroup.getUserId() != null  && !userId.equals(updGroup.getUserId())) {
            if (userServices.exist(updGroup.getUserId())) {
                throw new BadRequestException();
            }
            oldGroup.setUserId(updGroup.getUserId());
        }
        if (updGroup.getName() != null) {
            oldGroup.setName(updGroup.getName());
        }
        groupRepository.save(oldGroup);
    }

    public void remove(String userId, String groupId) {
        Group removingGroup = groupRepository.findGroupById(groupId);
        if (removingGroup == null) {
            throw new NotFoundObjectException();
        }
        if (!removingGroup.getUserId().equals(userId)){
            throw new NoAccessException();
        }
        groupRepository.delete(removingGroup);
    }

    public List<Group> found() {
        List<Group> groupList = groupRepository.findAll();
        if(groupList.isEmpty()) {
            throw new NotFoundObjectException();
        }
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
        if (group == null) {
            throw new NotFoundObjectException();
        }
        if (!userServices.exist(userId) || !group.getId().equals(groupId)) {
            throw new NoAccessException();
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

    boolean hasAccess(String userId, String groupId){
        Group group = groupRepository.findGroupById(groupId);
        return group == null || !group.getUserId().equals(userId);
    }

    boolean notExist(String group_id) {
        return groupRepository.findGroupById(group_id) == null;
    }
}

