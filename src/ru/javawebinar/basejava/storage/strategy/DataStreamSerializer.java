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

            writeWithException(contacts.entrySet(), dos, (contactTypeStringEntry, dataOutputStream) -> {
                dataOutputStream.writeUTF(contactTypeStringEntry.getKey().name());
                dataOutputStream.writeUTF(contactTypeStringEntry.getValue());
            });

            Map<SectionType, AbstractSection> sections = r.getSections();

            writeWithException(sections.entrySet(), dos, (sectionTypeStringEntry, dataOutputStream) -> {
                dataOutputStream.writeUTF(sectionTypeStringEntry.getKey().name());
                AbstractSection section = sectionTypeStringEntry.getValue();
                switch (sectionTypeStringEntry.getKey()) {
                    case OBJECTIVE, PERSONAL -> dataOutputStream.writeUTF(((TextSection) section).getTitle());
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        List<String> stringList = ((ListSection) section).getListSection();
                        writeWithException(stringList, dataOutputStream, (s, dataOutputStream3) -> dataOutputStream3.writeUTF(s));
                    }
                    case EDUCATION, EXPERIENCE -> {
                        List<Company> companies = ((CompanySection) section).getPositions();

                        writeWithException(companies, dataOutputStream, (company, dataOutputStream1) -> {
                            dataOutputStream1.writeUTF(company.getName());
                            dataOutputStream1.writeUTF(getStringNull(company.getWebSite()));
                            List<Period> periodList = company.getPeriods();

                            writeWithException(periodList, dataOutputStream1, (period, dataOutputStream2) -> {
                                dataOutputStream2.writeUTF(period.getName());
                                dataOutputStream2.writeUTF(getStringNull(period.getDescription()));
                                dataOutputStream2.writeUTF(period.getStartDate().toString());
                                dataOutputStream2.writeUTF(period.getEndDate() == null ? "now" : period.getEndDate().toString());
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

            readWithException(resume, dis, (r, dataInputStream) ->
                    r.addContact(ContactType.valueOf(dataInputStream.readUTF()), dataInputStream.readUTF()));

            readWithException(resume, dis, (resume1, dataInputStream) -> {
                SectionType sectionType = SectionType.valueOf(dataInputStream.readUTF());
                switch (sectionType) {
                    case OBJECTIVE, PERSONAL ->
                            resume1.addSection(sectionType, new TextSection(dataInputStream.readUTF()));
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        List<String> list = new ArrayList<>();
                        readWithException(list, dataInputStream, (list1, dataInputStream1) -> list1.add(dataInputStream1.readUTF()));
                        resume1.addSection(sectionType, new ListSection(list));
                    }
                    case EDUCATION, EXPERIENCE -> {
                        CompanySection companySection = new CompanySection();
                        readWithException(companySection, dataInputStream, (companySection1, dataInputStream12) -> {
                            Company company = new Company(dataInputStream12.readUTF(), setStringNull(dataInputStream12.readUTF()));

                            readWithException(company, dataInputStream12, (company1, dataInputStream13) ->
                                    company1.addPeriod(new Period(dataInputStream13.readUTF(), setStringNull(dataInputStream13.readUTF()),
                                            LocalDate.parse(dataInputStream13.readUTF()), setDateNull(dataInputStream13.readUTF()))));
                            companySection1.addPosition(company);
                        });
                        resume1.addSection(sectionType, companySection);
                    }
                }
            });
            return resume;
        }
    }

    @FunctionalInterface
    private interface WriteConsumer<T> {
        void doWrite(T t, DataOutputStream dataOutputStream) throws IOException;
    }

    @FunctionalInterface
    private interface ReadConsumer<T> {
        void doRead(T t, DataInputStream dataInputStream) throws IOException;
    }

    private <T> void readWithException(T t, DataInputStream dataInputStream, ReadConsumer<T> loopConsumer) throws IOException {
        int collectionSize = dataInputStream.readInt();
        for (int i = 0; i < collectionSize; i++) {
            loopConsumer.doRead(t, dataInputStream);
        }
    }

    private <T> void writeWithException(Collection<T> collection, DataOutputStream dataOutputStream, WriteConsumer<T> loopConsumer) throws IOException {
        dataOutputStream.writeInt(collection.size());
        for (T t : collection) {
            loopConsumer.doWrite(t, dataOutputStream);
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
