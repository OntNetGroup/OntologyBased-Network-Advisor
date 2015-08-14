nopen.topology.App = Backbone.View.extend({
	
	file : undefined,
	model : undefined,
	util : undefined,
	
	initialize : function(){
		console.log("Provisioning started!");
	},
	
	start : function(app) {
	
		//create file
		this.file = new nopen.topology.File;
		//create model
		this.model = new nopen.topology.Model;
		//create util
		this.util = new nopen.topology.Util;
		
		//set app
		this.file.setApp(this);
		this.util.setApp(this);
		this.model.setApp(this);
		
		this.initializeTopologyFileProcedures(app);
		this.initializeTopologyGraphProcedures(app);
		this.initializeTopologyPaperProcedures(app);
		
	},
	
	initializeTopologyFileProcedures : function(app) {
		
		var file = this.file;
		var graph = app.graph;
		
		if(file.getUrlParameter('topology')){
        	var filename = file.getUrlParameter('topology');
        	file.openTopologyFromURL(graph, filename);
        }
		
		$('#btn-open').click(function(){	
        	file.generateOpenTopologyDialog(graph);      	
        });
		
		$('#btn-save').click(function(){
			//All equipments need to be matched
         	file.generateSaveTopologyDialog(app);
         });
		
	},
	
	initializeTopologyGraphProcedures : function(app) {
		
		var graph = app.graph;
		var file = this.file;
		var model = this.model;
		var util = this.util;
		
		graph.on('add', function(cell) {
			
			//Add Node
			if(cell.get('subtype') == "Node") {
				
				var equipments = file.getAllEquipments();
				
				var content = '<form id="match">';
				for(var i = 0; i < equipments.length; i++){
					if(i == 0){
						content = content + '<input type="radio" name="equipment" value="' + equipments[i].equipment + '" checked>' 
								+ '<label>' + equipments[i].equipment + '</label> <br>';
					}
					else{
						content = content + '<input type="radio" name="equipment" value="' + equipments[i].equipment + '">' 
								+ '<label>' + equipments[i].equipment + '</label><br>';
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
				
				function matchEquipmentToNode() {
					
					var nodeId = cell.id;
					
					var filename = $('input[name=equipment]:checked', '#match').val();
					var equipment = file.openEquipment(filename);
					
					$.each(equipment.cells, function(index, element) {
						
						if(element.subType === 'Card') {
							var cardId = element.id;
							var card = file.openEquipmentCard(filename, cardId);
							
							//add card in card data
							equipment.cells[index].attrs.data = card;
						}
						
					});
					
					equipment = util.generateNewEquipmentIDs(equipment);
					
					model.addNewEquipment(nodeId, equipment);
					
					//model.printEquipments();
					
					dialog.close();
					
				}
				
			}
			
		});
		
	},
	
	initializeTopologyPaperProcedures : function(app) {
		
		var paper = app.paper;
		var file = this.file;
		var model = this.model;
		
	},
	
});