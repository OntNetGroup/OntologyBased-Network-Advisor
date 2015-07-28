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

		if((equipment.get('subType')) === 'Supervisor'){
		
			selectSupervisorWindow(equipment,graph);
			
		};

		if((equipment.get('subType')) === 'Card') {
			
			$("#itu-iframe").empty();
			console.log($("#filename").val());
			var filename = cellView.model.attr('equipment/template');
			
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
		                $("#itu-iframe").attr('src','/nopen/itu-visualizer.htm?equipment=' + $("#filename").val() + '&card=' + cellId);
		            },
		            close: function() {
		            }
		        });
		    });
		
		}
	});
};
