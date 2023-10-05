package ru.javawebinar.basejava.storage;

public class PathStorageStrategyTest extends AbstractStorageTest {

        public PathStorageStrategyTest() {
            super(new PathStorageWithStrategy(STORAGE_DIR.getAbsolutePath(), new StreamStorageStrategy()));
        }
}

