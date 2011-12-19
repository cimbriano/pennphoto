<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="edu.pennphoto.model.User.Interest"%>
<%@page import="edu.pennphoto.model.User.Attendance"%>
<%@page import="java.util.ArrayList"%>
<%@ page
	import="edu.pennphoto.db.UserDAO,edu.pennphoto.model.User,
	edu.pennphoto.model.Professor,
	edu.pennphoto.model.Student,
	java.util.Map,java.util.Collection"%>

<jsp:include page="partials/html-head.jsp" />

<%
	User user = (User) session.getAttribute("user");

	if (user == null) {
		response.sendRedirect("login.jsp?error=2");
	} else {
		boolean isProfessor = user instanceof Professor;
%>
<div id="wrapper">
	<div id="content">
	<table>
	<tr><td colspan="2" class="profile-field-name"><%= isProfessor?"PROFESSOR":"STUDENT"%></td></tr>
	<tr><td class="profile-field-name">First Name:</td><td><%=user.getFirstName()%></td></tr>
		<tr><td class="profile-field-name">Last Name:</td><td><%=user.getLastName()%></td></tr>
		<tr><td class="profile-field-name">Email:</td><td><%=user.getEmail()%></td></tr>
		<tr><td class="profile-field-name">Address:</td><td><%=user.getAddress()%></td></tr>
		<tr><td class="profile-field-name">City:</td><td><%=user.getCity()%></td></tr>
		<tr><td class="profile-field-name">State:</td><td><%=user.getState()%></td></tr>
		<tr><td class="profile-field-name">Zip:</td><td><%=user.getZip()%></td></tr>
		<tr><td class="profile-field-name">DOB:</td><td><%=user.getDob()%></td></tr>
		<tr><td class="profile-field-name">Gender:</td><td><%=user.getGender().toString()%></td></tr>
		<tr><td class="profile-field-name">Attended:</td><td>
		<%
		 ArrayList<Attendance> attendances = user.getAttendances();
		if(attendances != null){
			for (Attendance attendance : attendances) {
			%>
			<%=attendance.getInstitution() %> <%=attendance.getStartYear() %>-<%=attendance.getEndYear() %>
			<br/>
			<%
			}
			}
			%>
			</td></tr>
			<tr><td class="profile-field-name">Interests:</td><td>
			<%
			
		 ArrayList<Interest> interests = user.getInterests();
		 if(interests != null){
				for (Interest interest : interests) {
				%>
				<%=interest.getLabel() %>
				<br/>
				<%
				}
				}
				%>
	
		</td></tr>
		<%
			if (isProfessor) {
				Professor prof = (Professor) user;
				%>
				<tr><td class="profile-field-name">Title:</td><td><%= prof.getTitle()%></td></tr>
				<tr><td class="profile-field-name">Research Area:</td><td><%= prof.getResearchArea()%></td></tr>
				<tr><td class="profile-field-name">Currently Advising:</td><td>
				<%
					Map<Integer, String> students = UserDAO
							.getStudents(prof);
					for (String name : students.values()) {
						out.println(name + "<br/>");
					}
		%>
</td></tr>
		<%
			} else {
				Student student = (Student)user;
				%>
				
				<tr><td class="profile-field-name">Major:</td><td><%=student.getMajor()%></td></tr>
				<tr><td class="profile-field-name">GPA:</td><td><%= student.getGpa()%></td></tr>
				<tr><td class="profile-field-name">Advisor:</td><td>
				<%
				int advisorId = student.getAdvisorId();
				User advisor = UserDAO.getUserById(advisorId);
				if(advisor != null){
		 %>
				<%=advisor.getFirstName()%> <%=advisor.getLastName()%>
		<%
				}
				%>
				</td>
				<%
			}
		%>
		</table>
	</div>
	<!-- #content -->
	<jsp:include page="partials/footer.jsp" />
</div>
<%
	}
%>

