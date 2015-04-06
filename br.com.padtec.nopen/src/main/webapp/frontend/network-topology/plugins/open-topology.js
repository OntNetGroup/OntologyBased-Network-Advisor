function getTopologies(graph){
	
	$.ajax({
	   type: "GET",
	   url: "getAllTopologies.htm",
	   dataType: 'json',
	   success: function(data){ 		   
		   generateOpenTopologyDialog(graph, data)
	   },
	   error : function(e) {
		   alert("error: " + e.status);
	   }
	});
	
}

function generateOpenTopologyDialog(graph, data){
	
	$("#open-dialog").html('');
	
	dialog = $("#open-dialog").dialog({
	      autoOpen: false,
	      height: 300,
	      width: 350,
	      modal: true,
	      buttons: { 
	    	"Open": openTopology,  
	        Cancel: function() {
	          dialog.dialog( "close" );
	        }
	      },
	      close: function() { }
	});
	
	$("#open-dialog").append('<form>')
	
	for(var i = Object.keys(data).length-1; i >= 0; i--){

		if(i == Object.keys(data).length-1){
			$("#open-dialog").append('<input type="radio" name="topology" value="' + data[i].topology + '" checked>' + data[i].topology + '<br>');
		}
		else{
			$("#open-dialog").append('<input type="radio" name="topology" value="' + data[i].topology + '">' + data[i].topology + '<br>');
		}

	}
	
	$("#open-dialog").append('</form>') 
	
	$("#open-dialog").dialog("open");
	
	function openTopology(){
		
		var filename = $('input[name=topology]:checked', '#open-dialog').val();
		
		$.ajax({
		   type: "POST",
		   url: "openTopology.htm",
		   data: {
			   'filename' : filename
		   },
		   dataType: 'json',
		   success: function(data){ 		   
			   graph.fromJSON(data);
			   dialog.dialog( "close" );
		   },
		   error : function(e) {
			   alert("error: " + e.status);
			   dialog.dialog( "close" );
		   }
		});
		
	};
}