<%@ page import="	java.util.List,
					edu.pennphoto.model.User,
					edu.pennphoto.model.Circle" %>
 
<% 

User user = (User) session.getAttribute("user");
List<Circle> circles = user.getCircles();

%>
 
<form action="userServlet" method="post">
	<input type="hidden" name="action" value="add-friend" />
	
	<fieldset>
		<legend>Put Friend In</legend>
		<select name="circleID">
	<%
		
		if(circles != null && circles.size() > 0){
			for(Circle circle : circles) {
				%> 
				<option value="<%= circle.getCircleID()  %>"><%= circle.getName() %></option>
				<%
			}
			
			
			
		} else { %> 
			<option value="none">No Circles: Make one first></option>
	<% } %>
		
	</select>
		
		
	</fieldset>
	
	<fieldset>
		<legend>Friend To Add</legend>
		
		<input 	name="friendID"
			value="Friend ID" 
			onclick="if (this.value=='Friend ID') { this.value='' }" 
			onblur="if (this.value=='') { this.value='Friend ID' }"
			onkeydown="if (this.value=='Friend ID') { this.value='' }" />	
	</fieldset>
	
	
	<fieldset>
		<input class="submit" type="submit" value="Submit"/>
	</fieldset>
	
</form>