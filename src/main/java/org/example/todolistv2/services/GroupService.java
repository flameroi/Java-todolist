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
    private final GroupRepository groupRepository;
    private final UserService userServices;
    private final ItemService itemServices;

    @Autowired
    public GroupService(GroupRepository groupRepository, UserService userService, ItemService itemService){
        this.groupRepository = groupRepository;
        this.userServices = userService;
        this.itemServices = itemService;
    }

    public boolean create(String userId, Group newGroup) {
        if (userServices.notExist(userId)) {
            throw new NotFoundOwnerException();
        }
        if (newGroup == null || newGroup.getName() == null) {
            throw new BadRequestException();
        }
        if (!userId.equals(newGroup.getUserId())){
            throw new NoAccessException();
        }
        groupRepository.insert(newGroup);
        return true;
    }

    public boolean update(String userId, String groupId, Group updGroup) {
        if (updGroup == null) {
            throw new BadRequestException();
        }
        Group oldGroup = groupRepository.findGroupById(groupId);
        if (oldGroup == null) {
            throw new NotFoundObjectException();
        }
        if (updGroup.getUserId() != null  && !userId.equals(updGroup.getUserId())) {
            if (userServices.notExist(updGroup.getUserId())) {
                throw new BadRequestException();
            }
            oldGroup.setUserId(updGroup.getUserId());
        }
        if (updGroup.getName() != null) {
            oldGroup.setName(updGroup.getName());
        }
        groupRepository.save(oldGroup);
        return true;
    }

    public boolean remove(String userId, String groupId) {
        Group removingGroup = groupRepository.findGroupById(groupId);
        if (removingGroup == null) {
            throw new NotFoundObjectException();
        }
        if (!userId.equals(removingGroup.getUserId())){
            throw new NoAccessException();
        }
        groupRepository.delete(removingGroup);
        itemServices.removeByGroupId(groupId);
        return true;
    }

    public List<Group> find() {
        List<Group> groupList = groupRepository.findAll();
        if(groupList.isEmpty()) {
            throw new NotFoundObjectException();
        }
        return groupList;
    }

    public List<Group> find(String userId) {
        List<Group> groupList = groupRepository.findGroupByUserId(userId);
        if (groupList == null) {
            throw new NotFoundObjectException();
        }
        return groupList;
    }

    public Group getInfo(String userId, String groupId) {
        Group group = groupRepository.findGroupById(groupId);
        if (group == null) {
            throw new NotFoundObjectException();
        }

        if (userServices.notExist(userId) || !userId.equals(group.getUserId())) {
            throw new NoAccessException();
        }
        return group;
    }

    void removeByUserId(String groupId) {
        if (groupId == null) {
            throw new NullPointerException();
        }
        List<Group> removeGroups = groupRepository.findGroupByUserId(groupId);
        if (removeGroups != null) {
            for (Group group : removeGroups) {
                itemServices.removeByGroupId(group.getId());
            }
            groupRepository.deleteAll(removeGroups);
        }
    }

    boolean hasNotAccess(String userId, String groupId){
        if (groupId == null && userId == null) {
            throw new NullPointerException();
        }
        Group group = groupRepository.findGroupById(groupId);
        return group == null || !userId.equals(group.getUserId());
    }

    boolean notExist(String group_id) {
        return groupRepository.findGroupById(group_id) == null;
    }
}

