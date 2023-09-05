package ru.javawebinar.basejava.storage;

import exception.ExistStorageException;
import exception.NotExistStorageException;
import exception.StorageException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractArrayStorageTest {

    private Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";

    public AbstractArrayStorageTest(Storage st) {
        storage = st;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }

    @Test
    public void size() throws Exception {
        Assert.assertEquals(3, storage.size());
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test
    public void update() throws Exception {
        Resume r = new Resume("uuid3");
        storage.update(r);
        Assert.assertEquals(r, storage.get("uuid3"));
        Assert.assertThrows(NotExistStorageException.class, () -> storage.update(new Resume("uuid4")));
    }

    @Test
    public void getAll() throws Exception {
        Resume[] arrR = storage.getAll();

        Assert.assertTrue(arrR[0].equals(new Resume("uuid1")));
        Assert.assertTrue(arrR[1].equals(new Resume("uuid2")));
        Assert.assertTrue(arrR[2].equals(new Resume("uuid3")));
    }

    @Test
    public void save() throws Exception {

        storage.clear();
        for (int i = 0; i < 10000; i++) {
            try {
                storage.save(new Resume("uuid" + i));
            } catch (RuntimeException e) {
                Assert.fail("Wrong storage overflow");
            }
        }
        Assert.assertThrows(StorageException.class, () -> storage.save(new Resume("over")));
        Assert.assertThrows(ExistStorageException.class, () -> storage.save(new Resume("uuid1")));
        Assert.assertEquals(10000, storage.size());
    }

    @Test
    public void delete() throws Exception {
        Assert.assertThrows(NotExistStorageException.class, () -> storage.delete("wrong"));
        storage.delete("uuid3");
        Assert.assertEquals(2, storage.size());
        Assert.assertNull(storage.get("uuid3"));
    }

    @Test
    public void get() throws Exception {
        Assert.assertThrows(NotExistStorageException.class, () -> storage.get("wrong"));
        Assert.assertTrue(storage.get("uuid1").equals(new Resume("uuid1")));
    }

}
