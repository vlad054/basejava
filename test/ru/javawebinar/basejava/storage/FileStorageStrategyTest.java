package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.strategy.ObjectStreamSerializer;

public class FileStorageStrategyTest extends AbstractStorageTest {

        public FileStorageStrategyTest() {
            super(new FileStorage(STORAGE_DIR, new ObjectStreamSerializer()));
        }
}

