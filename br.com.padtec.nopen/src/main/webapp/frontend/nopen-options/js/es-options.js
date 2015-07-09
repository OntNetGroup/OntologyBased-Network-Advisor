function getTemplates(){
	
	var content
	
	$.ajax({
	   type: "GET",
	   async: false,
	   url: "getAllTemplates.htm",
	   dataType: 'json',
	   success: function(data){ 		   
		   generateOptionsContent(data)
	   },
	   error : function(e) {
		   alert("error: " + e.status);
	   }
	});
	
	function generateOptionsContent(data){
		
		content = '';
		
		for(var i = 0; i < Object.keys(data).length; i++){
			
			if(i == 0){
				content = '<hr/>';
			}
			
			content = content + '<div class="btn-group">'
									+ '<a class="btn" title="Edit" href="equipment-studio.htm?template=' + data[i].template + '"><i class="icon-edit"></i></a>' 
									+ '<a class="btn" title="Delete" onclick="deleteTemplate(\'' + data[i].template + '\')"><i class="icon-trash"></i></a>'
									+ '<span class="name">' + data[i].template + '</span>'
								+ '</div>'
								+ '<br/><hr/>'
		}
		
	}
	
	$('.btn-toolbar').append(content);

};

function deleteTemplate(filename){
	
	if (confirm('Are you sure you want to delete this file?')) {
	
		$.ajax({
		   type: "POST",
		   async: false,
		   url: "deleteTemplate.htm",
		   data: {
			   'filename' : filename
		   },
		   success: function(){ 		   
			   alert(filename + " deleted successfully!");
			   window.location.reload(true);
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
	}
};


function getEquipments(){
	
	var content
	
	$.ajax({
	   type: "GET",
	   async: false,
	   url: "getAllEquipments.htm",
	   dataType: 'json',
	   success: function(data){ 		   
		   generateOptionsContent(data)
	   },
	   error : function(e) {
		   alert("error: " + e.status);
	   }
	});
	
	function generateOptionsContent(data){
		
		content = '';
		
		for(var i = 0; i < Object.keys(data).length; i++){
			
			if(i == 0){
				content = '<hr/>';
			}
			
			content = content + '<div class="btn-group">'
									+ '<a class="btn" title="View" onclick="viewEquipment(\'' + data[i].equipment + '\')"><i class="icon-eye-open"></i></a>' 
									+ '<a class="btn" title="Delete" onclick="deleteEquipment(\'' + data[i].equipment + '\')"><i class="icon-trash"></i></a>'
									+ '<span class="name">' + data[i].equipment + '</span>'
								+ '</div>'
								+ '<br/><hr/>'
		}
		
	}
	
	$('.btn-toolbar-equipments').append(content);

};

function closeIframe(){
	$('#equipment-dialog').dialog("close");
};

function viewEquipment(filename){

	
	$(function ()    {
        $('#equipment-dialog').dialog({
            modal: true,
            //show: 'scale',
            height: 500,
			width: 500,
            position: 'top',
            title: 'Dynamically Loaded Page',
            open: function ()
            {
                $('#equipment-iframe').attr('src','/nopen/equipment-visualizer.htm?equipment=' + filename);
            },
            close: function() {
            }
        });
    });	

	
}

function deleteEquipment(filename){
	
	if (confirm('Are you sure you want to delete this file?')) {
	
		$.ajax({
		   type: "POST",
		   async: false,
		   url: "deleteEquipment.htm",
		   data: {
			   'filename' : filename
		   },
		   success: function(){ 		   
			   alert(filename + " deleted successfully!");
			   window.location.reload(true);
		   },
		   error : function(e) {
			   alert("error: " + e.status);
		   }
		});
	}
};


