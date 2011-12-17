<%@ page import="	java.util.List, 
					edu.pennphoto.db.PhotoDAO,
					edu.pennphoto.db.UserDAO, 
					edu.pennphoto.model.User,
					edu.pennphoto.model.Photo,
					edu.pennphoto.model.Event" %>

<% 
User user = (User) session.getAttribute("user");

if (user != null) { %>
	
	<div id="activity-feed">
<% 
List<Event> events = PhotoDAO.getRecentEvents(user.getUserID());
if(events.size() == 0){ %> 
		<p> Sorry, no recent events to show here. </p>
		
<% } else {
		//no events		
	for(Event event : events){ %> 
		
		<div class="activity-feed-item">			
			<% Photo photo = PhotoDAO.getPhotoById(event.getPhotoId());
			   User actor = UserDAO.getUserById(event.getUserId());
			%>
				<img src="<%= photo.getUrl() %>" />
				<span><%= actor.getFirstName() %>&nbsp;<%= actor.getLastName() %></span>
			
			<% if(event.getType().equals(Event.EventType.PHOTO)){
					//Photo stuff
			%>
				<span> added a new photo </span>			
					
			<%
				} else if(event.getType().equals(Event.EventType.TAG)){
					//Tag stuff
					
			%>
				<span>tagged photo with <%= event.getEventValue() %></span>
			<% } %>

		</div><!-- #activity-feed-item -->
		
		
			
		<% } //close events loop %>

	
	<% } //Close else %>			
	
</div><!-- #activity-feed -->
	
<% } %>


