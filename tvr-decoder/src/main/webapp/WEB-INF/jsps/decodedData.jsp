<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<div id="rawData" style="display: none;">
<c:set var="lastNewLine" value="0"/>
Hex Value: <a href="javascript:hideRawData()">Hide</a>
<div>
<c:forEach items="${rawData}" var="b" varStatus="cnt"><c:choose><c:when test="${b.byte}"><c:if test="${(cnt.count-lastNewLine)%40==0}"><br></c:if><span id="b-${b.byteOffset}" class="bytes">${b.value}</span></c:when><c:otherwise>${b.value}<c:set var="lastNewLine" value="${cnt.count}"/></c:otherwise></c:choose></c:forEach>
</div>
</div>
<jsp:include page="recursiveDecodedData.jsp"/>