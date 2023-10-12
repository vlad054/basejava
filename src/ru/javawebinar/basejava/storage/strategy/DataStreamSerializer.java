package ru.javawebinar.basejava.storage.strategy;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());

            Map<ContactType, String> contacts = r.getContacts();

            writeWithException(contacts.entrySet(), dos, (contactTypeStringEntry) -> {
                dos.writeUTF(contactTypeStringEntry.getKey().name());
                dos.writeUTF(contactTypeStringEntry.getValue());
            });

            Map<SectionType, AbstractSection> sections = r.getSections();

            writeWithException(sections.entrySet(), dos, (sectionTypeStringEntry) -> {
                dos.writeUTF(sectionTypeStringEntry.getKey().name());
                AbstractSection section = sectionTypeStringEntry.getValue();
                switch (sectionTypeStringEntry.getKey()) {
                    case OBJECTIVE, PERSONAL -> dos.writeUTF(((TextSection) section).getTitle());
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        List<String> stringList = ((ListSection) section).getListSection();
                        writeWithException(stringList, dos, dos::writeUTF);
                    }
                    case EDUCATION, EXPERIENCE -> {
                        List<Company> companies = ((CompanySection) section).getPositions();

                        writeWithException(companies, dos, (company) -> {
                            dos.writeUTF(company.getName());
                            dos.writeUTF(getStringNull(company.getWebSite()));
                            List<Period> periodList = company.getPeriods();

                            writeWithException(periodList, dos, (period) -> {
                                dos.writeUTF(period.getName());
                                dos.writeUTF(getStringNull(period.getDescription()));
                                dos.writeUTF(period.getStartDate().toString());
                                dos.writeUTF(period.getEndDate() == null ? "now" : period.getEndDate().toString());
                            });
                        });
                    }
                }
            });
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            Resume resume = new Resume(dis.readUTF(), dis.readUTF());

            readWithException(dis, () ->
                    resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));

            readWithException(dis, () -> {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                switch (sectionType) {
                    case OBJECTIVE, PERSONAL -> resume.addSection(sectionType, new TextSection(dis.readUTF()));
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        List<String> list = new ArrayList<>();
                        readWithException(dis, () -> list.add(dis.readUTF()));
                        resume.addSection(sectionType, new ListSection(list));
                    }
                    case EDUCATION, EXPERIENCE -> {
                        CompanySection companySection = new CompanySection();
                        readWithException(dis, () -> {
                            Company company = new Company(dis.readUTF(), setStringNull(dis.readUTF()));
                            readWithException(dis, () ->
                                    company.addPeriod(new Period(dis.readUTF(), setStringNull(dis.readUTF()),
                                            LocalDate.parse(dis.readUTF()), setDateNull(dis.readUTF()))));
                            companySection.addPosition(company);
                        });
                        resume.addSection(sectionType, companySection);
                    }
                }
            });
            return resume;
        }
    }

    @FunctionalInterface
    private interface WriteConsumer<T> {
        void doWrite(T t) throws IOException;
    }

    @FunctionalInterface
    private interface ReadConsumer {
        void doRead() throws IOException;
    }

    private void readWithException(DataInputStream dataInputStream, ReadConsumer loopConsumer) throws IOException {
        int collectionSize = dataInputStream.readInt();
        for (int i = 0; i < collectionSize; i++) {
            loopConsumer.doRead();
        }
    }

    private <T> void writeWithException(Collection<T> collection, DataOutputStream dataOutputStream, WriteConsumer<T> loopConsumer) throws IOException {
        dataOutputStream.writeInt(collection.size());
        for (T t : collection) {
            loopConsumer.doWrite(t);
        }
    }

    private String getStringNull(String str) {
        return str == null ? "-" : str;
    }

    private String setStringNull(String str) {
        return str.equals("-") ? null : str;
    }

    private LocalDate setDateNull(String str) {
        return str.equals("now") ? null : LocalDate.parse(str);
    }
}
