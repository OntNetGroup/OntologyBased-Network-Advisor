function importTopology (graph) {

	var file = $('#file').prop('files')[0]; 
	//document.getElementById('file').files[0];

	$.ajax({
	   type: "POST",
	   url: "importTopology.htm",
	   data: file,
	   processData: false,
	   //enctype: 'multipart/form-data',
	   success: function(data){   
		   graph.fromJSON(JSON.parse(data));
		   $("#btn-layout").click();
		   getUUID();
	   },
	   error : function(e) {
		   alert("error: " + e.status);
	   }
	});
	
};

function getUUID (){
	
	var file = $('#file').prop('files')[0]; 
	
	$.ajax({
	   type: "POST",
	   url: "getUUID.htm",
	   data: file,
	   processData: false,
	   success: function(data){
		   if(data != ""){
			   uuid = data;
		   }
	   },
	   error : function(e) {
		   alert("error: " + e.status);
	   }
	});
}