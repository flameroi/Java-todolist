package org.example.todolistv2.entity;

import org.springframework.data.annotation.Id;

public class User {
    @Id
    private String id;
    private String fullName;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public String toString() {
        return String.format("Имя:" + fullName + " id:" + id);
    }
}
