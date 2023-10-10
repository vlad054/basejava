package ru.javawebinar.basejava.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * ru.javawebinar.basejava.model.Resume class
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Resume implements Serializable {

    private static final long serialVersionUID = 1L;
    // Unique identifier
    private String uuid;
    private String fullName;
    private final Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private final Map<SectionType, AbstractSection> sections = new EnumMap<>(SectionType.class);
//        private SortedMap<SectionType, AbstractSection> sections = new TreeMap<>((o1, o2) -> o1.ordinal() - o2.ordinal());

    public Map<ContactType, String> getContacts() {
        return contacts;
    }

    public Resume() {
    }

    public Resume(String fullName) {
        this.uuid = UUID.randomUUID().toString();
        this.fullName = fullName;
    }

    public Resume(String uuid, String fullName) {
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public void addContact(ContactType c, String s) {
        contacts.put(c, s);
    }

    public void addSection(SectionType c, AbstractSection s) {
        sections.put(c, s);
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Map<SectionType, AbstractSection> getSections() {
        return sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return Objects.equals(uuid, resume.uuid) && Objects.equals(fullName, resume.fullName) && Objects.equals(contacts, resume.contacts) && Objects.equals(sections, resume.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName, contacts, sections);
    }

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
