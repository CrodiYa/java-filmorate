package ru.yandex.practicum.filmorate.storage;

import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.StorageData;

@NoArgsConstructor
public class TestEntity extends StorageData {

    public TestEntity(Long id) {
        super(id);
    }
}