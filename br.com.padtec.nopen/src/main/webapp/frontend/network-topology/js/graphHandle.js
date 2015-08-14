//Global var(provisorio)
topologies=[];
equipments=[];
itus=[];
cards=[];
supervisors=[];
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
				   generateDialog(data, cell , graph)
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
function generateDialog(data, cell , graph){
	
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
		console.log(equipment);
		console.log(cell.id);
		
		
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
			   changegraphID(equipment,cell);
			   dialog.close();
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});

		function changegraphID(equipment,cell){
			

//				var ITUelements = [], ITUlinks = [];
				
//				var cardCells = changeEquipmentElements(filename, card.id);
//				$.each(cardCells, function(index, element) {
//					}),
				
				
			
			console.log(equipment);
			console.log(graph);
			var equipName;
			var originalGraph = graph.toJSON();
			var equipmentgraph = openEquipment();
			console.log(equipmentgraph);
			$.each(equipmentgraph.getElements(), function(index, cell){
				
				if(cell.get('subType') === 'Rack'){
					console.log(cell.get('id'));
					cell.set('id',joint.util.uuid());
					console.log(cell.get('id'));
				}
				
				 if(cell.get('subType') === 'Shelf'){
					cell.set('id',joint.util.uuid());
				}
				
				 if(cell.get('subType') === 'Slot'){
					cell.set('id',joint.util.uuid());
				}
				
				 if(cell.get('subType') === 'Card'){
					
					
					
						var temporaryITU = new joint.dia.Graph;
						
						$.ajax({
							type: "POST",
							async: false,
							url: "openITUFileEquipment.htm",
							data: {
								'path' : equipment,
								'filename' : cell.id
							},
							dataType: 'json',
							success: function(data){
								temporaryITU.fromJSON(data);
							},
							error : function(e) {
								alert("error: " + e.status);
							}
						});
						
						itus.push(temporaryITU);
					   console.log(itus);
					
					cell.set('id',joint.util.uuid());
					cards.push(cell.get('id'));
					console.log(cards);
				}
				
				 if(cell.get('subType') === 'Supervisor'){
					cell.set('id',joint.util.uuid());
					supervisors.push(cell.get('id'));
					console.log(supervisors);
				}
				
				
			});
			
			equipments.push(equipmentgraph);
			console.log(equipmentgraph);
			console.log(equipments);
			
			

			
			
			
			
			//			 var graph1 = graph.fromJSON(equipment);
//			 console.log(graph1);
//		//	console.log('equipmanet' , equipament);
//			var elementos = equipament.getElements();
//		//	console.log('elementos' , elementos);
//			var equipSupervisor;
//			var c = [];
//	        var movingCards = [];
//			
//			for(var i = 0; i < elementos.length; i++){
//				var check = elementos[i];
//				//console.log(check);
//				if((check.attributes.subType) === 'Supervisor'){
//	               if((check.attributes.attrs.name.text === $('#save-dialog').find(":selected").val())){
////	            	   equipsupervisor = equipament.getCell(check.id);
//	               }else{
//	            	   var parent = check.attributes.parent;
//	            	   var grandParent = (equipament.getCell(parent)).attributes.parent;
//	            	   console.log(grandParent);
//	            	   (equipament.getCell(check.id)).remove();
//	            	   (equipament.getCell(parent)).remove();
//	            	   if ((equipament.getCell(grandParent)).getEmbeddedCells().length === 0){
//	            		   (equipament.getCell(grandParent)).remove();
//	            		   
//	            	   }
//	               }
//				}
//				if((check.attributes.subType) === 'Card'){
//					if((check.attributes.Supervisor === $('#save-dialog').find(":selected").val())){
//		            	   movingCards.push(check.id);
//		               }else{
//		            	   var parent = check.attributes.parent;
//		            	   var grandParent = (equipament.getCell(parent)).attributes.parent;
//		            	   console.log(grandParent);
//		            	   (equipament.getCell(check.id)).remove();
//		            	   (equipament.getCell(parent)).remove();
//		            	   if ((equipament.getCell(grandParent)).getEmbeddedCells().length === 0){
//		            		   (equipament.getCell(grandParent)).remove();
//		            		   
//		            	   }
//		               }
//				} 
//				
//				if((check.attributes.subType) === 'Slot'){
//					if(check.getEmbeddedCells().length === 0){
//						var parent = check.attributes.parent;
//						(equipament.getCell(check.id)).remove();					
//						 if ((equipament.getCell(parent)).getEmbeddedCells().length === 0){
//		            		   (equipament.getCell(parent)).remove();   
//		            	   }
//					}
//				}
//				
//				if((check.attributes.subType) === 'Shelf'){
//					if(check.getEmbeddedCells().length === 0){
//						(equipament.getCell(check.id)).remove();
//					}
//				}
//			};		
//			console.log(equipament);		
//		}
//		
//		$.ajax({
//			type: "POST",
//			url: "create"
//		});
		
		
		//Change equipments id
		
//	};
	
	
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
	
}
		function openEquipment(){
			
			var filename = equipment;

//			function changeEquipmentElements(eqName, cardID) {
				var temporary = new joint.dia.Graph;
				
				$.ajax({
					type: "POST",
					async: false,
					url: "openEquipmentInTopology.htm",
					data: {
						'filename' : filename
					},
					dataType: 'json',
					success: function(data){
						temporary.fromJSON(data);
					},
					error : function(e) {
						alert("error: " + e.status);
					}
				});
//				return equipGraph.getElements();
				
			   return temporary;
		
		
		}		
	
	}}



