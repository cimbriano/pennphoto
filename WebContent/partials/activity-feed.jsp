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
	<h3>Recent Updates</h3>
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
			<div class="img-wrap">
				<img src="<%= photo.getUrl() %>" alt="Photo Submitted by user <%= photo.getOwnerId() %>"/>
			</div> <!--  img-wrap -->
			<div class="activity-desc">
			<%= actor.getFirstName() %>&nbsp;<%= actor.getLastName() %>
			
			<% if(event.getType().equals(Event.EventType.PHOTO)){
					//Photo stuff
			%>
				 posted a new photo		
					
			<%
				} else if(event.getType().equals(Event.EventType.TAG)){
					//Tag stuff
					
			%>
				added the tag "<%= event.getEventValue() %>"
			<% } %>
			</div><!-- activity-desc -->
		</div><!-- #activity-feed-item -->
		
		
			
		<% } //close events loop %>

	
	<% } //Close else %>			
	
</div><!-- #activity-feed -->
	
<% } %>


