<%@ page import="	java.util.List, 
					edu.pennphoto.db.PhotoDAO, 
					edu.pennphoto.model.User,
					edu.pennphoto.model.Photo" %>


<jsp:include page="partials/html-head.jsp" />

	
	<% 
		User user = (User) session.getAttribute("user");
	
		if (user == null) {
			response.sendRedirect("login.jsp?error=2");
		}else{	
			
			
	%>
		<p>Successful Login or still logged in</p>		
		
		<jsp:include page="partials/top-scroll.jsp" />
	
	<div id="content">
	
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
		
		
		
		<div id="activity-feed"></div>
		
		<div id="friendship-browser"></div>
		
	</div><!-- #content -->
	
	<jsp:include page="partials/footer.jsp" />
		
		
		
<% } %> <!-- Closes check if user is null -->


	
