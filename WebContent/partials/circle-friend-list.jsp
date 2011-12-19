<%@ page import="	java.util.List,
					edu.pennphoto.model.User,
					edu.pennphoto.model.Circle,
					edu.pennphoto.db.UserDAO" %>
 
<% 

User user = (User) session.getAttribute("user");
List<Circle> circles = user.getCircles();

%>

<h2>Your Circles</h2>

<% for(Circle circle : circles) { %>

	<div class="circle-list">
	
	<span><%= circle.getName() %></span>
	<ul>
		
		<% List<Integer> friends = circle.getFriendIDs(); %>
		
		<% if(friends != null && friends.size() > 0) { %>
		
			<% for(Integer friend : friends) { %>
				<li> <%= UserDAO.getUserById(friend.intValue()).getEmail() %> </li>	
			<% } %>
			
		<% } else { %>
			<li>Empty Circle</li>
		<% } %>
	</ul>
	
	</div><!-- .circle-list -->
<% } %>