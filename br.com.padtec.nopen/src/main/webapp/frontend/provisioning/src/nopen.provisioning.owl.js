nopen.provisioning.OWL = Backbone.Model.extend({
	
	model : undefined,
	
	initialize : function() {
		
	},
	
	//Method to set model
	setModel : function(model) {
		this.model = model;
	},
	
	//Method to parse card JSON file to generate OWL instances
	parseCardToOWL : function(equipment, card) {
		
//		console.log('== PARSE CARD TO OWL ==')
//		console.log('Equipment: ' + JSON.stringify(equipment));
//		console.log('Card: ' + JSON.stringify(card));
		
	},
	
	//Method to get inputs from OWL file
	getInputs : function() {
		
	},
	
	//Method to get outputs from OWL file
	getOutputs : function() {
		
	},
	
	
	
});