<%@ page import="	java.util.List,
					edu.pennphoto.model.Circle,
					edu.pennphoto.model.User,
					edu.pennphoto.db.UserDAO" %>

<%
User user = (User) session.getAttribute("user"); 
if(user != null ){ %> 

<div id="submit-photo-wrapper">

	<form action="userServlet" method="post">
	
		<input type="hidden" name="action" value="photo" />
		<fieldset>
			<legend>Image URL</legend>
			<input 	id=url
					value="Image URL" 
					onclick="if (this.value=='Image URL') { this.value='' }" 
					onblur="if (this.value=='') { this.value='Image URL' }"
					onkeydown="if (this.value=='Image URL') { this.value='' }"
					type="url" 
					name="url"
					required />
		</fieldset>
		
		<fieldset>
			<legend>Privacy Settings</legend>
			<ul id="privacy-settings">
				<li>
					<input id=public name=privacy type=radio value="public" checked />
					<label>Public</label>
				</li>
				
				<li>
					<input id=private name=privacy type=radio value="private"/>
					<label>Private</label>
				</li>
			</ul>
			
			<div class="clear"></div>
			
			<fieldset>
				<legend>Visible To Circles?</legend>
				
				<%
				List<Circle> circles = user.getCircles();
				for(Circle circle : circles){
				%>
					<label><%= circle.getName() %></label><input type="checkbox" name="circleIds" value="<%= circle.getCircleID() %>" />
				<% } %>
			</fieldset>
			
			<fieldset>
				<legend>Visible to Friends</legend>	
				
				<%
				List<User> friends = UserDAO.getFriendsList(user.getUserID());
				
				for(User friend : friends){
				%>
					<label><%= friend.getEmail() %></label><input type="checkbox" name="friendIds" value="<%= friend.getUserID() %>" />
				<% } %>
					
			</fieldset>
			
		</fieldset>
		
		<fieldset>
			<legend>Rating</legend>
			<ul id="ratings">
				<% for(int i = 1; i <= 5; i++){ %>
					<li>
						<label><%= i %></label>
						<input name=rating type=radio value=<%=i%> />
					</li>
				
				<% } %>
			</ul>
		</fieldset>
		
		<fieldset>
			<legend>Tags</legend>
			<input name="tag" type="text"/>
			
		</fieldset>
			
		<fieldset>
			<input class="submit" type="submit" value="Submit"/>
		</fieldset>		
		
		
	</form>
	
</div><!-- #submit-photo-wrapper -->


<% } %>

