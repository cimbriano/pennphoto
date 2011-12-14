<%@ page import="	edu.pennphoto.db.UserDAO, 
					edu.pennphoto.model.User" %>


<% 	User user = (User) session.getAttribute("user"); %>
	<p><%= user.getFirstName() %></p>
	<p><%= user.getLastName() %></p>
	<p><%= user.getEmail() %></p>
	<p><%= user.getAddress() %></p>
	<p><%= user.getCity() %></p>
	<p><%= user.getState() %></p>
	<p><%= user.getZip() %></p>
	<p><%= user.getDob() %></p> 
