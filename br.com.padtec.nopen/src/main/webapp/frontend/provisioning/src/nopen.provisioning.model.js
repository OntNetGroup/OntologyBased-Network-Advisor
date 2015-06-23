nopen.provisioning.Model = Backbone.Model.extend({
	
	initialize : function() {
		
	},
	
	//Method to pre provisioning
	startPreProvisioning : function(graph) {
		
		var $this = this;
		
		$.each(graph.getLinks(), function(index, value) {
			
			var link = graph.getCell(value.id);
			var source = graph.getCell(link.get('source').id);
			var target = graph.getCell(link.get('target').id);
			
			var cards = $this.getCards(source);
			console.log('CARDS' + index + ": " + JSON.stringify(cards));
			
			//console.log('Source: ' + JSON.stringify(source.attr('equipment/data').cells));
			//console.log('Target: ' + target.attr('equipment/data').cells[0]);
			
			
		});
		
	},
	
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