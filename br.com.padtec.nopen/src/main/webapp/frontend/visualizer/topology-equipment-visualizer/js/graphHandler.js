function graphHandler(app, graph , paper) {
	
	
//	$.getJSON('/otn-protocol', { get_param: 'value' }, function(data) {
//	    $.each(data, function(index, element) {
//	        $('body').append($('<div>', {
//	            text: element.name
//	        }));
//	    });
//	});

graph.on('change' , function (cell) {
		//The equipment cant be removed from inside the equipmentholder
		var parentId = cell.get('parent');
		if (!parentId) return;

		var parent = graph.getCell(parentId);
		var parentBbox = parent.getBBox();
		var cellBbox = cell.getBBox();

		if (parentBbox.containsPoint(cellBbox.origin()) && parentBbox.containsPoint(cellBbox.topRight()) &&
				parentBbox.containsPoint(cellBbox.corner()) &&
				parentBbox.containsPoint(cellBbox.bottomLeft())) {

			return;
		}
		cell.set('position', cell.previous('position'));

	},this);

paper.on('cell:pointerclick', function( cellView , evt, x, y) {
	
	var cellId = cellView.model.id;
	var equipment = graph.getCell(cellId);
	
	if(equipment.get('subType') === 'Card'){
		equipment.prop('attributes','');
		console.log(equipment);
		console.log(cellView);
		
		$.ajax({
			   type: "POST",
			   async: false,
			   url: "getCardAttributes.htm",
			   data: {
				   'card' : cellId
			   },
//			   dataType: 'json',
			   success: function(data){
				   console.log(data);
			   },
			   error : function(e) {
				   alert("error: " + e.status);
			   }
			});
	};
	
},this);

};