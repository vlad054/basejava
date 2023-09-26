package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.List;

public class CompanySection extends AbstractSection {

    private final List<Company> positions = new ArrayList<>();

    public void AddPosition(Company c) {
        positions.add(c);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompanySection that = (CompanySection) o;

        return positions.equals(that.positions);
    }

    @Override
    public int hashCode() {
        return positions.hashCode();
    }

    public List<Company> getPositions() {
        return positions;
    }

    public String toString() {
        String str = "";
        for (Company c : positions) {
            str = str + c.toString() + "\n";
        }
        return str;
    }
}
