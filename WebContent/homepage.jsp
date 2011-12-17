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
		} else{	
			request.setAttribute("search-tag", "tag");
		%>
	<p>Successful Login or still logged in</p>		
			
	<div id="content">
	
		<jsp:include page="partials/top-scroll.jsp" />
		<jsp:include page="partials/search-photo-form.jsp" />
		<jsp:include page="partials/top-photos.jsp" />
		<jsp:include page="partials/activity-feed.jsp" />
	
		<div class="clear"></div>

		<jsp:include page="partials/friendship-browser.jsp" />		
		
		<div class="clear"></div>
				
				
	</div><!-- #content -->	
	
	<jsp:include page="partials/footer.jsp" />
	
<% } %> <!-- Closes check if user is null -->

</div><!-- #wrapper -->
	
