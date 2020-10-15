package org.example.todolistv2.controllers;

import org.example.todolistv2.entity.Item;
import org.example.todolistv2.exceptions.BadRequestException;
import org.example.todolistv2.exceptions.NotFoundObjectException;
import org.example.todolistv2.exceptions.NotFoundOwnerException;
import org.example.todolistv2.services.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class ItemController {
    ItemService itemServices;

    @RequestMapping(method = GET, value = "/users/{userId}/groups/{groupId}/items")
    @ResponseBody
    public List<Item> find(@PathVariable String userId,
                           @PathVariable String groupId) {
        return itemServices.find(userId, groupId);
    }

    @RequestMapping(method = GET, value = "/users/{userId}/groups/{groupId}/items/{itemId}}")
    @ResponseBody
    public Item find(@PathVariable String userId,
                     @PathVariable String groupId,
                     @PathVariable String itemId) {
        return itemServices.getInfo(userId, groupId, itemId);
    }

    @RequestMapping(method = POST, value = "/users/{userId}/groups/{groupId}/items")
    @ResponseBody
    public ResponseEntity<?> addItem(@PathVariable String userId,
                                     @PathVariable String groupId,
                                     @RequestBody Item item) {
        itemServices.create(userId, groupId, item);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = PUT, value = "/users/{userId}/groups/{groupId}/items/{itemId}")
    @ResponseBody
    public ResponseEntity<?> update(@PathVariable String userId,
                                    @PathVariable String groupId,
                                    @PathVariable String itemId,
                                    @RequestBody Item itemUpd) {
        itemServices.update(userId, groupId, itemId, itemUpd);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = DELETE, value = "/users/{userId}/groups/{groupId}/items/{itemId}")
    @ResponseBody
    public ResponseEntity<?> remove(@PathVariable String userId,
                                    @PathVariable String groupId,
                                    @PathVariable String itemId) {
        itemServices.remove(userId, groupId, itemId);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler
//    public ResponseEntity<String> exceptionHandler(Exception exception, WebRequest request) {
    public ResponseEntity<String> exceptionHandler(Exception exception) {
        if (exception instanceof NotFoundOwnerException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (exception instanceof NotFoundObjectException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (exception instanceof BadRequestException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
