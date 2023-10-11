package ru.javawebinar.basejava.storage.strategy;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {

    private interface WriteConsumer<T> {
        void doWrite(T t, DataOutputStream dataOutputStream) throws IOException;
    }

    private <T> void writeWithException(Collection<T> collection, DataOutputStream dataOutputStream, WriteConsumer<T> loopConsumer) throws IOException {
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

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());

            Map<ContactType, String> contacts = r.getContacts();
            dos.writeInt(contacts.size());

            writeWithException(contacts.entrySet(), dos, (contactTypeStringEntry, dataOutputStream) -> {
                dataOutputStream.writeUTF(contactTypeStringEntry.getKey().name());
                dataOutputStream.writeUTF(contactTypeStringEntry.getValue());
            });

            Map<SectionType, AbstractSection> sections = r.getSections();
            dos.writeInt(sections.size());

            writeWithException(sections.entrySet(), dos, (sectionTypeStringEntry, dataOutputStream) -> {
                dataOutputStream.writeUTF(sectionTypeStringEntry.getKey().name());
                AbstractSection section = sectionTypeStringEntry.getValue();
                switch (sectionTypeStringEntry.getKey()) {
                    case OBJECTIVE, PERSONAL -> dataOutputStream.writeUTF(((TextSection) section).getTitle());
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        List<String> stringList = ((ListSection) section).getListSection();
                        dataOutputStream.writeInt(stringList.size());
                        writeWithException(stringList, dataOutputStream, (s, dataOutputStream3) -> dataOutputStream3.writeUTF(s));
                    }
                    case EDUCATION, EXPERIENCE -> {
                        List<Company> companies = ((CompanySection) section).getPositions();
                        dataOutputStream.writeInt(companies.size());

                        writeWithException(companies, dataOutputStream, (company, dataOutputStream1) -> {
                            dataOutputStream1.writeUTF(company.getName());
                            dataOutputStream1.writeUTF(getStringNull(company.getWebSite()));
                            List<Period> periodList = company.getPeriods();
                            dataOutputStream1.writeInt(periodList.size());

                            writeWithException(periodList, dataOutputStream1, (period, dataOutputStream2) -> {
                                dataOutputStream2.writeUTF(period.getName());
                                dataOutputStream2.writeUTF(getStringNull(period.getDescription()));
                                dataOutputStream2.writeUTF(period.getStartDate().toString());
                                dataOutputStream2.writeUTF(period.getEndDate() == null ? "now" : period.getEndDate().toString());
                            });
                        });
                    }
                };
            });
        };
    }


    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            Resume resume = new Resume(dis.readUTF(), dis.readUTF());
            int contactsSize = dis.readInt();
            for (int i = 0; i < contactsSize; i++) {
                resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }

            int sectionSize = dis.readInt();

            for (int i = 0; i < sectionSize; i++) {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                switch (sectionType) {
                    case OBJECTIVE, PERSONAL -> resume.addSection(sectionType, new TextSection(dis.readUTF()));
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        int listSize = dis.readInt();
                        List<String> list = new ArrayList<>();
                        for (int l = 0; l < listSize; l++) {
                            list.add(dis.readUTF());
                        }
                        resume.addSection(sectionType, new ListSection(list));
                    }
                    case EDUCATION, EXPERIENCE -> {
                        int listSize = dis.readInt();
                        CompanySection companySection = new CompanySection();
                        for (int k = 0; k < listSize; k++) {
                            Company company = new Company(dis.readUTF(), setStringNull(dis.readUTF()));
                            int periodsSize = dis.readInt();
                            for (int j = 0; j < periodsSize; j++) {
                                company.addPeriod(new Period(dis.readUTF(), setStringNull(dis.readUTF()), LocalDate.parse(dis.readUTF()), setDateNull(dis.readUTF())));
                            }
                            companySection.addPosition(company);
                        }
                        resume.addSection(sectionType, companySection);
                    }
                }
            }
            return resume;
        }
    }
}
