package org.example.todolistv2.services;

import org.example.todolistv2.entity.Group;
import org.example.todolistv2.entity.Item;
import org.example.todolistv2.entity.User;
import org.example.todolistv2.exceptions.BadRequestException;
import org.example.todolistv2.exceptions.NotFoundObjectException;
import org.example.todolistv2.mongotemplates.GroupRepository;
import org.example.todolistv2.mongotemplates.ItemRepository;
import org.example.todolistv2.mongotemplates.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public UserService(UserRepository userRepository, GroupRepository groupRepository, ItemRepository itemRepository){
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    public boolean create(User newUser) {
        if (newUser == null ||
                (newUser.getId() != null && notExist(newUser.getId())) ||
                newUser.getFullName() == null) {
            throw new BadRequestException();
        }
        userRepository.insert(newUser);

        return true;
    }

    public boolean update(String userId, User newUserInfo) {
        if (newUserInfo == null || newUserInfo.getFullName() == null) {
            throw new BadRequestException();
        }
        User oldUser = userRepository.findUserById(userId);
        if (oldUser == null) {
            throw new NotFoundObjectException();
        }
        oldUser.setFullName(newUserInfo.getFullName());
        userRepository.save(oldUser);
        return true;
    }

    public boolean remove(String userId) {
        User removeUser = userRepository.findUserById(userId);
        if (removeUser == null) {
            throw new NotFoundObjectException();
        }

        List<Group> removeGroups = groupRepository.findGroupByUserId(userId);
        if (removeGroups != null) {
            for (Group group : removeGroups) {
                List<Item> removeItem = itemRepository.findItemsByGroupId(group.getId());
                if (removeItem != null) {
                    itemRepository.deleteAll(removeItem);
                    return true;
                }
            }
            groupRepository.deleteAll(removeGroups);
        }

        userRepository.delete(removeUser);
        return true;
    }

    public List<User> find() {
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

    boolean notExist(String user_id) {
        return userRepository.findUserById(user_id) == null;
    }
}
