
function graphHandle(graph){
	
	graph.on('add', function(cell) {
	
		if(cell.attr('text/text') == "NODE"){
			$.ajax({
			   type: "GET",
			   url: "getAllTemplateEquipment.htm",
			   dataType: 'json',
			   success: function(data){ 		   
				   generateDialog(data, cell)
			   },
			   error : function(e) {
				   alert("error: " + e.status);
			   }
			});
		}
		
	});
	
	
	function generateDialog(data, cell){
		
		dialog = $("#dialog").dialog({
		      autoOpen: false,
		      height: 300,
		      width: 350,
		      modal: true,
		      buttons: { 
		    	"Match": matchEquipment,  
		        Cancel: function() {
		          dialog.dialog( "close" );
		          $("#dialog").html('');
		        }
		      },
		      close: function() {
		    	$("#dialog").html('');
		        //form[ 0 ].reset();
		        //allFields.removeClass( "ui-state-error" );
		      }
		});
		
		function matchEquipment(){
			alert($('input[name=equipment]:checked', '#dialog').val() + " " + cell.id);
			dialog.dialog( "close");
			$("#dialog").html('');
		};
		

		$("#dialog").append('<form>')

		for(var i = Object.keys(data).length-1; i >= 0; i--){

			if(i == Object.keys(data).length-1){
				$("#dialog").append('<input type="radio" name="equipment" value="' + data[i].equipment + '" checked>' + data[i].equipment + '<br>');
			}
			else{
				$("#dialog").append('<input type="radio" name="equipment" value="' + data[i].equipment + '">' + data[i].equipment + '<br>');
			}

		}
		
		$("#dialog").append('</form>') 
		$("#dialog").dialog("open");
		
	}
	
};


function paperHandle(paper){
	
	paper.on('cell:pointerdblclick', function(cellView, evt, x, y) { 
	    alert(cellView.model.id);
	})
	
}