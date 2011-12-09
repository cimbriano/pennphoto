<div id="create-circle-wrapper">

	<form action="userServlet" method="post">
	
		<input type="hidden" name="action" value="create-circle" />
		
		
		
		
		<fieldset>
			<legend>Image URL</legend>
			<input 	id=url
					value="Image URL" 
					onclick="if (this.value=='Image URL') { this.value='' }" 
					onblur="if (this.value=='') { this.value='Image URL' }"
					type="url" 
					name="url"
					required />
		</fieldset>
		
		<fieldset>
			<legend>Privacy Settings</legend>
			<ul id="privacy-settings">
				<li>
					<input id=public name=privacy type=radio />
					<label>Public</label>
				</li>
				
				<li>
					<input id=private name=privacy type=radio />
					<label>Private</label>
				</li>
			</ul>
			
			<div class="clear"></div>
			
			<fieldset>
				<legend>Visible To Circles?</legend>
			</fieldset>
			
			<fieldset>
				<legend>Visible to Friends</legend>		
			</fieldset>
			
		</fieldset>
		
		<fieldset>
			<legend>Rating</legend>
			<ul id="ratings">
				<li>
					<input id=rating1 name=rating type=radio />
					<label>1</label>
				</li>
				<li>
					<input id=rating2 name=rating type=radio />
					<label>2</label>
				</li>
				<li>
					<input id=rating3 name=rating type=radio />
					<label>3</label>
				</li>
				<li>
					<input id=rating4 name=rating type=radio />
					<label>4</label>
				</li>
				<li>
					<input id=rating5 name=rating type=radio />
					<label>5</label>
				</li>
			</ul>
		</fieldset>
		
		<fieldset>
			<legend>Tags</legend>
			
		</fieldset>
			
		<fieldset>
			<input class="submit" type="submit" value="Submit"/>
		</fieldset>		
		
		
	</form>
	
</div><!-- #submit-photo-wrapper -->

<!-- 

<element name="url" type="string"></element>
<element name="is_private" type="boolean"></element>

<element name="rating" type="tns:ratingType" maxOccurs="unbounded"
minOccurs="0">
</element>

<element name="tag" type="tns:tagType" maxOccurs="unbounded"
minOccurs="0"></element>

 -->