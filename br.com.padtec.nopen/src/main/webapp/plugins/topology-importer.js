function importTopology (graph) {

	//alert(reader);
	var file = document.getElementById('file').files[0];
	
	
		$.ajax({
		   type: "POST",
		   url: "../../importTopology",
		   data: file,
		   processData: false,
		   //enctype: 'multipart/form-data',
		   contentType: "application/json; charset=\"utf-8\"",
		   dataType: "text",
		   success: function(data){   
			   alert(data);
			   graph.fromJSON(JSON.parse(data));
			   $("#btn-layout").click();
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
};
