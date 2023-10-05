package ru.javawebinar.basejava.storage;

import exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PathStorageWithStrategy extends AbstractStorage<Path> {

    private final Path directory;
    private final StorageStrategy strategy;

    protected PathStorageWithStrategy(String dir, StorageStrategy storageStrategy) {
        directory = Paths.get(dir);

        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException(dir + "is not directory");
        }
        if (!Files.isReadable(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not readable/writebale");
        }

        this.strategy = storageStrategy;
    }

    @Override
    protected List<Resume> getList() {
        List<Resume> list = new ArrayList<>();
        try {
            Files.list(directory).forEach(path -> list.add(doGet(path)));
        } catch (IOException e) {
            throw new StorageException("IO error", directory.toString());
        }
        return list;
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return Paths.get(directory.toString(), uuid);
    }

    @Override
    protected void doSave(Resume r, Path file) {
        doUpdate(file, r);
    }

    @Override
    protected void doDelete(Path file) {
        try {
            Files.delete(file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.toString());
        }
    }

    @Override
    protected Resume doGet(Path file) {
        try {
            return strategy.doRead(new BufferedInputStream(Files.newInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO error", file.toString(), e);
        }
    }

    @Override
    protected void doUpdate(Path file, Resume r) {
        try {
            strategy.doWrite(r, new BufferedOutputStream(Files.newOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO error", file.toString(), e);
        }
    }

    @Override
    protected boolean isExist(Path file) {
        return Files.exists(file);
    }

    @Override
    public void clear() {
        try {
            Files.list(directory).forEach(this::doDelete);
        } catch (IOException e) {
            throw new StorageException("IO error", null);
        }
    }

    @Override
    public int size() {
        try {
            return (int) Files.list(directory).count();
        } catch (IOException e) {
            throw new StorageException("IO error", directory.toString());
        }
    }
}
