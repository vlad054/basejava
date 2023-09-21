package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.*;

public class MapUuidStorage extends AbstractStorage<String>{

    protected Map<String, Resume> storage = new HashMap<>();

    @Override
    protected String getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected void doSave(Resume r, String i) {
        storage.put(i, r);
    }

    @Override
    protected void doDelete(String i) {
        storage.remove(i);
    }

    @Override
    protected Resume doGet(String o) {
        return storage.get(o);
    }

    @Override
    protected void doUpdate(String o, Resume r) {
        storage.replace(o, r);
    }

    @Override
    protected boolean isExist(String o) {
        return storage.containsKey(o);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected List<Resume> getList() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public int size() {
        return storage.size();
    }
}
