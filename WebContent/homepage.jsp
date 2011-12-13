<%@ page import="	java.util.List, 
					edu.pennphoto.db.PhotoDAO, 
					edu.pennphoto.model.User,
					edu.pennphoto.model.Photo,
					edu.pennphoto.model.Event" %>


<jsp:include page="partials/html-head.jsp" />

<div id="wrapper">

	
	<% 
		User user = (User) session.getAttribute("user");
	
		if (user == null) {
			response.sendRedirect("login.jsp?error=2");
		}else{	
			
			
	%>
		<p>Successful Login or still logged in</p>		
		
		<jsp:include page="partials/top-scroll.jsp" />
	
	<div id="content">
	
		<!-- TODO  - put this is a partial? Need the user object -->
		<div id="top-pics">
		
		<% List<Photo> photos = PhotoDAO.getTopPhotosForUser(user.getUserID());
		
			for (Photo photo : photos) {
				
				int owner = photo.getOwnerId();
				
		%>
		
			<div class="top-pics-photo">
				<div class="img-wrap">
					<img src="<%= photo.getUrl() %>" alt="Photo Submitted by user <%= owner %>"/>
				</div>
				
				<div class="photo-meta">
					<!-- TODO Owner info here? or on a tool tip? -->
				</div>
			</div>
		
		<%
				
		}
		
		%>
			<div class="clear"></div>
		
		</div>
		
		
		
		<div id="activity-feed">
			<% 
			
			List<Event> events = PhotoDAO.getRecentEvents(user.getUserID());
			if(events.size() == 0){
				%> 
				<p> Sorry, no recent events to show here. </p>
				
				<%
				
			} else {
				//no events
 				
				for(Event event : events){
					
					%> 
					
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
				
		<div id="friendship-browser"></div>
		
	</div><!-- #content -->
	
	<jsp:include page="partials/footer.jsp" />
		
		
		
<% } %> <!-- Closes check if user is null -->

</div><!-- #wrapper -->
	
