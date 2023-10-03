package ru.javawebinar.basejava.storage;

import exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;

public class ObjectStreamStorage extends AbstractFileStorage{

    protected ObjectStreamStorage(File directory) {
        super(directory);
    }

    @Override
    protected void doWrite(Resume r, OutputStream os) throws IOException {
        try(ObjectOutputStream oos = new ObjectOutputStream(os)){
            oos.writeObject(r);
        }
    }

    @Override
    protected Resume doRead(InputStream is) throws IOException {
        try(ObjectInputStream oos = new ObjectInputStream(is)){
            return (Resume) oos.readObject();
        } catch (ClassNotFoundException e) {
            throw new StorageException("Error read resume", null, e);
        }
    }
}
