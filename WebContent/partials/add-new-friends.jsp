<%@ page import="	java.util.List,
					edu.pennphoto.model.User,
					edu.pennphoto.model.Circle,
					edu.pennphoto.db.UserDAO" %>
 
<% 

User user = (User) session.getAttribute("user");
List<Circle> circles = user.getCircles();

%>

<div id="add-new-friends">

<fieldset>
	<legend>Add Friends to Circles</legend>
	
	<form id="add-friend-form" action="userServlet" method="post">
	<input type="hidden" name="action" value="add-friend" />
	<input id="add-friend-circleID" type="hidden" name="circleID" value="circleID" />
	<input id="add-friend-friendID" type="hidden" name="friendID" value="friendID" />
	
	<fieldset>
		<legend>Which Circle? </legend>
		
		<select id="circles">
			<option value="default">Select a Circle</option>
		
		<% for(Circle circle : circles){ %>
			<option value="<%= circle.getCircleID() %>"><%= circle.getName() %></option>
		<% } %>
		</select>
	</fieldset>

	<fieldset>
		<legend>Pick A Suggested Friend</legend>
		
		<% for(User friendRec : UserDAO.getFriendRecommendations(user.getUserID())) { %>		
			<div class="friend-rec">
			<span><%=friendRec.getEmail() %></span>
			<button class="add-friend-button" type="button" friend="<%= friendRec.getUserID() %>">Add to Circle</button>
			</div>
		<% } %>
			
	</fieldset>
	
	<fieldset>
		<legend>Add User By Email</legend>
		
		<input id="friend-by-email" type="text" value="Friend to Add"/>
		<button id="add-friend-button-by-email" type="button">Add to Circle</button>
	</fieldset>

</form>
	
	
</fieldset>



</div>