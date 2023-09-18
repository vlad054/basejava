package ru.javawebinar.basejava.storage;

import exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    @Override
    protected boolean isExist(Object o) {
        return (Integer) o >= 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public List<Resume> getAllSorted() {
        Resume[] resumes = Arrays.copyOfRange(storage, 0, size);
        List<Resume> list = Arrays.asList(resumes);

        list.sort((o1, o2) -> {
            if (o1.getFullName().equals(o2.getFullName())) {
                return o1.getUuid().compareTo(o2.getUuid());
            }
            return o1.getFullName().compareTo(o2.getFullName());
        });
        return list;
    }


    protected abstract Integer getSearchKey(String uuid);

    protected void doSave(Resume r, Object i) {

        if (isOverflow()) {
            throw new StorageException("Storage overflow", r.getUuid());
        } else {
            doSaveArr(r, (Integer) i);
            size++;
        }
    }

    protected void doDelete(Object i) {
        doDeleteArr((Integer) i);
        storage[size - 1] = null;
        size--;
    }

    protected boolean isOverflow() {
        return size >= STORAGE_LIMIT;
    }

    @Override
    protected Resume doGet(Object i) {
        return storage[(int) i];
    }

    @Override
    protected void doUpdate(Object o, Resume r) {
        storage[(int) o] = r;
    }

    protected abstract void doSaveArr(Resume r, int i);

    protected abstract void doDeleteArr(int i);
}
