package ru.javawebinar.basejava.storage;

import exception.StorageException;
import org.junit.Assert;
import org.junit.Test;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {

    public AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test
    public void saveOverflow() throws Exception {
        storage.clear();
        for (int i = 0; i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
            try {
                storage.save(new Resume("uuid" + i));
            } catch (RuntimeException e) {
                Assert.fail("Wrong storage overflow");
            }
        }
        Assert.assertThrows(StorageException.class, () -> storage.save(resumeOver));
        assertSize(10000);
    }

}
