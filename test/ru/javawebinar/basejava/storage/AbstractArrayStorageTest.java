package ru.javawebinar.basejava.storage;

import exception.NotExistStorageException;
import exception.StorageException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractArrayStorageTest {


    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_OVER = "uuid_over";

    private static final Resume resume1 = new Resume(UUID_1);
    private static final Resume resume2 = new Resume(UUID_2);
    private static final Resume resume3 = new Resume(UUID_3);
    private static final Resume resumeOver = new Resume(UUID_OVER);

    private final Storage storage;

    public AbstractArrayStorageTest(Storage st) {
        storage = st;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(resume1);
        storage.save(resume2);
        storage.save(resume3);
    }

    @Test
    public void size() throws Exception {
        assertSize(3);
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        assertSize(0);
        Assert.assertArrayEquals(new Resume[0], storage.getAll());
    }

    @Test
    public void update() throws Exception {
        storage.update(resume3);
        Assert.assertEquals(resume3, storage.get(UUID_3));
        updateNotExist(resumeOver);
    }

    @Test
    public void getAll() throws Exception {
        Resume[] expected = {resume1, resume2, resume3};
        Assert.assertArrayEquals(expected, storage.getAll());
    }

    @Test
    public void save() throws Exception {
        storage.save(resumeOver);
        assertSize(4);
        assertGet(resumeOver);
    }

    @Test
    public void saveOverflow() throws Exception {
        storage.clear();
        for (int i = 0; i < 10000; i++) {
            try {
                storage.save(new Resume("uuid" + i));
            } catch (RuntimeException e) {
                Assert.fail("Wrong storage overflow");
            }
        }
        Assert.assertThrows(StorageException.class, () -> storage.save(resumeOver));
        assertSize(10000);
    }

    @Test
    public void delete() throws Exception {
        Assert.assertThrows(NotExistStorageException.class, () -> storage.delete(UUID_OVER));
        storage.delete(UUID_1);
        assertSize(2);
        getNotExist(resume1);
    }

    @Test
    public void get() throws Exception {
        assertGet(resume1);
        assertGet(resume2);
        assertGet(resume3);
        getNotExist(resumeOver);
    }

    private void assertSize(int s) {
        Assert.assertEquals(s, storage.size());
    }

    private void assertGet(Resume r) {
        Assert.assertEquals(r, storage.get(r.getUuid()));
    }

    private void getNotExist(Resume r) {
        Assert.assertThrows(NotExistStorageException.class, () -> storage.get(r.getUuid()));
    }

    private void updateNotExist(Resume r) {
        Assert.assertThrows(NotExistStorageException.class, () -> storage.update(r));
    }

}
