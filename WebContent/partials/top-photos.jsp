<%@ page import="	java.util.List, 
					edu.pennphoto.db.PhotoDAO, 
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
		
		int owner = photo.getOwnerId();	
%>

		<div class="top-pics-photo">
			<div class="img-wrap">
				<img src="<%= photo.getUrl() %>" alt="Photo Submitted by user <%= owner %>"/>
			</div>
			
			<div class="photo-meta">
			
				
				<% 
				//Get photo tags
				List<Tag> tags = PhotoDAO.getPhotoTags(photo.getPhotoId());
				if(tags != null){
					for(Tag tag : tags){ %>
					<p>Tag: <%= tag.getTagText()%></p> 		
					<% } %>
				<% } //end if tags != null %>
				
			<% int yourRating = PhotoDAO.getPhotoRatingByUser(user.getUserID(), photo.getPhotoId());%>
			
		<form action="userServlet" method="post">
	
			<input type="hidden" name="action" value="rating" />
				<fieldset>
					<legend>Your Rating</legend>
					<ul class="rating">
					
					<% for(int i = 1; i <= 5; i++){ %>
						<li>
							<label><%= i %></label>
							<input id="rating<%= i %>" name= rating type=radio />
						</li>
					
					<% } %>
					
					</ul>
				</fieldset>
				
				<fieldset>
					<legend>Submit Rating</legend>
					<input class="submit" type="submit" value="Submit"/>
				</fieldset>
			
			</form>
							
				
			</div>
		</div>
	
	<% }//end for Photos %>
	<div class="clear"></div>
		
</div><!-- #top-pics -->


<% } //end if user != null%>