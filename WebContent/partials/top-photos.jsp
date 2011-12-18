<%@ page import="	java.util.List, 
					edu.pennphoto.db.PhotoDAO,
					edu.pennphoto.db.UserDAO,
					edu.pennphoto.web.WebFormat, 
					edu.pennphoto.model.User,
					edu.pennphoto.model.Photo,
					edu.pennphoto.model.Tag" %>

<%

User user = (User) session.getAttribute("user");

if(user != null){
	String searchTag = request.getParameter("search-tag");
	%>
<div id="top-pics">
		
<% List<Photo> photos = searchTag == null || searchTag.trim().equals("") ? 
		PhotoDAO.getTopPhotosForUser(user.getUserID()):
	PhotoDAO.searchPhotosByTag(searchTag, user.getUserID());

	for (Photo photo : photos) {
		
		int ownerId = photo.getOwnerId();	
		User owner = UserDAO.getUserById(ownerId); 
		%>		
		<div class="top-pics-photo">
			<p>PhotoID: <%= photo.getPhotoId() %></p>
			<p>Posted by: <%= owner.getFirstName() %>&nbsp;<%= owner.getLastName() %></p>
			<div class="img-wrap">
				<img src="<%= photo.getUrl() %>" alt="Photo Submitted by user <%= owner %>"/>
			</div>
			
			<div class="photo-meta">
			
				
				<% 
				//Get photo tags
				List<Tag> tags = PhotoDAO.getPhotoTags(photo.getPhotoId());
				if(tags != null && tags.size() > 0){ %>
					<p>Tags:
				<% 	for(int i = 0; i < tags.size(); i++){ 
						Tag tag = tags.get(i);
						out.print(tag.getTagText());
						if(i < tags.size() - 1){
							out.print(", ");
						}
					 } %>
				<% } //end if tags != null %>
				</p>
				
			<% int yourRating = PhotoDAO.getPhotoRatingByUser(user.getUserID(), photo.getPhotoId());%>
			<p>Average Rating: <%= WebFormat.format(photo.getAverageRating()) %></p>
		<form action="userServlet" method="post">
	
			<input type="hidden" name="action" value="rating" />
			<input type="hidden" name="photo" value="<%= photo.getPhotoId() %>" />
				<fieldset>
					<legend>Your Rating</legend>
					<ul class="rating">
					
					<% for(int i = 1; i <= 5; i++){ %>
						<li>
							<label><%= i %></label>
							<input name=rating type=radio value=<%=i%> <%= (yourRating == i) ? "checked" : "" %> />
						</li>
					
					<% } %>
					
					</ul>
				</fieldset>
				
				<fieldset>
					<legend>Submit Rating</legend>
					<input class="submit" type="submit" value="Submit"/>
				</fieldset>
			
			</form>
			<form action="userServlet" method="post">
	
			<input type="hidden" name="action" value="tag-photo" />
			<input type="hidden" name="photo" value="<%= photo.getPhotoId() %>" />
				<fieldset>
					<legend>Tag this photo</legend>
					<input name="tag" type="text"/>
					<input class="submit" type="submit" value="Add Tag"/>
				</fieldset>
			
			</form>				
				
			</div>
		</div>
	
	<% }//end for Photos %>
	<div class="clear"></div>
		
</div><!-- #top-pics -->


<% } //end if user != null%>