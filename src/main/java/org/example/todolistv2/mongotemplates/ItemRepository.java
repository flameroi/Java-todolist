package org.example.todolistv2.mongotemplates;

import org.example.todolistv2.entity.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ItemRepository extends MongoRepository<Item, String> {
    Item findItemById(String itemId);

    List<Item> findItemsByGroupId(String groupId);

}
