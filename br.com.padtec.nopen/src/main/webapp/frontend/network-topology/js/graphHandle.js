/**
 * Procedure to change/set Equipments to Nodes.
 * @param graph
 */
function equipmentSettings(cell){
	
	if(cell.get('type') == "basic.Circle"){
		$.ajax({
		   type: "GET",
		   url: "getAllEquipmentsToMatch.htm",
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
			   url: "getAllEquipmentsToMatch.htm",
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
	
	var content = '<form id="match">';
	for(var i = 0; i < Object.keys(data).length; i++){
		if(i == 0){
			content = content + '<input type="radio" name="equipment" value="' + data[i].equipment + '" checked>' 
					+ '<label>' + data[i].equipment + '</label> <br>';
		}
		else{
			content = content + '<input type="radio" name="equipment" value="' + data[i].equipment + '">' 
					+ '<label>' + data[i].equipment + '</label><br>';
		}

	}
	content = content +  '</form>';
	
	var dialog = new joint.ui.Dialog({
		width: 300,
		type: 'neutral',
		title: 'Match Equipment to Node',
		content: content,
		buttons: [
			{ action: 'cancel', content: 'Cancel', position: 'left' },
			{ action: 'match', content: 'Match', position: 'left' }
		]
	});
	dialog.on('action:match', matchEquipmentToNode);
	dialog.on('action:cancel', dialog.close);
	dialog.open();
	
	function matchEquipmentToNode(){
		
		var equipment = $('input[name=equipment]:checked', '#match').val();
		
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
				
			   dialog.close();
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});

		
		//Change equipments id
		
	};
	
	
//	$("#dialog").html('');
//	
//	dialog = $("#dialog").dialog({
//	      autoOpen: false,
//	      height: 300,
//	      width: 350,
//	      modal: true,
//	      buttons: { 
//	    	"Match": matchEquipmentToNode,  
//	        Cancel: function() {
//	          dialog.dialog( "close" );
//	        }
//	      },
//	      close: function() { }
//	});
	
};



