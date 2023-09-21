package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.*;

public class MapResumeStorage extends AbstractStorage<Resume> {

    protected Map<String, Resume> storage = new HashMap<>();

    @Override
    protected Resume getSearchKey(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected void doSave(Resume r, Resume i) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected void doDelete(Resume o) {
        storage.remove(o.getUuid());
    }

    @Override
    protected Resume doGet(Resume o) {
        return storage.get(o.getUuid());
    }

    @Override
    protected void doUpdate(Resume o, Resume r) {
        storage.replace(r.getUuid(), r);
    }

    @Override
    protected boolean isExist(Resume o) {
        return o != null;
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
