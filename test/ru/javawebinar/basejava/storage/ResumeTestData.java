package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.*;

import java.util.Arrays;
import java.util.List;

public class ResumeTestData{

    public static void main(String[] args) {

        Resume resume = new Resume("Григорий Кислин");
        resume.AddContact(ContactType.PHONE, "+7(921) 855-0482");
        resume.AddContact(ContactType.SKYPE, "skype:grigory.kislin");
        resume.AddContact(ContactType.MAIL, "gkislin@yandex.ru");
        resume.AddContact(ContactType.LINKEDIN, "https://www.linkedin.com/in/gkislin");
        resume.AddContact(ContactType.GITHUB, "https://github.com/gkislin");
        resume.AddContact(ContactType.STACKOVERFLOW, "https://stackoverflow.com/users/548473");
        resume.AddContact(ContactType.HOMEPAGE, "http://gkislin.ru/");

        resume.AddSection(SectionType.OBJECTIVE, new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));
        resume.AddSection(SectionType.PERSONAL, new TextSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры."));

        List<String> listAch = Arrays.asList("Организация команды и успешная реализация Java проектов для сторонних заказчиков: приложения автопарк на стеке Spring Cloud/микросервисы, система мониторинга показателей спортсменов на Spring Boot, участие в проекте МЭШ на Play-2, многомодульный Spring Boot + Vaadin проект для комплексных DIY смет",
                "С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. Более 3500 выпускников.",
                "Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.",
                "Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера.",
                "Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Commet, HTML5, Highstock для алгоритмического трейдинга.",
                "Создание JavaEE фреймворка для отказоустойчивого взаимодействия слабо-связанных сервисов (SOA-base архитектура, JAX-WS, JMS, AS Glassfish). Сбор статистики сервисов и информации о состоянии через систему мониторинга Nagios. Реализация онлайн клиента для администрирования и мониторинга системы по JMX (Jython/ Django).",
                "Реализация протоколов по приему платежей всех основных платежных системы России (Cyberplat, Eport, Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) и Никарагуа.");
        resume.AddSection(SectionType.ACHIEVEMENT, new ListSection(listAch));

        List<String> listQual = Arrays.asList("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2",
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
        resume.AddSection(SectionType.QUALIFICATIONS, new ListSection(listQual));

        CompanySection sectionExp = new CompanySection();

        CompanyPosition companyPosition1 = new CompanyPosition("Java Online Projects", "http://javaops.ru/");
        companyPosition1.AddPeriod(new Period("Автор проекта.", "Создание, организация и проведение Java онлайн проектов и стажировок.", "10/2013", "Сейчас"));
        sectionExp.AddPosition(companyPosition1);

        CompanyPosition companyPosition2 = new CompanyPosition("Wrike", "https://www.wrike.com/");
        companyPosition2.AddPeriod(new Period("Старший разработчик (backend)", "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO."
                                                    , "10/2014", "01/2016"));
        sectionExp.AddPosition(companyPosition2);

        CompanyPosition companyPosition3 = new CompanyPosition("RIT Center", null);
        companyPosition3.AddPeriod(new Period("Java архитектор", "Организация процесса разработки системы ERP для разных окружений: релизная политика, версионирование, ведение CI (Jenkins), миграция базы (кастомизация Flyway), конфигурирование системы (pgBoucer, Nginx), AAA via SSO. Архитектура БД и серверной части системы. Разработка интергационных сервисов: CMIS, BPMN2, 1C (WebServices), сервисов общего назначения (почта, экспорт в pdf, doc, html). Интеграция Alfresco JLAN для online редактирование из браузера документов MS Office. Maven + plugin development, Ant, Apache Commons, Spring security, Spring MVC, Tomcat,WSO2, xcmis, OpenCmis, Bonita, Python scripting, Unix shell remote scripting via ssh tunnels, PL/Python"
                , "04/2012", "10/2014"));
        sectionExp.AddPosition(companyPosition3);

        CompanyPosition companyPosition4 = new CompanyPosition("RIT Center", null);
        companyPosition4.AddPeriod(new Period("Java архитектор", "Организация процесса разработки системы ERP для разных окружений: релизная политика, версионирование, ведение CI (Jenkins), миграция базы (кастомизация Flyway), конфигурирование системы (pgBoucer, Nginx), AAA via SSO. Архитектура БД и серверной части системы. Разработка интергационных сервисов: CMIS, BPMN2, 1C (WebServices), сервисов общего назначения (почта, экспорт в pdf, doc, html). Интеграция Alfresco JLAN для online редактирование из браузера документов MS Office. Maven + plugin development, Ant, Apache Commons, Spring security, Spring MVC, Tomcat,WSO2, xcmis, OpenCmis, Bonita, Python scripting, Unix shell remote scripting via ssh tunnels, PL/Python"
                , "04/2012", "10/2014"));
        sectionExp.AddPosition(companyPosition4);

        CompanyPosition companyPosition5 = new CompanyPosition("Luxoft (Deutsche Bank)", "http://www.luxoft.ru/");
        companyPosition5.AddPeriod(new Period("Ведущий программист", "Участие в проекте Deutsche Bank CRM (WebLogic, Hibernate, Spring, Spring MVC, SmartGWT, GWT, Jasper, Oracle). Реализация клиентской и серверной части CRM. Реализация RIA-приложения для администрирования, мониторинга и анализа результатов в области алгоритмического трейдинга. JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Highstock, Commet, HTML5."
                , "12/2010", "04/2012"));
        sectionExp.AddPosition(companyPosition5);

        CompanyPosition companyPosition6 = new CompanyPosition("Yota", "https://www.yota.ru/");
        companyPosition6.AddPeriod(new Period("Ведущий специалист", "Дизайн и имплементация Java EE фреймворка для отдела \"Платежные Системы\" (GlassFish v2.1, v3, OC4J, EJB3, JAX-WS RI 2.1, Servlet 2.4, JSP, JMX, JMS, Maven2). Реализация администрирования, статистики и мониторинга фреймворка. Разработка online JMX клиента (Python/ Jython, Django, ExtJS)"
                , "06/2008", "12/2010"));
        sectionExp.AddPosition(companyPosition6);

        CompanyPosition companyPosition7 = new CompanyPosition("Enkata", "http://enkata.com/");
        companyPosition7.AddPeriod(new Period("Разработчик ПО", "Реализация клиентской (Eclipse RCP) и серверной (JBoss 4.2, Hibernate 3.0, Tomcat, JMS) частей кластерного J2EE приложения (OLAP, Data mining)."
                , "03/2007", "06/2008"));
        sectionExp.AddPosition(companyPosition7);

        CompanyPosition companyPosition8 = new CompanyPosition("Siemens AG", "https://www.siemens.com/ru/ru/home.html");
        companyPosition8.AddPeriod(new Period("Разработчик ПО", "Разработка информационной модели, проектирование интерфейсов, реализация и отладка ПО на мобильной IN платформе Siemens @vantage (Java, Unix)."
                , "01/2005", "02/2007"));
        sectionExp.AddPosition(companyPosition8);

        CompanyPosition companyPosition9 = new CompanyPosition("Alcatel", "http://www.alcatel.ru/");
        companyPosition9.AddPeriod(new Period("Инженер по аппаратному и программному тестированию", "Тестирование, отладка, внедрение ПО цифровой телефонной станции Alcatel 1000 S12 (CHILL, ASM)."
                , "09/1997", "01/2005"));
        sectionExp.AddPosition(companyPosition9);

        resume.AddSection(SectionType.EXPERIENCE, sectionExp);

        CompanySection sectionEdu = new CompanySection();

        CompanyPosition companyPosition11 = new CompanyPosition("Coursera", "https://www.coursera.org/course/progfun");
        companyPosition11.AddPeriod(new Period("'Functional Programming Principles in Scala' by Martin Odersky", null, "03/2013", "05/2013"));
        sectionEdu.AddPosition(companyPosition11);

        CompanyPosition companyPosition12 = new CompanyPosition("Luxoft", "http://www.luxoft-training.ru/training/catalog/course.html?ID=22366");
        companyPosition12.AddPeriod(new Period("Курс 'Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.'", null, "03/2011", "04/2011"));
        sectionEdu.AddPosition(companyPosition12);

        CompanyPosition companyPosition13 = new CompanyPosition("Siemens AG", "http://www.siemens.ru/");
        companyPosition13.AddPeriod(new Period("3 месяца обучения мобильным IN сетям (Берлин)", null, "01/2005", "04/2005"));
        sectionEdu.AddPosition(companyPosition13);

        CompanyPosition companyPosition14 = new CompanyPosition("Alcatel", "http://www.alcatel.ru/");
        companyPosition14.AddPeriod(new Period("6 месяцев обучения цифровым телефонным сетям (Москва)", null, "09/1997", "03/1998"));
        sectionEdu.AddPosition(companyPosition14);

        CompanyPosition companyPosition15 = new CompanyPosition("Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики", "http://www.ifmo.ru/");
        companyPosition15.AddPeriod(new Period("Аспирантура (программист С, С++)", null, "09/1993", "07/1996"));
        companyPosition15.AddPeriod(new Period("Инженер (программист Fortran, C)", null, "09/1987", "07/1993"));
        sectionEdu.AddPosition(companyPosition15);

        CompanyPosition companyPosition16 = new CompanyPosition("Заочная физико-техническая школа при МФТИ", "https://mipt.ru/");
        companyPosition16.AddPeriod(new Period("Закончил с отличием", null, "09/1984", "06/1987"));
        sectionEdu.AddPosition(companyPosition16);

        resume.AddSection(SectionType.EDUCATION, sectionEdu);

        System.out.println(resume.toString());


    }
}
