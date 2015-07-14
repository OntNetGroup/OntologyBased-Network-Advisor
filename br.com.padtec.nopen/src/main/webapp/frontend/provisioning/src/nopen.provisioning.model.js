nopen.provisioning.Model = Backbone.Model.extend({
	
	app : undefined,
	
	initialize : function() {
		
	},
	
	setApp : function(app) {
		this.app = app;
	},
	
	//Method to pre provisioning
	startPreProvisioning : function(graph) {
		
		var owl = this.app.owl;
		var file = this.app.file;
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
				
				//var layers = $this.getLayers(target);

				/*
				 * CHANGE FOR A METHOD THAT GET OUTPUTS FROM OWL FILE
				 */
				var outputs = owl.getOutputsTest();
				
				var test = owl.getOutputsFromOWL(source.id);
				
				content = createContent(sourceName, targetName, outputs);
				$('.dialog .body').html(content);
				prepareList('.outputsList');
				generateOutputEvents();
				changeSVGColor(sourceName, targetName);
				
				currentLinkIndex++;
			}
			//currentLinkIndex === links.length
			else {
				
				//var layers = $this.getLayers(target);

				/*
				 * CHANGE FOR A METHOD THAT GET OUTPUTS FROM OWL FILE
				 */
				var outputs = owl.getOutputsTest();
				
				var test = owl.getOutputsFromOWL(source.id);
				
				content = createContent(sourceName, targetName, outputs);
				$('.dialog .body').html(content);
				$('.dialog .controls .control-button[data-action="next"]').text('Finish');
				prepareList('.outputsList');
				generateOutputEvents();
				changeSVGColor(sourceName, targetName);
				
				currentLinkIndex++;
			}
			
		};
		
		//create content
		function createContent(sourceName, targetName, outputs) {
			
			var content = 
				'<div class="preProvisioningContainer"><div class="outputsList"><div id="listContainer">' +
				    '<ul id="expList" class="list"> <li class="outputs">' + sourceName + '</li> ' ;
			
			$.each(outputs, function(layer, value) {
				
				content = content + 
				    	'<li class="layer" value="' + layer + '" class="collapsed expanded">' + layer ;
				
				$.each(outputs[layer], function(key, output) {
					
					content = content +
						'<ul style="display: block;">' +
			                '<li class="outputItem" id="' + output.id + '" title="Output" value="' + output.name + '" class="collapsed expanded">' + output.name + '</li>' +
			            '</ul>';
					
				});
				
				content = content + '</li>';
				
			});
			
			content = content + '</ul></div></div>';
			content = content + 
				'<div class="inputsList"><div id="listContainer">' +
			    	'<ul id="expList" class="list"> <li class="inputs">' + targetName + '</li></ul>' + 
			    '</div></div></div>';
			
			content = content + '<div class="image">' + file.getTopologySVG() + '</div>'

			return content;
			
		};
		
		function changeSVGColor(sourceName, targetName) {
			
			//Set SVG elements
			$('.rotatable').each(function(){
				if($(this).text() === targetName || $(this).text() === sourceName) {
					var id = $(this).attr('id');
					$('#' + id + ' circle').attr('fill', "#FFF200");
				}
				else {
//					$(this).attr('opacity', ".5")
				}
			})
			
		};
		
		//generate events to outputs list
		function generateOutputEvents() {
			
			$('.outputsList #expList').find('li.outputItem').click( function(event) {
				
				$('.dialog .controls .control-button[data-action="next"]').attr("disabled", true);
				
				$('.outputsList #expList li').removeClass('active');
				$(this).toggleClass('active');
				
				$('.inputsList #expList li:not(:first)').remove();
				
				/*
				 * CHANGE FOR A METHOD THAT GET INPUTS FROM OWL FILE
				 */
				var inputs = owl.getInputsTest();
				
				var content = "";
				$.each(inputs, function(layer, value) {
					
					content = content + 
					    	'<li class="layer" value="' + layer + '" class="collapsed expanded">' + layer ;
					
					$.each(inputs[layer], function(key, input) {
						
						content = content +
							'<ul style="display: block;">' +
				                '<li class="inputItem" id="' + input.id + '" title="Input" value="' + input.name + '" class="collapsed expanded">' + input.name + '</li>' +
				            '</ul>';
						
					});
					
					content = content + '</li>';
					
				});
				
				$('.inputsList #expList').append(content);
				prepareList('.inputsList');
				
				generateInputEvents();
				
			});
			
		};
		
		//generate events to inputs list
		function generateInputEvents() {
			
			$('.inputsList #expList').find('li.inputItem').click(function(event) {
				
				$('.inputsList #expList li').removeClass('active');
				$(this).toggleClass('active');
				
				$('.dialog .controls .control-button[data-action="next"]').attr("disabled", false);
				
			});
			
			$('.dialog .controls .control-button[data-action="next"]').click(function(event) {
				
				var output = {
						"id": $('.outputItem.active').attr('id'),
						"name": $('.outputItem.active').attr('value'),
						"type": "Output_Card"
				};
				
				var input = {
						"id": $('.inputItem.active').attr('id'),
						"name": $('.inputItem.active').attr('value'),
						"type": "Input_Card"
				};
				
				console.log('Out: ' + JSON.stringify(output));
				console.log('In: ' + JSON.stringify(input));
				
			});
			
		};
		
		//generate events to list
		function prepareList(type) {
			$(type + ' #expList').find('li:has(ul)').click( function(event) {
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