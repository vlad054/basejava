package ru.javawebinar.basejava.storage;

import exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.strategy.StreamSerializer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {

    private final File directory;
    private final StreamSerializer strategy;

    protected FileStorage(File directory, StreamSerializer strategy) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writebale");
        }

        this.directory = directory;
        this.strategy = strategy;
    }

    @Override
    protected List<Resume> getList() {
        List<Resume> list = new ArrayList<>();
        for (File f : getListFiles()) {
            list.add(doGet(f));
        }
        return list;
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected void doSave(Resume r, File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName());
        }
        doUpdate(file, r);
    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("IO error", file.getName());
        }
    }

    @Override
    protected Resume doGet(File file) {
        try {
            return strategy.doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected void doUpdate(File file, Resume r) {
        try {
            strategy.doWrite(r, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    public void clear() {
        for (File f : getListFiles()) {
            if (!f.delete()) {
                throw new StorageException("IO error", f.getName());
            }
        }
    }

    @Override
    public int size() {
        return getListFiles().length;
    }

    private File[] getListFiles() {
        File[] list = directory.listFiles();
        if (list == null) {
            throw new StorageException("IO error", directory.getName());
        }
        return list;
    }
}

