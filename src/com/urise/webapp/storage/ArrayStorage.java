package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {

    private final static int NUM_RES = 10000;
    Resume[] storage = new Resume[NUM_RES];
    int size;

    public void clear() {
        Arrays.fill(storage, 0, size - 1, null);
        size = 0;
    }

    public void save(Resume r) {
        if (size == storage.length) {
            System.out.println("Массив storage заполнен, добавление не возможно");
        } else if (getIndex(r.getUuid()) != -1) {
            System.out.println("Найден экземпляр с uuid=" + r.getUuid() + " добавление отменено");
        } else {
            storage[size] = r;
            size++;
            System.out.println("Экземпляр с uuid=" + r.getUuid() + " добавлен");
        }
    }

    public Resume get(String uuid) {
        int i = getIndex(uuid);
        if (i != -1) {
            return storage[i];
        } else {
            System.out.println("Не найден экземпляр с uuid=" + uuid);
            return null;
        }
    }

    public void delete(String uuid) {
        int i = getIndex(uuid);
        if (i != -1) {
            storage[i].setUuid(storage[size - 1].getUuid());
            storage[size - 1] = null;
            size--;
        } else {
            System.out.println("Не найден экземпляр с uuid=" + uuid);
        }
    }

    public void update(Resume r) {
        int i = getIndex(r.getUuid());
        if (i != -1) {
            storage[i] = r;
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

    private int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

}
