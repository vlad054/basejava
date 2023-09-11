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

//    @Override
//    public void save(Resume r) {
//        int ind = getIndex(r.getUuid());
//
//        if (size >= STORAGE_LIMIT) {
//            throw new StorageException("Storage overflow", r.getUuid());
//        } else if (ind >= 0) {
//            throw new ExistStorageException(r.getUuid());
//        } else {
//            saveResume(r, ind);
//        }
//    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

//    @Override
//    public void delete(String uuid) {
//        int index = getIndex(uuid);
//        if (index < 0) {
//            throw new NotExistStorageException(uuid);
//        } else {
//            deleteResume(index);
//            storage[size - 1] = null;
//            size--;
//        }
//    }

//    @Override
//    public Resume get(String uuid) {
//        int index = getIndex(uuid);
//        if (index < 0) {
//            throw new NotExistStorageException(uuid);
//        }
//        return storage[index];
//    }

//    @Override
//    public void update(Resume r) {
//        int index = getIndex(r.getUuid());
//        if (index < 0) {
//            throw new NotExistStorageException(r.getUuid());
//        } else {
//            storage[index] = r;
//        }
//    }

    @Override
    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    protected abstract int getIndex(String uuid);

    protected void saveResume(Resume r, int i){
        saveResumeArr(r, i);
        size++;
    }

    protected void deleteResume(int i, String uuid){
        deleteResumeArr(i);
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
