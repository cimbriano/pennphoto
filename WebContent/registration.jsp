<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">

<!-- Stylesheets -->
<link rel="stylesheet" type="text/css" media="all" href="styles.css" />
<link rel="stylesheet" type="text/css" media="all" href="main-nav.css" />

<title>PennPhoto Registration</title>

</head>

<body onload="load();">
	<div id="wrapper">

		<div id="header">
			<div id="logo"></div>
			<div class="clear"></div>
		</div><!-- #header -->

		<div id="content">
			<form action="userServlet" method="post">
				<input type="hidden" name="action" value="register" /><br/>
				<input type="text" name="first-name" value="First Name"/><br/>
				<input type="text" name="last-name" value="Last Name"/><br/>
				<input type="text" name="dob" value="Date of Birth"/><br/>
				<input type="text" name="email-login" value="Email"/><br/>
				<input type="password" name="pwd" value="Password"/><br/>
				<select name="gender">
					<option value="M">Male</option>
					<option value="F">Female</option>
				</select><br/>
				<select id="is-professor" name="is-professor" onchange="display_role();">
					<option value="T">Professor</option>
					<option value="F">Student</option>
				</select><br/>
				
				<div id="professor-fields" class="hidden">
					<input type="text" name="research-area" value="Research Area"/><br/>
					<input type="text" name="title" value="Title"/><br/>
				</div>
				
				<div id="student-fields" class="hidden">
					<input type="text" name="major" value="Major"/><br/>
					<input type="text" name="gpa" value="GPA"/><br/>
				</div>

				<input type="text" name="street-address" value="Address"/><br/>
				<input type="text" name="city" value="City"/><br/>
				<input type="text" name="state" value="State"/><br/>
				<input type="text" name="zipcode" value="Zip Code"/><br/>
				
				
				<input type="submit" value="Sign Up"/>

				<% if (request.getParameter("error") != null) { %>
					<div class="error">Please check your information and try again.</div>
				<% } %>
			</form>
		</div>

		<jsp:include page="partials/footer.jsp" />		
		
	</div><!--  #wrapper -->

	<script type="text/javascript">
		function load() {
			display_role();
		}
	
		function display_role() {
			var e = document.getElementById("is-professor");
			var isProf = e.options[e.selectedIndex].value;
			if (isProf == "T") {
				document.getElementById("professor-fields").style.display = "block";			
				document.getElementById("student-fields").style.display = "none";			
			} else if (isProf == "F") {
				document.getElementById("professor-fields").style.display = "none";			
				document.getElementById("student-fields").style.display = "block";
			}
		}
	</script>

</body>
</html>