function getTopologies(){
	
	var content
	
	$.ajax({
	   type: "GET",
	   async: false,
	   url: "getAllTopologies.htm",
	   dataType: 'json',
	   success: function(data){ 		   
		   generateOptionsContent(data)
	   },
	   error : function(e) {
		   alert("error: " + e.status);
	   }
	});
	
	function generateOptionsContent(data){
		
		content = '';
		
		for(var i = 0; i < Object.keys(data).length; i++){
			
			if(i == 0){
				content = '<hr/>';
			}
			
			content = content + '<div class="btn-group">'
									+ '<a class="btn" title="Edit" href="network-topology.htm?topology=' + data[i].topology + '"><i class="icon-edit"></i></a>' 
									+ '<a class="btn" title="Delete" onclick="deleteTopology(\'' + data[i].topology + '\')"><i class="icon-trash"></i></a>'
									+ '<span class="name">' + data[i].topology + '</span>'
								+ '</div>'
								+ '<br/><hr/>'
		}
		
	}
	
	$('.btn-toolbar').append(content);

};

function deleteTopology(filename){
	
	if (confirm('Are you sure you want to delete this file?')) {
	
		$.ajax({
		   type: "POST",
		   async: false,
		   url: "deleteTopology.htm",
		   data: {
			   'filename' : filename
		   },
		   success: function(){ 		   
			   alert(filename + " deleted successfully!");
			   window.location.reload(true);
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
	}
};

