package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.*;

public class MapResumeStorage extends AbstractStorage {

    protected Map<String, Resume> storage = new HashMap<>();

    @Override
    protected Object getSearchKey(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected void doSave(Resume r, Object i) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected void doDelete(Object o) {
        Resume resume = (Resume) o;
        storage.remove(resume.getUuid());
    }

    @Override
    protected Resume doGet(Object o) {
        Resume resume = (Resume) o;
        return storage.get(resume.getUuid());
    }

    @Override
    protected void doUpdate(Object o, Resume r) {
        storage.replace(r.getUuid(), r);
    }

    @Override
    protected boolean isExist(Object o) {
        Resume resume = (Resume) o;
        if (resume != null) {
            return true;
        }
        return false;
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
