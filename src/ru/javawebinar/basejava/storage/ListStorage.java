package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ListStorage extends AbstractStorage {

    protected List<Resume> storage = new ArrayList<>();

    @Override
    protected Integer getIndex(String uuid) {
        ListIterator<Resume> iterator = storage.listIterator();
        int ind;
        while (iterator.hasNext()) {
            ind = iterator.nextIndex();
            if (iterator.next().getUuid().equals(uuid)) {
                return ind;
            }
        }
        return null;
    }

    @Override
    protected void saveResume(Resume r, Object i) {
        storage.add(r);
    }

    @Override
    protected void deleteResume(Object i) {
        storage.remove((int)i);
    }

    @Override
    protected boolean isOverflow() {
        return false;
    }

    @Override
    protected Resume getByIndex(int i) {
        return storage.get(i);
    }

    @Override
    protected void setByIndex(int i, Resume r) {
        storage.set(i, r);
    }

    @Override
    protected boolean isExist(Object o) {
        return o != null;
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return storage.toArray(new Resume[storage.size()]);
    }

    @Override
    public int size() {
        return storage.size();
    }
}
