<!DOCTYPE HTML>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<html>
<head>
	<title>TVR Decoder</title>
	<meta name="google-site-verification" content="slrW544StiiGPgULPcN6D3hvx-BCNi8ch9PXDwCceJk" />
	<script src="/jquery-1.3.2.min.js" type="text/javascript"></script>
    <script type="text/javascript">
	    function doDecode() {
	        $("#display").slideUp('slow',function(){
                $("#display").html("Loading...").show();
		        $.post('/t/decode', {tag: $('#tag_field').val(), value: $('#value_field').val()},function(data) {
					$("#display").html(data).slideDown('slow',function() {
						$(".decoded,.composite-decoded").each(function() {
			    			$(this).click(function(e) {
								e.stopPropagation();
							});
				    	});
			    	});
			    	toggleCompositeDecodedVisibility();
		    	});
            });
	    }
	
	    function onOptionChange() {
	        var value_field = $('#value_field')
	        var options = $('#tag_field').attr("options");
	        var selectedIndex = $('#tag_field').attr("selectedIndex");
	        var maxLength = options[selectedIndex].getAttribute("data-maxlength");
	        value_field.attr("maxLength", maxLength);
	        if (value_field.val().length > maxLength) {
	            value_field.val(value_field.val().substr(0, maxLength))
	        }
	    }
	
		function highlightBytes(start, end) {
			$("#rawData").show("slow");
			$(".bytes").removeClass("highlight");
			var i = 0;
			for (i = start; i < end; i++) { 
				$("#b-" + i).addClass("highlight");
			}
		}

		function hideRawData() {
			$("#rawData").hide("slow");
		}

		function toggleCompositeDecodedVisibility() {
		    var checked = $('#showCompositeDecodedData').is(':checked');
		    $(".composite-decodedData").toggle(checked);
        }

    </script>
    <link rel="stylesheet" type="text/css" href="/tvr.css" />
</head>
<body onload="onOptionChange()">
    <div id="container"><div id="content">
    <form onsubmit="doDecode();return false">
    <select id="tag_field" onchange="onOptionChange()">
    <c:forEach items="${tagInfos}" var="tagInfo">
        <option value="${tagInfo.tag}" data-maxlength="${tagInfo.maxLength}">${tagInfo.shortName}</option>
    </c:forEach>
    </select>
    <input type="text" id="value_field"/>
    <input type="submit" value="Decode"/>
    <input type="checkbox" id="showCompositeDecodedData" value="checked" onclick="toggleCompositeDecodedVisibility()"/><label for="showCompositeDecodedData" style="font-size:small">Show Raw Data for composite tags</label>
    </form>
    <div id="display">
    </div>
    </div>
    <div id="footer">
    	<p><a href="http://code.google.com/p/tvr-decoder/">Project</a></p>
    </div>
    </div>
    <script type="text/javascript">
    var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
    document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
    </script>
    <script type="text/javascript">
    try {
    var pageTracker = _gat._getTracker("UA-7717677-1");
    pageTracker._trackPageview();
    } catch(err) {}</script>
</body>
