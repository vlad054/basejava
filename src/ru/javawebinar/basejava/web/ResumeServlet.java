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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

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

        Map<String, String[]> mapParam =  request.getParameterMap();

        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());
            if (value == null || value.trim().isEmpty()) {
                switch (type) {
                    case EXPERIENCE -> value = mapParam.get("companyPNameX")[0];
                    case EDUCATION -> value = mapParam.get("companyPNameE")[0];
                }
            }

            if (value != null && !value.trim().isEmpty()) {
                switch (type) {
                    case OBJECTIVE, PERSONAL -> r.addSection(type, new TextSection(value.trim()));
                    case ACHIEVEMENT, QUALIFICATIONS ->
                            r.addSection(type, new ListSection(List.of(value.replaceAll("\r\n\r\n|\n\n","\r\n").split("\\n"))));
                    case EXPERIENCE ->         fillSection(r, SectionType.EXPERIENCE, mapParam.get("companyPNameX"), mapParam.get("companyPWebX"),
                            mapParam.get("periodNameX"), mapParam.get("periodStartDateX"),
                            mapParam.get("periodEndDateX"), mapParam.get("periodDescX"));
                    case EDUCATION ->         fillSection(r, SectionType.EDUCATION, mapParam.get("companyPNameE"), mapParam.get("companyPWebE"),
                            mapParam.get("periodNameE"), mapParam.get("periodStartDateE"),
                            mapParam.get("periodEndDateE"), null);
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

    private void fillSection(Resume r, SectionType type, String[] arrCompanyName, String[] arrWeb,
                             String[] arrPeriod, String[] arrPeriodStart, String[] arrPeriodEnd, String[] arrDesc){

        CompanySection companySection = (CompanySection)r.getSections().get(type);
        if (companySection == null) {
            companySection = new CompanySection();
            r.addSection(type, companySection);
        }
        companySection.getPositions().clear();

        for (int i=0; i < arrCompanyName.length; i++) {
            String companyName = arrCompanyName[i];
            if (!companyName.isEmpty()) {
                Company company = new Company(arrCompanyName[i], arrWeb[i]);
                companySection.addPosition(company);

                company.addPeriod(new Period(arrPeriod[i], (arrDesc != null) ? arrDesc[i] : null,
                        LocalDate.parse(arrPeriodStart[i], DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        (!arrPeriodEnd[i].isEmpty() && !arrPeriodEnd[i].equals("Сейчас")) ? LocalDate.parse(arrPeriodEnd[i], DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null));
            }
        }
    }
}
