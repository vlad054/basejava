package ru.javawebinar.basejava.storage;

public class FileStorageStrategyTest extends AbstractStorageTest {

        public FileStorageStrategyTest() {
            super(new FileStorage(STORAGE_DIR, new StorageFileStrategy()));
        }
}

