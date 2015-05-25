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
				            	
				            	// Get all elements and check if exist some in or out port
				            	$.each($('#itu-iframe').get(0).contentWindow.app.graph.getElements(), function(index, value) {
				            		
//				            		console.log(JSON.stringify(value));
//				            		console.log(value.get('subtype'));
				            		
				            		if(value.get('subtype') === 'in') {
				            			cell.attr('.inPort/fill', '#1AFF00');
						            	cell.attr('.inPort/stroke', '#000000');
						            	hasIn = true;
						            	
						            	cell.attributes.inPorts[value.id] = value.attr('text/text');
						            	
						            	console.log("ID IN: " + value.id);
						            	console.log("IN PORT: " + cell.attributes.inPorts[value.id]);
				            		}
				            		
				            		if(value.get('subtype') === 'out') {
				            			cell.attr('.outPort/fill', '#1AFF00');
						            	cell.attr('.outPort/stroke', '#000000');
						            	hasOut = true;
						            	
						            	cell.attributes.outPorts[value.id] = value.attr('text/text');
						            	
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
	
	function createConnectionDialog(cell, source, target) {
		
		var content = '<table class="connectionInOut"><tr><td>' +
		 				'<table class="connectionOut">' + 
							'<tr>' +
								'<th>out: ' + source.attr('name/text') + '</th></tr>';
		
		$.each(source.attributes.outPorts, function(index, value){
			content = content + '<tr><td id="'+ index + '">' + source.attributes.outPorts[index] + '</td></tr>';
		});
			
		content = content +  '</table></td>';
		
		content = content + '<td><table class="connectionIn">' + 
								'<tr><th>in: ' + target.attr('name/text') + '</th></tr>' + 
							'</table></td></tr></table>';
		
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
		
		$('.connectionOut td').click(function(){
			
			$('.connectionOut td').removeClass('active');
			
			$(this).toggleClass('active');
			var index = $(this).attr('id');
			
			var content = '';
			$.each(target.attributes.inPorts, function(index, value){
				content = content + '<tr><td id="'+ index + '">' + target.attributes.inPorts[index] + '</td></tr>';
			});
			
			$(".connectionIn").find("tr:gt(0)").remove();
			$(".connectionIn").append(content);
			
			console.log("SHOW: " + target.attr('name/text'));
			console.log("ID: " + index);
			console.log("NAME: " + source.attributes.outPorts[index]);
		});
		
		$('.connectionIn').delegate('td', 'click', function() {
			
			var sourceIndex = $('.connectionOut td.active').attr('id');
			var targetIndex = $(this).attr('id');
			
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
