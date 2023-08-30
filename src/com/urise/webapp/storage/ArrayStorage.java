package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    int size;

    public void clear() {
        Arrays.fill(storage, 0, size - 1, null);
        size = 0;
    }

    public void save(Resume r) {
        if (size == storage.length) {
            System.out.println("Массив storage заполнен, добавление не возможно");
        }

        if (getIn(r.getUuid()) == null) {
            storage[size] = r;
            size++;
            System.out.println("Экземпляр с uuid=" + r.getUuid() + " добавлен");
        } else {
            System.out.println("Найден экземпляр с uuid=" + r.getUuid() + " добавление отменено");
        }
    }

    public Resume get(String uuid) {
        Resume r = getIn(uuid);
        if (r != null) {
            return r;
        } else {
            System.out.println("Не найден экземпляр с uuid=" + uuid);
            return null;
        }
    }

    public void delete(String uuid) {
        Resume r = getIn(uuid);
        if (r != null) {
            r.setUuid(storage[size - 1].getUuid());
            storage[size - 1] = null;
            size--;
        } else {
            System.out.println("Не найден экземпляр с uuid=" + uuid);
        }
    }

    private Resume getIn(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return storage[i];
            }
        }
        return null;
    }

    public void update(Resume r) {
        Resume resume = getIn(r.getUuid());
        if (resume != null) {
            resume = r;
        } else {
            System.out.println("Не найден экземпляр с uuid=" + r.getUuid());
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }
}
