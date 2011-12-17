<%@ page import="	edu.pennphoto.db.UserDAO, 
					edu.pennphoto.model.User,
					edu.pennphoto.model.Professor,
					java.util.Map,
					java.util.Collection" %>


<% 	User user = (User) session.getAttribute("user"); %>
	<p><%= user.getFirstName() %></p>
	<p><%= user.getLastName() %></p>
	<p><%= user.getEmail() %></p>
	<p><%= user.getAddress() %></p>
	<p><%= user.getCity() %></p>
	<p><%= user.getState() %></p>
	<p><%= user.getZip() %></p>
	<p><%= user.getDob() %></p> 
	<% if(user instanceof Professor){ 
			out.println("<p>Currently Advising:</p>");
			Map<Integer, String> students = UserDAO.getStudents((Professor) user);
			for(String name : students.values()){
				out.println(name + "<br/>");	
			}	%>
	
	<% } else { %>
	
	<% } %>
