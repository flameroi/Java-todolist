package org.example.todolistv2.services;

import org.example.todolistv2.entity.Item;
import org.example.todolistv2.exceptions.BadRequestException;
import org.example.todolistv2.exceptions.NoAccessException;
import org.example.todolistv2.exceptions.NotFoundObjectException;
import org.example.todolistv2.exceptions.NotFoundOwnerException;
import org.example.todolistv2.mongotemplates.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private GroupService groupServices;

    public void create(String userId, String groupId , Item newItem) {
        if (groupServices.exist(groupId)) {
            throw new NotFoundOwnerException();
        }
        if (newItem.getGroupId() == null || newItem.getName() == null) {
            throw new BadRequestException();
        }
        if (newItem.getActivity() == null) {
            newItem.setActivity(true);
        }
        if(groupServices.hasAccess(userId, groupId) || !newItem.getGroupId().equals(groupId)){
            throw  new NoAccessException();
        }
    }

    public void remove(String  userId, String groupId, String itemId) {
        Item removingItem = itemRepository.findItemById(itemId);
        if (removingItem == null) {
            throw new NotFoundObjectException();
        }
        if(groupServices.hasAccess(userId, groupId) || !removingItem.getGroupId().equals(groupId)){
            throw  new NoAccessException();
        }
        itemRepository.delete(removingItem);
    }


    public List<Item> found() {
        List<Item> itemList = itemRepository.findAll();
        if (itemList.isEmpty()) {
            throw new NotFoundObjectException();
        }
        return itemList;
    }


    public List<Item> found(String userId, String groupId) {
        List<Item> itemList = itemRepository.findItemsByGroupId(groupId);
        if (itemList == null) {
            throw new NotFoundObjectException();
        }
        if(groupServices.hasAccess(userId, groupId)){
            throw  new NoAccessException();
        }
        return itemList;
    }

    public Item getInfo(String userId, String groupId, String item_id) {
        if (groupServices.exist(groupId)) {
            throw new NotFoundOwnerException();
        }
        Item itemInfo = itemRepository.findItemById(item_id);
        if (itemInfo == null) {
            throw new NotFoundObjectException();
        }
        if(groupServices.hasAccess(userId, groupId) || !itemInfo.getGroupId().equals(groupId)){
            throw  new NoAccessException();
        }
        return itemInfo;
    }

    public void update(String userId, String groupId, String itemId, Item updItem) {
        if (updItem == null) {
            throw new BadRequestException();
        }
        Item oldItem = itemRepository.findItemById(itemId);
        if (oldItem == null) {
            throw new NotFoundObjectException();
        }
        if(groupServices.hasAccess(userId, groupId)){
            throw  new NoAccessException();
        }
        if (updItem.getGroupId() != null && !groupId.equals(updItem.getGroupId())) {
            if (groupServices.exist(updItem.getGroupId())) {
                oldItem.setGroupId(updItem.getGroupId());
            } else {
                throw new NotFoundOwnerException();
            }
        }
        if (updItem.getName() != null) {
            oldItem.setName(updItem.getName());
        }
        if (updItem.getActivity() != null) {
            oldItem.setActivity(updItem.getActivity());
        }
    }

    public void removeByGroupId(String groupId) {
        List<Item> removeItem = itemRepository.findItemsByGroupId(groupId);
        if (removeItem != null) {
            itemRepository.deleteAll(removeItem);
        }
    }
}