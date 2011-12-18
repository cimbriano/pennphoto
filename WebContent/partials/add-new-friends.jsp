<%@ page import="	java.util.List,
					edu.pennphoto.model.User,
					edu.pennphoto.model.Circle,
					edu.pennphoto.db.UserDAO" %>
 
<% 

User user = (User) session.getAttribute("user");
List<Circle> circles = user.getCircles();

%>

<div id="add-new-friends">

<form>

	<fieldset>
		<legend>Add New Friends</legend>
		<select>
			<option value="default" disabled="disabled" selected="selected">Select a Circle</option>
		
		<% for(Circle circle : circles){ %>
			<option value="<%= circle.getCircleID() %>"><%= circle.getName() %></option>
		<% } %>
		</select>
		
		
		
		
		<% for(User friendRec : UserDAO.getFriendRecommendations(user.getUserID())) { %>

		
		
			<div class="friend-rec">
			<span><%=friendRec.getFirstName() %>, <%= friendRec.getLastName() %></span>
			<input class="submit" type="submit" value="Add to circle" />
			</div>
		
		<% } %>
		
	</fieldset>

</form>

</div>