function importTopology (graph) {

	var file = document.getElementById('file').files[0];

		$.ajax({
		   type: "POST",
		   url: "importTopology.htm",
		   data: file,
		   processData: false,
		   //enctype: 'multipart/form-data',
		   success: function(data){   
			   graph.fromJSON(JSON.parse(data));
			   $("#btn-layout").click();
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
};
