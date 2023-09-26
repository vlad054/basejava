package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.List;

public class CompanySection extends AbstractSection {

    private List<CompanyPosition> positions = new ArrayList<>();

    public void AddPosition(CompanyPosition c) {
        positions.add(c);
    }

    @Override
    public String toString() {
        String str = "";
        for (CompanyPosition c : positions) {
            str = str + c.toString() + "\n";
        }
        return str;
    }
}
