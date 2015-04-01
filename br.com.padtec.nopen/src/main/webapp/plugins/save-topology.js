function generateSaveTopologyDialog(graph){
	
	dialog = $("#save-dialog").dialog({
	      autoOpen: false,
	      height: 180,
	      width: 350,
	      modal: true,
	      buttons: { 
	    	"Save": checkTopologyFile,  
	        Cancel: function() {
	          dialog.dialog( "close" );
	        }
	      },
	      close: function() { }
	});

	$("#save-dialog").dialog("open");
	
	function checkTopologyFile(){
		
		if($("#save-filename").val() == ""){
			alert("File name cannot be empty!")
		}
		else{
			$.ajax({
			   type: "POST",
			   url: "checkTopologyFile.htm",
			   data: {
				 'filename': $("#save-filename").val(),
			   },
			   success: function(data){ 		   
				   
				   if(data == "exist"){		   
					   if (confirm('The file already exist, do you want to replace it?')) {
						    saveTopology();
					   } 
				   }
				   else{
					   saveTopology();
				   }
			   },
			   error : function(e) {
				   alert("error: " + e.status);
				   $("#save-dialog").dialog("close");
			   }
			});
		}
		
	};
	
	
	function saveTopology(){
		
		$.ajax({
		   type: "POST",
		   url: "saveTopology.htm",
		   data: {
			 'filename': $("#save-filename").val(),
			 'graph': JSON.stringify(graph.toJSON()),
		   },
		   success: function(){ 		   
			   alert($("#save-filename").val() + ' saved successfully!');
			   $("#save-dialog").dialog("close");
		   },
		   error : function(e) {
			   alert("error: " + e.status);
			   $("#save-dialog").dialog("close");
		   }
		});
		
		
	};
	
}