nopen.provisioning.App = Backbone.View.extend({
	
	file : undefined,
	model : undefined,
	
	initialize : function(){
		console.log("Provisioning started!");
	},
	
	start : function(app) {
		
		//create file
		this.file = new nopen.provisioning.File;
		//create model
		this.model = new nopen.provisioning.Model;
		
		//initialize procedures
		this.initializeProvisioningFileProcedures(app);
		this.initializeTopologyProcedures(app);
		this.initializeProvisioningGraphProcedures(app);
		
	},
	
	//Provisioning graph procedures
	initializeProvisioningGraphProcedures : function(app) {
		
		var file = this.file;
		var graph = app.graph;
		
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
		var graph = app.graph;
		
		$('#btn-open-topology').click(function(){	
        	file.generateImportTopologyDialog(graph);      	
        });
		
	},
	
});