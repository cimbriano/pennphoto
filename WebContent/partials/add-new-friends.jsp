<%@ page import="	java.util.List,
					edu.pennphoto.model.User,
					edu.pennphoto.model.Circle,
					edu.pennphoto.db.UserDAO" %>
 
<% 

User user = (User) session.getAttribute("user");
List<Circle> circles = user.getCircles();

%>

<div id="add-new-friends">

<form id="add-friend-form" action="userServlet" method="post">

	<input type="hidden" name="action" value="add-friend" />
	<input id="add-friend-circleID" type="hidden" name="circleID" value="circleID" />
	<input id="add-friend-friendID" type="hidden" name="friendID" value="friendID" />

	<fieldset>
		<legend>Add New Friends</legend>
		<select id="circles">
			<option value="default">Select a Circle</option>
		
		<% for(Circle circle : circles){ %>
			<option value="<%= circle.getCircleID() %>"><%= circle.getName() %></option>
		<% } %>
		</select>
		
		
		
		
		<% for(User friendRec : UserDAO.getFriendRecommendations(user.getUserID())) { %>		
			<div class="friend-rec">
			<span><%=friendRec.getEmail() %></span>
			<button class="add-friend-button" type="button" friend="<%= friendRec.getUserID() %>">Add to Circle</button>
			</div>
		<% } %>
		
	</fieldset>

</form>

</div>