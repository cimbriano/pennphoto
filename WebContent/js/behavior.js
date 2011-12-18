/**
 * 
 */

jQuery(document).ready(function () {
		
	jQuery(".add-friend-button").click(function (){
		var friendID = jQuery(this).attr("friend");
		var circleID = jQuery("#circles").attr("value");
		
		//console.log(friendID);
		//console.log(circleID);
		
		jQuery("#add-friend-circleID").attr("value", circleID);
		jQuery("#add-friend-friendID").attr("value", friendID);
		
		jQuery("#add-friend-form").submit();
		
	});
	
	jQuery("#circles").change(function () {
		
		alert("changed!");
		
	});
		
});