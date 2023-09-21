package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ListStorage extends AbstractStorage<Integer> {

    protected List<Resume> storage = new ArrayList<>();

    @Override
    protected Integer getSearchKey(String uuid) {
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
    protected void doSave(Resume r, Integer i) {
        storage.add(r);
    }

    @Override
    protected void doDelete(Integer i) {
        storage.remove((int)i);
    }

    @Override
    protected Resume doGet(Integer i) {
        return storage.get(i);
    }

    @Override
    protected void doUpdate(Integer i, Resume r) {
        storage.set(i, r);
    }

    @Override
    protected boolean isExist(Integer o) {
        return o != null;
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected List<Resume> getList() {
        return new ArrayList<>(List.copyOf(storage));
    }

    @Override
    public int size() {
        return storage.size();
    }
}
