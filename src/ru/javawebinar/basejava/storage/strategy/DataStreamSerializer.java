package ru.javawebinar.basejava.storage.strategy;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());

            Map<ContactType, String> contacts = r.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactType, String> m : contacts.entrySet()) {
                dos.writeUTF(m.getKey().name());
                dos.writeUTF(m.getValue());
            }

            Map<SectionType, AbstractSection> sections = r.getSections();
            dos.writeInt(sections.size());
            for (Map.Entry<SectionType, AbstractSection> m : sections.entrySet()) {
                dos.writeUTF(m.getKey().name());
                AbstractSection section = m.getValue();
                switch (m.getKey()) {
                    case ACHIEVEMENT, QUALIFICATIONS -> {
//                        ((ListSection)section).getListSection().stream().forEach(str1 -> dos.writeUTF(str1));
                        dos.writeInt(((ListSection) section).getListSection().size());
                        ((ListSection) section).getListSection().stream().forEach(str -> {
                            try {
                                dos.writeUTF(str);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                    case EDUCATION, EXPERIENCE -> {
                        List<Company> companies = ((CompanySection) section).getPositions();
                        dos.writeInt(companies.size());
                        for (Company company : companies) {
                            dos.writeUTF(company.getName());
                            dos.writeUTF(getStringNull(company.getWebSite()));
                            dos.writeInt(company.getPeriods().size());
                            company.getPeriods().stream().forEach(period -> {
                                try {
                                    dos.writeUTF(period.getName());
                                    dos.writeUTF(getStringNull(period.getDescription()));
                                    dos.writeUTF(period.getStartDate().toString());
                                    dos.writeUTF(period.getEndDate() == null ? "now" : period.getEndDate().toString());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        }
                    }
                    case OBJECTIVE, PERSONAL -> {
                        dos.writeUTF(((TextSection) section).getTitle());
                    }
                }
                ;
            }
        }
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
                } ;
            }
            System.out.println(resume.toString());
            return resume;
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
