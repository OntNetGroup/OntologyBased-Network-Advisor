nopen.provisioning.PreProvisioning = Backbone.Model.extend({
	
	app : undefined,
	
	inititalize : function() {
		
		
	},
	
	setApp : function(app) {
		this.app = app;
	},
	
	start : function(app, subnetworks) {
		
		var graph = app.graph;
		var owl = this.app.owl;
		var file = this.app.file;
		var model = this.app.model;
		var links = [];
		
		$.each(graph.getLinks(), function(index, value) {
			
			var link = graph.getCell(value.id);
			var source = graph.getCell(link.get('source').id);
			var target = graph.getCell(link.get('target').id);
			
			var sourceCards = model.getCards(source);
			var targetCards = model.getCards(target);
			
			links.push({
				'link' : link,
				'source' : source,
				'target' : target,
			});
			
			//console.log('SOURCE' + index + ": " + model.getElementName(source));
			//console.log('TARGET' + index + ": " + model.getElementName(target));
			
		});
		
		var dialog = new joint.ui.Dialog({
			width: 500,
			type: 'neutral',
			closeButton: false,
			title: 'Pre Provisioning',
			clickOutside: false,
			modal: false,
			content: 'Unconnected links were detected, click in the <b>Next</b> button to connect them.',
			buttons: [
			          { action: 'showImage', content: 'Show/Hide Image', position: 'right' },
			          { action: 'next', content: 'Next', position: 'right' },
			]
		});
		dialog.on('action:next', next);
		dialog.on('action:showImage', showImage);
		dialog.open();

		var currentLinkIndex = 1;
		$('.viewport').hide();
		$('.dialog .controls .control-button[data-action="showImage"]').hide();
		
		//block outside
		$('#black_overlay').show();
		
		function showImage() {
			if($('.dialog .fg .image').is(':visible')) {
				$('.dialog .fg .image').hide();
			}
			else {
				$('.dialog .fg .image').show();
			}
		};
		
		var source = undefined;
		var target = undefined;
		
		function next() {
			
			$('.dialog .controls .control-button[data-action="showImage"]').show();
			
			//finish pre provisioning
			if (currentLinkIndex > links.length) {
				dialog.close();
				$('#black_overlay').hide();
				
				model.generateProvisioning(app, subnetworks);
				$('.viewport').show();
				
				return;
			}
			
			//disable button
			$('.dialog .controls .control-button[data-action="next"]').attr("disabled", true);
			
			var content = '';
			
			var link = links[currentLinkIndex - 1];
			
			source = graph.getCell(link.source.id);
			target = graph.getCell(link.target.id);
			
			var sourceName = model.getEquipmentName(link.source);
			var targetName = model.getEquipmentName(link.target);
			
			if(currentLinkIndex < links.length) {
				
				//var layers = model.getLayers(target);

				var outputs = owl.getOutputsFromOWL(source.id);
				
				content = createContent(sourceName, targetName, outputs);
				
				$('.dialog .body').html(content);
				$('.dialog .fg .image').remove();
				$('.dialog .fg').append('<div class="image">' + file.getTopologySVG() + '</div>');
				
				prepareList('.outputsList');
				generateOutputEvents();
				changeSVGColor(sourceName, targetName);
				
				currentLinkIndex++;
			}
			//currentLinkIndex === links.length
			else {
				
				//var layers = model.getLayers(target);

				var outputs = owl.getOutputsFromOWL(source.id);
				
				content = createContent(sourceName, targetName, outputs);
				
				$('.dialog .body').html(content);
				$('.dialog .fg .image').remove();
				$('.dialog .fg').append('<div class="image">' + file.getTopologySVG() + '</div>');
				
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
			
			//content = content + '<div class="image">' + file.getTopologySVG() + '</div>'

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
					var id = $(this).attr('id');
					$('#' + id + ' circle').attr('fill', "#00c6ff");
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
				
				var inputs = owl.getInputsFromOWL(target.id);
				
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
	
	
});