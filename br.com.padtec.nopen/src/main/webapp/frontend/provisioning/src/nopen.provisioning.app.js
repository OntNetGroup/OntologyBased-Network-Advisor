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
		this.initializeTopologyProcedures(app);
		this.initializeToolbarProcedures(app);
		this.initializeProvisioningGraphProcedures(app);
		this.initializeProvisioningPaperProcedures(app);
		this.initializeProvisioningFileProcedures(app);
		
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
		var validator = app.validator;
		
		// validar inserção de links no grafo
	    graph.on('add change:target change:source', function(event, cell) {
	    	
	    	console.log('CELL: ' + JSON.stringify(cell));
	    	console.log(event);
	    	
	    	
	    	// Check if cell is not a link
	    	if (cell.type !== 'link') return;
	    	
	    	// impedir a troca de target ou source (quando o usuário arrasta uma das pontas da 'seta')
//	    	if(command.action === 'change:source') {
//	    		nopen.provisioning.Util.generateAlertDialog('Invalid operation!');
//	    		return;
//	    	}
//	    	if(command.data.previous.target) {
//	    		if(command.data.previous.target.id) {
//	    			nopen.provisioning.Util.generateAlertDialog('Invalid operation!');
//	    			return;
//	    		}
//	    	}
	    	var linkID = cell.id;
	    	var link = graph.getCell(linkID).toJSON();
	    	var sourceID = link.source.id;
	        var targetID = link.target.id;
	        
	        if (sourceID && targetID) {
	        	var targetElement = graph.getCell(targetID);
	        	var targetSubtype = targetElement.attributes.subtype;

	        	var sourceElement = graph.getCell(sourceID);
	        	var sourceSubtype = sourceElement.attributes.subtype;
	        	
	        	var result = "result";
	        	if(result === "success") {
					return;
				} else {
					nopen.provisioning.Util.generateAlertDialog(result);
				}
	        } else {
	        	nopen.provisioning.Util.generateAlertDialog('Please, connect to an Access Group');
	        }
	    }, app);
	},
	
	//Provisioning paper procedures
	initializeProvisioningPaperProcedures : function(app) {
		
		var paper = app.paper;
		var graph = app.graph;
		
		// Garantir que as interfaces de entrada e saída permaneçam contidas em suas respectivas barras
		var position = undefined;
        paper.on('cell:pointerdown', function(cellView, evt) {
        	var cell = graph.getCell(cellView.model.id);
        	if(cell.type === 'link') return;
        	
        	position = cell.get('position');
        });
		
        paper.on('cell:pointerup', function(cellView, evt) {
        	var cell = graph.getCell(cellView.model.id);
        	if(cell.type === 'link') return;
        	
        	cell.set('position', position);
        });
		
	},

	//Toolbar procedures
	initializeToolbarProcedures : function(app) {
		
		$('#loading').hide();
	    $(document).ajaxStart(function() {
	    	$('#loading').show();
	    })
	    .ajaxStop(function() {
	        $('#loading').hide();
	    });
		
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