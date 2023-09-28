package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.*;

import java.time.LocalDate;
import java.util.Arrays;

public class ResumeTestData{

    public static void main(String[] args) {

        Resume resume = new Resume("Григорий Кислин");
        resume.addContact(ContactType.PHONE, "+7(921) 855-0482");
        resume.addContact(ContactType.SKYPE, "skype:grigory.kislin");
        resume.addContact(ContactType.MAIL, "gkislin@yandex.ru");
        resume.addContact(ContactType.LINKEDIN, "https://www.linkedin.com/in/gkislin");
        resume.addContact(ContactType.GITHUB, "https://github.com/gkislin");
        resume.addContact(ContactType.STACKOVERFLOW, "https://stackoverflow.com/users/548473");
        resume.addContact(ContactType.HOMEPAGE, "http://gkislin.ru/");

        resume.addSection(SectionType.OBJECTIVE, new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));
        resume.addSection(SectionType.PERSONAL, new TextSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры."));

        java.util.List<String> listAch = Arrays.asList("Организация команды и успешная реализация Java проектов для сторонних заказчиков: приложения автопарк на стеке Spring Cloud/микросервисы, система мониторинга показателей спортсменов на Spring Boot, участие в проекте МЭШ на Play-2, многомодульный Spring Boot + Vaadin проект для комплексных DIY смет",
                "С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. Более 3500 выпускников.",
                "Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.",
                "Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера.",
                "Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Commet, HTML5, Highstock для алгоритмического трейдинга.",
                "Создание JavaEE фреймворка для отказоустойчивого взаимодействия слабо-связанных сервисов (SOA-base архитектура, JAX-WS, JMS, AS Glassfish). Сбор статистики сервисов и информации о состоянии через систему мониторинга Nagios. Реализация онлайн клиента для администрирования и мониторинга системы по JMX (Jython/ Django).",
                "Реализация протоколов по приему платежей всех основных платежных системы России (Cyberplat, Eport, Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) и Никарагуа.");
        resume.addSection(SectionType.ACHIEVEMENT, new List(listAch));

        java.util.List<String> listQual = Arrays.asList("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2",
                "Version control: Subversion, Git, Mercury, ClearCase, Perforce",
                "DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle, MySQL, SQLite, MS SQL, HSQLDB",
                "Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, Groovy",
                "XML/XSD/XSLT, SQL, C/C++, Unix shell scripts",
                "Java Frameworks: Java 8 (Time API, Streams), Guava, Java Executor, MyBatis, Spring (MVC, Security, Data, Clouds, Boot), JPA (Hibernate, EclipseLink), Guice, GWT(SmartGWT, ExtGWT/GXT), Vaadin, Jasperreports, Apache Commons, Eclipse SWT, JUnit, Selenium (htmlelements).",
                "Python: Django.",
                "JavaScript: jQuery, ExtJS, Bootstrap.js, underscore.js",
                "Scala: SBT, Play2, Specs2, Anorm, Spray, Akka",
                "Технологии: Servlet, JSP/JSTL, JAX-WS, REST, EJB, RMI, JMS, JavaMail, JAXB, StAX, SAX, DOM, XSLT, MDB, JMX, JDBC, JPA, JNDI, JAAS, SOAP, AJAX, Commet, HTML5, ESB, CMIS, BPMN2, LDAP, OAuth1, OAuth2, JWT.",
                "Инструменты: Maven + plugin development, Gradle, настройка Ngnix",
                "администрирование Hudson/Jenkins, Ant + custom task, SoapUI, JPublisher, Flyway, Nagios, iReport, OpenCmis, Bonita, pgBouncer",
                "Отличное знание и опыт применения концепций ООП, SOA, шаблонов проектрирования, архитектурных шаблонов, UML, функционального программирования",
                "Родной русский, английский \"upper intermediate\""
                );
        resume.addSection(SectionType.QUALIFICATIONS, new List(listQual));

        CompanySection sectionExp = new CompanySection();

        Company company1 = new Company("Java Online Projects", "http://javaops.ru/");
        company1.addPeriod(new Period("Автор проекта.", "Создание, организация и проведение Java онлайн проектов и стажировок.",
                LocalDate.of(2013,10,1), null));
//                --;"10/2013", "Сейчас"));
        sectionExp.addPosition(company1);

        Company company2 = new Company("Wrike", "https://www.wrike.com/");
        company2.addPeriod(new Period("Старший разработчик (backend)", "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO.",
                LocalDate.of(2014, 10, 1), LocalDate.of(2016, 1, 1)));
//                                                    , new '10/2014', "01/2016"));
        sectionExp.addPosition(company2);

        Company company3 = new Company("RIT Center", null);
        company3.addPeriod(new Period("Java архитектор", "Организация процесса разработки системы ERP для разных окружений: релизная политика, версионирование, ведение CI (Jenkins), миграция базы (кастомизация Flyway), конфигурирование системы (pgBoucer, Nginx), AAA via SSO. Архитектура БД и серверной части системы. Разработка интергационных сервисов: CMIS, BPMN2, 1C (WebServices), сервисов общего назначения (почта, экспорт в pdf, doc, html). Интеграция Alfresco JLAN для online редактирование из браузера документов MS Office. Maven + plugin development, Ant, Apache Commons, Spring security, Spring MVC, Tomcat,WSO2, xcmis, OpenCmis, Bonita, Python scripting, Unix shell remote scripting via ssh tunnels, PL/Python"
                , LocalDate.of(2012, 4, 1), LocalDate.of(2014, 10, 1)));
//                , "04/2012", "10/2014"));
        sectionExp.addPosition(company3);

        Company company4 = new Company("RIT Center", null);
        company4.addPeriod(new Period("Java архитектор", "Организация процесса разработки системы ERP для разных окружений: релизная политика, версионирование, ведение CI (Jenkins), миграция базы (кастомизация Flyway), конфигурирование системы (pgBoucer, Nginx), AAA via SSO. Архитектура БД и серверной части системы. Разработка интергационных сервисов: CMIS, BPMN2, 1C (WebServices), сервисов общего назначения (почта, экспорт в pdf, doc, html). Интеграция Alfresco JLAN для online редактирование из браузера документов MS Office. Maven + plugin development, Ant, Apache Commons, Spring security, Spring MVC, Tomcat,WSO2, xcmis, OpenCmis, Bonita, Python scripting, Unix shell remote scripting via ssh tunnels, PL/Python"
                , LocalDate.of(2012, 4, 1), LocalDate.of(2014, 10, 1)));
//                , "04/2012", "10/2014"));
        sectionExp.addPosition(company4);

        Company company5 = new Company("Luxoft (Deutsche Bank)", "http://www.luxoft.ru/");
        company5.addPeriod(new Period("Ведущий программист", "Участие в проекте Deutsche Bank CRM (WebLogic, Hibernate, Spring, Spring MVC, SmartGWT, GWT, Jasper, Oracle). Реализация клиентской и серверной части CRM. Реализация RIA-приложения для администрирования, мониторинга и анализа результатов в области алгоритмического трейдинга. JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Highstock, Commet, HTML5."
                , LocalDate.of(2010, 12, 1), LocalDate.of(2012, 4, 1)));
//                , "12/2010", "04/2012"));
        sectionExp.addPosition(company5);

        Company company6 = new Company("Yota", "https://www.yota.ru/");
        company6.addPeriod(new Period("Ведущий специалист", "Дизайн и имплементация Java EE фреймворка для отдела \"Платежные Системы\" (GlassFish v2.1, v3, OC4J, EJB3, JAX-WS RI 2.1, Servlet 2.4, JSP, JMX, JMS, Maven2). Реализация администрирования, статистики и мониторинга фреймворка. Разработка online JMX клиента (Python/ Jython, Django, ExtJS)"
                , LocalDate.of(2008, 6, 1), LocalDate.of(2010, 12, 1)));
//                , "06/2008", "12/2010"));
        sectionExp.addPosition(company6);

        Company company7 = new Company("Enkata", "http://enkata.com/");
        company7.addPeriod(new Period("Разработчик ПО", "Реализация клиентской (Eclipse RCP) и серверной (JBoss 4.2, Hibernate 3.0, Tomcat, JMS) частей кластерного J2EE приложения (OLAP, Data mining)."
                , LocalDate.of(2007, 3, 1), LocalDate.of(2008, 6, 1)));
//                , "03/2007", "06/2008"));
        sectionExp.addPosition(company7);

        Company company8 = new Company("Siemens AG", "https://www.siemens.com/ru/ru/home.html");
        company8.addPeriod(new Period("Разработчик ПО", "Разработка информационной модели, проектирование интерфейсов, реализация и отладка ПО на мобильной IN платформе Siemens @vantage (Java, Unix)."
                , LocalDate.of(2005, 1, 1), LocalDate.of(2007, 2, 1)));
//                , "01/2005", "02/2007"));
        sectionExp.addPosition(company8);

        Company company9 = new Company("Alcatel", "http://www.alcatel.ru/");
        company9.addPeriod(new Period("Инженер по аппаратному и программному тестированию", "Тестирование, отладка, внедрение ПО цифровой телефонной станции Alcatel 1000 S12 (CHILL, ASM)."
                , LocalDate.of(1997, 9, 1), LocalDate.of(2005, 1, 1)));
//                , "09/1997", "01/2005"));
        sectionExp.addPosition(company9);

        resume.addSection(SectionType.EXPERIENCE, sectionExp);

        CompanySection sectionEdu = new CompanySection();

        Company company11 = new Company("Coursera", "https://www.coursera.org/course/progfun");
        company11.addPeriod(new Period("'Functional Programming Principles in Scala' by Martin Odersky", null,
                LocalDate.of(2013, 3, 1), LocalDate.of(2013, 5, 1)));
//                "03/2013", "05/2013"));
        sectionEdu.addPosition(company11);

        Company company12 = new Company("Luxoft", "http://www.luxoft-training.ru/training/catalog/course.html?ID=22366");
        company12.addPeriod(new Period("Курс 'Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.'", null,
                LocalDate.of(2011, 3, 1), LocalDate.of(2011, 4, 1)));
//                "03/2011", "04/2011"));
        sectionEdu.addPosition(company12);

        Company company13 = new Company("Siemens AG", "http://www.siemens.ru/");
        company13.addPeriod(new Period("3 месяца обучения мобильным IN сетям (Берлин)", null,
                LocalDate.of(2005, 1, 1), LocalDate.of(2005, 4, 1)));
//                "01/2005", "04/2005"));
        sectionEdu.addPosition(company13);

        Company company14 = new Company("Alcatel", "http://www.alcatel.ru/");
        company14.addPeriod(new Period("6 месяцев обучения цифровым телефонным сетям (Москва)", null,
                LocalDate.of(1997, 9, 1), LocalDate.of(1998, 3, 1)));
//                "09/1997", "03/1998"));
        sectionEdu.addPosition(company14);

        Company company15 = new Company("Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики", "http://www.ifmo.ru/");
        company15.addPeriod(new Period("Аспирантура (программист С, С++)", null,
                LocalDate.of(1993, 9, 1), LocalDate.of(1996, 7, 1)));
//                "09/1993", "07/1996"));
        company15.addPeriod(new Period("Инженер (программист Fortran, C)", null,
                LocalDate.of(1987, 9, 1), LocalDate.of(1993, 7, 1)));
//                "09/1987", "07/1993"));
        sectionEdu.addPosition(company15);

        Company company16 = new Company("Заочная физико-техническая школа при МФТИ", "https://mipt.ru/");
        company16.addPeriod(new Period("Закончил с отличием", null,
                LocalDate.of(1984, 9, 1), LocalDate.of(1987, 6, 1)));
//                "09/1984", "06/1987"));
        sectionEdu.addPosition(company16);

        resume.addSection(SectionType.EDUCATION, sectionEdu);

        System.out.println(resume.toString());
    }
}
