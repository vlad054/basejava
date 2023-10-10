package ru.javawebinar.basejava.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Company implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String webSite;
    private final List<Period> periods = new ArrayList<>();

    public Company(){}

    public Company(String name, String webSite) {
        Objects.requireNonNull(name , "cant be null");
        this.name = name;
        this.webSite = webSite;
    }

    public void addPeriod(Period period){
        periods.add(period);
    }

    public String getName() {
        return name;
    }

    public String getWebSite() {
        return webSite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Company company = (Company) o;

        if (!name.equals(company.name)) return false;
        if (!Objects.equals(webSite, company.webSite)) return false;
        return periods.equals(company.periods);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (webSite != null ? webSite.hashCode() : 0);
        return result;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public String toString(){
        String str = name + "\n";
        for(Period p: periods){
            str = str + p.toString() +"\n";
        }
        return str;
    }
}
