package org.example.todolistv2.services;

import org.example.todolistv2.entity.Group;
import org.example.todolistv2.exceptions.BadRequestException;
import org.example.todolistv2.exceptions.NoAccessException;
import org.example.todolistv2.exceptions.NotFoundObjectException;
import org.example.todolistv2.mongotemplates.GroupRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class GroupServiceTest {
    @Autowired
    private GroupService groupService;
    @MockBean
    private GroupRepository groupRepository;
    @MockBean
    private ItemService itemService;
    @MockBean
    private UserService userService;

    @BeforeAll
    @Test
    static void init(){
    }

    @DisplayName("GroupCreating")
    @Test
    void create() {
        String userId = "randomUserId";
        Group group = new Group();
        assertThrows(BadRequestException.class, () -> groupService.create(userId, null));
        assertThrows(BadRequestException.class, () -> groupService.create(userId, group));
        group.setName("randomName");
        assertThrows(NoAccessException.class, () -> groupService.create(userId, group));
        group.setUserId(userId);
        assertTrue(groupService.create(userId, group));
        verify(groupRepository, times(1)).insert(any(Group.class));
    }

    @DisplayName("GroupUpdating")
    @Test
    void update() {
        String userId = "randomUserId";
        String groupId = "randomGroupId";
        Group gettingGroup = new Group();
        gettingGroup.setId(groupId);
        gettingGroup.setUserId(userId);
        Group sendingGroup = new Group();

        when(groupRepository.findGroupById(any())).thenReturn(null);
        when(userService.notExist(anyString())).thenReturn(false);
        when(groupRepository.findGroupById(groupId)).thenReturn(gettingGroup);

        assertThrows(BadRequestException.class, ()-> groupService.update(userId, groupId, null));
        assertThrows(NotFoundObjectException.class, ()-> groupService.update(userId, "notEqualsGroupId", sendingGroup));
        sendingGroup.setUserId("notEqualsUserId");
        assertThrows(BadRequestException.class, ()-> groupService.update(userId, groupId, null));
        sendingGroup.setUserId(userId);
        assertTrue(groupService.update(userId, groupId, sendingGroup));
        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @DisplayName("GroupRemoving")
    @Test
    void remove() {
        String userId = "randomUserId";
        String groupId = "randomGroupId";
        Group gettingGroup = new Group();

        when(groupRepository.findGroupById(any())).thenReturn(null);
        when(groupRepository.findGroupById(groupId)).thenReturn(gettingGroup);
        when(itemService.removeByGroupId(anyString())).thenReturn(true);

        assertThrows(NotFoundObjectException.class, ()-> groupService.remove(userId, "notEqualGroupId"));
        assertThrows(NoAccessException.class, ()-> groupService.remove(userId, groupId));
        gettingGroup.setUserId(userId);

        assertTrue(groupService.remove(userId, groupId));
        verify(groupRepository, times(1)).delete(any(Group.class));
    }

    @DisplayName("GroupFindList")
    @Test
    void found() {
        String userId = "randomUserId";
        List<Group> groupList= new ArrayList<>();
        groupList.add(new Group());

        when(groupRepository.findGroupByUserId(any())).thenReturn(null);
        when(groupRepository.findGroupByUserId(userId)).thenReturn(groupList);

        assertThrows(NotFoundObjectException.class, ()-> groupService.find("notEqualGroupId"));
        assertEquals(groupList, groupService.find(userId));
    }

    @DisplayName("GroupGetInfo")
    @Test
    void getInfo() {
        String userId = "randomUserId";
        String groupId = "randomGroupId";
        Group gettingGroup = new Group();

        when(groupRepository.findGroupById(any())).thenReturn(null);
        when(groupRepository.findGroupById(groupId)).thenReturn(gettingGroup);

        assertThrows(NotFoundObjectException.class, ()-> groupService.getInfo(userId, "notEqualGroupId"));
        assertThrows(NoAccessException.class, ()-> groupService.getInfo(userId, groupId));

        gettingGroup.setUserId(userId);
        assertEquals(gettingGroup, groupService.getInfo(userId, groupId));
    }

    //Внутренний функционал
    @DisplayName("Поверка внутренней функции - hasAccess")
    @Test
    void hasAccess() {
        String userId = "randomUserId";
        String groupId = "randomGroupId";
        Group gettingGroup = new Group();

        when(groupRepository.findGroupById(any())).thenReturn(null);
        when(groupRepository.findGroupById(groupId)).thenReturn(gettingGroup);

        assertTrue(groupService.hasNotAccess(userId, "notEqualGroupId"));
        assertTrue(groupService.hasNotAccess(userId, groupId));

        gettingGroup.setUserId(userId);
        assertFalse(groupService.hasNotAccess(userId, groupId));
    }

}