package ru.javawebinar.basejava.storage;

import exception.ExistStorageException;
import exception.NotExistStorageException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorageTest {
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_OVER = "uuid_over";

    protected static final Resume resume1 = new Resume(UUID_1);
    protected static final Resume resume2 = new Resume(UUID_2);
    protected static final Resume resume3 = new Resume(UUID_3);
    protected static final Resume resumeOver = new Resume(UUID_OVER);

    protected final Storage storage;

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
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
    public void delete() throws Exception {
        Assert.assertThrows(NotExistStorageException.class, () -> storage.delete(UUID_OVER));
        storage.delete(UUID_1);
        assertSize(2);
    }

    @Test
    public void get() throws Exception {
        assertGet(resume1);
        assertGet(resume2);
        assertGet(resume3);
    }

    @Test
    public void getNotExist() {
        Assert.assertThrows(NotExistStorageException.class, () -> storage.get(UUID_OVER));
    }

    @Test
    public void updateNotExist() {
        Assert.assertThrows(NotExistStorageException.class, () -> storage.update(resumeOver));
    }

    @Test
    public void saveExist() {
        Assert.assertThrows(ExistStorageException.class, () -> storage.save(resume1));
    }

    @Test
    public void deleteNotExist() {
        Assert.assertThrows(NotExistStorageException.class, () -> storage.delete(UUID_OVER));
    }

    protected void assertSize(int s) {
        Assert.assertEquals(s, storage.size());
    }

    private void assertGet(Resume r) {
        Assert.assertEquals(r, storage.get(r.getUuid()));
    }
}
