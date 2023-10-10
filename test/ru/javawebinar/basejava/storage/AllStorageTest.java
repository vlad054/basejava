package ru.javawebinar.basejava.storage;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ArrayStorageTest.class,
        SortedArrayStorageTest.class,
        ListSectionStorageTest.class,
        MapUuidStorageTest.class,
        MapResumeStorageTest.class,
        FileStorageStrategyTest.class,
        PathStorageStrategyTest.class,
        XmlPathStorageTest.class,
})
public class AllStorageTest {

}
