package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.Comparator;

public class SortedArrayStorage extends AbstractArrayStorage {

    private static final Comparator<Resume> RESUME_COMPARATOR = (o1, o2) -> o1.getUuid().compareTo(o2.getUuid());

    @Override
    protected void doSaveArr(Resume r, int ind) {
        System.arraycopy(storage, -ind - 1, storage, -ind, size + ind + 1);
        storage[-ind - 1] = r;
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        Resume searchKey = new Resume(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey, RESUME_COMPARATOR);
    }

    @Override
    protected void doDeleteArr(int ind) {
        System.arraycopy(storage, ind + 1, storage, ind, size - ind + 1);
    }

}
