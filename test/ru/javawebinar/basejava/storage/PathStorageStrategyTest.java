package ru.javawebinar.basejava.storage;

public class PathStorageStrategyTest extends AbstractStorageTest {

        public PathStorageStrategyTest() {
            super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new StoragePathStrategy()));
        }
}

