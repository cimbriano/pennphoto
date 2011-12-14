<%@ page import="edu.pennphoto.model.User" %>

<%

User user = (User) session.getAttribute("user");

if(user != null){%>
<div id="search-photo">
<form action="" method="post">
	Search by tag: <input type="text" name="search-tag" value="<%=request.getParameter("search-tag") != null?request.getParameter("search-tag"):"" %>" /> 
	<input type="submit" value="Search"/>
</form>
	<div class="clear"></div>
		
</div><!-- search-photo -->


<% } %>