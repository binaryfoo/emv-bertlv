<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<c:forEach items="${decodedData}" var="item">
	<c:if test="${not item.composite}">
		<div class="decoded" onclick="highlightBytes(${item.startIndex},${item.endIndex})">
	    <span class="rawData">${item.rawData}</span> <span class="decodedData">${item.decodedData}</span>
	    </div>
    </c:if>
	<c:if test="${item.composite}">
			<table class="composite-decoded" onclick="highlightBytes(${item.startIndex},${item.endIndex})">
				<tr>
					<td colspan="2">
		    		<span class="composite-label">${item.rawData}</span> <span class="composite-decodedData">${item.decodedData}</span>
		    		</td>
	    		</tr>
				<tr>
					<td class="indent"></td>
					<td>
		    		<c:set var="decodedData" value="${item.children}" scope="request"/>
					<jsp:include page="recursiveDecodedData.jsp"/>
					</td>
				</tr>
			</table>
    </c:if>
</c:forEach>
