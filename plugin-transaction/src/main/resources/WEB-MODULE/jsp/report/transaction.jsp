<%@ page contentType="text/html; charset=utf-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="a" uri="http://www.unidal.org/cat/layout" %>
<jsp:useBean id="ctx" type="org.unidal.cat.transaction.report.page.Context" scope="request"/>
<jsp:useBean id="payload" type="org.unidal.cat.transaction.report.page.Payload" scope="request"/>
<jsp:useBean id="model" type="org.unidal.cat.transaction.report.page.Model" scope="request"/>

<a:layout>
   View of transaction page under report, op=${payload.action}, domain=${payload.domain}
   <br> <a href="?op=hour&${nav}">link</a>
</a:layout>