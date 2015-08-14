nopen.topology.Util = Backbone.Model.extend({
	
	app : undefined,
	
	initialize : function(){
		
	},
	
	setApp : function(app) {
		this.app = app;
	},
	
	generateNewEquipmentIDs : function(equipment) {
		
		var ids = {};
		
		//create new ids
		$.each(equipment.cells, function(index, element) {
			
			ids[element.id] = joint.util.uuid(); 
			
			if(element.subType === 'Card') {
				
				var card = element.attrs.data;
				$.each(card.cells, function(key, itu) {
					ids[itu.id] = joint.util.uuid(); 
				});
				
			}
		});
		
		//change ids
		$.each(equipment.cells, function(index, element) {
			
			element.id = ids[element.id];
			
			if(element.embeds) {
				
				var embeddedElements = [];
				
				$.each(element.embeds, function(key, embedded) {
					element.embeds.push(ids[embedded]);
					embeddedElements.push(embedded);
				});
				
				$.each(embeddedElements, function(key, embedded) {
					element.embeds.splice(element.embeds.indexOf(embedded), 1);
				});
				
			}
			
			if(element.parent) {
				element.parent = ids[element.parent];
			}
			
			if(element.type === 'link') {
				element.source.id = ids[element.source.id];
				element.target.id = ids[element.target.id];
			}
			
			if(element.subType === 'Card') {
				
				$.each(element.inPorts, function(key, port) {
					element.inPorts[ids[key]] = port;
					delete element.inPorts[key];
				});
				
				$.each(element.outPorts, function(key, port) {
					element.outPorts[ids[key]] = port;
					delete element.outPorts[key];
				});
				
				$.each(element.connectedPorts, function(key, port) {
					element.connectedPorts[ids[key]] = port;
					delete element.connectedPorts[key];
				});
				
				var card = element.attrs.data;
				$.each(card.cells, function(key, itu) {
					
					if(itu.type === 'link') {
						itu.source.id = ids[itu.source.id];
						itu.target.id = ids[itu.target.id];
					}
					
					if(itu.embeds) {
						
						var embeddedElements = [];
						
						$.each(itu.embeds, function(key, embedded) {
							itu.embeds.push(ids[embedded]);
							embeddedElements.push(embedded);
						});
						
						$.each(embeddedElements, function(key, embedded) {
							itu.embeds.splice(itu.embeds.indexOf(embedded), 1);
						});
						
					}
					
					if(itu.parent) {
						itu.parent = ids[itu.parent];
					}
					
					itu.id = ids[itu.id]
				});
				
			}
			
		});
		
//		console.log('IDs: ' + JSON.stringify(ids));
		
		return equipment;
	},
	
});