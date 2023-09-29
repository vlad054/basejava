package ru.javawebinar.basejava.storage;

import exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {

    private final File directory;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writebale");
        }

        this.directory = directory;
    }

    @Override
    protected List<Resume> getList() {
        List<Resume> list = new ArrayList<>();
        final File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("IO error", directory.getName());
        }
        for (File f : files) {
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
            if (!file.createNewFile()) {
                throw new StorageException("IO error", file.getName());
            }
            doWrite(r, file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete()){
            throw new StorageException("IO error", file.getName());
        }
    }

    @Override
    protected Resume doGet(File file) {
        try {
            return doRead(file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected void doUpdate(File file, Resume r) {
        try {
            doWrite(r, file);
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
        final File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("IO error", directory.getName());
        }
        for (File f : files) {
            if (!f.delete()){
                throw new StorageException("IO error", f.getName());
            }
        }
    }

    @Override
    public int size() {
        String[] list = directory.list();

        if (list == null) {
            throw new StorageException("IO error", directory.getName());
        }
        return list.length;
    }

    protected abstract void doWrite(Resume r, File file) throws IOException;

    protected abstract Resume doRead(File file) throws IOException;
}

