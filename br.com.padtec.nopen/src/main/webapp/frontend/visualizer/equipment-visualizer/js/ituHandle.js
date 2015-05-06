// GLOBAL VARS
var cardArray = new Object();
var cellId;

function closeIframe(){
	$('#itu-dialog').dialog("close");
};

function ituHandle(paper, graph){

	paper.on('cell:pointerdblclick', function( cellView , evt, x, y) {
	
		cellId = cellView.model.id;
		
		var equipment = graph.getCell(cellId);
		
		if((equipment.get('subType')) === 'supervisor'){
		
			selectSupervisorWindow(equipment,graph);
			
		};
		
		if((equipment.get('subType')) === 'card') {
			
			$("#itu-iframe").empty();
			
			$(function ()    {
		        $('#itu-dialog').dialog({
		            modal: true,
		            //show: 'scale',
		            height: 500,
					width: 500,
		            title: 'Dynamically Loaded Page',
		            open: function ()
		            {
		                $('#itu-iframe').attr('src','/nopen/itu-visualizer.htm');
		            },
		            close: function() {
		            }
		        });
		    });
		
		}
	});
};
