<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<div id="rawData" style="display: none;">
Hex Value: <c:forEach items="${rawData}" var="b" varStatus="counter"><span id="b-${counter.count - 1}" class="bytes">${b}</span></c:forEach>
<a href="javascript:hideRawData()">Hide</a>
</div>
<jsp:include page="recursiveDecodedData.jsp"/>