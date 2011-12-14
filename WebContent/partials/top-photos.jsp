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
				
					<p>Rating by logged in user: <% PhotoDAO.getPhotoRatingByUser(user.getUserID(), photo.getPhotoId()); %></p>
					
					<% 
					//Get photo tags
					List<Tag> tags = PhotoDAO.getPhotoTags(photo.getPhotoId());
					if(tags != null){
						for(Tag tag : tags){
					%>
						<p>Tag: <%= tag.getTagText()%></p> 
							
					<%
						}
					}
					
					%>
				
					<!-- TODO Owner info here? or on a tool tip? -->
				
					
				</div>
			</div>
		
		<% } %>
	<div class="clear"></div>
		
</div><!-- #top-pics -->


<% } %>