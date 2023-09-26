package ru.javawebinar.basejava.model;

public class Period {
    private String name;
    private String description;
    private String startDate;
    private String endtDate;

    public Period(String name, String description, String startDate, String endtDate) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endtDate = endtDate;
    }

    @Override
    public String toString() {
        String str;
        if (description == null) str = "";
        else str = "\n   " + description;
        return startDate + "-" + endtDate + " " + name + str;
    }
}
