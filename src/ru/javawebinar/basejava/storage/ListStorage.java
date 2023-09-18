package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ListStorage extends AbstractStorage {

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
    protected void doSave(Resume r, Object i) {
        storage.add(r);
    }

    @Override
    protected void doDelete(Object i) {
        storage.remove((int) i);
    }

    @Override
    protected Resume doGet(Object i) {
        return storage.get((int) i);
    }

    @Override
    protected void doUpdate(Object i, Resume r) {
        storage.set((int) i, r);
    }

    @Override
    protected boolean isExist(Object o) {
        return o != null;
    }

    @Override
    public void clear() {
        storage.clear();
    }

//    @Override
//    public Resume[] getAll() {
//        return storage.toArray(new Resume[storage.size()]);
//    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> resumes = new ArrayList<>(List.copyOf(storage));
        resumes.sort((o1, o2) -> {
            if (o1.getFullName().equals(o2.getFullName())) {
                return o1.getUuid().compareTo(o2.getUuid());
            }
            return o1.getFullName().compareTo(o2.getFullName());
        });
        return resumes;
    }

    @Override
    public int size() {
        return storage.size();
    }

    public static void main(String[] args) {
        ListStorage listStorage = new ListStorage();
        System.out.println(listStorage.getAllSorted());
    }
}
