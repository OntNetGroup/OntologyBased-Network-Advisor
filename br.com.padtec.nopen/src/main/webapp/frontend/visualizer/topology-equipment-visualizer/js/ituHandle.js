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

		if((equipment.get('subType')) === 'Card') {
			
			console.log(equipment);
			
			$("#itu-iframe").empty();
			
			var cardSupervisor = cellView.model.attributes.Supervisor;
			
			console.log(cardSupervisor);
			
			var filename = cellView.model.attr('equipment/template');
			console.log(filename);
			
			$(function ()    {
		        $('#itu-dialog').dialog({
		            modal: true,
		            //show: 'scale',
		            height: 800,
	    			width: 600,
		            title: 'Dynamically Loaded Page',
		            open: function ()
		            {
//		                $('#itu-iframe').attr('src','/nopen/itu-visualizer.htm');
		                $("#itu-iframe").attr('src','/nopen/itu-visualizer.htm?equipment=' + cardSupervisor + '&card=' + cellId);
		            },
		            close: function() {
		            }
		        });
		    });
		
		}
	});
};
