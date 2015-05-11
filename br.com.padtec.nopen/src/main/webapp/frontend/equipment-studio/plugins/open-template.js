function getTemplates(graph){

	$.ajax({
		type: "GET",
		url: "getAllTemplates.htm",
		dataType: 'json',
		success: function(data){ 		   
			generateOpenTemplateDialog(graph, data)
		},
		error : function(e) {
			alert("error: " + e.status);
		}
	});

}

function getUrlParameter(sParam)
{
	var sPageURL = window.location.search.substring(1);
	var sURLVariables = sPageURL.split('&');
	for (var i = 0; i < sURLVariables.length; i++) 
	{
		var sParameterName = sURLVariables[i].split('=');
		if (sParameterName[0] == sParam) 
		{
			return sParameterName[1];
		}
	}
}      

function openFromURL(filename, graph){

	$.ajax({
		type: "POST",
		url: "openTemplate.htm",
		data: {
			'filename' : filename
		},
		dataType: 'json',
		success: function(data){
			$("#filename").val(filename);
			graph.fromJSON(data);
			loadEquipments();
		},
		error : function(e) {
			//alert("error: " + e.status);
		}
	});

	function loadEquipments(){

		$.each(graph.getElements(), function(index, cell){

			if(cell.get('subType') === 'rack'){

				var equipmentID = cell.get('id');
				var equipmentType = cell.get('subType');

				insertEquipmentholder(equipmentType, equipmentID);

			};
			if(cell.get('subType') === 'shelf'){
				var equipmentID = cell.get('id');
				var equipmentType = cell.get('subType');

				var parentID = cell.get('parent');
				var parent = graph.getCell(parentID);
				var containerType = parent.get('subType');
				var containerID = parent.get('id');

				insertEquipmentholder( equipmentType, equipmentID , containerType , containerID);

			}
			if(cell.get('subType') === 'slot'){

				var equipmentID = cell.get('id');
				var equipmentType = cell.get('subType');

				var parentID = cell.get('parent');
				var parent = graph.getCell(parentID);
				var containerType = parent.get('subType');
				var containerID = parent.get('id');

				insertEquipmentholder( equipmentType, equipmentID , containerType , containerID);

			}

			if(cell.get('subType')=== 'card'){

				var equipmentID = cell.get('id');
				var equipmentType = cell.get('subType');

				var parentID = cell.get('parent');
				var parent = graph.getCell(parentID);
				var containerType = parent.get('subType');
				var containerID = parent.get('id');

				insertEquipmentholder( equipmentType, equipmentID , containerType , containerID);

			}
			if(cell.get('subType')=== 'supervisor'){

				var equipmentID = cell.get('id');
				var equipmentType = cell.get('subType');
				var tech = cell.get('tech');

				var parentID = cell.get('parent');
				var parent = graph.getCell(parentID);
				var containerType = parent.get('subType');
				var containerID = parent.get('id');

				insertSupervisor( equipmentType, equipmentID , containerType , containerID);

				//setTechnology(equipmentType, equipmentID , tech);

			}
		});
	}

}

function generateOpenTemplateDialog(graph, data){

	var content = '<form id="open">';
	for(var i = 0; i < Object.keys(data).length; i++){
		if(i == 0){
			content = content + '<input type="radio" name="template" value="' + data[i].template + '" checked>' 
			+ '<label>' + data[i].template + '</label> <br>';
		}
		else{
			content = content + '<input type="radio" name="template" value="' + data[i].template + '">' 
			+ '<label>' + data[i].template + '</label><br>';
		}

	}
	content = content +  '</form>';

	var dialog = new joint.ui.Dialog({
		width: 300,
		type: 'neutral',
		title: 'Open Template',
		content: content,
		buttons: [
		          { action: 'cancel', content: 'Cancel', position: 'left' },
		          { action: 'open', content: 'Open', position: 'left' }
		          ]
	});
	dialog.on('action:open', openTemplate);
	dialog.on('action:cancel', dialog.close);
	dialog.open();

	function openTemplate(){

		var filename = $('input[name=template]:checked', '#open').val();

		$.ajax({
			type: "POST",
			url: "openTemplate.htm",
			data: {
				'filename' : filename
			},
			dataType: 'json',
			success: function(data){
				$("#filename").val(filename);
				graph.fromJSON(data);
				loadEquipments();
				dialog.close();
			},
			error : function(e) {
				alert("error: " + e.status);
				dialog.close();
			}
		});

		function loadEquipments(){

			$.each(graph.getElements(), function(index, cell){

				if(cell.get('subType') === 'rack'){

					var equipmentID = cell.get('id');
					var equipmentType = cell.get('subType');

					insertEquipmentholder(equipmentType, equipmentID);

				}
				if(cell.get('subType') === 'shelf'){
					var equipmentID = cell.get('id');
					var equipmentType = cell.get('subType');

					var parentID = cell.get('parent');
					var parent = graph.getCell(parentID);
					var containerType = parent.get('subType');
					var containerID = parent.get('id');

					insertEquipmentholder( equipmentType, equipmentID , containerType , containerID);

				}
				if(cell.get('subType') === 'slot'){

					var equipmentID = cell.get('id');
					var equipmentType = cell.get('subType');

					var parentID = cell.get('parent');
					var parent = graph.getCell(parentID);
					var containerType = parent.get('subType');
					var containerID = parent.get('id');

					insertEquipmentholder( equipmentType, equipmentID , containerType , containerID);

				}

				if(cell.get('subType') === 'card'){

					var equipmentID = cell.get('id');
					var equipmentType = cell.get('subType');

					var parentID = cell.get('parent');
					var parent = graph.getCell(parentID);
					var containerType = parent.get('subType');
					var containerID = parent.get('id');

					insertEquipmentholder( equipmentType, equipmentID , containerType , containerID);

				}
				if(cell.get('subType') === 'supervisor'){

					var equipmentID = cell.get('id');
					var equipmentType = cell.get('subType');
					var tech = cell.get('tech');

					var parentID = cell.get('parent');
					var parent = graph.getCell(parentID);
					var containerType = parent.get('subType');
					var containerID = parent.get('id');

					insertSupervisor( equipmentType, equipmentID , containerType , containerID);

					//setTechnology(equipmentType, equipmentID , tech);

				}
			});
		}

	};
}
