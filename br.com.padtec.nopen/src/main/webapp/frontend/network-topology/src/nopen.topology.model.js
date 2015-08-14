nopen.topology.Model = Backbone.Model.extend({
	
	app : undefined,
	equipments : undefined,
	
	initialize : function(){
		this.equipments = {};
	},
	
	setApp : function(app) {
		this.app = app;
	},
	
	addNewEquipment : function (nodeId, equipment) {
		this.equipments[nodeId] = equipment;
	},
	
	printEquipments : function() {
		console.log('Equipments: ' + JSON.stringify(this.equipments));
	},
	
	generetaMatchEquipmentToNodeDialog : function(node) {
		
		var $this = this;
		var file = this.app.file;
		
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
		
		function matchEquipmentToNode() {
			
			var filename = $('input[name=equipment]:checked', '#match').val();
			var equipment = file.openEquipment(filename); 
			
			
			
			
			$this.matchEquipmentToNode(equipment, node);
		}
		
	},
	
	matchEquipmentToNode : function(equipment, node) {
		
		node.attr('equipment/id', equipment.id);
		node.attr('equipment/name', equipment.name);
		
	},
	
});