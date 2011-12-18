<%@ page import="	java.util.List, 
					edu.pennphoto.db.UserDAO, 
					edu.pennphoto.model.User" %>
<%

User user = (User) session.getAttribute("user");
if(user != null){ %>
	
<div id="top-scroll">

	<h3>Friend Recommendations</h3>
	<% List<User> recs = UserDAO.getFriendsOfFriends(user.getUserID()); %>
	
	<% if(recs != null){ %>
		<ul id="recs">
		
		<% for(User friendRec : recs) { %>
			<li>
				<div class="recomended-friend">
					<span><%= friendRec.getFirstName() %></span> <span><%= friendRec.getLastName() %></span>
				</div>
			</li>
			
		
		<% }%>
		
		
		</ul>
		
		<div class="clear"></div>
	<% } else { %>
		<p>Sorry, no friends to suggest.</p>
	<% } %>
	
</div><!-- #top-scroll -->
	
	
<% } %>




