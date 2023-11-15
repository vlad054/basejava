<%@ page import="ru.javawebinar.basejava.model.Resume" %>
<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: mva
  Date: 13.11.2023
  Time: 12:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <table border="1" cellpadding="8" cellspacing="0">
        <tr>
            <th>Имя</th>
            <th>Email</th>
        </tr>
        <%
            for (Resume resume : (List<Resume>) request.getAttribute("resumes")) {
        %>
        <tr>
            <td><a href="resume?uuid=<%=resume.getUuid()%>"><%=resume.getFullName()%></a>
            </td>
            <td><%=resume.getContacts().get(ContactType.MAIL)%>
            </td>
        </tr>
        <%
            }
        %>
    </table>
</section>
</body>
</html>
