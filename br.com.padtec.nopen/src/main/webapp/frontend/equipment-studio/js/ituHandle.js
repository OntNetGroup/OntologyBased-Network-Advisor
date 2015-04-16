// GLOBAL VARS
var cardArray = new Object();
var cellId;

var saveDialog = new joint.ui.Dialog({
	type: 'neutral' ,
	width: 420,
	draggable: false,
	title: 'Card Saved! ',
	content: 'The card was saved!!',
	open: function() {}
});


function closeIframe(){
	$('#itu-dialog').dialog("close");
};

function ituHandle(paper, graph){

	paper.on('cell:pointerdblclick', function( cellView , evt, x, y) {
	
		cellId = cellView.model.id;
		
		var equipment = graph.getCell(cellId);
		if((equipment.get('subType')) === 'card' || (equipment.get('subType')) === 'supervisor') {
			
			$("#itu-iframe").empty();
			
			$(function ()    {
		        $('#itu-dialog').dialog({
		            modal: true,
		            //show: 'scale',
		            height: $(window).height(),
					width: $(window).width(),
		            title: 'Dynamically Loaded Page',
		            open: function ()
		            {
		                $('#itu-iframe').attr('src','/nopen/itu-studio.htm');
		            },
		            close: function() {
		            	if($('#itu-iframe').get(0).contentWindow.closeType == "save"){
		            		cardArray[cellId] = $('#itu-iframe').get(0).contentWindow.app.graph.toJSON();
		            		saveDialog.open();		            		
		            	}
		            	//var iFrameValue = JSON.stringify($('#itu-iframe').get(0).contentWindow.app.graph.toJSON(), null, 2);
		            	//alert(iFrameValue);
		            	

		            }
		        });
		    });
			
		}
	});
};
