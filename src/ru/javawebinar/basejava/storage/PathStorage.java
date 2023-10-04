package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PathStorage extends AbstractPathStorage{

    private StorageStrategy strategy;

    protected PathStorage(String dir, StorageStrategy strategy) {
        super(dir);
        this.strategy = strategy;
    }

    @Override
    protected void doWrite(Resume r, OutputStream file) throws IOException {
        strategy.doWrite(r,file);
    }

    @Override
    protected Resume doRead(InputStream file) throws IOException {
        return strategy.doRead(file);
    }
}
