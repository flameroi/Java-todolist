package org.example.todolistv2.entity;

import org.springframework.data.annotation.Id;

public class Item {
    @Id
    private String id;
    private String name;
    private String groupId;
    private Boolean activity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getActivity() {
        return activity;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setActivity(Boolean activity) {
        this.activity = activity;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Group: " + groupId + " id: " + id + " name: " + name);
    }
}
