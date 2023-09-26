package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.List;

public class CompanyPosition {
    private String name;
    private String webSite;
    private List<Period> periods = new ArrayList<>();

    public CompanyPosition(String name, String webSite) {
        this.name = name;
        this.webSite = webSite;
    }

    public void AddPeriod(Period period){
        periods.add(period);
    }

    @Override
    public String toString(){
        String str = name + "\n";
        for(Period p: periods){
            str = str + p.toString() +"\n";
        }
        return str;
    }
}
