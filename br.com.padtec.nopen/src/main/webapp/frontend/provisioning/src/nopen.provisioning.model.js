nopen.provisioning.Model = Backbone.Model.extend({
	
	initialize : function() {
		
	},
	
	//Method to pre provisioning
	startPreProvisioning : function(graph) {
		
		var $this = this;
		var links = [];
		
		$.each(graph.getLinks(), function(index, value) {
			
			var link = graph.getCell(value.id);
			var source = graph.getCell(link.get('source').id);
			var target = graph.getCell(link.get('target').id);
			
			var sourceCards = $this.getCards(source);
			var targetCards = $this.getCards(target);
			
			links.push({
				'link' : link,
				'source' : source,
				'target' : target,
			});
			
			//console.log('SOURCE' + index + ": " + $this.getElementName(source));
			//console.log('TARGET' + index + ": " + $this.getElementName(target));
			
		});
		
		var dialog = new joint.ui.Dialog({
			width: 500,
			type: 'neutral',
			closeButton: false,
			title: 'Pre Provisioning',
			clickOutside: false,
			modal: false,
			content: 'Unconnected links were detected, click in the <b>Next</b> button to connect them. <hr>',
			buttons: [
			          { action: 'next', content: 'Next', position: 'right' },
			]
		});
		dialog.on('action:next', next);
		dialog.open();

		var currentLinkIndex = 1;
		
		//block outside
		$('#black_overlay').show();
		
		function next() {
			
			//finish pre provisioning
			if (currentLinkIndex > links.length) {
				dialog.close();
				$('#black_overlay').hide();
				return;
			}
			
			//disable button
			$('.dialog .controls .control-button[data-action="next"]').attr("disabled", true);
			
			var content = '';
			
			var link = links[currentLinkIndex - 1];
			
			var source = graph.getCell(link.source.id);
			var target = graph.getCell(link.target.id);
			
			var sourceName = $this.getEquipmentName(link.source);
			var targetName = $this.getEquipmentName(link.target);
			
			if(currentLinkIndex < links.length) {
				
				var layers = $this.getLayers(target);
				var input = $this.getInputs(target);
				
				content = createContent();
				$('.dialog .body').html(content);
				prepareList();
				
				currentLinkIndex++;
			}
			//currentLinkIndex === links.length
			else {
				
				var layers = $this.getLayers(target);
				var input = $this.getInputs(target);
				
				content = createContent();
				$('.dialog .body').html(content);
				$('.dialog .controls .control-button[data-action="next"]').text('Finish');
				prepareList();
				
				currentLinkIndex++;
			}
			
		};
		
		
		function createContent() {
			
			var content = 
				'<div id="listContainer" style="overflow:auto;overflow-x:hidden;">' +
				    '<ul id="expList" class="list">' +
				        '<li title="Layer" value="Layer" class="collapsed expanded">Layer1' +
				            '<ul style="display: block;">' +
				                '<li title="Output" value="Output" class="collapsed expanded">Out_1' +
				                '</li>' +
				            '</ul>' +
				        '</li>' +
				    '</ul>' +
				'</div>';
			
			return content;
		}
		
		function prepareList() {
			$('#expList').find('li:has(ul)').click( function(event) {
				if (this == event.target) {
					$(this).toggleClass('expanded');
					$(this).children('ul').toggle('medium');
				}
				return false;
			})
			.addClass('collapsed').children('ul').hide();

			//Create the toggle 
			var toggle = false;
			$('#listToggle').unbind('click').click(function(){
				if(toggle == false){
					$('.collapsed').addClass('expanded');
					console.log($('.collapsed').children());
					$('.collapsed').children().show('medium');
					$('#listToggle').text("Collapse All");
					toggle = true;
				}else{
					$('.collapsed').removeClass('expanded');
					console.log($('.collapsed').children());
					$('.collapsed').children().hide('medium');
					$('#listToggle').text("Expand All");
					toggle = false;          
				}
			});

			$('#expList').find('li').click( function(event) {
				
				$('.dialog .controls .control-button[data-action="next"]').attr("disabled", false);
				return false;
			});
		}
		
	},
	
	//Method to get equipment name
	getEquipmentName : function(equipment) {
		return equipment.attr('text/text')
	},
	
	
	//Method to get a ITU element by the id of it.
	getITUElementByID : function(equipment, id) {
		
		var cards = this.getCards(equipment);
		
		$.each(cards, function(index, card) {
			
			var itus = card.attrs.data.cells;
			$.each(itus, function(index, itu) {
				
				if(itu.id === id) {
					return itu;
				}
				
			});
			
		});
		
	},
	
	//Method do get inputs as array of object ( inputs = [{ "type" : "", "id" : "", "name" : "" },...] )
	getInputs : function(equipment) {
		
		var inputs = [];
		var cards = this.getCards(equipment);
		
		$.each(cards, function(index, card) {
			
			var itus = card.attrs.data.cells;
			$.each(itus, function(index, itu) {
				
				//if Input_Card, add new object in array
				if(itu.subtype === "Input_Card") {
					var input = {
						"type" : itu.subtype,	
						"id" : itu.id,
						"name" : itu.attrs.text.text,
					};
					inputs.push(input);
				}
				
			});
			
			console.log('INPUTS: ' + JSON.stringify(inputs));
			
		});
		
		return inputs;
		
	},
	
	//Method do get outputs as array of object ( outputs = [{ "type" : "", "id" : "", "name" : "" },...] )
	getOutputs : function(equipment) {
		
		var outputs = [];
		var cards = this.getCards(equipment);
		
		$.each(cards, function(index, card) {
			
			var itus = card.attrs.data.cells;
			$.each(itus, function(index, itu) {
				
				//if Output_Card, add new object in array
				if(itu.subtype === "Output_Card") {
					var output = {
						"type" : itu.subtype,	
						"id" : itu.id,
						"name" : itu.attrs.text.text,
					};
					outputs.push(output);
				}
				
			});
			
			console.log('OUTPUTS: ' + JSON.stringify(outputs));
			
		});
		
		return inputs;
		
	},
	
	//Method do get layers as array of object ( layers = [{ "type" : "", "id" : "", "name" : "" },...] )
	getLayers : function(equipment) {
		
		var layers = [];
		var cards = this.getCards(equipment);
		
		$.each(cards, function(index, card) {
			
			var itus = card.attrs.data.cells;
			$.each(itus, function(index, itu) {
				
				//if Card_Layer, add new object in array
				if(itu.subtype === "Card_Layer") {
					var layer = {
						"type" : itu.subtype,	
						"id" : itu.id,
						"name" : itu.lanes.label,
					};
					layers.push(layer);
				}
				
			});
			
			console.log('LAYERS: ' + JSON.stringify(layers));
			
		});
		
		return layers;
		
	},
	
	//Method to get cards of a equipment
	getCards : function(equipment) {
		
		var cards = [];
		//var cells = element.attributes.attrs.equipment.data.cells;
		var cells = equipment.attr('equipment/data').cells;
		
		$.each(cells, function(index, cell) {
			
			if(cell.subType === 'Card') {
				cards.push(cell);
			}
			
		})
		
		return cards;
		
	},
	
	getImplementedTechnologies : function() {
		var result = "error";
		$.ajax({
			   type: "POST",
			   async: false,
			   url: "getImplementedTechnologies.htm",
			   success: function(data){
				   result = data;
			   },
			   error : function(e) {
				   alert("error: " + e.status);
			   }
			});
		
		return result;
	},
	
	
	getUppermostLayer : function(tech) {
		var result = "error";
		$.ajax({
			   type: "POST",
			   async: false,
			   url: "getUppermostLayer.htm",
			   data: {
				   'technology' : tech
			   },
			   success: function(data){
				   result = data;
			   },
			   error : function(e) {
				   alert("error: " + e.status);
			   }
			});
		
		return result;
	},
	
	
	getEquipmentsByLayer : function(clientMEF) {
		var result = "error";
		$.ajax({
			   type: "POST",
			   async: false,
			   url: "getEquipmentsByLayer.htm",
			   data: {
				   'clientMEF' : clientMEF
			   },
			   success: function(data){
				   result = data;
			   },
			   error : function(e) {
				   alert("error: " + e.status);
			   }
			});
		
		return result;
	}
});