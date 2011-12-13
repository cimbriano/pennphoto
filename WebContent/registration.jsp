<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ page import="java.util.Map, edu.pennphoto.db.UserDAO, edu.pennphoto.model.User" %>
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
				<input type="text" name="first-name" placeholder="First Name"/><br/>
				<input type="text" name="last-name" placeholder="Last Name"/><br/>
				<input type="text" name="dob" placeholder="Date of Birth"/><br/>
				<input type="text" name="email" placeholder="Email"/><br/>
				<input type="password" name="password" placeholder="Password"/><br/>
				<select name="gender">
					<option value="M">Male</option>
					<option value="F">Female</option>
				</select><br/>
				<select id="is-professor" name="is-professor" onchange="display_role();">
					<option value="T">Professor</option>
					<option value="F">Student</option>
				</select><br/>
				
				<div id="professor-fields" class="hidden">
					<input type="text" name="research-area" placeholder="Research Area"/><br/>
					<input type="text" name="title" placeholder="Title"/><br/>
				</div>
				
				<div id="student-fields" class="hidden">
					<input type="text" name="major" placeholder="Major"/><br/>
					<input type="text" name="gpa" placeholder="GPA"/><br/>
					<select id="advisor" name="advisor">
						<% 	Map<Integer, String> advisors = UserDAO.getProfessors();
							for (Map.Entry<Integer, String> advisor : advisors.entrySet()) { %>
								<option value=<%= advisor.getKey() %>><%= advisor.getValue() %></option>
						<% 	} %>
					</select><br/>
				</div>

				<input type="text" name="street-address" placeholder="Address"/><br/>
				<input type="text" name="city" placeholder="City"/><br/>
				<select id="state" name="state">
						<% 	Map<Integer, String> states = UserDAO.getStates();
							for (Map.Entry<Integer, String> state : states.entrySet()) { %>
								<option value=<%= state.getKey() %>><%= state.getValue() %></option>
						<% 	} %>
				</select><br/>
				<input type="text" name="zip-code" placeholder="Zip Code"/><br/>
				
				<select id="institution-1" name="institution-1" onchange="display_other_inst(1);">
					<% 	Map<Integer, String> institutions = UserDAO.getInstitutions();
						for (Map.Entry<Integer, String> institution : institutions.entrySet()) { %>
							<option value=<%= institution.getKey() %>><%= institution.getValue() %></option>
					<% 	} %>
					<option value="0">Other</option>
				</select><br/>
				<input id=other-institution-1 name="other-institution-1" class="hidden" placeholder="Other Institution"/>
				<input id="institution-1-from-year" name="institution-1-from-year" placeholder="From Year"/>
				<input id="institution-1-to-year" name="institution-1-to-year" placeholder="To Year"/>
				<br/>
				
				<input id="interest-1" name="interest-1" placeholder="Interest"/>
				<br/>
				
				<input type="submit" value="Sign Up"/>

				<% if (request.getParameter("error") != null) { 
					String message = request.getParameter("message");
					if (message == null) message = "Please check your information and try again.";%>
					<div class="error"><%= message %></div>
				<% } %>
			</form>
		</div>

		<jsp:include page="partials/footer.jsp" />		
		
	</div><!--  #wrapper -->

	<script type="text/javascript">
		function load() {
			display_role();
			mark_error_field();
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
		
		function display_other_inst(num) {
			var e = document.getElementById("institution-" + num);
			var institution = e.options[e.selectedIndex].value;
			if (institution === "0") {
				document.getElementById("other-institution-" + num).style.display = "block";			
			} else {
				document.getElementById("other-institution-" + num).style.display = "none";			
			}
		}
		
		function mark_error_field() {
			var error = <%= request.getParameter("error") %>;
			var field = "<%= request.getParameter("field") %>";
			
			if ( error && field) {
				document.getElementsByName(field)[0].className += " error-field";
			}
		}
	</script>

</body>
</html>