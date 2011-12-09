<jsp:include page="partials/html-head.jsp" />

<div id="login-wrapper">

	<div id="login-content">
	
		<h1>Penn Photo Login!</h1>
		
		<div id="login-info">
				<ul>
					<li>Penn photo is awesome</li>
					<li>Its also really cool</li>
					<li>You'll be the talk of the town</li>
				</ul>
			</div>
	
		<div id="login-form-wrapper">
		
			<form action="userServlet" method="post">
				<input type="hidden" name="action" value="login" />
				<input 	value="Email" 
						onclick="if (this.value=='Email') { this.value='' }" 
						onblur="if (this.value=='') { this.value='Email' }"
						type="text" 
						name="email-login" /> </br>
				
				<input 	value="password" 
						onclick="if (this.value=='password') { this.value='' }" 
						onblur="if (this.value=='') { this.value='password' }"
						type="password" 
						name="pwd" /> </br>
						
				<input class="submit" type="submit" value="Login"/>
			</form>
			
			<div class="clear"></div>
			
			<% 	String error = request.getParameter("error");
				if (error != null) { 
			%>
				<div class="error">
				
					<% if(error.equals("2")) { %> 
						<p>Error: sent here from homepage</p>
					<% } else if(error.equals("bl")) { %>
						<p>Invalid login/password combination</p>
					<% } else if(error.equals("bp")){ %>
						<p>Bag page request</p>
					<% } %>
				</div> <!-- #error-->
				
			<% } %>
		
		</div><!-- #login-form-wrapper -->
		
		<% 	if (session.getAttribute("user") == null) {
				//response.sendRedirect("login.jsp?error=redirectedFromLoginPage");
			} else{	%>
			
			<p>ALERT: USER IS STIL LOGGED IN</p>		
		<% } %>
	</div>

<jsp:include page="partials/footer.jsp" />

</div><!-- #login-wraper -->
