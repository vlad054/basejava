package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.*;

public class MapStorage extends AbstractStorage{

    protected Map<String, Resume> storage = new HashMap<>();

    @Override
    protected Object getIndex(String uuid) {
        return uuid;
    }

    @Override
    protected void saveResume(Resume r, Object i) {
        storage.put((String) i, r);
    }

    @Override
    protected void deleteResume(Object i) {
        storage.remove(i);
    }

    @Override
    protected boolean isOverflow() {
        return false;
    }

    @Override
    protected Resume getByIndex(Object o) {
        return storage.get(o);
    }

    @Override
    protected void setByIndex(Object o, Resume r) {
        storage.replace((String) o, r);
    }

    @Override
    protected boolean isExist(Object o) {
        return storage.containsKey(o);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return storage.values().toArray(new Resume[0]);
    }

    @Override
    public int size() {
        return storage.size();
    }
}
