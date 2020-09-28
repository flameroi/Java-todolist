package org.example.todolistv2.entity;

import org.springframework.data.annotation.Id;

public class Group {
    @Id
    private String id;
    private String name;
    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }
}
