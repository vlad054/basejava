package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.strategy.StreamStorageStrategy;

public class PathStorageStrategyTest extends AbstractStorageTest {

        public PathStorageStrategyTest() {
            super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new StreamStorageStrategy()));
        }
}

