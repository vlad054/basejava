package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ResumeServlet extends HttpServlet {

    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        Resume r = storage.get(uuid);
        r.setFullName(fullName);
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && !value.trim().isEmpty()) {
                r.addContact(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }

        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && !value.trim().isEmpty()) {
                switch (type) {
                    case OBJECTIVE, PERSONAL -> r.addSection(type, new TextSection(value.trim()));
                    case ACHIEVEMENT, QUALIFICATIONS ->
                            r.addSection(type, new ListSection(List.of(value.replaceAll("\r\n\r\n|\n\n","\r\n").split("\\n"))));
                    case EXPERIENCE, EDUCATION -> setSection(r, value, type);
                }
            }
        }
        storage.update(r);
        response.sendRedirect("resume");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");

        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("list.jsp").forward(request, response);
            return;
        }

        Resume r;
        switch (action) {
            case "delete" -> {
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            }
            case "view", "edit" -> {
                if (uuid == null) {
                    r = new Resume("");
                    storage.save(r);
                } else {
                    r = storage.get(uuid);
                }
            }
            default -> throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        request.getRequestDispatcher(
                ("view".equals(action) ? "view.jsp" : "edit.jsp")
        ).forward(request, response);
    }

    //strong data format
    private void setSection(Resume r, String value, SectionType type){
        int s = (type == SectionType.EXPERIENCE)?2:1;
        List<String> sectionList = List.of(value.trim().split("\r\n\r\n"));
        if (!sectionList.isEmpty()) {
            CompanySection companySection = new CompanySection();
            for (String str : sectionList) {
                String[] arrStr = str.split("\r\n");
                Company company = new Company(arrStr[0], null);
                for (int i=1; i < arrStr.length; i = i + s){
                    LocalDate startDate = LocalDate.of(Integer.parseInt(arrStr[i].substring(3,7))
                            , Integer.parseInt(arrStr[i].substring(0,2)),
                            1);
                    LocalDate endDate;
                    if (arrStr[i].substring(7).contains("/")){
                        endDate = LocalDate.of(Integer.parseInt(arrStr[i].substring(11,15))
                                , Integer.parseInt(arrStr[i].substring(8,10)),
                                1);
                    } else {
                        endDate = null;
                    }
                    String desc = null;
                    String name = arrStr[i].substring(15).trim();
                    if (type==SectionType.EXPERIENCE) { desc = arrStr[i+1].trim();}
                    company.addPeriod(new Period(name, desc, startDate, endDate));
                }
                companySection.addPosition(company);
            }
            r.addSection(type, companySection);
        }
    }
}
