package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.*;

public class MapStorage extends AbstractStorage{

    protected Map<String, Resume> storage = new HashMap<>();

    @Override
    protected Object getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected void doSave(Resume r, Object i) {
        storage.put((String) i, r);
    }

    @Override
    protected void doDelete(Object i) {
        storage.remove(i);
    }

    @Override
    protected Resume doGet(Object o) {
        return storage.get(o);
    }

    @Override
    protected void doUpdate(Object o, Resume r) {
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
