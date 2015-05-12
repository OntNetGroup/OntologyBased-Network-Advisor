function importTopology (graph) {

	var file = $('#file').prop('files')[0]; 
	//document.getElementById('file').files[0];

	$.ajax({
	   type: "POST",
	   url: "importTopology.htm",
	   data: file,
	   processData: false,
	   dataType: 'json',
	   success: function(data){   
		   graph.fromJSON(transformToGraph(data));
		   $("#btn-layout").click();
	   },
	   error : function(e) {
		   alert("error: " + e.status);
	   }
	});
	
};

function transformToGraph(data){
	
	uuid = data.topology.id;
	
	var cellsArray = new Array();
	
	if(data.topology.nodes.node){
	
		$.each(data.topology.nodes.node, function( index, value ) {
	
			var circle = {
				fill: '#00c6ff'
			}
			
			var text = {
				text : value.name,
				'font-size' : '8',
			}
			
			var equipment = {
				template : value.equipment	
			}
			
			var attrs = {
				circle : circle,
				text : text,	
				equipment : equipment
			}
			
			var cell = {
				type : "basic.Circle",
				id : value.id,
				attrs : attrs
			};
			
			cellsArray.push(cell);
			
		});
	}
		
	if(data.topology.links.link){
		
		$.each(data.topology.links.link, function( index, value ) {
			
			var source = {
				id : value.source,
			}
			
			var target = {
				id : value.target,
			}
			
			var cell = {
				type : "link",
				source : source,
				target : target,
				id : value.id
			};
			
			cellsArray.push(cell);
			
		});
		
	}
	
	
	var cells = {
		cells : cellsArray
	};
	
	return cells;
	
}
