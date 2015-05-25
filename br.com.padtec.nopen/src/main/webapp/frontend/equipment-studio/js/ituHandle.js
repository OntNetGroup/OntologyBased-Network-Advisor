var deletedITUFiles = [];
var index = 0;

function closeIframe(){
	$('#itu-dialog').dialog("close");
};

function ituHandle(paper, graph){

	
	
	paper.on('cell:mouseover', function( cellView , evt, x, y) {
		
		cellId = cellView.model.id;
		var cell = graph.getCell(cellId);
		
		if(cell.get('subType') === 'card') {
			
			$('.outPort').mouseenter(function() {
				cell.attr('.outPort/fill', '#15D200');
			});
			
			$('.outPort').mouseleave(function() {
				cell.attr('.outPort/fill', '#1AFF00');
			});
		}
		
	});
	
	paper.on('cell:pointerdblclick', function( cellView , evt, x, y) {
	
		cellId = cellView.model.id;
		
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
	
	// valida a criação de links no grafo
	graph.on('change:source change:target add', function(cell) {
		if(isNotLink(cell)) return;
		
		var card = graph.getCell(cell.attributes.source.id);
	});
	
    /* ------ AUXILIAR FUNCTIONS ------- */
	// Check if cell is not a link
	function isNotLink(cell) {
	    if (cell.attributes.type !== 'link') return true;
	};
};
