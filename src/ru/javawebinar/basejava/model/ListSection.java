package ru.javawebinar.basejava.model;

import java.util.Objects;

public class ListSection extends AbstractSection {
    private java.util.List<String> listSection;
    private static final long serialVersionUID = 1L;

    public ListSection(){}
    public ListSection(java.util.List<String> list) {
        Objects.requireNonNull(list, "cant be null");
        this.listSection = list;
    }

    public java.util.List<String> getListSection() {
        return listSection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListSection listSection = (ListSection) o;

        return this.listSection.equals(listSection.listSection);
    }

    @Override
    public int hashCode() {
        return listSection.hashCode();
    }

    public String toString() {
        String str = "";
        for (String s : listSection) {
            str = str + "-" + s + "\n";
        }
        return str;
    }
}
