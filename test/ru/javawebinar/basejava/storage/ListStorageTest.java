package ru.javawebinar.basejava.storage;

public class ListStorageTest extends AbstractArrayStorageTest{
    public ListStorageTest() {
        super(new ListStorage());
    }

    @Override
    public void saveOverflow() throws Exception {}
}
