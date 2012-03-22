<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<c:set var="rawDataId" value="0" scope="request"/>
<c:if test="${!empty rawData}">
    <c:set var="data" value="${rawData}" scope="request"/>
    <jsp:include page="rawDataFragment.jsp"/>
</c:if>
<jsp:include page="recursiveDecodedData.jsp"/>