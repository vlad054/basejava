package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

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
    protected void deleteResume(int i, String uuid) {
        Iterator<Resume> iterator = storage.iterator();
        while (iterator.hasNext()){
            Resume r = iterator.next();
            if (Objects.equals(r , new Resume(uuid))){
                iterator.remove();
            }
        }
    }

    @Override
    protected boolean isOverflow() {
        return false;
    }

    @Override
    protected Resume getByIndex(int i) {
        return (Resume) storage.toArray()[i];
    }

    @Override
    protected void setByIndex(int i, Resume r) {
        storage.toArray()[i] = r;
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
