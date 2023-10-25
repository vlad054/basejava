package ru.javawebinar.basejava.storage;

import exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.strategy.StreamSerializer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class PathStorage extends AbstractStorage<Path> {

    private final Path directory;
    private final StreamSerializer strategy;

    protected PathStorage(String dir, StreamSerializer storageStrategy) {
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
//        return getListPaths().map(this::doGet).toList();
//        List<Resume> list = new ArrayList<>();
//        getListPaths().forEach(path -> list.add(doGet(path)));
//        return list;

        return getListPaths().map(this::doGet).collect(Collectors.toList());
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
        getListPaths().forEach(this::doDelete);
    }

    @Override
    public int size() {
        return (int) getListPaths().count();
    }

    private Stream<Path> getListPaths() {
        try {
            return Files.list(directory);
        } catch (IOException e) {
            throw new StorageException("IO error", (Exception)  null);
        }
    }
}
