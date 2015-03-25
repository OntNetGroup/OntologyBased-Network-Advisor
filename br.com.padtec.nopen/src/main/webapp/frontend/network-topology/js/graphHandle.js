function graphHandle(graph){
	
	graph.on('add', function(cell) {
		
		$.ajax({
		   type: "GET",
		   url: "getAllTemplateEquipment.htm",
		   dataType: 'json',
		   success: function(data){ 
			   for(var i=0; i<Object.keys(data).length; i++){
				   alert(data[i].equipment);
			   }
			   
			   
			   //var obj = $.parseJSON(data);
			   //alert(Object.keys(obj).length);
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
		
	});
	
};