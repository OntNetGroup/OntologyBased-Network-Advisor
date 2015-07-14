nopen.provisioning.App = Backbone.View.extend({
	
	file : undefined,
	model : undefined,
	owl : undefined,
	preProvisioning : undefined,
	test : undefined,
	
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
		//create preProvisioning
		this.preProvisioning = new nopen.provisioning.PreProvisioning;
		
		//create Test
		this.test = new nopen.provisioning.Test;
		this.test.setApp(this);
		
		//set app
		this.file.setApp(this);
		this.owl.setApp(this);
		this.preProvisioning.setApp(this);
		this.model.setApp(this);

		//initailize tests
		this.initializeTestProcedures(app);
		
		//initialize procedures
		this.initializeProvisioningFileProcedures(app);
		this.initializeTopologyProcedures(app);
		this.initializeToolbarProcedures(app);
		this.initializeProvisioningGraphProcedures(app);
		
	},
	
	//Test procedures
	initializeTestProcedures : function(app) {
		//this.test.execute(app);
	},
	
	
	//Provisioning graph procedures
	initializeProvisioningGraphProcedures : function(app) {
		
		var file = this.file;
		var graph = app.graph;
		var paper = app.paper;
		
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
			file.generateImportTopologyDialog(app);
        });
		
	},
	
});