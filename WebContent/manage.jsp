<%@ page import=" 	edu.pennphoto.model.User,
					edu.pennphoto.db.UserDAO" %>

<jsp:include page="partials/html-head.jsp" />

<div id="wrapper">	
	
	<% 
		User user = (User) session.getAttribute("user");
	
		if (user == null) {
			response.sendRedirect("login.jsp?error=2");
		}else{	
	%>
		<p>Successful Login or still logged in</p>		
	<% } %>


	<jsp:include page="partials/top-scroll.jsp" />
	
	<div id="content">
		
		<jsp:include page="partials/create-circle.jsp" /> </br>

		<jsp:include page="partials/add-new-friends.jsp" /> </br>	
		
		
		<div id="all-friends">
			<jsp:include page="partials/circle-friend-list.jsp" /> </br>	
		</div>
		
	</div><!-- #content -->
	
	<jsp:include page="partials/footer.jsp" />

</div> <!-- #wrapper -->