var deletedITUFiles = [];
var index = 0;

function closeIframe(){
	$('#itu-dialog').dialog("close");
};

function ituHandle(paper, graph, validator){

	//Rigth click on mouse, show connections
	paper.$el.on('contextmenu', function(evt) { 
	    evt.stopPropagation(); // Stop bubbling so that the paper does not handle mousedown.
	    evt.preventDefault();  // Prevent displaying default browser context menu.
	    var cellView = paper.findView(evt.target);
	    if (cellView) {
	       // The context menu was brought up when clicking a cell view in the paper.
	       console.log(cellView.model);  // So now you have access to both the cell view and its model.

	       var source = cellView.model;
	       var target = cellView.model;
	       
	       if(source.get('subType') !== 'Card') return;
	       
	       var content = '<table class="connectionInOut"><tr><td>' +
			'<table class="connectionOut">' + 
				'<tr>' +
					'<th>Output</th></tr>';

	       $.each(source.attributes.outPorts, function(index, value){

	    	   var outPort = source.attributes.outPorts[index];

	    	   if(source.attributes.connectedPorts[index]) {
	    		   var inPortIndex = source.attributes.connectedPorts[index]["id"];
	    		   var inPort = source.attributes.connectedPorts[index]["name"];
	    		   content = content + '<tr><td class="connected" id="'+ index + '">' + outPort + '<span class="tag" id="' + inPortIndex + '">' + inPort + '</span></td></tr>';
	    	   }
	    	   else {
	    		   content = content + '<tr><td class="disconnected" id="'+ index + '">' + outPort + '</td></tr>';
	    	   }
	       });

	       content = content +  '</table></td><td><table class="connectionIn">' + 
	       '<tr><th>Input</th></tr>'; 

	       $.each(target.attributes.inPorts, function(index, value){
				
				var inPort = target.attributes.inPorts[index];
				
				if(target.attributes.connectedPorts[index]) {
					var outPortIndex = target.attributes.connectedPorts[index]["id"];
					var outPort = target.attributes.connectedPorts[index]["name"];
					content = content + '<tr><td class="connected" id="'+ index + '">' + inPort + ' <span class="tag" id="' + outPortIndex + '">' + outPort + ' </span></td></tr>';
				}
				else{
					content = content + '<tr><td class="disconnected" id="'+ index + '">' + inPort + '</td></tr>';
				}
			});
			
	       content = content + '</table></td></tr></table>';
	       
	       var dialog = new joint.ui.Dialog({
	    	   width: 500,
	    	   type: 'neutral',
	    	   title: target.attr('name/text') + ' Connections',
	    	   content: content,
	    	   buttons: [
	    	             { action: 'cancel', content: 'Close', position: 'left' },
	    	             ]
	       });
	       dialog.on('action:cancel', cancel);
	       dialog.on('action:close', cancel);
	       dialog.open();

	       function cancel() {
	    	   dialog.close();
	       };
	       
	       
	       //delete connections
			$('.connectionInOut').delegate('span.tag', 'click', function(){
				if(confirm("Really delete this connection?")) { 
					$(this).parent().switchClass('connected', 'disconnected');
					
					var id = $(this).attr('id');
					var idParent = $(this).parent().attr('id');
					
					var port1 = undefined, port2 = undefined;
					$.each(graph.getElements(), function(index, c) {
						
						//delete ports
						if(c.get('subType') === 'Card') {
							if(c.attributes.connectedPorts[id]) {
								port1 = c.id;
								delete c.attributes.connectedPorts[id];
							}
							if(c.attributes.connectedPorts[idParent]){
								port2 = c.id;
								delete c.attributes.connectedPorts[idParent];
							}
						}
						
					});
					
					var removed = false;
					$.each(graph.getLinks(), function(index, l) {
						//check if link already removed
						if(!removed) {
							if(l.get('source').id === port1 && l.get('target').id === port2) {
								l.remove();
								removed = true;
							}
							else if(l.get('target').id === port1 && l.get('source').id === port2) {
								l.remove();
								removed = true;
							}
						}
					});
					
					//remove tag
					$('span#' + id + '.tag').remove();
					$('span#' + idParent + '.tag').remove();
					$('td#' + id).switchClass('connected', 'disconnected');
					
				}
			});
	       
	    }
	});
	
	//open itu studio when double click on Card
	paper.on('cell:pointerdblclick', function( cellView , evt, x, y) {
	
		var cellId = cellView.model.id;
		var equipment = graph.getCell(cellId);
		
		if((equipment.get('subType')) === 'Card') {
			
			$("#itu-iframe").empty();
			
			if($('#filename').val() == ""){
				if (confirm('You need save the Template before edit the Card. Do you like to do it?')) {
        			generateSaveTemplateDialog(graph);
				} 
        	}
			else if(!equipment.get('SupervisorID')) {
				alert('Error! The Card needs to be connected with a Supervisor');
				return;
			}
        	else{
				$(function ()    {
			        $('#itu-dialog').dialog({
			            modal: true,
			            //show: 'scale',
			            height: $(window).height(),
						width: $(window).width(),
			            //title: 'Dynamically Loaded Page',
			            open: function ()
			            {
		            		$('#itu-iframe').attr('src','/nopen/itu-studio.htm?equipment=' + $("#filename").val() + '&card=' + cellId);
			            },
			            close: function() {
			            	
			            	//var ituGraph = $('#itu-iframe').get(0).contentWindow.app.graph;
			            	
			            	//If changes were not discarded
			            	if(!$('#itu-iframe').get(0).contentWindow.discardChanges) {
			            	
				            	var cell = graph.getCell(cellId);
				            	var hasIn = false, hasOut = false;
				            	
				            	cell.attr(".inPort/title", "");
				            	cell.attr(".outPort/title", "");
				            	
				            	
				            	var inIds = [];
				            	var outIds = [];
				            	
				            	// Get all elements and check if exist some in or out port
				            	$.each($('#itu-iframe').get(0).contentWindow.app.graph.getElements(), function(index, value) {
				            		
				            		if(value.get('subtype') === 'Input_Card') {
				            			cell.attr('.inPort/fill', '#1AFF00');
						            	cell.attr('.inPort/stroke', '#000000');
						            	hasIn = true;
						            	
						            	cell.attributes.inPorts[value.id] = value.attr('text/text');
						            	inIds.push(value.id);
				            		}
				            		
				            		if(value.get('subtype') === 'Output_Card') {
				            			cell.attr('.outPort/fill', '#1AFF00');
						            	cell.attr('.outPort/stroke', '#000000');
						            	hasOut = true;
						            	
						            	cell.attributes.outPorts[value.id] = value.attr('text/text');
						            	outIds.push(value.id);
				            		}
				            		
				            	});
				        		
				            	//delete inPort if the in port was delete in ITU studio
				            	$.each(cell.attributes.inPorts, function(index, value) {
				            		var deleteIndex = true;
				            		$.each(inIds, function(i, value) {
					            		if(index === value) {
					            			deleteIndex = false;
					            		}
					            	});
				            		if(deleteIndex) {
				            			deleteConnectedPort(index, cell.attributes.connectedPorts[index]["id"]);
				            			delete cell.attributes.inPorts[index];
				            		}
				            	});
				            	
				            	//delete outPort if the out port was delete in ITU studio
				            	$.each(cell.attributes.outPorts, function(index, value) {
				            		var deleteIndex = true;
				            		$.each(outIds, function(i, value) {
					            		if(index === value) {
					            			deleteIndex = false;
					            		}
					            	});
				            		if(deleteIndex) {
				            			deleteConnectedPort(index, cell.attributes.connectedPorts[index]["id"]);
				            			delete cell.attributes.outPorts[index];
				            		}
				            	});
				            	
				            	//delete connected ports
				            	function deleteConnectedPort(id, idParent) {
				            		
				            		var port1 = undefined, port2 = undefined;
				    				$.each(graph.getElements(), function(index, c) {
				    					
				    					//delete ports
				    					if(c.get('subType') === 'Card') {
				    						if(c.attributes.connectedPorts[id]) {
				    							port1 = c.id;
				    							delete c.attributes.connectedPorts[id];
				    						}
				    						if(c.attributes.connectedPorts[idParent]){
				    							port2 = c.id;
				    							delete c.attributes.connectedPorts[idParent];
				    						}
				    					}
				    					
				    				});
				    				
				    				var removed = false;
				    				$.each(graph.getLinks(), function(index, l) {
				    					//check if link already removed
				    					if(!removed) {
				    						if(l.get('source').id === port1 && l.get('target').id === port2) {
				    							l.remove();
				    							removed = true;
				    						}
				    						else if(l.get('target').id === port1 && l.get('source').id === port2) {
				    							l.remove();
				    							removed = true;
				    						}
				    					}
				    				});
									
				            	}
				            	
				            	if(!hasIn) {
				            		cell.attr('.inPort/fill', 'none');
					            	cell.attr('.inPort/stroke', 'none');
				            	}
				            	if(!hasOut) {
				            		cell.attr('.outPort/fill', 'none');
					            	cell.attr('.outPort/stroke', 'none');
				            	}
				            	
				            	//Save template
				            	var saveDialog = new joint.ui.Dialog({
				        			type: 'neutral' ,
				        			width: 420,
				        			draggable: false,
				        			title: 'Template Saved! ',
				        			content: 'The template was saved!!',
				        			open: function() {}
				        		});
				            	
				            	//Save template
				            	$.ajax({
				         		   type: "POST",
				         		   async: false,
				         		   url: "saveTemplate.htm",
				         		   data: {
				         			 'filename': $("#filename").val(),
				         			 'graph': JSON.stringify(graph.toJSON()),
				         		   },
				         		   success: function(){ 	
				         			  saveDialog.open();
				         		   },
				         		   error : function(e) {
				         			   alert("error: " + e.status);
				         		   }
				         		});
			            	
			            	}
			            	
			            },
			        });
			    });
        	}
		}
	});
	
	graph.on('remove', function(cell){
		
		if(cell.get('subType') === 'Card'){
			deletedITUFiles[index] = cell.id;
			index++;
		}
		
	});
	
	//delete link if not exist a target
	validator.validate('add', function(err, command, next) {

		var cell = graph.getCell(command.data.id);
		
		if(isNotLink(cell)) return;
		
		if(!cell.attributes.target.id){
			cell.remove();
		}
		
	});
	
	//create a dialog connection only if exist a target
	validator.validate('change:target change:source', function(err, command, next) {

		var cell = graph.getCell(command.data.id);
		
		if(isNotLink(cell)) return;
		
		if(!cell.attributes.target.id){
			cell.remove();
			return;
		}
		
		var source = graph.getCell(cell.attributes.source.id);
		var target = graph.getCell(cell.attributes.target.id);
		
//		console.log('source ID: ' + source.attr('name/text'));
//		console.log('target ID: ' + target.id);
		
		createConnectionDialog(cell, source, target);
		
	});
	
	//Generate connection dialog when drag out port to in port
	function createConnectionDialog(cell, source, target) {
		
		var content = '<table class="connectionInOut"><tr><td>' +
		 				'<table class="connectionOut">' + 
							'<tr>' +
								'<th>out: ' + source.attr('name/text') + '</th></tr>';
		
		//Generate out ports
		$.each(source.attributes.outPorts, function(index, value){
			
			var outPort = source.attributes.outPorts[index];
			
			if(source.attributes.connectedPorts[index]) {
				var inPortIndex = source.attributes.connectedPorts[index]["id"];
				var inPort = source.attributes.connectedPorts[index]["name"];
				content = content + '<tr><td class="connected" id="'+ index + '">' + outPort + '<span class="tag" id="' + inPortIndex + '">' + inPort + '</span></td></tr>';
			}
			else {
				content = content + '<tr><td class="disconnected" id="'+ index + '">' + outPort + '</td></tr>';
			}
		});
			
		content = content +  '</table></td>';
		
		content = content + '<td><table class="connectionIn">' + 
								'<tr><th>in: ' + target.attr('name/text') + '</th></tr>' + 
							'</table></td></tr></table>';
		
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
		
		//Generate in ports when click on out port
		$('.connectionOut').delegate('td.disconnected', 'click', function() {
			
			$('.connectionOut td').removeClass('active');
			
			$(this).toggleClass('active');
			var index = $(this).attr('id');
			console.log($(this));
			var content = '';
			$.each(target.attributes.inPorts, function(index, value){
				
				var inPort = target.attributes.inPorts[index];
				
				if(target.attributes.connectedPorts[index]) {
					var outPortIndex = target.attributes.connectedPorts[index]["id"];
					var outPort = target.attributes.connectedPorts[index]["name"];
					content = content + '<tr><td class="connected" id="'+ index + '">' + inPort + ' <span class="tag" id="' + outPortIndex + '">' + outPort + ' </span></td></tr>';
				}
				else{
					content = content + '<tr><td class="disconnected" id="'+ index + '">' + inPort + '</td></tr>';
				}
			});
			
			$(".connectionIn").find("tr:gt(0)").remove();
			$(".connectionIn").append(content);
			
			console.log("SHOW: " + target.attr('name/text'));
			console.log("ID: " + index);
			console.log("NAME: " + source.attributes.outPorts[index]);
		});
		
		//delete connections
		$('.connectionInOut').delegate('span.tag', 'click', function(){
			if(confirm("Really delete this connection?")) { 
				$(this).parent().switchClass('connected', 'disconnected');
				
				var id = $(this).attr('id');
				var idParent = $(this).parent().attr('id');
				
				var port1 = undefined, port2 = undefined;
				$.each(graph.getElements(), function(index, c) {
					
					//delete ports
					if(c.get('subType') === 'Card') {
						if(c.attributes.connectedPorts[id]) {
							port1 = c.id;
							delete c.attributes.connectedPorts[id];
						}
						if(c.attributes.connectedPorts[idParent]){
							port2 = c.id;
							delete c.attributes.connectedPorts[idParent];
						}
					}
					
				});
				
				var removed = false;
				$.each(graph.getLinks(), function(index, l) {
					//check if link already removed
					if(!removed) {
						if(l.get('source').id === port1 && l.get('target').id === port2) {
							l.remove();
							removed = true;
						}
						else if(l.get('target').id === port1 && l.get('source').id === port2) {
							l.remove();
							removed = true;
						}
					}
				});
				
				//remove tag
				$(this).remove(); 
			}
		});
		
		//handle with in connections
		$('.connectionIn').delegate('td.disconnected', 'click', function() {
			
			var sourceIndex = $('.connectionOut td.active').attr('id');
			var targetIndex = $(this).attr('id');
			
			//outPort index -> inPort index
			source.attributes.connectedPorts[sourceIndex] = {};
			source.attributes.connectedPorts[sourceIndex]["id"] = targetIndex;
			source.attributes.connectedPorts[sourceIndex]["name"] = target.attributes.inPorts[targetIndex];
			source.attributes.connectedPorts[sourceIndex]["type"] = "Input_Card";
			
			target.attributes.connectedPorts[targetIndex] = {};
			target.attributes.connectedPorts[targetIndex]["id"] = sourceIndex;
			target.attributes.connectedPorts[targetIndex]["name"] = source.attributes.outPorts[sourceIndex];
			target.attributes.connectedPorts[targetIndex]["type"] = "Output_Card";
			
//			console.log("CONNECTION: " + source.attributes.connectedPorts[sourceIndex]);
//			console.log("CONNECTION " + source.attributes.outPorts[sourceIndex] + " > " + target.attributes.inPorts[targetIndex] + " CREATED")

			
//			console.log(source.attributes.connectedPorts[sourceIndex]);
	
			var	sourceID = source.attributes.connectedPorts[sourceIndex]["id"]
			var	sourceName = source.attributes.connectedPorts[sourceIndex]["name"];
			var sourceType = source.attributes.connectedPorts[sourceIndex]["type"];	
			
			var targetID = target.attributes.connectedPorts[targetIndex]["id"];
			var targetName = target.attributes.connectedPorts[targetIndex]["name"];
			var targetType = target.attributes.connectedPorts[targetIndex]["type"];
	
			
			
var result = EquipStudioPerformBind(sourceID, sourceName, sourceType, targetID, targetName, targetType);
        	
        	if(result === "success") {
        		dialog.close();
			} else {
				return new joint.ui.Dialog({
					type: 'alert',
					width: 400,
					title: 'Error',
					content: result,
				}).open();		
			}
			
			
		});
		
	}
	
	
    /* ------ AUXILIAR FUNCTIONS ------- */
	// Check if cell is not a link
	function isNotLink(cell) {
	    if (cell.attributes.type !== 'link') return true;
	};
};
