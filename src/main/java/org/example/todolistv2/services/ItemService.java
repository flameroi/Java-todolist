package org.example.todolistv2.services;

import org.example.todolistv2.entity.Item;
import org.example.todolistv2.mongotemplates.ItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ItemService {
    ItemRepository itemRepository;
    GroupService groupServices;

    public ResponseEntity<?> create(Item newItem, String groupId) {
        if (groupServices.exist(groupId) && newItem != null) {
            return ResponseEntity.ok(newItem);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<?> remove(String itemId) {
        Item dropedItem = itemRepository.findItemById(itemId);
        if (dropedItem != null) {
            itemRepository.delete(dropedItem);
            return ResponseEntity.ok(dropedItem);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    public ResponseEntity<?> found() {
        List<Item> itemList = itemRepository.findAll();
        if (itemList != null) {
            return ResponseEntity.ok(itemList);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    public ResponseEntity<?> found(String group_id) {
        List<Item> itemList = itemRepository.findItemsByGroupId(group_id);
        if (itemList != null) {
            return ResponseEntity.ok(itemList);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<?> found(String group_id, String item_id) {
        Item item = itemRepository.findItemById(item_id);
        if (groupServices.exist(group_id)) {
            if (item != null) {
                return ResponseEntity.ok(item);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<?> update(String itemId, String groupId, Item updItem) {
        if (updItem != null) {
            Item oldItem = itemRepository.findItemById(itemId);
            if (oldItem != null) {
                if (updItem.getGroupId() != null && groupId != updItem.getGroupId()) {
                    if (groupServices.exist(updItem.getGroupId())) {
                        oldItem.setGroupId(updItem.getGroupId());
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                    }
                }
                if (updItem.getName() != null) {
                    oldItem.setName(updItem.getName());
                }
                if (updItem.getActivity() != null) {
                    oldItem.setActivity(updItem.getActivity());
                }
                return ResponseEntity.ok(oldItem);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    public void removeByGroupId(String groupId) {
        List<Item> removeItem = itemRepository.findItemsByGroupId(groupId);
        if (removeItem != null) {
            itemRepository.deleteAll(removeItem);
        }
    }
}