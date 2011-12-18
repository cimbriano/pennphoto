/**
 * 
 */

jQuery(document).ready(function () {
		
	jQuery(".add-friend-button").click(function (){
		var friendID = jQuery(this).attr("friend");
		var circleID = jQuery("#circles").attr("value");
				
		jQuery("#add-friend-circleID").attr("value", circleID);
		jQuery("#add-friend-friendID").attr("value", friendID);
		
		jQuery("#add-friend-form").submit();
		
	});
	
	
	jQuery("#add-friend-button-by-email").click(function () {
		var email = jQuery("#friend-by-email").attr("value");
		jQuery("#add-friend-friendID").attr("value", email);
		jQuery("#add-friend-form").submit();
	});
	
	jQuery("#circles").change(function () {
		var circleID = jQuery("#circles").attr("value");
		jQuery("#add-friend-circleID").attr("value", circleID);		
	});
		
});