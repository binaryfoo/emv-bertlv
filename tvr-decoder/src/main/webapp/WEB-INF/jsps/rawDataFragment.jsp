<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<c:set var="rawDataId" value="${rawDataId+1}" scope="request"/>
<div id="rawData-${rawDataId}" style="display: none;">
<c:set var="lastNewLine" value="0"/>
Hex <a href="javascript:hideRawData(${rawDataId})" style="font-size: small">(Hide)</a>:
<c:forEach items="${data}" var="b" varStatus="cnt"><c:choose><c:when test="${b.byte}"><c:if test="${(cnt.count-lastNewLine)%60==0}"><br></c:if><span id="b-${b.byteOffset}" class="bytes">${b.value}</span></c:when><c:otherwise>${b.value}<c:set var="lastNewLine" value="${cnt.count}"/></c:otherwise></c:choose></c:forEach>
</div>