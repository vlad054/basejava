package ru.javawebinar.basejava.storage;

public class FileStorageStrategyTest extends AbstractStorageTest {

        public FileStorageStrategyTest() {
            super(new FileStorageWithStrategy(STORAGE_DIR, new StreamStorageStrategy()));
        }
}

