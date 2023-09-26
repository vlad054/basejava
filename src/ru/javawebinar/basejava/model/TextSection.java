package ru.javawebinar.basejava.model;

public class TextSection extends AbstractSection{
    private String title;

    public TextSection(String title){
        this.title = title;
    }
    @Override
    public String toString() {
        return title;
    }
}
