package org.example.todolistv2.mongotemplates;

import org.example.todolistv2.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    User findUserById(String id);
    List<User> findByFullName(String fullName);
}
