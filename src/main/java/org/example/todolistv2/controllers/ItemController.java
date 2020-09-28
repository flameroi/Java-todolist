package org.example.todolistv2.controllers;

import org.example.todolistv2.entity.Item;
import org.example.todolistv2.services.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.*;

public class ItemController {
    ItemService itemServices;

    @RequestMapping(method = GET, value = "/users/{userId}/groups/{groupId}/items")
    @ResponseBody
    public ResponseEntity<?> found(@PathVariable(value = "groupId") String groupId) {
        return itemServices.found(groupId);
    }

    @RequestMapping(method = GET, value = "/users/{userId}/groups/{groupId}/items/{itemId}}")
    @ResponseBody
    public ResponseEntity<?> found(@PathVariable(value = "groupId") String groupId,
                                   @PathVariable(value = "itemId") String itemId) {
        return itemServices.found(groupId, itemId);
    }

    @RequestMapping(method = POST, value = "/users/{userId}/groups/{groupId}/items")
    @ResponseBody
    public ResponseEntity<?> addItem(@PathVariable(value = "userId") String userId,
                                     @PathVariable(value = "groupId") String group_id,
                                     @RequestBody Item item) {
        return itemServices.create(item, group_id);
    }

    @RequestMapping(method = PUT, value = "/users/{userId}/groups/{groupId}/items/{itemId}")
    @ResponseBody
    public ResponseEntity<?> update(@PathVariable(value = "userId") String userId,
                                    @PathVariable(value = "groupId") String groupId,
                                    @PathVariable(value = "itemId") String itemId,
                                    @RequestBody Item itemUpd) {
        return itemServices.update(itemId, groupId, itemUpd);
    }

    @RequestMapping(method = DELETE, value = "/users/{userId}/groups/{groupId}/items/{itemId}")
    @ResponseBody
    public ResponseEntity<?> remove(@PathVariable(value = "itemId") String itemId) {
        return itemServices.remove(itemId);
    }
}
