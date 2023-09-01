package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    public void saveIn(Resume r) {
        int ind = Arrays.binarySearch(storage, 0, size, r);
        if (ind > 0) {
            System.out.println("Resume " + r.getUuid() + " already exist");
        } else {
            for (int i = size; i >= -ind; i--) {
                storage[i] = storage[i - 1];
            }
            storage[-ind - 1] = r;
            size++;
        }
    }

    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }

    @Override
    protected void deleteIn(int ind) {
        for (int i = ind; i < size; i++) {
            storage[i] = storage[i + 1];
        }
    }

}
