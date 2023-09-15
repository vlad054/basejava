package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected void doSaveArr(Resume r, int ind) {
        System.arraycopy(storage, -ind - 1, storage, -ind, size + ind + 1);
        storage[-ind - 1] = r;
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        Resume searchKey = new Resume(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }

    @Override
    protected void doDeleteArr(int ind) {
        System.arraycopy(storage, ind + 1, storage, ind, size - ind + 1);
    }

}
