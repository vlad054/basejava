package ru.javawebinar.basejava.model;

import java.util.List;

public class ListSection extends AbstractSection {
    private List<String> listSection;

    public ListSection(List<String> list) {
        this.listSection = list;
    }

    @Override
    public String toString() {
        String str = "";
        for (String s : listSection) {
            str = str + "-" + s + "\n";
        }
        return str;
    }
}
