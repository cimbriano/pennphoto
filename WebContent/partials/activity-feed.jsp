<%@ page import="	java.util.List, 
					edu.pennphoto.db.PhotoDAO, 
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
			<!-- To do, get user name from this -->
			<span><%= event.getUserId() %></span>
			
			<% 
				if(event.getType().equals(Event.EventType.PHOTO)){
					//Photo stuff
			%>
				<span> submitted a photo from </span>
				<span><%= event.getEventValue() %></span>		
					
					
			<%
				} else if(event.getType().equals(Event.EventType.TAG)){
					//Tag stuff
					
			%>
				<span>tagged photo <%= event.getPhotoId() %></span>
				<span>with <%= event.getEventValue() %></span>
			<% } %>

		</div><!-- #activity-feed-item -->
		
		
			
		<% } //close events loop %>

	
	<% } //Close else %>			
	
</div><!-- #activity-feed -->
	
<% } %>


