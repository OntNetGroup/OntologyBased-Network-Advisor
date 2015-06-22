nopen.provisioning.File = Backbone.Model.extend({
	
	initialize : function(){
		
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

	//Method to open a provisioning file from URL
	openFromURL : function(filename, graph) {
		$.ajax({
		   type: "POST",
		   url: "openProvisioning.htm",
		   data: {
			   'filename' : filename
		   },
		   dataType: 'json',
		   success: function(data){
			   $("#filename").val(filename);
			   graph.fromJSON(data);
		   },
		   error : function(e) {
			   //alert("error: " + e.status);
		   }
		});
	},
	
	//Method to generate a save provisioning dialog
	generateSaveProvisioningDialog : function(graph) {
		
		var $this = this;
		
		var content = '<div id="save-dialog" title="Save Provisioning">'
			+ 'Name: <input type="text" id="save-filename" value="' + $('#filename').val() + '"/>'
			+ '</div>'
			+ '<div id="name-error-message">' + 'Name cannot be empty!' + '</div>';
			
		var dialog = new joint.ui.Dialog({
			width: 300,
			type: 'neutral',
			title: 'Save Provisioning',
			content: content,
			buttons: [
				{ action: 'cancel', content: 'Cancel', position: 'left' },
				{ action: 'save', content: 'Save', position: 'left' }
			]
		});
		
		dialog.on('action:save', checkProvisioningFileExist);
		dialog.on('action:cancel', cancel);

		dialog.open();
		
		function checkProvisioningFileExist(){
			$this.checkProvisioningFileExist(graph, dialog);
		}
		
		function cancel(){
			dialog.close();
		}
		
	},
	
	//Method to check if provisioning file exist
	checkProvisioningFileExist : function(graph, dialog) {
		
		var $this = this;
		
		if($("#save-filename").val() == ""){
			$('#name-error-message').show();
		}
		else{
			$.ajax({
			   type: "POST",
			   async: false,
			   url: "checkProvisioningFile.htm",
			   data: {
				 'filename': $("#save-filename").val(),
			   },
			   success: function(data){ 		   
				   
				   if(data == "exist"){		   
					   if (confirm('The file already exist, do you want to replace it?')) {
						   $this.saveProvisioning(graph);
						   dialog.close();
					   } 
				   }
				   else{
					   $this.saveProvisioning(graph);
					   dialog.close();
				   }
			   },
			   error : function(e) {
				   alert("error: " + e.status);
				   dialog.close();
			   }
			});
		}
		
	},
	
	//Method to save a provisioning file
	saveProvisioning : function(graph, dialog) {
		
		$('#filename').val($("#save-filename").val());
		
		$.ajax({
		   type: "POST",
		   async: false,
		   url: "saveProvisioning.htm",
		   data: {
			 'filename': $("#save-filename").val(),
			 'graph': JSON.stringify(graph.toJSON()),
		   },
		   success: function(){ 		   
			   alert($("#save-filename").val() + ' saved successfully!');
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
		
	},
	
	//Method to generate a open dialog to provisioning files
	generateOpenProvisioningDialog : function(graph) {
		
		var $this = this;
		
		$.ajax({
		   type: "GET",
		   url: "getAllProvisioning.htm",
		   dataType: 'json',
		   success: function(data){ 		   
			   
			   	var content = '<form id="open">';
				for(var i = Object.keys(data).length-1; i >= 0; i--){
					if(i == Object.keys(data).length-1){
						content = content + '<input type="radio" name="provisioning" value="' + data[i].provisioning + '" checked>' 
								+ '<label>' + data[i].provisioning + '</label> <br>';
					}
					else{
						content = content + '<input type="radio" name="provisioning" value="' + data[i].provisioning + '">' 
								+ '<label>' + data[i].provisioning + '</label><br>';
					}

				}
				content = content +  '</form>';
				
				var dialog = new joint.ui.Dialog({
					width: 300,
					type: 'neutral',
					title: 'Open Provisioning',
					content: content,
					buttons: [
						{ action: 'cancel', content: 'Cancel', position: 'left' },
						{ action: 'open', content: 'Open', position: 'left' }
					]
				});
				dialog.on('action:open', openProvisioning);
				dialog.on('action:cancel', dialog.close);
				dialog.open();
				
				function openProvisioning() {
					$this.openProvisioning(graph);
					dialog.close();
				}
			   
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
		
	},
	
	//Method to open a provisioning file from dialog
	openProvisioning : function(graph) {
		
		var filename = $('input[name=provisioning]:checked', '#open').val();
		
		$.ajax({
		   type: "POST",
		   async: false,
		   url: "openProvisioning.htm",
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
	
	//method to generate import topology dialog
	generateImportTopologyDialog : function(graph) {
		
		var $this = this;
		
		$.ajax({
		   type: "GET",
		   url: "getAllTopologies.htm",
		   dataType: 'json',
		   success: function(data){
			   
			   	var content = '<form id="open-topology">';
				for(var i = Object.keys(data).length-1; i >= 0; i--){
					if(i == Object.keys(data).length-1){
						content = content + '<input type="radio" name="topology" value="' + data[i].topology + '" checked>' 
								+ '<label>' + data[i].topology + '</label> <br>';
					}
					else{
						content = content + '<input type="radio" name="topology" value="' + data[i].topology + '">' 
								+ '<label>' + data[i].topology + '</label><br>';
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
				dialog.on('action:open', importTopology);
				dialog.on('action:cancel', dialog.close);
				dialog.open();
				
				function importTopology() {
					$this.importTopology(graph, dialog);
				}
			   
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
		
	},
	
	//method to import topology
	importTopology : function(graph, dialog) {
		
		var filename = $('input[name=topology]:checked', '#open-topology').val();
		
		$.ajax({
		   type: "POST",
		   url: "openTopologyOnProvisioning.htm",
		   data: {
			   'filename' : filename
		   },
		   dataType: 'json',
		   success: function(data){ 		   
			   graph.fromJSON(data);
			   dialog.close();
		   },
		   error : function(e) {
			   alert("error: " + e.status);
			   dialog.close();
		   }
		});
		
	},
	
});