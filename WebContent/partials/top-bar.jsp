<%@ page import="edu.pennphoto.model.User" %>

<div id="top-bar">

<div id="user-actions">
	<ul>
		<li><a href="userServlet/profile">
		
		<% 
			User user = (User) session.getAttribute("user");
			if (user != null) { 
				
		%> 
			<%= user.getEmail() %>
			
		<%
			}
		%>
		
		</a></li>
		<li><a href="userServlet/logout">Logout</a></li>
	</ul>
</div>

</div><!-- #top-bar -->