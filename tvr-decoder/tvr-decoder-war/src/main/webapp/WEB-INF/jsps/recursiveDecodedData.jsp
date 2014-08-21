<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<c:forEach items="${decodedData}" var="item">
    <c:if test="${item.hexDump != null}">
        <c:set var="data" value="${item.hexDump}" scope="request"/>
        <jsp:include page="rawDataFragment.jsp"/>
    </c:if>
	<c:if test="${not item.composite}">
		<div class="decoded" data-s="${item.startIndex}" data-e="${item.endIndex}" data-i="${rawDataId}">
	    <span class="rawData">${item.rawData}</span> <span class="decodedData">${item.decodedData}</span>
	    </div>
    </c:if>
	<c:if test="${item.composite}">
        <c:set var="itemId" value="${item.startIndex}-${item.endIndex}" scope="request"/>
        <table class="composite-decoded" data-s="${item.startIndex}" data-e="${item.endIndex}" data-i="${rawDataId}">
            <tr>
                <td colspan="2">
                <span class="composite-label">${item.rawData} <span class="glyphicon glyphicon-zoom-out expander" data-item="${itemId}"></span> <span class="composite-chunked collapsed" data-item="${itemId}">${item.decodedData}</span></span>
                </td>
            </tr>
            <tr class="detail expanded" data-item="${itemId}">
                <td class="indent"></td>
                <td>
                <c:set var="decodedData" value="${item.children}" scope="request"/>
                <jsp:include page="recursiveDecodedData.jsp"/>
                </td>
            </tr>
        </table>
    </c:if>
</c:forEach>
