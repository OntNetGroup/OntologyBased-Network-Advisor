nopen.topology.File = Backbone.Model.extend({
	
	app : undefined,
	
	initialize : function(){
		
	},
	
	setApp : function(app) {
		this.app = app;
	},
	
	getAllEquipments : function() {
		
		var equipments = undefined;
		
		$.ajax({
		   type: "POST",
		   async: false,
		   url: "getAllEquipmentsToMatch.htm",
		   dataType: 'json',
		   success: function(data){
			   equipments = data;
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
		
		return equipments;
		
	},
	
	openEquipment : function(filename) {
		
		var equipment = undefined;
		
		$.ajax({
			type: "POST",
			async: false,
			url: "openEquipmentInTopology.htm",
			data: {
				'filename' : filename
			},
			dataType: 'json',
			success: function(data){
				equipment = data;
			},
			error : function(e) {
				alert("error: " + e.status);
			}
		});
		
		return equipment;
		
	},
	
	openEquipmentCard : function(filename, cardId) {
		
		var card = undefined;
		
		$.ajax({
			type: "POST",
			async: false,
			url: "openEquipmentCardInTopology.htm",
			data: {
				'filename' : filename,
				'cardName' : cardId
			},
			dataType: 'json',
			success: function(data){
				card = data;
			},
			error : function(e) {
				alert("error: " + e.status);
			}
		});
		
		return card;
		
	},
	
	openCard : function(filename) {
		
		
		
	},
	
	getAllTopologies : function(graph) {
		
		var topologies = [];
		
		$.ajax({
		   type: "POST",
		   async: false,
		   url: "getAllTopologies.htm",
		   dataType: 'json',
		   success: function(data){ 	
			   topologies = data;
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
		
		return topologies;
		
	},
	
	//Method to get paramentes from url
	getUrlParameter : function(sParam) {
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
	},   
	
	openTopologyFromURL : function(graph, filename){
		
		this.openTopology(graph, filename)
		
	},
	
	generateOpenTopologyDialog : function(graph){
		
		var $this = this;
		
		var topologies = $this.getAllTopologies(graph);
		
		var content = '<form id="open">';
		for(var i = 0; i < topologies.length; i++){
			if(i == 0){
				content = content + '<input type="radio" name="topology" value="' + topologies[i].topology + '" checked>' 
						+ '<label>' + topologies[i].topology + '</label> <br>';
			}
			else{
				content = content + '<input type="radio" name="topology" value="' + topologies[i].topology + '">' 
						+ '<label>' + topologies[i].topology + '</label><br>';
			}

		}
		content = content +  '</form>';
		
		var dialog = new joint.ui.Dialog({
			width: 300,
			type: 'neutral',
			title: 'Open Topology',
			content: content,
			buttons: [
				{ action: 'cancel', content: 'Cancel', position: 'left' },
				{ action: 'open', content: 'Open', position: 'left' }
			]
		});
		dialog.on('action:open', open);
		dialog.on('action:cancel', dialog.close);
		dialog.open();

		function open(){
			var filename = $('input[name=topology]:checked', '#open').val();
			$this.openTopology(graph, filename);
			dialog.close();
		}
		
	},
	
	openTopology : function(graph, filename){
		
		$.ajax({
		   type: "POST",
		   url: "openTopology.htm",
		   data: {
			   'filename' : filename
		   },
		   dataType: 'json',
		   success: function(data){ 		   
			   graph.fromJSON(data);
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
		
	},
	
	generateSaveTopologyDialog : function(app){
		
		var $this = this;
		
		var content = '<div id="save-dialog" title="Save Topology">'
			+ 'Name: <input type="text" id="save-filename" value="' + $('#filename').val() + '"/>'
			+ '</div>'
			+ '<div id="name-error-message">' + 'Name cannot be empty!' + '</div>';
			
		var dialog = new joint.ui.Dialog({
			width: 300,
			type: 'neutral',
			title: 'Save Topology',
			content: content,
			buttons: [
				{ action: 'cancel', content: 'Cancel', position: 'left' },
				{ action: 'save', content: 'Save', position: 'left' }
			]
		});
		
		dialog.on('action:save', checkTopologyFile);
		dialog.on('action:cancel', cancel);

		dialog.open();
		
		function cancel(){
			dialog.close();
		}
		
		function checkTopologyFile(){
			
			var filename = $("#save-filename").val();
			
			if(filename == ""){
				$('#name-error-message').show();
			}
			else{
				$.ajax({
				   type: "POST",
				   url: "checkTopologyFile.htm",
				   data: {
					 'filename': filename,
				   },
				   success: function(data){ 		   
					   
					   if(data == "exist"){		   
						   if (confirm('The file already exist, do you want to replace it?')) {
							   $this.saveTopology(app, filename);
							   dialog.close();
						   } 
					   }
					   else{
						   $this.saveTopology(app, filename);
						   dialog.close();
					   }
				   },
				   error : function(e) {
					   alert("error: " + e.status);
					   dialog.close();
				   }
				});
			}
			
		};
	},
	
	saveTopology : function(app, filename){
		
		var graph = app.graph;
		var paper = app.paper;
		
		var topologySVG = "";
		paper.toSVG( function(svgString) {
			topologySVG = svgString;
		});
		
		$('#filename').val(filename);
		
		$.ajax({
		   type: "POST",
		   url: "saveTopology.htm",
		   data: {
			 'filename': filename,
			 'graph': JSON.stringify(graph.toJSON()),
			 'svg' : topologySVG,
		   },
		   success: function(){ 		   
			   alert(filename + ' saved successfully!');
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
		
	},
	
});