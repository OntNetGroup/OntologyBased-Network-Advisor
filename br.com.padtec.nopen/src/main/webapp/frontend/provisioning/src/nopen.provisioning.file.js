nopen.provisioning.File = Backbone.Model.extend({
	
	app : undefined,
	topologySVG : undefined,
	
	initialize : function(){
		
	},
	
	setApp : function(app) {
		this.app = app;
	},
	
	setTopologySVG : function(svg) {
		this.topologySVG = svg
	},
	
	getTopologySVG : function() {
		return this.topologySVG;
	},
	
	//Method to get paramentes from url
	getUrlParameter : function(sParam) {
	    var sPageURL = window.location.search.substring(1);
	    var sURLVariables = sPageURL.split('&');
	    for (var i = 0; i < sURLVariables.length; i++) 
	    {
	        var sParameterName = sURLVariables[i].split('=');
	        if (sParameterName[0] == sParam) 
	        {
	            return sParameterName[1];
	        }
	    }
	},      

	//Method to open a provisioning file from URL
	openFromURL : function(graph, filename) {
		
		//open provisioning
		this.openProvisioning(graph, filename);
		
	},
	
	//Method to generate a save provisioning dialog
	generateSaveProvisioningDialog : function(graph) {
		
		var $this = this;
		
		var content = '<div id="save-dialog" title="Save Provisioning">'
			+ 'Name: <input type="text" id="save-filename" value="' + $('#filename').val() + '"/>'
			+ '</div>'
			+ '<div id="name-error-message">' + 'Name cannot be empty!' + '</div>';
			
		var dialog = new joint.ui.Dialog({
			width: 300,
			type: 'neutral',
			title: 'Save Provisioning',
			content: content,
			buttons: [
				{ action: 'cancel', content: 'Cancel', position: 'left' },
				{ action: 'save', content: 'Save', position: 'left' }
			]
		});
		
		dialog.on('action:save', checkProvisioningFileExist);
		dialog.on('action:cancel', cancel);

		dialog.open();
		
		function checkProvisioningFileExist(){
			$this.checkProvisioningFileExist(graph, dialog);
		}
		
		function cancel(){
			dialog.close();
		}
		
	},
	
	//Method to check if provisioning file exist
	checkProvisioningFileExist : function(graph, dialog) {
		
		var $this = this;
		
		var filename = $("#save-filename").val();
		
		if(filename == ""){
			$('#name-error-message').show();
		}
		else{
			$.ajax({
			   type: "POST",
			   async: false,
			   url: "checkProvisioningFile.htm",
			   data: {
				 'filename': filename,
			   },
			   success: function(data){ 		   
				   
				   if(data == "exist"){		   
					   if (confirm('The file already exist, do you want to replace it?')) {
						   $this.saveProvisioning(graph, filename);
						   dialog.close();
					   } 
				   }
				   else{
					   $this.saveProvisioning(graph, filename);
					   dialog.close();
				   }
			   },
			   error : function(e) {
				   alert("error: " + e.status);
				   dialog.close();
			   }
			});
		}
		
	},
	
	//Method to save a provisioning file
	saveProvisioning : function(graph, filename) {
		
		$('#filename').val(filename);
		
		$.ajax({
		   type: "POST",
		   async: false,
		   url: "saveProvisioning.htm",
		   data: {
			 'filename': filename,
			 'graph': JSON.stringify(graph.toJSON()),
		   },
		   success: function(){},
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
		
		var saveDialog = new joint.ui.Dialog({
			type: 'neutral' ,
			width: 420,
			draggable: false,
			title: 'Provisioning Saved! ',
			content: 'The Provisioning ' + filename + ' was saved',
			open: function() {}
		});
		
		saveDialog.open();
		
	},
	
	//Method to generate a open dialog to provisioning files
	generateOpenProvisioningDialog : function(graph) {
		
		var $this = this;
		
		$.ajax({
		   type: "GET",
		   url: "getAllProvisioning.htm",
		   dataType: 'json',
		   success: function(data){ 		   
			   
			   	var content = '<form id="open">';
				for(var i = Object.keys(data).length-1; i >= 0; i--){
					if(i == Object.keys(data).length-1){
						content = content + '<input type="radio" name="provisioning" value="' + data[i].provisioning + '" checked>' 
								+ '<label>' + data[i].provisioning + '</label> <br>';
					}
					else{
						content = content + '<input type="radio" name="provisioning" value="' + data[i].provisioning + '">' 
								+ '<label>' + data[i].provisioning + '</label><br>';
					}

				}
				content = content +  '</form>';
				
				var dialog = new joint.ui.Dialog({
					width: 300,
					type: 'neutral',
					title: 'Open Provisioning',
					content: content,
					buttons: [
						{ action: 'cancel', content: 'Cancel', position: 'left' },
						{ action: 'open', content: 'Open', position: 'left' }
					]
				});
				dialog.on('action:open', openProvisioning);
				dialog.on('action:cancel', dialog.close);
				dialog.open();
				
				function openProvisioning() {
					var filename = $('input[name=provisioning]:checked', '#open').val();
					$this.openProvisioning(graph, filename);
					dialog.close();
				}
			   
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
		
	},
	
	//Method to open a provisioning file from dialog
	openProvisioning : function(graph, filename) {
		
		var model = this.app.model;
		var owl = this.app.owl;
		
		$.ajax({
		   type: "POST",
		   async: false,
		   url: "openProvisioning.htm",
		   data: {
			   'filename' : filename
		   },
		   dataType: 'json',
		   success: function(data){ 		   
			   graph.fromJSON(data);
			   
//			   //generate owl instances
//			   $.each(graph.getElements(), function(index, equipment) {
//				 
//				   if(equipment.get('subtype') === 'Access_Group') {
//					   
//					   var cards = model.getCards(equipment);
//					   $.each(cards, function(key, card) {
//						   owl.parseCardToOWL(equipment, card);
//					   });
//					   
//				   }
//				   
//			   });
			   
			   //hide links
			   model.hideLinks();
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
		
		//execute reasoning
//		owl.executeReasoning();
		
	},
	
	//method to generate import topology dialog
	generateImportTopologyDialog : function(app) {
		
		var graph = app.graph;
		var $this = this;
		
		$.ajax({
		   type: "POST",
		   async: false,
		   url: "getAllTopologies.htm",
		   dataType: 'json',
		   success: function(topologies){
			   
			   	var content = '<form id="open-topology">';
				for(var i = 0 ; i < topologies.length; i++){
					if(i == 0){
						content = content + '<input type="radio" name="topology" value="' + topologies[i].topology + '" checked>' 
								+ '<label>' + topologies[i].topology + '</label> <br>';
					}
					else{
						content = content + '<input type="radio" name="topology" value="' + topologies[i].topology + '">' 
								+ '<label>' + topologies[i].topology + '</label><br>';
					}

				}
				content = content +  '</form>';
				
				var dialog = new joint.ui.Dialog({
					width: 300,
					type: 'neutral',
					title: 'Open Topology',
					content: content,
					buttons: [
						{ action: 'cancel', content: 'Cancel', position: 'left' },
						{ action: 'openTopology', content: 'Open', position: 'left' }
					]
				});
				dialog.on('action:openTopology', importTopology);
				dialog.on('action:cancel', dialog.close);
				dialog.open();
				
				function importTopology() {
					graph.clear();
					
					var filename = $('input[name=topology]:checked', '#open-topology').val();
					$this.importTopology(app, filename);
					
					dialog.close();
				}
			   
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
		
	},
	
	//method to import topology
	importTopology : function(app, filename) {
		
		var graph = app.graph;
		var $this = this;
		
		$.ajax({
		   type: "POST",
		   async: false,
		   url: "openTopologyOnProvisioning.htm",
		   data: {
			   'filename' : filename
		   },
		   dataType: 'json',
		   success: function(topology){ 	
			   
			   console.log('TOPOLOGY: ' + JSON.stringify(topology));
			   
			   graph.fromJSON(topology);
			   //import equipments
			   $this.importEquipments(app, filename);
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
		
		$.ajax({
		   type: "POST",
		   async: false,
		   url: "openTopologySVG.htm",
		   data: {
			   'filename' : filename
		   },
		   success: function(svg){ 		   
			   //import equipments
			   $this.setTopologySVG(svg);
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
		
	},
	
	//Method to import equipments
	importEquipments : function(app, filename) {
	
		var graph = app.graph;
		var $this = this;
		var model = this.app.model;
		var preProvisioning = this.app.preProvisioning;
		var owl = this.app.owl;
		
		var subnetworks = {};
		
		//open each equipments
		$.each(graph.getElements(), function(index, node){
			
			var equipment = graph.getCell(node.id);
			
			$.ajax({
			   type: "POST",
			   async: false,
			   url: "openTopologyEquipmentOnProvisioning.htm",
			   data: {
				   'filename' : filename,
				   'nodeId' : node.id
			   },
			   dataType: 'json',
			   success: function(equip){
				   //add equipment data on element equipment data attribute
				   equipment.attr('equipment/data', equip)
				   var tech = model.getEquipmentTechnology(equip);
				   
				   if(!subnetworks[tech]) {
					   subnetworks[tech] = [];
				   }
				   
				   subnetworks[tech].push(equipment);
				   
				   var cards = model.getCardsInPreProvisioning(equipment);
				   
				   $.each(cards, function(index, card){
					   
					   //create instances in OWL file
					   owl.parseCardToOWL(equipment, card);
					   
				   });
				   
			   },
			   error : function(e) {
				   alert("error: " + e.status);
			   }
			});
			
		});
		
		//parse connected ports
		owl.parseConnectedPortsToOWL(graph);
		
		//execute reasoning
		owl.executeReasoning();
		
		//start pre provsioning
		preProvisioning.start(app, subnetworks);
		
	},
	
	//import itu files
	importITUFiles : function(app, subnetworks) {

		var graph = app.graph;
		var $this = this;
		var model = this.app.model;
		var preProvisioning = this.app.preProvisioning;
		var owl = this.app.owl;
		
		$.each(graph.getElements(), function(index, value){
			
			var equipment = graph.getCell(value.id);
			var cards = model.getCardsInPreProvisioning(equipment);
			var equipmentName = equipment.attr('equipment/template');
			
			//open each card
			$.each(cards, function(index, card){
				
				var filename = card.id;
				
				$.ajax({
				   type: "POST",
				   async: false,
				   url: "openITUOnProvisioning.htm",
				   data: {
					   'equipment' : equipmentName,
					   'filename' : filename
				   },
				   dataType: 'json',
				   success: function(data){
					   //add ITU data in card data attribute
					   card.attrs.data = data;
					   //create instances in OWL file
					   owl.parseCardToOWL(equipment, card);
				   },
				   error : function(e) {
					   //alert("error: " + e.status);
				   }
				});
				
			});
			
		});
		
		//parse connected ports
		owl.parseConnectedPortsToOWL(graph);
		
		//execute reasoning
		owl.executeReasoning();
		
		//start pre provsioning
		preProvisioning.start(app, subnetworks);
	}
	
});