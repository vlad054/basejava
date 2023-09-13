package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;

public class MapStorageTest extends AbstractStorageTest {
    public MapStorageTest() {
        super(new MapStorage());
    }

    @Override
    public void getAll() throws Exception {
        Resume[] exp = {resume1, resume2, resume3};
        List<Resume> expected = Arrays.asList(exp);
        List<Resume> actual = Arrays.asList(storage.getAll());

        Assert.assertEquals(expected.size(), actual.size());
        Assert.assertTrue(expected.containsAll(actual));
        Assert.assertTrue(actual.containsAll(expected));
    }
}
