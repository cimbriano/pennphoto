<%@ page import=" edu.pennphoto.etl.Importer" %>

<jsp:include page="partials/html-head.jsp" />


	
	<div id="content">

		<div id="all-friends"><% Importer.main(null); %></div>
		
	</div><!-- #content -->
	
	<jsp:include page="partials/footer.jsp" />