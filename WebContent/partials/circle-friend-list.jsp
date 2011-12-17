<%@ page import="	java.util.List,
					edu.pennphoto.model.User,
					edu.pennphoto.model.Circle" %>
 
<% 

User user = (User) session.getAttribute("user");
List<Circle> circles = user.getCircles();

%>


<% for(Circle circle : circles) { %>
	<p><%= circle.getName() %></p>
	<ul>
		<% for(int id : circle.getFriendIDs()) { %>
			
			<li><%= id %></li>
			
		<% } %>
	</ul>
<% } %>