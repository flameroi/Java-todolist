package org.example.todolistv2.controllers;

import org.example.todolistv2.entity.Item;
import org.example.todolistv2.exceptions.BadRequestException;
import org.example.todolistv2.exceptions.NotFoundObjectException;
import org.example.todolistv2.exceptions.NotFoundOwnerException;
import org.example.todolistv2.services.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

public class ItemController {
    ItemService itemServices;

    @RequestMapping(method = GET, value = "/users/{userId}/groups/{groupId}/items")
    @ResponseBody
    public List<Item> found(@PathVariable String userId,
                            @PathVariable String groupId) {
        return itemServices.found(groupId);
    }

    @RequestMapping(method = GET, value = "/users/{userId}/groups/{groupId}/items/{itemId}}")
    @ResponseBody
    public Item found(@PathVariable String userId,
                      @PathVariable String groupId,
                      @PathVariable String itemId) {
        return itemServices.getInfo(groupId, itemId);
    }

    @RequestMapping(method = POST, value = "/users/{userId}/groups/{groupId}/items")
    @ResponseBody
    public Item addItem(@PathVariable String userId,
                        @PathVariable String groupId,
                        @RequestBody Item item) {
        return itemServices.create(item, groupId);
    }

    @RequestMapping(method = PUT, value = "/users/{userId}/groups/{groupId}/items/{itemId}")
    @ResponseBody
    public Item update(@PathVariable String userId,
                       @PathVariable String groupId,
                       @PathVariable String itemId,
                       @RequestBody Item itemUpd) {
        return itemServices.update(itemId, groupId, itemUpd);
    }

    @RequestMapping(method = DELETE, value = "/users/{userId}/groups/{groupId}/items/{itemId}")
    @ResponseBody
    public Item remove(@PathVariable String userId,
                       @PathVariable String groupId,
                       @PathVariable String itemId) {
        return itemServices.remove(itemId);
    }

    @ExceptionHandler
    public ResponseEntity<String> exceptionHandler(Exception exception, WebRequest request) {
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
