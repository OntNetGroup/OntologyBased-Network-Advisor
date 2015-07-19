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
		this.initializeProvisioningValidatorProcedures(app);
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
		var validator = app.validator;
		var link = app.link;
		var model = this.model;
		
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
		
        var transition = false;
        paper.on('cell:pointermove', function(cellView, evt, x, y) {
        	
        	var cell = graph.getCell(cellView.model.id);
        	
        	if(cell.get('type') === 'link') {
        		transition = true;
        	}
        	
        });
        
        paper.on('cell:pointerup', function(cellView, evt, x, y) {
        	
        	var cell = graph.getCell(cellView.model.id);
        	console.log(cell.get('type'))

        	if(cell.get('type') === 'link') {
        		transition = false;
        	}
        	
        });
        
        paper.on('cell:mouseover', function(cellView, evt) {
        	
        	var cell = graph.getCell(cellView.model.id);

        	if(cell.get('subtype') !== 'Access_Group') return;
        	
        	cell.attr('circle/stroke', "red");
        	cell.attr('circle/stroke-width', 3);
        	cell.attr('text/display', 'normal');
        	
        	if(transition) return;
        	
        	$.each(graph.getLinks(), function(index, link) {
        		
        		//show links
        		if(link.get('source').id === cell.id) {
        			model.showLink(link.id);
        			var connectedCell = graph.getCell(link.get('target').id);
        			connectedCell.attr('circle/stroke', "blue");
        			connectedCell.attr('circle/stroke-width', 3);
        			connectedCell.attr('text/display', 'normal');
        		}
        		if(link.get('target').id === cell.id) {
        			model.showLink(link.id);
        			var connectedCell = graph.getCell(link.get('source').id)
        			connectedCell.attr('circle/stroke', "blue");
        			connectedCell.attr('circle/stroke-width', 3);
        			connectedCell.attr('text/display', 'normal');
        		}
        		
        	});
        	
        });
        
        paper.on('cell:mouseout', function(cellView, evt) {

        	if(transition) return;
        	
        	var cell = graph.getCell(cellView.model.id);
        	if(cell.get('subtype') !== 'Access_Group') return;
        	
        	$.each(graph.getElements(), function(index, cell) {
        		if(cell.get('subtype') === 'Access_Group') {
        			cell.attr('circle/stroke', "black");
        			cell.attr('circle/stroke-width', 1);
            		cell.attr('text/display', 'none');
        		}
        	});
        	
        	//hide links
        	model.hideLinks();
        	
        });
        
	},

	//Validator procedures
	initializeProvisioningValidatorProcedures : function(app) {
		
		var graph = app.graph;
		var validator = app.validator;
		var model = this.model;
		
		//create a dialog connection only if exist a target
		validator.validate('change:target change:source', function(err, command, next) {

			var cell = graph.getCell(command.data.id);
			
			if(!cell.attributes.target.id){
				cell.remove();
				return;
			}
			
			var source = graph.getCell(cell.attributes.source.id);
			var target = graph.getCell(cell.attributes.target.id);
			
			createConnectionDialog(cell, source, target);
			
		});
		
		//Generate connection dialog when drag a equipment to another
		function createConnectionDialog(cell, source, target) {
			
			var sourceName = model.getEquipmentName(source);
			var targetName = model.getEquipmentName(target);
			
			var content = 
				'<div class="bindsContainer"><div class="outputsList"><div id="listContainer">' +
				    '<ul id="expList" class="list"> <li class="outputs">' + sourceName + '</li> ' ;
			
			content = content + '</ul></div></div>';
			content = content + 
				'<div class="inputsList"><div id="listContainer">' +
			    	'<ul id="expList" class="list"> <li class="inputs">' + targetName + '</li></ul>' + 
			    '</div></div></div>';
			
			//Create dialog
			var dialog = new joint.ui.Dialog({
				width: 500,
				type: 'neutral',
				title: 'Create Connection',
				content: content,
				buttons: [
				          { action: 'cancel', content: 'Cancel', position: 'left' },
				          ]
			});
			dialog.on('action:cancel', cancel);
			dialog.on('action:close', cancel);
			dialog.open();
			
			function cancel() {
				cell.remove();
				dialog.close();
			};
		}
		
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