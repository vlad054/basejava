package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage{

    protected List<Resume> storage = new ArrayList<>();

    @Override
    protected int getIndex(String uuid) {
        return storage.indexOf(new Resume(uuid));
    }

    @Override
    protected void saveResume(Resume r, int i) {
        storage.add(r);
    }

    @Override
    protected void deleteResume(int i) {
        storage.remove(i);
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
