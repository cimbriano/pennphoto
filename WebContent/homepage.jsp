<%@ page import="	java.util.List, 
					edu.pennphoto.db.PhotoDAO, 
					edu.pennphoto.model.User,
					edu.pennphoto.model.Photo,
					edu.pennphoto.model.Event" %>


<jsp:include page="partials/html-head.jsp" />

<div id="wrapper">

<a href="testpage.jsp" style="fontsize: 40px ">GO TO  TESTPAGE</a>
	
	<% 
		User user = (User) session.getAttribute("user");
	
		if (user == null) {
			response.sendRedirect("login.jsp?error=2");
		}else{	
			
			
	%>
	<p>Successful Login or still logged in</p>		
			
	<div id="content">
	
		<jsp:include page="partials/top-photos.jsp" />
		
	
		
		
		
		
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
		
				<div class="clear"></div>
		
				
		<jsp:include page="partials/friendship-browser.jsp" />				
		
		
	</div><!-- #content -->	
	
	<jsp:include page="partials/footer.jsp" />
		
		
		
<% } %> <!-- Closes check if user is null -->

</div><!-- #wrapper -->
	
