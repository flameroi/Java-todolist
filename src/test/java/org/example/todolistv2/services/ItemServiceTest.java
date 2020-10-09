package org.example.todolistv2.services;

import org.example.todolistv2.entity.Item;
import org.example.todolistv2.entity.User;
import org.example.todolistv2.exceptions.BadRequestException;
import org.example.todolistv2.exceptions.NoAccessException;
import org.example.todolistv2.exceptions.NotFoundObjectException;
import org.example.todolistv2.mongotemplates.ItemRepository;
import org.example.todolistv2.mongotemplates.UserRepository;
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
class ItemServiceTest {
    @Autowired
    private ItemService itemService;
    @MockBean
    private ItemRepository itemRepository;
    @MockBean
    private GroupService groupService;

    @DisplayName("ItemCreate")
    @Test
    void create() {
        String userId = "randomUserId";
        String groupId = "randomGroupId";
        Item item = new Item();

        //when(groupService.(anyString())).thenReturn(null);

        assertThrows(BadRequestException.class, () -> itemService.create(userId, groupId, null));
        assertThrows(BadRequestException.class, () -> itemService.create(userId, groupId, item));
        //assertTrue(itemService.create(userId, groupId, item));
        assertThrows(BadRequestException.class, () -> itemService.create(userId, groupId, item));
        item.setGroupId("not" + groupId);
        assertThrows(BadRequestException.class, () -> itemService.create(userId, groupId, item));
        item.setGroupId(groupId);
        assertThrows(BadRequestException.class, () -> itemService.create(userId, groupId, item));
        item.setName("randomName");
        assertTrue(itemService.create(userId, groupId, item));
        verify(itemRepository, times(1)).insert(any(Item.class));
    }

    @DisplayName("ItemRemove")
    @Test
    void remove() {
        String userId = "randomUserId";
        String groupId = "randomGroupId";

        String preparedItemId = "randomUserId";
        Item returnedMockItem = new Item();
        returnedMockItem.setId(preparedItemId);

        //Проверка пустого юзера
        assertThrows(NotFoundObjectException.class, () -> itemService.remove(userId, groupId, null));
        //Проверка несуществующего юзера
        when(itemRepository.findItemById(anyString())).thenReturn(null);
        assertThrows(NotFoundObjectException.class, () -> itemService.remove(userId, groupId, "123"));

        //Проверка на наличие доступа к группе
        when(itemRepository.findItemById(preparedItemId)).thenReturn(returnedMockItem);
        assertThrows(NoAccessException.class, () -> itemService.remove(userId, groupId, preparedItemId));
        //Группа совпала и она предоставляет доступ
        returnedMockItem.setGroupId(groupId);
        assertTrue(itemService.remove(userId, groupId, preparedItemId));

        verify(itemRepository, times(1)).delete(any());
    }

    @DisplayName("ItemsFindByGroup")
    @Test
    void find() {
        String userId = "randomUserId";
        String groupId = "randomGroupId";

        String preparedItemId = "randomUserId";
        Item returnedMockItem = new Item();
        returnedMockItem.setId(preparedItemId);
        List<Item> preparedMockItems = new ArrayList<>();

        when(itemRepository.findItemsByGroupId(anyString())).thenReturn(null);
        when(itemRepository.findItemsByGroupId(groupId)).thenReturn(preparedMockItems);

        assertThrows(NotFoundObjectException.class, () -> itemService.find(userId, "AnotherValue"));
        assertEquals(preparedMockItems, itemService.find(userId, groupId));

    }

    @DisplayName("ItemGetInfo")
    @Test
    void getInfo() {
        String userId = "randomUserId";
        String groupId = "randomGroupId";
        String preparedItemId = "randomUserId";

        Item preparedMockItem = new Item();

        when(itemRepository.findItemById(anyString())).thenReturn(null);
        when(itemRepository.findItemById(preparedItemId)).thenReturn(preparedMockItem);
        assertThrows(NotFoundObjectException.class, () -> itemService.getInfo(userId, groupId, "123"));
        assertThrows(NoAccessException.class, () -> itemService.getInfo(userId, groupId, preparedItemId));

        preparedMockItem.setGroupId(groupId);
        assertEquals(preparedMockItem, itemService.getInfo(userId, groupId, preparedItemId));
    }

    @DisplayName("ItemUpdate")
    @Test
    void update() {
        String userId = "randomUserId";
        String groupId = "randomGroupId";
        String itemId = "randomItemId";

        Item sendingMockItem = new Item();
        Item returnedMockItem = new Item();
        returnedMockItem.setId(itemId);
        returnedMockItem.setGroupId(groupId);

        when(itemRepository.findItemById(anyString())).thenReturn(null);
        when(itemRepository.findItemById(itemId)).thenReturn(returnedMockItem);
        when(groupService.notExist(anyString())).thenReturn(true);


        assertThrows(NotFoundObjectException.class, () -> itemService.update(userId, groupId, "anotherItemId", sendingMockItem));
        assertThrows(BadRequestException.class, () -> itemService.update(userId, groupId, itemId, null));
        assertThrows(BadRequestException.class, () -> itemService.update(userId, groupId, itemId, sendingMockItem));
        sendingMockItem.setId("notEquals");
        assertThrows(BadRequestException.class, () -> itemService.update(userId, groupId, itemId, sendingMockItem));
        sendingMockItem.setId(itemId);
        sendingMockItem.setGroupId("notEquals");
        sendingMockItem.setName("notEquals");
        sendingMockItem.setActivity(false);
        assertTrue(itemService.update(userId, groupId, itemId, sendingMockItem));

        verify(itemRepository, times(1)).save(any());
    }

    @DisplayName("Поверка внутренней функции - removeByGroupId")
    @Test
    void removeByGroupId() {
        String groupId = "randomGroupId";
        Item returnedMockItem = new Item();
        List<Item> items = new ArrayList<>();
        when(itemRepository.findItemsByGroupId(anyString())).thenReturn(null);
        assertThrows(NullPointerException.class, () -> itemService.removeByGroupId(null));
        assertFalse(itemService.removeByGroupId("randomValue"));

        items.add(returnedMockItem);
        when(itemRepository.findItemsByGroupId(groupId)).thenReturn(items);
        assertTrue(itemService.removeByGroupId(groupId));

        verify(itemRepository, times(1)).deleteAll(any());
    }
}