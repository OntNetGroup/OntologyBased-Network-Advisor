/**
 * Procedure to change/set Equipments to Nodes.
 * @param graph
 */
function equipmentSettings(cell){
	
	if(cell.get('type') == "basic.Circle"){
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
	
};

/**
 * Procedure to handle with added Nodes.
 * @param graph
 */
function graphHandle(graph){
	
	graph.on('add', function(cell) {
		
		//Add Node
		if(cell.get('type') == "basic.Circle"){
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
	
	graph.on('change:target', function(link) { 
		
		if (link.get("target").id != null) {
			
			var source = graph.getCell(link.get("source").id);
			var target = graph.getCell(link.get("target").id);
			
			$.ajax({
			   type: "POST",
			   url: "createConnection.htm",
			   data: {
				 'source': source.id,
				 'target': target.id
			   },
			   success: function(data){ 		   
				   //link.remove();
			   },
			   error : function(e) {
				   alert("error: " + e.status);
			   }
			});
		}
		
	});

};

/**
 * Procedure to generate a Dialog with a Equipments List to Match with Node
 * @param data
 * @param cell
 */
function generateDialog(data, cell){
	
	$("#dialog").html('');
	
	dialog = $("#dialog").dialog({
	      autoOpen: false,
	      height: 300,
	      width: 350,
	      modal: true,
	      buttons: { 
	    	"Match": matchEquipmentToNode,  
	        Cancel: function() {
	          dialog.dialog( "close" );
	        }
	      },
	      close: function() { }
	});
	
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
	
	
	function matchEquipmentToNode(){
		
		var equipment = $('input[name=equipment]:checked', '#dialog').val();
		
		$.ajax({
		   type: "POST",
		   url: "matchEquipmentToNode.htm",
		   data: {
			 'idNode' : cell.id,
			 'idEquipment' : equipment
		   },
		   success: function(data){
			   cell.attr('equipment/template', equipment);
			   cell.attr('text/text', equipment);
				
			   dialog.dialog( "close");
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});

	};
	
};




function paperHandle(paper){
	
	paper.on('cell:pointerdblclick', function(cellView, evt, x, y) { 
	    alert(cellView.model.id);
	});
	
	/*
	paper.on('cell:pointerup', function(cellView, evt, x, y) { 

		var link = cellView.model;
		
		if(link.get('type') == "link"){
				
			if(link.get('target').id == null){
				alert(link.get('target').id);
				link.remove();
			}
		}
	});
	*/
};

