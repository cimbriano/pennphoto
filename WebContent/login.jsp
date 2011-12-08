<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">

<!-- Stylesheets -->
<link rel="stylesheet" type="text/css" media="all" href="styles.css" />
<link rel="stylesheet" type="text/css" media="all" href="main-nav.css" />

<title>Welcome to PennPhoto</title>

</head>
<body>
	<div id="wrapper">

		<div id="header">
			<div id="logo"></div>
			<div class="clear"></div>
		</div><!-- #header -->

		<div id="content">
			<form action="userServlet" method="post">
				<input type="hidden" name="action" value="login" />
				<label>Email</label> <input type="text" name="email-login" />
				<label>Password</label> <input type="password" name="pwd" />
				<input type="submit" value="Login"/>

				<% if (request.getParameter("error") != null) { %>
					<div class="error">Please check your information and try again.</div>
				<% } %>
			</form>
		</div>

		<jsp:include page="partials/footer.jsp" />		
		
	</div><!--  #wrapper -->
</body>
</html>