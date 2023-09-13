package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    @Override
    protected boolean isExist(Object o) {
        return (Integer)o >=0;
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
    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    protected abstract Integer getIndex(String uuid);

    protected void saveResume(Resume r, Object i) {
        saveResumeArr(r, (Integer)i);
        size++;
    }

    protected void deleteResume(Object i) {
        deleteResumeArr((Integer)i);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected boolean isOverflow() {
        return size >= STORAGE_LIMIT;
    }

    @Override
    protected Resume getByIndex(int i) {
        return storage[i];
    }

    @Override
    protected void setByIndex(int i, Resume r) {
        storage[i] = r;
    }

    protected abstract void saveResumeArr(Resume r, int i);

    protected abstract void deleteResumeArr(int i);
}
