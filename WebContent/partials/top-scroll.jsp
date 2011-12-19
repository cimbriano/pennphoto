<%@ page import="	java.util.List, 
					edu.pennphoto.db.UserDAO, 
					edu.pennphoto.model.User" %>
<%

User user = (User) session.getAttribute("user");
if(user != null){ %>
	
<div id="top-scroll">

	<h3>Friend Recommendations</h3>
	<% List<User> recs = UserDAO.getFriendRecommendations(user.getUserID()); %>
	
	<% if(recs != null){ %>
		<ul id="recs">
		
		<% int count = 0; %>
		<% for(User friendRec : recs) { %>
			<% if(count < 7) { %>
				<li>
				<div class="recomended-friend">
					<span><%= friendRec.getFirstName() %></span> <span><%= friendRec.getLastName() %></span> </br>
					<span><%= friendRec.getEmail() %></span>
				</div>
				</li>
			
				<% count++; %>
			<% } %>
		
			
			
		
		<% }%>
		
		
		</ul>
		
		<div class="clear"></div>
	<% } else { %>
		<p>Sorry, no friends to suggest.</p>
	<% } %>
	
</div><!-- #top-scroll -->
	
	
<% } %>




