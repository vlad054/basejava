package ru.javawebinar.basejava.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class CompanySection extends AbstractSection {

    private static final long serialVersionUID = 1L;
    private final List<Company> positions = new ArrayList<>();

    public void addPosition(Company c) {
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

    public List<String> getPositionNames() {
        List<String> ret = new ArrayList<>();
        for (Company company : positions){
            ret.add(company.getName());
        }
        return ret;
    }

    public String toString() {
        String str = "";
        for (Company c : positions) {
            str = str + c.toString() + "\n";
        }
        return str;
    }

    @Override
    public String toHtml() {
        String str = "<br>";
        for (Company c : positions) {
            str = str + " - " +  c.toString() + " <br> ";
        }
        return str;
    }
}
