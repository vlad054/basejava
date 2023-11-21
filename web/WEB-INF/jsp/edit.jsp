<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" required size=50 value="${resume.fullName}"></dd>
        </dl>
        <h3>Контакты:</h3>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size=30 value="${resume.getContacts().get(type)}"></dd>
            </dl>
        </c:forEach>

        <h3>Секции:</h3>
        <c:forEach var="type" items="<%=SectionType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd>
                    <label>
                    <textarea
                    cols="100" rows="10" name="${type.name()}" >${resume.getSections().get(type)}</textarea>
                    </label>
                </dd>

            </dl>
        </c:forEach>


        <h3>Добавить секцию :</h3>
        <dd><label> Section Name <input list="sections" name="sectionName" required size=50>
            <datalist id="sections">
                <option value=${SectionType.EXPERIENCE.name()}></option>
                <option value=${SectionType.EDUCATION.name()}></option>
            </datalist>
        </label></dd> <br>
        <dd><label> Company Name <input type="text" title="Company Name" name="companyName" required size=50></label></dd> <br>
        <dd><label> Company Web <input type="text" title="Company Web" name="companyWeb" size=50></label></dd> <br>
        <dd><label> Period Name <input type="text" title="Period Name" name="periodName" required size=50></label></dd> <br>
        <dd><label> Period start Date <input type="text" title="Period start Date dd/mm/yyyy" name="periodStartDate" required size=50></label></dd> <br>
        <dd><label> Period end Date <input type="text" title="Period end Date dd/mm/yyyy" name="periodEndDate"  size=50></label></dd> <br>
        <label title="Section Descripion"> Description
                <textarea
                        cols="100" rows="10" name="periodDesc" ></textarea>
        </label>
        <br>
        <hr>
        <button type="submit">Сохранить</button>
        <button type="button" onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="footer.jsp"/>
</body>
</html>