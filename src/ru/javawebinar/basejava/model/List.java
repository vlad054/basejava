package ru.javawebinar.basejava.model;

public class List extends AbstractSection {
    private final java.util.List<String> listSection;

    public List(java.util.List<String> list) {
        this.listSection = list;
    }

    public java.util.List<String> getListSection() {
        return listSection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        List list = (List) o;

        return listSection.equals(list.listSection);
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
