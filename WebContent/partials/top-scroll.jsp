<%@ page import="	java.util.List, 
					edu.pennphoto.db.UserDAO, 
					edu.pennphoto.model.User" %>
<%

User user = (User) session.getAttribute("user");
if(user != null){ %>
	
<div id="top-scroll">

	<h3>Friend Reccomendations</h3>
	<% List<User> recs = UserDAO.getFriendsOfFriends(user.getUserID()); %>
	
	<% if(recs != null){ %>
		<ul>
		
		<% for(User friendRec : recs) { %>
			<li>
				<div class="reccomended-friend">
					<span><%= friendRec.getFirstName() %></span> <span><%= friendRec.getLastName() %></span>
				</div>
			</li>
			
		
		<% }%>
		
		</ul>
	<% } else { %>
		<p>Sorry, no friends to suggest.</p>
	<% } %>
	
</div><!-- #top-scroll -->
	
	
<% } %>




