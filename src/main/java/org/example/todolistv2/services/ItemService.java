package org.example.todolistv2.services;

import org.example.todolistv2.entity.Item;
import org.example.todolistv2.exceptions.BadRequestException;
import org.example.todolistv2.exceptions.NotFoundObjectException;
import org.example.todolistv2.exceptions.NotFoundOwnerException;
import org.example.todolistv2.mongotemplates.ItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ItemService {
    ItemRepository itemRepository;
    GroupService groupServices;

    public Item create(Item newItem, String groupId) {
        if (groupServices.exist(groupId)) {
            throw new NotFoundOwnerException();
        }
        if (newItem != null) {
            throw new BadRequestException();
        }
        return newItem;
    }

    public Item remove(String itemId) {
        Item removingItem = itemRepository.findItemById(itemId);
        if (removingItem == null) {
            throw new NotFoundObjectException();
        }
        itemRepository.delete(removingItem);
        return removingItem;
    }


    public List<Item> found() {
        List<Item> itemList = itemRepository.findAll();
        if (itemList.isEmpty()) {
            throw new NotFoundObjectException();
        }
        return itemList;
    }


    public List<Item> found(String group_id) {
        List<Item> itemList = itemRepository.findItemsByGroupId(group_id);
        if (itemList == null) {
            throw new NotFoundObjectException();
        }
        return itemList;
    }

    public Item getInfo(String group_id, String item_id) {
        Item itemInfo = itemRepository.findItemById(item_id);
        if (groupServices.exist(group_id)) {
            throw new NotFoundOwnerException();
        }
        if (itemInfo == null) {
            throw new NotFoundObjectException();
        }
        return itemInfo;
    }

    public Item update(String itemId, String groupId, Item updItem) {
        if (updItem == null) {
            throw new BadRequestException();
        }
        Item oldItem = itemRepository.findItemById(itemId);
        if (oldItem == null) {
            throw new NotFoundObjectException();
        }
        if (updItem.getGroupId() != null && groupId != updItem.getGroupId()) {
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
        return oldItem;
    }


    public void removeByGroupId(String groupId) {
        List<Item> removeItem = itemRepository.findItemsByGroupId(groupId);
        if (removeItem != null) {
            itemRepository.deleteAll(removeItem);
        }
    }
}