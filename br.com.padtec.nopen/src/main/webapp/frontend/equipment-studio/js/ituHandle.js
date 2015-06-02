var deletedITUFiles = [];
var index = 0;

function closeIframe(){
	$('#itu-dialog').dialog("close");
};

function ituHandle(paper, graph, validator){

	paper.on('cell:pointerdblclick', function( cellView , evt, x, y) {
	
		var cellId = cellView.model.id;
		var equipment = graph.getCell(cellId);
		
		if((equipment.get('subType')) === 'card') {
			
			$("#itu-iframe").empty();
			
			if($('#filename').val() == ""){
				if (confirm('You need save the Template before edit the card. Do you like to do it?')) {
        			generateSaveTemplateDialog(graph);
				} 
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
				            	
				            	// Get all elements and check if exist some in or out port
				            	$.each($('#itu-iframe').get(0).contentWindow.app.graph.getElements(), function(index, value) {
				            		
				            		if(value.get('subtype') === 'in') {
				            			cell.attr('.inPort/fill', '#1AFF00');
						            	cell.attr('.inPort/stroke', '#000000');
						            	hasIn = true;
						            	
						            	cell.attributes.inPorts[value.id] = value.attr('text/text');
						            	
						            	cell.attr(".inPort/title", cell.attr(".inPort/title") + value.attr('text/text') + '\r\n');
						            	
						            	console.log("ID IN: " + value.id);
						            	console.log("IN PORT: " + cell.attributes.inPorts[value.id]);
				            		}
				            		
				            		if(value.get('subtype') === 'out') {
				            			cell.attr('.outPort/fill', '#1AFF00');
						            	cell.attr('.outPort/stroke', '#000000');
						            	hasOut = true;
						            	
						            	cell.attributes.outPorts[value.id] = value.attr('text/text');
						            	
						            	cell.attr(".outPort/title", cell.attr(".outPort/title") + value.attr('text/text') + '\r\n');
						            	
						            	console.log("ID OUT: " + value.id);
						            	console.log("OUT PORT: " + cell.attributes.outPorts[value.id]);
				            		}
				            		
				            	});
				        		
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
		
		//console.log(cell.get('subType'));
		
		if(cell.get('subType') === 'card'){
			deletedITUFiles[index] = cell.id;
			index++;
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
		
		console.log('source ID: ' + source.attr('name/text'));
		console.log('target ID: ' + target.id);
		
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
				var inPortIndex = source.attributes.connectedPorts[index][0];
				var inPort = source.attributes.connectedPorts[index][1];
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
		
		//Generate in ports
		$('.connectionOut').delegate('td.disconnected', 'click', function() {
			
			$('.connectionOut td').removeClass('active');
			
			$(this).toggleClass('active');
			var index = $(this).attr('id');
			
			var content = '';
			$.each(target.attributes.inPorts, function(index, value){
				
				var inPort = target.attributes.inPorts[index];
				
				if(target.attributes.connectedPorts[index]) {
					var outPortIndex = target.attributes.connectedPorts[index][0];
					var outPort = target.attributes.connectedPorts[index][1];
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
					if(c.get('subType') === 'card') {
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
				
				$.each(graph.getLinks(), function(index, l) {
					
					if(l.get('source') && l.get('target') && port1 && port2) {
						
						if(l.get('source').id === port1 && l.get('target').id === port2) {
							l.remove();
						}
						else if(l.get('target').id === port1 && l.get('source').id === port2) {
							l.remove();
						}
					}
					
				});
				
				
				console.log('ID TAG: ' + $(this).attr('id'));
				console.log('ID TD: ' + $(this).parent().attr('id'));
				
				$(this).remove(); 
			}
		});
		
		$('.connectionIn').delegate('td.disconnected', 'click', function() {
			
			var sourceIndex = $('.connectionOut td.active').attr('id');
			var targetIndex = $(this).attr('id');
			
			//outPort index -> inPort index
			source.attributes.connectedPorts[sourceIndex] = {};
			source.attributes.connectedPorts[sourceIndex][0] = targetIndex;
			source.attributes.connectedPorts[sourceIndex][1] = target.attributes.inPorts[targetIndex];
			
			target.attributes.connectedPorts[targetIndex] = {};
			target.attributes.connectedPorts[targetIndex][0] = sourceIndex;
			target.attributes.connectedPorts[targetIndex][1] = source.attributes.outPorts[sourceIndex];
			
			console.log("CONNECTION: " + source.attributes.connectedPorts[sourceIndex]);
			console.log("CONNECTION " + source.attributes.outPorts[sourceIndex] + " > " + target.attributes.inPorts[targetIndex] + " CREATED")
			dialog.close();
			
		});
		
	}
	
	
	
	
	// valida a criação de links no grafo
	graph.on('change:source change:target add', function(cell) {
		
	});
	
    /* ------ AUXILIAR FUNCTIONS ------- */
	// Check if cell is not a link
	function isNotLink(cell) {
	    if (cell.attributes.type !== 'link') return true;
	};
};
