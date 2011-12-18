<jsp:include page="partials/html-head.jsp" />

	<jsp:include page="partials/header.jsp" />
	
	<% if (session.getAttribute("user") == null) {
		response.sendRedirect("login.jsp?error=2");
		}else{	
	%>
		<p>Successful Login or still logged in</p>		
	<% } %>


	<jsp:include page="partials/top-scroll.jsp" />
	
	<div id="content">
		<%--<jsp:include page="partials/friendship-browser.jsp" /> --%>
		<jsp:include page="partials/add-friend-to-circle.jsp" />
		<jsp:include page="partials/submit-photo.jsp" />
		<jsp:include page="partials/create-circle.jsp" />
	</div><!-- #content -->
	
	<jsp:include page="partials/footer.jsp" />