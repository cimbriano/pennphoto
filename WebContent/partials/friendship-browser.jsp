<%@ page import="edu.pennphoto.model.User" %>
<div id="friendship-browser">
	<div id="infovis"></div>

	<div id="inner-details"></div>
	<div id="friendship-photos"></div>

</div><!-- friendship-browser -->

<!-- JIT Library File -->
<script src="ext-lib/jit-yc.js" type="text/javascript"></script>

<script type="text/javascript">
	window.onload = function() {
//		$("p").ajaxError(function(event, request, settings, exception) {
//			alert(settings.url + ": " + exception.message);
//			});
				
		init_JIT();
		<% User user = (User) session.getAttribute("user"); %>
		load_browser_photos(<%= user.getUserID() %>);
	};
	
	function init_JIT() {
		var target = "http://localhost:8080/pennphoto/UserServlet";
		$.post(target, { "action": "init-friends" },
				 function(data){
					console.log(data);
					init_JIT_callback(data);
				 }, "json");
	}
	
	function load_browser_photos(friend_id) {
		$("#friendship-photos").empty();
		
		var target = "http://localhost:8080/pennphoto/UserServlet";
		$.post(target, { "action": "browser-photos", "friend-id" : friend_id },
			 function(data){
				console.log(data);
				browser_photo_callback(data);
			 }, "json");		
	}

	function browser_photo_callback(photos) {
		if (photos) {
			for (var i=0; i < photos.length; i++) {
				var photo = photos[i];
				var tag = "<img class='friendship-photo' photo-id='" + photo.id + "' src='" + photo.url + "' />";
				$("#friendship-photos").append(tag);
			}
		}
	}

	
	function init_JIT_callback(json) {
		var infovis = document.getElementById('infovis');
		var w = infovis.offsetWidth - 50, h = infovis.offsetHeight - 50;
		    
		var ht = new $jit.Hypertree(
				{
					//id of the visualization container  
					injectInto : 'infovis',
					//canvas width and height  
					width : w,
					height : h,
					//Change node and edge styles such as  
					//color, width and dimensions.  
					Node : {
						dim : 9,
						color : "#f00"
					},
					Edge : {
						lineWidth : 2,
						color : "#088"
					},
					offset: 0.1,
					onBeforeCompute : function(node) {
					//	Log.write("centering");
					},
					//Attach event handlers and add text to the  
					//labels. This method is only triggered on label  
					//creation  
					onCreateLabel : function(domElement, node) {
						domElement.innerHTML = node.name;
						$jit.util.addEvent(domElement, 'click', function() {
							ht.onClick(node.id, {
								onComplete : function() {
									ht.controller.onComplete();
								}
							});
						});
					},
					//Change node styles when labels are placed  
					//or moved.  
					onPlaceLabel : function(domElement, node) {
						var style = domElement.style;
						style.display = '';
						style.cursor = 'pointer';
						if (node._depth <= 1) {
							style.fontSize = "0.8em";
							style.color = "#ddd";
							style.zIndex = "100";
						} else if (node._depth == 2) {
							style.fontSize = "0.7em";
							style.color = "#555";
	
						} else {
							style.display = 'none';
						}
	
						var left = parseInt(style.left);
						var w = domElement.offsetWidth;
						style.left = (left - w / 2) + 'px';
					},
	
					onComplete : function() {
						//Log.write("done");

						var node = ht.graph.getClosestNodeToOrigin("current");
						load_browser_photos(node.id);
						
						//FROM DEMO: Shows friends of name for now. To currently hidden div
						//Build the right column relations list.  
						//This is done by collecting the information (stored in the data property)   
						//for all the nodes adjacent to the centered node.
						var html = "<h4>" + node.name + "</h4><b>Connections:</b>";
						html += "<ul>";
						node
								.eachAdjacency(function(adj) {
									var child = adj.nodeTo;
									if (child.data) {
										var rel = (child.data.band == node.name) ? child.data.relation
												: node.data.relation;
										html += "<li>"
												+ child.name;
										}
								});
						html += "</ul>";
						$jit.id('inner-details').innerHTML = html;
					}
		});
	    //load JSON data.
	    ht.loadJSON(json, 0);
	    //compute positions and plot.
	    ht.refresh();
	    //end
	    ht.controller.onComplete();
	}
</script>
