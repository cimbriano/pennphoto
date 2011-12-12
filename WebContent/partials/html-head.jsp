<%@ page import="edu.pennphoto.model.User" %>


<!DOCTYPE html>
<html>
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<!-- Stylesheets -->
<link rel="stylesheet" type="text/css" media="all" href="styles.css" />
<link rel="stylesheet" type="text/css" media="all" href="main-nav.css" />

<!-- Google Fonts -->
<link href='http://fonts.googleapis.com/css?family=Astloch|Lancelot|Vast+Shadow|Meddon|Buda:300|Megrim|Monoton' rel='stylesheet' type='text/css'>

<!-- Google Hosted JQuery -->
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.7.0/jquery.min.js" type="text/javascript"></script>

<!-- Raphael js library -->
<script src="ext-lib/raphael-min.js" type="text/javascript"></script>

<!--  Our Behavior JS -->
<script src="js/behavior.js" type="text/javascript"></script>


<title>PennPhoto Group 17 Final Project</title>

</head>

<body>

<% 
	User sessionUser = (User) session.getAttribute("user");
	
%>

<% if (sessionUser != null) {
%> 
	<jsp:include page="top-bar.jsp" />
<%	
}
%>

<div id="wrapper">