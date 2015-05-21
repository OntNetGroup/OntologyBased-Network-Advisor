var deletedITUFiles = [];
var index = 0;

function closeIframe(){
	$('#itu-dialog').dialog("close");
};

function ituHandle(paper, graph){

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
			            	
			            	var cell = graph.getCell(cellId);
			            	cell.attr('.inPort/fill', '#1AFF00');
			            	cell.attr('.inPort/stroke', '#000000');
			            	cell.attr('.outPort/fill', '#1AFF00');
			            	cell.attr('.outPort/stroke', '#000000');
			            	
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
