<%@ page import="java.util.Map, edu.pennphoto.db.UserDAO, edu.pennphoto.model.User" %>

<jsp:include page="partials/html-head.jsp" />

<div id="login-wrapper">

<script type="text/javascript">
var instAreaNum = 1;
var interstAreaNum = 1;
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
		function get_institution_options(num){
			var result = '<select id="institution-'+num+'" name="institution-'+num+'" onchange="display_other_inst('+num+');">';
			<% 	Map<Integer, String> institutions = UserDAO.getInstitutions();
				for (Map.Entry<Integer, String> institution : institutions.entrySet()) { %>
				result += '<option value="<%= institution.getKey() %>"><%= institution.getValue() %></option>';
			<% 	} %>
			result += '<option value="0">Other</option>';
			result += '</select>';
			return result;
		}
		function addInstitutionArea(){
			var maxNum = 3;
			if(instAreaNum < maxNum){
				instAreaNum++;
				var newdiv = document.createElement('div');
				newdiv.innerHTML = getInstitutionArea(instAreaNum);
				document.getElementById("institutionsArea").appendChild(newdiv);
				if(instAreaNum == maxNum){
					document.getElementById("addInstAttended").style.display = "none";
				}
			}
		}
		function getInstitutionArea(num){
			var instArea = '<div class="inst-attended"> <div> Institution '+ num +'.</div>' + get_institution_options(num);
			instArea += '<input id=other-institution-'+num+' name="other-institution-'+num+'" class="hidden" placeholder="Other Institution"/>';
			instArea += '<input id="institution-'+num+'-from-year" name="institution-'+num+'-from-year" placeholder="From Year"/>';
			instArea += '<input id="institution-'+num+'-to-year" name="institution-'+num+'-to-year" placeholder="To Year"/>';
			instArea += '</div>';
			return instArea;
		}
		function getInterestArea(num){
			return '<div class="inst-attended"><div> '+ num +'.</div><input id="interest-'+num+'" name="interest-'+num+'" placeholder="Interest"/></div>';
		}
		function addInterestArea(){
			var maxNum = 3;
			if(interstAreaNum < maxNum){
				interstAreaNum++;
				var newdiv = document.createElement('div');
				newdiv.innerHTML = getInterestArea(interstAreaNum);
				document.getElementById("interestsArea").appendChild(newdiv);
				if(interstAreaNum == maxNum){
					document.getElementById("addInterest").style.display = "none";
				}
			}
		}
	</script>

	<h1>Registration</h1>

		<div id="registration-form-wrapper">	
		
			<form action="userServlet" method="post">
			<% if (request.getParameter("error") != null) { 
					String message = request.getParameter("message");
					if (message == null) message = "Please check your information and try again.";%>
					<div class="error"><%= message %></div>
				<% } %>
				
				<input type="hidden" name="action" value="register" /><br/>
				<input type="text" name="first-name" placeholder="First Name"/><br/>
				<input type="text" name="last-name" placeholder="Last Name"/><br/>
				<input type="text" name="dob" placeholder="Date of Birth: MM/DD/YYYY"/><br/>
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
							if(states != null){
								for (Map.Entry<Integer, String> state : states.entrySet()) { %>
								
								
								<option value=<%= (state != null) ? state.getKey() : " " %> > <%= state.getValue() %></option>
							<% 	} %>
								
						<% } %>
							
				</select><br/>
				<input type="text" name="zip-code" placeholder="Zip Code"/><br/>
				<div id="institutionsArea">
				<div>Institutions Attended:</div>
				<%--<select id="institution-1" name="institution-1" onchange="display_other_inst(1);">
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
				--%>
				<script>
					document.write(getInstitutionArea(instAreaNum));
				</script>
				</div>
				<input type="button" onclick="addInstitutionArea()" id="addInstAttended" value="Add Institution Attended" style="width:200px"/><br>
				<div id="interestsArea">
				<div>Interests:</div>
				<script>
					document.write(getInterestArea(interstAreaNum));
				</script>
					<%--
						<input id="interest-1" name="interest-1" placeholder="Interest"/>
					 --%>
				</div>
				<input type="button" onclick="addInterestArea()" id="addInterest" value="Add Interest" style="width:200px"/><br>
				<br/>
				
				<input class="submit" type="submit" value="Sign Up"/>
			</form>
		
		
		
		</div><!-- #login-form-wrapper -->

	<jsp:include page="partials/footer.jsp" />

</div><!-- #login-wrapper -->

