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
            //    console.log(cell.attributes);
				var equipmentID = cell.get('id');
				var equipmentType = cell.get('subType');
				var equipmentName = cell.attributes.attrs.name.text;				
				app.RackCounter++
				
				insertEquipmentholder(equipmentName , equipmentType, equipmentID);

			};
			if(cell.get('subType') === 'shelf'){
				var equipmentID = cell.get('id');
				var equipmentType = cell.get('subType');
				var equipmentName = cell.attributes.attrs.name.text;
				app.ShelfCounter++;
				
				var parentID = cell.get('parent');
				var parent = graph.getCell(parentID);
				var containerType = parent.get('subType');
				var containerID = parent.get('id');
				var containerName = parent.attributes.attrs.name.text;
				
				insertEquipmentholder(equipmentName ,equipmentType, equipmentID ,containerName, containerType , containerID);

			}
			if(cell.get('subType') === 'slot'){

				var equipmentID = cell.get('id');
				var equipmentType = cell.get('subType');
				var equipmentName = cell.attributes.attrs.name.text;
				app.SlotCounter++;
				
				var parentID = cell.get('parent');
				var parent = graph.getCell(parentID);
				var containerType = parent.get('subType');
				var containerID = parent.get('id');
				var containerName = parent.attributes.attrs.name.text;
				
				insertEquipmentholder(equipmentName ,equipmentType, equipmentID ,containerName, containerType , containerID);

			}

			if(cell.get('subType')=== 'card'){

				var equipmentID = cell.get('id');
				var equipmentType = cell.get('subType');
				var equipmentName = cell.attributes.attrs.name.text;
				app.CardCounter++;
				
				var parentID = cell.get('parent');
				var parent = graph.getCell(parentID);
				var containerType = parent.get('subType');
				var containerID = parent.get('id');
				var containerName = parent.attributes.attrs.name.text;
				
				insertEquipmentholder(equipmentName ,equipmentType, equipmentID ,containerName, containerType , containerID);

			}
			if(cell.get('subType')=== 'supervisor'){

				var equipmentID = cell.get('id');
				var equipmentType = cell.get('subType');
				var equipmentName = cell.attributes.attrs.name.text;
				app.SupervisorCounter++;
				
				var parentID = cell.get('parent');
				var parent = graph.getCell(parentID);
				var containerType = parent.get('subType');
				var containerID = parent.get('id');
				var containerName = parent.attributes.attrs.name.text;
				
				insertSupervisor(equipmentName ,equipmentType, equipmentID ,containerName, containerType , containerID);

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
	                console.log(cell.attributes);
					var equipmentID = cell.get('id');
					var equipmentType = cell.get('subType');
					var equipmentName = cell.attributes.attrs.name.text;				
					app.RackCounter++
					
					insertEquipmentholder(equipmentName , equipmentType, equipmentID);

				};
				if(cell.get('subType') === 'shelf'){
					var equipmentID = cell.get('id');
					var equipmentType = cell.get('subType');
					var equipmentName = cell.attributes.attrs.name.text;
					app.ShelfCounter++;
					
					var parentID = cell.get('parent');
					var parent = graph.getCell(parentID);
					var containerType = parent.get('subType');
					var containerID = parent.get('id');
					var containerName = parent.attributes.attrs.name.text;
					
					insertEquipmentholder(equipmentName ,equipmentType, equipmentID ,containerName, containerType , containerID);

				}
				if(cell.get('subType') === 'slot'){

					var equipmentID = cell.get('id');
					var equipmentType = cell.get('subType');
					var equipmentName = cell.attributes.attrs.name.text;
					app.SlotCounter++;
					
					var parentID = cell.get('parent');
					var parent = graph.getCell(parentID);
					var containerType = parent.get('subType');
					var containerID = parent.get('id');
					var containerName = parent.attributes.attrs.name.text;
					
					insertEquipmentholder(equipmentName ,equipmentType, equipmentID ,containerName, containerType , containerID);

				}

				if(cell.get('subType')=== 'card'){

					var equipmentID = cell.get('id');
					var equipmentType = cell.get('subType');
					var equipmentName = cell.attributes.attrs.name.text;
					app.CardCounter++;
					
					var parentID = cell.get('parent');
					var parent = graph.getCell(parentID);
					var containerType = parent.get('subType');
					var containerID = parent.get('id');
					var containerName = parent.attributes.attrs.name.text;
					
					insertEquipmentholder(equipmentName ,equipmentType, equipmentID ,containerName, containerType , containerID);

				}
				if(cell.get('subType')=== 'supervisor'){

					var equipmentID = cell.get('id');
					var equipmentType = cell.get('subType');
					var equipmentName = cell.attributes.attrs.name.text;
					app.SupervisorCounter++;
					
					var parentID = cell.get('parent');
					var parent = graph.getCell(parentID);
					var containerType = parent.get('subType');
					var containerID = parent.get('id');
					var containerName = parent.attributes.attrs.name.text;
					
					insertSupervisor(equipmentName ,equipmentType, equipmentID ,containerName, containerType , containerID);

					//setTechnology(equipmentType, equipmentID , tech);

				}
			});
		}

	};
}
