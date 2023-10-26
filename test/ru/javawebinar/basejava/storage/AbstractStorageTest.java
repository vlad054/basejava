package ru.javawebinar.basejava.storage;

import exception.ExistStorageException;
import exception.NotExistStorageException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.model.Resume;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public abstract class AbstractStorageTest {
    private static final String NAME_1 = "name1";
    private static final String NAME_2 = "name2";
    private static final String NAME_3 = "name3";
    private static final String NAME_OVER = "NAME_OVER";

//    protected static final Resume resume1 = new Resume(NAME_1);
//    protected static final Resume resume2 = new Resume(NAME_2);
//    protected static final Resume resume3 = new Resume(NAME_3);
//    protected static final Resume resumeOver = new Resume(NAME_OVER);

    protected static final File STORAGE_DIR = new File("C:\\obuch\\java\\basejava\\out\\tt");
//    protected static final File STORAGE_DIR = Config.get().getStorageDir();
    protected static final Resume resume1 = ResumeTestData.fillResume(UUID.randomUUID().toString(),NAME_1);
    protected static final Resume resume2 = ResumeTestData.fillResume(UUID.randomUUID().toString(),NAME_2);
    protected static final Resume resume3 = ResumeTestData.fillResume(UUID.randomUUID().toString(),NAME_3);
    protected static final Resume resumeOver = ResumeTestData.fillResume(UUID.randomUUID().toString(),NAME_OVER);


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
        Assert.assertArrayEquals(new Resume[0], storage.getAllSorted().toArray());
    }

    @Test
    public void update() throws Exception {
        storage.update(resume3);
        Resume r = storage.get(resume3.getUuid());
        System.out.println(r.toString());
        System.out.println(resume3.toString());
        Assert.assertEquals(resume3, storage.get(resume3.getUuid()));
    }

    @Test
    public void getAllStorage() throws Exception {
        Resume[] exp = {resume1, resume2, resume3};
        List<Resume> expected = Arrays.asList(exp);
        List<Resume> actual = storage.getAllSorted();

        Assert.assertEquals(expected.size(), actual.size());
        Assert.assertTrue(expected.containsAll(actual));
        Assert.assertTrue(actual.containsAll(expected));
    }

    @Test
    public void save() throws Exception {
        storage.save(resumeOver);
        assertSize(4);
        assertGet(resumeOver);
    }

    @Test
    public void delete() throws Exception {
        Assert.assertThrows(NotExistStorageException.class, () -> storage.delete(NAME_OVER));
        storage.delete(resume1.getUuid());
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
        Assert.assertThrows(NotExistStorageException.class, () -> storage.get(NAME_OVER));
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
        Assert.assertThrows(NotExistStorageException.class, () -> storage.delete(NAME_OVER));
    }

    protected void assertSize(int s) {
        Assert.assertEquals(s, storage.size());
    }

    private void assertGet(Resume r) {
        Assert.assertEquals(r, storage.get(r.getUuid()));
    }
}
