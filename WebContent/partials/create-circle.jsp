<div id="create-circle-wrapper">

	<form action="userServlet" method="post">
	
		<input type="hidden" name="action" value="create-circle" />
			
		<fieldset>
			<legend>Add a Circle</legend>
			<input 	id=circle-name
					value="Circle Name" 
					onclick="if (this.value=='Circle Name') { this.value='' }" 
					onblur="if (this.value=='') { this.value='Circle Name' }"
					type="text" 
					name="circle-name"
					required />
					
			<input class="submit" type="submit" value="Submit"/>
		</fieldset>
			
	</form>
	
</div>