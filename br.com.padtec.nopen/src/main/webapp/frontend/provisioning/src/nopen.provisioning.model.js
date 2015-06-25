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
			
			console.log('SOURCE' + index + ": " + $this.getElementName(source));
			console.log('TARGET' + index + ": " + $this.getElementName(target));
			
		});
		
		var dialog = new joint.ui.Dialog({
			width: 500,
			type: 'neutral',
			title: 'Pre Provisioning',
			content: 'Unconnected links were detected, click in the <b>Next</b> button to connect them. <hr>',
			buttons: [
			          { action: 'next', content: 'Next', position: 'right' },
			]
		});
		dialog.on('action:next', next);
		dialog.on('action:close', cancel);
		dialog.open();

		function cancel() {
			dialog.close();
		};
		
		var currentLinkIndex = 1;
		function next() {
			
			//finish pre provisioning
			if (currentLinkIndex > links.length) {
				dialog.close();
				return;
			}
			
			//disable button
			$('.dialog .controls .control-button[data-action="next"]').attr("disabled", true);
			
			var content = '';
			
			var link = links[currentLinkIndex - 1];
			var source = $this.getElementName(link.source);
			var target = $this.getElementName(link.target);
			
			if(currentLinkIndex < links.length) {
				
				content = createContent();
				$('.dialog .body').html(content);
				prepareList();
				
				currentLinkIndex++;
			}
			//currentLinkIndex === links.length
			else {
				
				content = source + " -> " + target;
				
				$('.dialog .body').html(content);
				$('.dialog .controls .control-button[data-action="next"]').text('Finish');

				currentLinkIndex++;
			}
			
		};
		
		
		function createContent() {
			
			var content = 
				'<div id="listContainer" style="overflow:auto;overflow-x:hidden;">' +
				    '<ul id="expList" class="list">' +
				        '<li title="Sharepoint Demo Website" value="https://hosted.compulite.ca" class="collapsed expanded">Sharepoint Demo Website' +
				            '<ul style="display: block;">' +
				                '<li title="Academic" value="https://hosted.compulite.ca/academic" class="collapsed expanded">Academic' +
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
				
				siteUrl =  $(this).attr('value');
				if(this.id != 'myList'){
					RefreshSiteLists(siteUrl);
				} else{
					RefreshSiteLists(siteUrl);
				}
				return false;
			});
		}
		
	},
	
	//Method to get element name
	getElementName : function(element) {
		return element.attr('text/text')
	},
	
	//Method to get cards of a element
	getCards : function(element) {
		
		var cards = [];
		var cells = element.attr('equipment/data').cells;
		$.each(cells, function(index, cell) {
			
			if(cell.subType === 'Card') {
				cards.push(cell);
			}
			
		})
		
		return cards;
		
	}
	
});