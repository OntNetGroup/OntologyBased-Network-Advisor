nopen.provisioning.Algorithm = Backbone.Model.extend({
	app : undefined,

	initialize : function() {
		console.log("Algorithm started");
	},

	setApp : function(app) {
		this.app = app;
	},

	/*
	 * Receives two Outout_Card and check if them can be connected 
	 * */
	checkProvisioningConnection : function(source, target){
		console.log('sourcePort: ' + JSON.stringify(source));
		console.log('targetPort: ' + JSON.stringify(target));
		
	}

});