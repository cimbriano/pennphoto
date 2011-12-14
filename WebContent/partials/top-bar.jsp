<%@ page import="edu.pennphoto.model.User" %>

<div id="top-bar">

<a href="homepage.jsp">Home</a>

<div id="user-actions">
	<ul>
	
		<% 
			User user = (User) session.getAttribute("user");
			if (user != null) { 
				
		%> 
			<li><a href="userServlet/profile"> <%= user.getEmail() %> </a></li>
			<li><a href="userServlet/logout?userID=<%= user.getUserID() %>">Logout</a></li>
			
		<% } %>
		
		
	</ul>
</div>

</div><!-- #top-bar -->