package org.example.todolistv2.mongotemplates;

import org.example.todolistv2.entity.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GroupRepository extends MongoRepository<Group, String> {
    Group findGroupById(String groupId);

    List<Group> findGroupByUserId(String userId);
}
