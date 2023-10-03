package ru.javawebinar.basejava.model;

import java.util.Objects;

public class TextSection extends AbstractSection {

    private static final long serialVersionUID = 1L;
    private String title;

    public TextSection(String title) {
        Objects.requireNonNull(title, "cant be null");
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextSection that = (TextSection) o;

        return Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }

    public String getTitle() {
        return title;
    }

    public String toString() {
        return title;
    }
}
