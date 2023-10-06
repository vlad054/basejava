package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.strategy.StreamStorageStrategy;

public class FileStorageStrategyTest extends AbstractStorageTest {

        public FileStorageStrategyTest() {
            super(new FileStorage(STORAGE_DIR, new StreamStorageStrategy()));
        }
}

