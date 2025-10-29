package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class AbstractStorageTest {

    private TestStorage storage;
    private TestEntity entity;

    @BeforeEach
    public void setUp() {
        storage = new TestStorage();
        entity = new TestEntity();
    }

    @Test
    public void shouldAddObject() {
        TestEntity entity1 = storage.add(entity);

        assertEquals(1L, entity1.getId());
        assertEquals(entity, entity1);
    }

    @Test
    public void shouldUpdateObject() {
        storage.add(entity);

        TestEntity entity1 = storage.update(new TestEntity(1L));

        assertEquals(1L, entity1.getId());
        assertEquals(entity, entity1);
    }

    @Test
    public void shouldThrowNotFoundWhenUpdateObjectThatDoesNotExist() {
        assertThrows(NotFoundException.class, () -> storage.update(new TestEntity(1L)));
    }

    @Test
    public void shouldRemoveObject() {
        storage.add(entity);

        TestEntity entity1 = storage.remove(1L);

        assertEquals(1L, entity1.getId());
        assertEquals(entity, entity1);
        assertEquals(0, storage.getAll().size());
    }

    @Test
    public void shouldThrowNotFoundWhenRemoveObjectThatDoesNotExist() {
        assertThrows(NotFoundException.class, () -> storage.remove(1L));
    }

    @Test
    public void shouldRemoveAll() {
        storage.add(entity);
        storage.clear();

        assertEquals(0, storage.getAll().size());
    }

    @Test
    public void shouldGetObject() {
        storage.add(entity);
        TestEntity entity1 = storage.get(1L);

        assertEquals(1L, entity1.getId());
        assertEquals(entity, entity1);
    }

    @Test
    public void shouldThrowNotFoundWhenGetObjectThatDoesNotExist() {
        assertThrows(NotFoundException.class, () -> storage.get(1L));
    }

    @Test
    public void shouldReturnAll() {
        TestEntity t1 = new TestEntity();
        TestEntity t2 = new TestEntity();

        storage.add(t1);
        storage.add(t2);

        storage.clear();

        assertEquals(0, storage.getAll().size());
    }

    @Test
    public void shouldReturnTrueIfContains() {
        storage.add(entity);
        assertTrue(storage.contains(1L));
    }

    @Test
    public void shouldReturnFalseIfNotContains() {
        assertFalse(storage.contains(1L));
    }

    @Test
    public void shouldReturnSize() {
        storage.add(entity);
        assertEquals(storage.getAll().size(), storage.size());
    }

    @Test
    public void shouldThrowNotFoundIfNotContains() {
        assertThrows(NotFoundException.class, () -> storage.throwIfNotFound(1L));
    }

    @Test
    public void shouldNotThrowNotFoundIfContains() {
        storage.add(entity);
        assertDoesNotThrow(() -> storage.throwIfNotFound(1L));
    }

}
