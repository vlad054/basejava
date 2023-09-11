package ru.javawebinar.basejava.storage;

import exception.ExistStorageException;
import exception.NotExistStorageException;
import exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    @Override
    public void save(Resume r) {
        int ind = getIndex(r.getUuid());

        if (isOverflow()) {
            throw new StorageException("Storage overflow", r.getUuid());
        } else if (ind >= 0) {
            throw new ExistStorageException(r.getUuid());
        } else {
            saveResume(r, ind);
        }
    }

    @Override
    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        } else {
            deleteResume(index, uuid);
        }
    }

    @Override
    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
        return getByIndex(index);
    }

    @Override
    public void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index < 0) {
            throw new NotExistStorageException(r.getUuid());
        } else {
            setByIndex(index, r);
        }
    }

    protected abstract int getIndex(String uuid);

    protected abstract void saveResume(Resume r, int i);

    protected abstract void deleteResume(int i, String uuid);

    protected abstract boolean isOverflow();

    protected abstract Resume getByIndex(int i);

    protected abstract void setByIndex(int i, Resume r);

}
