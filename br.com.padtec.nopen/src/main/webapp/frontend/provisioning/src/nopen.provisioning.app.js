nopen.provisioning.App = Backbone.View.extend({
	
	file : undefined,
	model : undefined,
	owl : undefined,
	
	initialize : function(){
		console.log("Provisioning started!");
	},
	
	start : function(app) {
		
		//create file
		this.file = new nopen.provisioning.File;
		//create model
		this.model = new nopen.provisioning.Model;
		//create owl
		this.owl = new nopen.provisioning.OWL;
		
		//set app
		this.file.setApp(this);
		this.owl.setApp(this);
		this.model.setApp(this);

		
		//initialize procedures
		this.initializeProvisioningFileProcedures(app);
		this.initializeTopologyProcedures(app);
		this.initializeToolbarProcedures(app);
		this.initializeProvisioningGraphProcedures(app);
		
	},
	
	
	//Provisioning graph procedures
	initializeProvisioningGraphProcedures : function(app) {
		
		var file = this.file;
		var graph = app.graph;
		
		/* 
		 * PROVISORIO
		 * author: missael
		 */
		var implementedTechnologies = this.model.getImplementedTechnologies();
		
		_.each(implementedTechnologies, function(technology, index) {
			
			var uppermostLayer = this.model.getUppermostLayer(technology);
			var container = Stencil.createContainer(technology, uppermostLayer);
			var subnetwork = Stencil.createSubnetwork();
			var equipmentIDs = this.model.getEquipmentsByLayer(uppermostLayer);
			
			graph.addCell(container);
			graph.addCell(subnetwork);
			container.embed(subnetwork);
			
			_.each(equipmentIDs, function(equipmentID, index) {
				var node = Stencil.createNode(equipmentID);
				graph.addCell(node);
				
				var newLink = new joint.dia.Link({
					source: {id: subnetwork.attributes.id},
					target: {id: equipmentID}
				});
				graph.addCell(newLink);
				container.embed(node);
			}, this);
			
		}, this);
		
	},

	//Toolbar procedures
	initializeToolbarProcedures : function(app) {
		
		$('#btn-show-hide-inspector').click(function(){
			
			if($('.inspector-container').is(':visible')) {
				$('.inspector-container').hide();
				$('.paper-container').css({
					right: 0,
				});
			} else {
				$('.inspector-container').show();
				$('.paper-container').css({
					right: 241,
				});
			}
			
		});
		
	},
	
	//Provisioning file procedures
	initializeProvisioningFileProcedures : function(app) {
		
		var file = this.file;
		var graph = app.graph;
		
		//procedure to open a file from URL
		if(file.getUrlParameter('provisioning')){
        	var provisioning = file.getUrlParameter('provisioning');
        	file.openFromURL(provisioning, app.graph);
        }
		
		//procedure to save a provisioning file
		$('#btn-save').click(function(){
        	file.generateSaveProvisioningDialog(graph);   	
        });
		
		//procedure to open a provisioning file
		$('#btn-open').click(function(){	
			file.generateOpenProvisioningDialog(graph);
        });
		
	},
	
	//Topology procedures
	initializeTopologyProcedures : function(app) {
		
		var file = this.file;
		var model = this.model;
		var graph = app.graph;
		
		//import topology
		$('#btn-open-topology').click(function(){
			file.generateImportTopologyDialog(graph);
        });
		
	},
	
});