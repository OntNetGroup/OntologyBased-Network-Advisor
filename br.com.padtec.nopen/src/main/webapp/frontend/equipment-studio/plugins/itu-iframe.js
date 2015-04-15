// GLOBAL VARS
var cellId;

function closeIFrame(){
	$('#itu-dialog').dialog("close");
};

function equipmentHandle2(paper, graph){

	paper.on('cell:pointerdblclick', function( cellView , evt, x, y) {
	
		cellId = cellView.model.id;

		var equipment = graph.getCell(cellId);
		if((equipment.get('subType')) === 'card' || (equipment.get('subType')) === 'supervisor') {
			
			$("#itu-iframe").empty();
			
			$(function ()    {
		        $('#itu-dialog').dialog({
		            modal: true,
		                  
		            height: $(window).height(),
					width: $(window).width(),
		            title: 'Dynamically Loaded Page',
		            open: function ()
		            {
		                //$(this).load("/nopen/itu-studio.htm");
		                $('#itu-iframe').attr('src','/nopen/itu-studio.htm');
		            },   
		            close: function() {
		            	//alert("hello");
		            	var iFrameValue = JSON.stringify($('#itu-iframe').get(0).contentWindow.app.graph.toJSON(), null, 2);
		            	alert(iFrameValue);
		            }
		        });
		    });
			
		}
	});
};
