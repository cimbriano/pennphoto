<%@ page import="	java.util.List, 
					edu.pennphoto.db.PhotoDAO, 
					edu.pennphoto.model.User,
					edu.pennphoto.model.Photo,
					edu.pennphoto.model.Event,
					edu.pennphoto.web.ResponseCode" %>


<jsp:include page="partials/html-head.jsp" />

<div id="wrapper">
	
	<% 
		User user = (User) session.getAttribute("user");
	
		if (user == null) {
			response.sendRedirect("login.jsp?error=2");
		} else{	
			request.setAttribute("search-tag", "tag");
		%>
	<div id="message">
	<%
		String code = request.getParameter(ResponseCode.PARAMETER_NAME);
		if(code != null){
			out.println(ResponseCode.getMessage(Integer.parseInt(code)));
		}
	%>
	</div>			
	<div id="content">
	
		<jsp:include page="partials/top-scroll.jsp" />
		<jsp:include page="partials/search-photo-form.jsp" />
		<jsp:include page="partials/top-photos.jsp" />
		<jsp:include page="partials/activity-feed.jsp" />
		
		<jsp:include page="partials/submit-photo.jsp" /> </br>	
	
		<div class="clear"></div>


		<h2>Friendship Browser</h2>
		<jsp:include page="partials/friendship-browser.jsp" />		
		
		<div class="clear"></div>
				
				
	</div><!-- #content -->	
	
	<jsp:include page="partials/footer.jsp" />
	
<% } %> <!-- Closes check if user is null -->

</div><!-- #wrapper -->
	
