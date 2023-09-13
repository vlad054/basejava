package ru.javawebinar.basejava.storage;

import exception.ExistStorageException;
import exception.NotExistStorageException;
import exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    @Override
    public void save(Resume r) {
        Object searchKey = getNotExistingSearchKey(r.getUuid());
        if (isOverflow()) {
            throw new StorageException("Storage overflow", r.getUuid());
        } else {
            saveResume(r, searchKey);
        }
    }

    @Override
    public void delete(String uuid) {
        Object searchKey = getExistingSearchKey(uuid);
        deleteResume(searchKey);
    }

    @Override
    public Resume get(String uuid) {
        Object searchKey = getExistingSearchKey(uuid);
        return getByIndex(searchKey);
    }

    @Override
    public void update(Resume r) {
        Object searchKey = getExistingSearchKey(r.getUuid());
        setByIndex(searchKey, r);
    }

    protected abstract Object getIndex(String uuid);

    protected abstract void saveResume(Resume r, Object i);

    protected abstract void deleteResume(Object i);

    protected abstract boolean isOverflow();

    protected abstract Resume getByIndex(Object o);

    protected abstract void setByIndex(Object i, Resume r);

    protected abstract boolean isExist(Object o);

    private Object getExistingSearchKey(String uuid) {
        Object searchKey = getIndex(uuid);
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private Object getNotExistingSearchKey(String uuid) {
        Object searchKey = getIndex(uuid);
        if (isExist(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

}
