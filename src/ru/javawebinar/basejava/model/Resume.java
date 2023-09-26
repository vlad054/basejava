package ru.javawebinar.basejava.model;

import java.util.*;

/**
 * ru.javawebinar.basejava.model.Resume class
 */
public class Resume {

    // Unique identifier
    private final String uuid;
    private String fullName;

    private Map<ContactType, String> contacts = new HashMap<>();
//    private Map<SectionType, AbstractSection> sections = new HashMap<>();
    private SortedMap<SectionType, AbstractSection> sections = new TreeMap<>((o1, o2) -> o1.ordinal() - o2.ordinal());

    public Resume(String fullName) {
        this.uuid = UUID.randomUUID().toString();
        this.fullName = fullName;
    }

    public Resume(String uuid, String fullName) {
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public void AddContact(ContactType c, String s) {
        contacts.put(c, s);
    }

    public void AddSection(SectionType c, AbstractSection s) {
        sections.put(c, s);
    }

//    public String toString(){
//        return "name = " + fullName ;
//    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;

        return uuid.equals(resume.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

//    @Override
//    public String toString() {
//        return uuid;
//    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    //    @Override
//    public int compareTo(Resume o) {
//        return uuid.compareTo(o.uuid);
//    }
    @Override
    public String toString() {
        String str = fullName + "\n";

        for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
            if (entry.getKey().isWebsite()) {
                str = str + entry.getKey().getTitle();
            } else {
                str = str + entry.getKey().getTitle() + entry.getValue();
            }
            str = str + "\n";
        }
        str = str + "\n";

        for (Map.Entry<SectionType, AbstractSection> entry : sections.entrySet()) {
            str = str + entry.getKey().getTitle() + "\n";
            str = str + entry.getValue().toString() + "\n";
            str = str + "\n";
        }

        return str;
    }
}
