var saveDialog = new joint.ui.Dialog({
	type: 'neutral' ,
	width: 420,
	draggable: false,
	title: 'Card Saved! ',
	content: 'The card was saved!!',
	open: function() {}
});


function saveITUFile(equipment, filename, graph){
	
	$.ajax({
		type: "POST",
		url: "saveITUFile.htm",
		data: {
			'path' : equipment,
			'filename' : filename,
			'graph': JSON.stringify(graph.toJSON()),
		},
		dataType: 'json',
		success: function(data){
			saveDialog.open();
		},
		error : function(e) {
			//alert("error: " + e.status);
		}
	});
	
}