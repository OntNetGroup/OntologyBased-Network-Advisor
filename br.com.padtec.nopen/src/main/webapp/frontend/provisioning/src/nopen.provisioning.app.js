nopen.provisioning.App = Backbone.View.extend({
	
	file : undefined,
	model : undefined,
	owl : undefined,
	preProvisioning : undefined,
	test : undefined,
	util : undefined,
	
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
		//create util
		this.util = new nopen.provisioning.Util;
		
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
		
		var test = this.test;
		
//		$('#btn-pre').click(function(){
//			test.executeMatrixDialog(app);
//		});
		
	},
	
	
	//Provisioning graph procedures
	initializeProvisioningGraphProcedures : function(app) {
		
		var file = this.file;
		var graph = app.graph;
		var paper = app.paper;
		var validator = app.validator;
		
		
	},
	
	//Provisioning paper procedures
	initializeProvisioningPaperProcedures : function(app) {
		
		var paper = app.paper;
		var graph = app.graph;
		var validator = app.validator;
		var link = app.link;
		var model = this.model;
		var owl = this.owl;
		var util = this.util;
		
		// keep equipment position
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
		
        //handle with equipment click
        var clicked = false;
        var cellClickedId = undefined;
        paper.on('cell:pointerclick', function(cellView, evt) {
        	
        	var cell = graph.getCell(cellView.model.id);

        	if(cell.get('subtype') !== 'Access_Group') {
        		
        		$.each(graph.getElements(), function(index, cell) {
            		if(cell.get('subtype') === 'Access_Group') {
            			cell.attr('circle/stroke', "black");
            			cell.attr('circle/stroke-width', 1);
                		cell.attr('text/display', 'none');
            		}
            	});
        		
        		clicked = false;
        		return;
        	}
        	
        	cell.attr('circle/stroke', "red");
        	cell.attr('circle/stroke-width', 3);
        	cell.attr('text/display', 'normal');
        	
        	$.each(graph.getLinks(), function(index, link) {
        		
        		var source = graph.getCell(link.get('source').id);
        		var target = graph.getCell(link.get('target').id);
        		
        		//show links
        		if(link.get('source').id === cell.id && source.attr('.rotatable/display') === 'normal' && target.attr('.rotatable/display') === 'normal') {
        			model.showLink(link.id);
        			var connectedCell = graph.getCell(link.get('target').id);
        			connectedCell.attr('circle/stroke', "blue");
        			connectedCell.attr('circle/stroke-width', 3);
        			connectedCell.attr('text/display', 'normal');
        		}
        		if(link.get('target').id === cell.id && source.attr('.rotatable/display') === 'normal' && target.attr('.rotatable/display') === 'normal') {
        			model.showLink(link.id);
        			var connectedCell = graph.getCell(link.get('source').id)
        			connectedCell.attr('circle/stroke', "blue");
        			connectedCell.attr('circle/stroke-width', 3);
        			connectedCell.attr('text/display', 'normal');
        		}
        		
        	});
        	
        	clicked = true;
        	cellClickedId = cell.id;
        	
        });
        
        paper.on('blank:pointerclick', function(evt, x, y) {
        	
        	$.each(graph.getElements(), function(index, cell) {
        		if(cell.get('subtype') === 'Access_Group') {
        			cell.attr('circle/stroke', "black");
        			cell.attr('circle/stroke-width', 1);
            		cell.attr('text/display', 'none');
        		}
        	});
        	
        	//hide links
        	model.hideLinks();
        	
        	clicked = false;
        	
        });
        
     	//handle with link movement and equipment hover
        var transition = false;
        paper.on('cell:pointermove', function(cellView, evt, x, y) {
        	
        	var cell = graph.getCell(cellView.model.id);
        	
        	if(cell.get('type') === 'link') {
        		transition = true;
        	}
        	
        });
        
        paper.on('cell:pointerup', function(cellView, evt, x, y) {
        	
        	var cell = graph.getCell(cellView.model.id);

        	if(cell.get('type') === 'link') {
        		transition = false;
        	}
        	
        });
        
        var transitionCell = undefined;
        var hoverCell = undefined;
        paper.on('cell:mouseover', function(cellView, evt) {
        	
        	var cell = graph.getCell(cellView.model.id);

        	if(cell.get('subtype') !== 'Access_Group') return;
        	
        	if(transition) {
        		transitionCell = {
        				'stroke' : cell.attr('circle/stroke'),
        				'strokeWidth' : cell.attr('circle/stroke-width'),
        				'textDisplay' : cell.attr('text/display'),
        		}	
        		if(cell.id !== cellClickedId) {
        			cell.attr('circle/stroke', "limegreen");
        			cell.attr('circle/stroke-width', 3);
        			cell.attr('text/display', 'normal');
        		}
            	return;
        	}
        	
        	if(clicked) {
        		hoverCell = {
        				'stroke' : cell.attr('circle/stroke'),
        				'strokeWidth' : cell.attr('circle/stroke-width'),
        				'textDisplay' : cell.attr('text/display'),
        		}
        		if(cell.id !== cellClickedId) {
        			cell.attr('circle/stroke', "red");
                	cell.attr('circle/stroke-width', 3);
                	cell.attr('text/display', 'normal');
        		}
        		return;
        	}
        	
        	cell.attr('circle/stroke', "red");
        	cell.attr('circle/stroke-width', 3);
        	cell.attr('text/display', 'normal');
        	
        	$.each(graph.getLinks(), function(index, link) {
        		
        		var source = graph.getCell(link.get('source').id);
        		var target = graph.getCell(link.get('target').id);
        		
        		//show links
        		if(link.get('source').id === cell.id && source.attr('.rotatable/display') === 'normal' && target.attr('.rotatable/display') === 'normal') {
        			model.showLink(link.id);
        			var connectedCell = graph.getCell(link.get('target').id);
        			connectedCell.attr('circle/stroke', "blue");
        			connectedCell.attr('circle/stroke-width', 3);
        			connectedCell.attr('text/display', 'normal');
        		}
        		if(link.get('target').id === cell.id && source.attr('.rotatable/display') === 'normal' && target.attr('.rotatable/display') === 'normal') {
        			model.showLink(link.id);
        			var connectedCell = graph.getCell(link.get('source').id)
        			connectedCell.attr('circle/stroke', "blue");
        			connectedCell.attr('circle/stroke-width', 3);
        			connectedCell.attr('text/display', 'normal');
        		}
        		
        	});
        	
        });
        
        paper.on('cell:mouseout', function(cellView, evt) {

        	var cell = graph.getCell(cellView.model.id);
        	if(cell.get('subtype') !== 'Access_Group') return;
        	
        	if(transition) {
        		cell.attr('circle/stroke', transitionCell.stroke);
    			cell.attr('circle/stroke-width', transitionCell.strokeWidth);
        		cell.attr('text/display', transitionCell.textDisplay);
        		return;
        	}
        	
        	if(clicked) {
        		if(cell.id !== cellClickedId) {
        			cell.attr('circle/stroke', hoverCell.stroke);
        			cell.attr('circle/stroke-width', hoverCell.strokeWidth);
            		cell.attr('text/display', hoverCell.textDisplay);
        		}
        		return;
        	}
        	
        	$.each(graph.getElements(), function(index, cell) {
        		if(cell.get('subtype') === 'Access_Group') {
        			cell.attr('circle/stroke', "black");
        			cell.attr('circle/stroke-width', 1);
            		cell.attr('text/display', 'none');
        		}
        	});
        	
//        	if(transition) return;
        	
        	//hide links
        	model.hideLinks();
        	
        });
        
      //Rigth click on mouse, show connections
    	paper.$el.on('contextmenu', function(evt) { 
    	    evt.stopPropagation(); // Stop bubbling so that the paper does not handle mousedown.
    	    evt.preventDefault();  // Prevent displaying default browser context menu.
    	    var cellView = paper.findView(evt.target);
    	    if (cellView) {
    	       // The context menu was brought up when clicking a cell view in the paper.

    	       var cell = cellView.model;
    	       if(cell.get('subtype') !== 'Access_Group') return;
    	       
    	       var connectedPorts = model.getConnectedPorts(cell);
//    	       console.log('connectedPorts' + JSON.stringify(connectedPorts));
    	       createEquipmentConnectionDialog(cell, connectedPorts);
    	       
    	    }
    	    
    	});
    	
    	//Generate connection dialog when drag a equipment to another
		function createEquipmentConnectionDialog(equipment, connectedPorts) {
			
			var outputs = owl.getOutputsFromOWL(equipment.id);
			var inputs = owl.getInputsFromOWL(equipment.id);
			var equipmentName = model.getEquipmentName(equipment);
			
			var content = 
				'<div class="bindsContainer"><div class="outputsList"><div id="listContainer">' +
				    '<ul id="expList" class="list"> <li class="outputs">Outputs</li> ' ;
			
			$.each(outputs, function(layer, value) {
				
				content = content + 
				    	'<li class="layer" value="' + layer + '" class="collapsed expanded">' + layer ;
				
				$.each(outputs[layer], function(key, output) {
					if(!connectedPorts[output.id]) {
						content = content +
							'<ul style="display: block;">' +
				                '<li class="outputItem" id="' + output.id + '" title="Output" value="' + output.name + '" class="collapsed expanded">' + output.name + '</li>' +
				            '</ul>';
					} 
					else {
						var input = connectedPorts[output.id];
						
						content = content +
						'<ul style="display: block;">' +
			                '<li class="outputItem connected" id="' + output.id + '" title="Output" value="' + output.name + '" class="collapsed expanded">' + 
			                	output.name +
			                	'<span class="tag" id="' + input.id + '">' + input.name + '</span>' +
			                '</li>' +
			            '</ul>';
					}
				});
				
				content = content + '</li>';
				
			});
			
			content = content + '</ul></div></div>';
			content = content + 
				'<div class="inputsList"><div id="listContainer">' +
			    	'<ul id="expList" class="list"> <li class="inputs">Inputs</li>';
			
			$.each(inputs, function(layer, value) {
				
				content = content + 
				    	'<li class="layer" value="' + layer + '" class="collapsed expanded">' + layer ;
				
				$.each(inputs[layer], function(key, input) {
					if(!connectedPorts[input.id]) {
						content = content +
							'<ul style="display: block;">' +
				                '<li class="inputItem" id="' + input.id + '" title="Input" value="' + input.name + '" class="collapsed expanded">' + input.name + '</li>' +
				            '</ul>';
					}
					else {
						var output = connectedPorts[input.id];
						
						content = content +
						'<ul style="display: block;">' +
						'<li class="inputItem connected" id="' + input.id + '" title="Input" value="' + input.name + '" class="collapsed expanded">' + 
								input.name +  
			                	'<span class="tag" id="' + output.id + '">' + output.name + '</span>' +
			                '</li>' +
			            '</ul>';
					}
				});
				
				content = content + '</li>';
				
			});
			
			content = content + '</ul></div></div></div>';
			
			//Create dialog
			var dialog = new joint.ui.Dialog({
				width: 500,
				type: 'neutral',
				title: equipmentName,
				content: content,
				buttons: [
				          { action: 'cancel', content: 'Close', position: 'right' },
				          ]
			});
			dialog.on('action:cancel', cancel);
			dialog.on('action:close', cancel);
			dialog.open();
			
			util.prepareList('.outputsList');
			util.prepareList('.inputsList');
			
			function cancel() {
				dialog.close();
			};
			
			$('span.tag').click(function(){
				
				var id = this.id;
				var parentId = $(this).closest('li').attr('id');
				
			});
			
		};
        
	},

	//Validator procedures
	initializeProvisioningValidatorProcedures : function(app) {
		
		var graph = app.graph;
		var validator = app.validator;
		var model = this.model;
		var owl = this.owl;
		var util = this.util;
		
		//create a dialog connection only if exist a target
		validator.validate('change:target change:source', function(err, command, next) {

			var cell = graph.getCell(command.data.id);
			
			if(!cell.attributes.target.id){
				cell.remove();
				return;
			}
			
			var source = graph.getCell(cell.attributes.source.id);
			var target = graph.getCell(cell.attributes.target.id);
			
			var connectedPorts = model.getConnectedPorts(source);
			createConnectionDialog(cell, source, target, connectedPorts);
			
		});
		
		//Generate connection dialog when drag a equipment to another
		function createConnectionDialog(cell, source, target, connectedPorts) {
			
			var outputs = owl.getOutputsFromOWL(source.id);
			
			var sourceName = model.getEquipmentName(source);
			var targetName = model.getEquipmentName(target);
			
			var content = 
				'<div class="bindsContainer"><div class="outputsList"><div id="listContainer">' +
				    '<ul id="expList" class="list"> <li class="outputs">' + sourceName + '</li> ' ;
			
			$.each(outputs, function(layer, value) {
				
				content = content + 
				    	'<li class="layer" value="' + layer + '" class="collapsed expanded">' + layer ;
				
				$.each(outputs[layer], function(key, output) {
					if(!connectedPorts[output.id]) {
						content = content +
							'<ul style="display: block;">' +
				                '<li class="outputItem" id="' + output.id + '" title="Output" value="' + output.name + '" class="collapsed expanded">' + output.name + '</li>' +
				            '</ul>';
					}
				});
				
				content = content + '</li>';
				
			});
			
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
			
			util.prepareList('.outputsList');
			generateOutputEvents(source, target);
			
			function cancel() {
				cell.remove();
				dialog.close();
			};
			
			//generate events to outputs list
			function generateOutputEvents(source, target) {
				
				$('.outputsList #expList').find('li.outputItem').click( function(event) {
					
					$('.dialog .controls .control-button[data-action="next"]').attr("disabled", true);
					
					$('.outputsList #expList li').removeClass('active');
					$(this).toggleClass('active');
					
					$('.inputsList #expList li:not(:first)').remove();
					
					var inputs = owl.getInputsFromOWL(target.id);
					
					var content = "";
					$.each(inputs, function(layer, value) {
						
						content = content + 
						    	'<li class="layer" value="' + layer + '" class="collapsed expanded">' + layer ;
						
						$.each(inputs[layer], function(key, input) {
							
							content = content +
								'<ul style="display: block;">' +
					                '<li class="inputItem" id="' + input.id + '" title="Input" value="' + input.name + '" class="collapsed expanded">' + input.name + '</li>' +
					            '</ul>';
							
						});
						
						content = content + '</li>';
						
					});
					
					$('.inputsList #expList').append(content);
					util.prepareList('.inputsList');
					
					generateInputEvents(source, target);
					
				});
				
			};
			
			//generate events to inputs list
			function generateInputEvents(source, target) {
				
				$('.inputsList #expList').find('li.inputItem').click(function(event) {
					
					$('.inputsList #expList li').removeClass('active');
					$(this).toggleClass('active');
					
					var output = {
							"id": $('.outputItem.active').attr('id'),
							"name": $('.outputItem.active').attr('value'),
							"type": "Output_Card"
					};
					
					var input = {
							"id": $('.inputItem.active').attr('id'),
							"name": $('.inputItem.active').attr('value'),
							"type": "Input_Card"
					};
					
					//connect target/source ports
					model.connectPorts(source, output, target, input)
					
					console.log('Out: ' + JSON.stringify(output));
					console.log('In: ' + JSON.stringify(input));
					
					dialog.close();
					
				});
				
			};
			
		}
		
	},
	
	//Toolbar procedures
	initializeToolbarProcedures : function(app) {
		
		var graph = app.graph;
		var model = this.model;
		var util = this.util;
		
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
		
		$('#btn-layer').click(function(){
			
			var content = 'Choose a techology: <select class="technology">';
			
			$.each(graph.getElements(), function(index, element) {
				
				if(element.get('subtype') === 'Layer_Network') {
					content = content + '<option value="' + element.get('technology') + '">' + element.get('technology') + '</option>';
				}
				
			})
			
			content = content + '</select>';
			
			var dialog = new joint.ui.Dialog({
			    width: 250,
			    closeButton: false,
			    title: 'Change Technology',
			    clickOutside: false,
				modal: false,
			    content: content,
			    buttons: [
				          { action: 'cancel', content: 'Cancel', position: 'right' },
				          { action: 'ok', content: 'Ok', position: 'right' },
				]
			});
			dialog.on('action:cancel', cancel);
			dialog.on('action:ok', changeTechnology);
			dialog.open();
			
			function cancel() {
				dialog.close();
			}
			
			function changeTechnology() {
				var tech = $( ".technology option:selected" ).text();
				model.showTech(graph, tech);
				dialog.close();
			}
			
		});
		
	},
	
	//Provisioning file procedures
	initializeProvisioningFileProcedures : function(app) {
		
		var file = this.file;
		var graph = app.graph;
		
		//procedure to open a file from URL
		if(file.getUrlParameter('provisioning')){
        	var filename = file.getUrlParameter('provisioning');
        	file.openFromURL(graph, filename);
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