nopen.provisioning.Algorithm = Backbone.Model.extend({
	app : undefined,
    connection: undefined,
    
	initialize : function() {
		console.log("Algorithm started");
	},

	setApp : function(app) {
		this.app = app;
		this.connection = app.connection;
	},

	doProvisioning: function(type,source,target) {
		var rps = this.connection.getConnectionBetweenReferencePoints(source.id,target.id);
		if(!this.connection.askHasPathSo(rps[0].rp_so, rps[0].rp_sk)){
			console.log('jesusi eh o senhor');
			return;
		}
		if(type === 'Simple Connection'){
			this.simpleConnection(rps,source,target);
		}
	},

	simpleConnection : function(rps,source,target){
		var connection = this.connection;
		var rp_so, rp_sk;
		
		$.each(rps, function(index, rp){
			rp_so = rp.rp_so;
			rp_sk = rp.rp_sk;
		});
		
		
		var rp = rp_so;
		
		do{
			console.log(rp+" : "+this.connection.selectQuery(rp, ["a"]));
			var vertical_rps = this.connection.selectQuery(rp, ["ont:vertical_path_so"]);
			
			$.each(vertical_rps, function(index, vertical_rp){
//			    console.log(vertical_rp + '-so--' + connection.askHasPathSo(vertical_rp, rp_sk));
				if(connection.askHasPathSo(vertical_rp, rp_sk)){
					if(connection.askQuery(vertical_rp, ["a"], "ont:FP")){
						connection.insertTriple(rp, "ont:mc", vertical_rp);
					}	
					rp = vertical_rp;
					return false;
				}
			});	
			
		}while(!connection.askQuery(rp, ["ont:links_input", "a"], "ont:PM_Input_So"))
			
		rp = this.connection.selectQuery(rp, ["ont:has_path"])[0];
		
		do{
			console.log(rp+" : "+this.connection.selectQuery(rp, ["a"]));
			var vertical_rps = this.connection.selectQuery(rp, ["ont:vertical_path_sk"]);
			
			$.each(vertical_rps, function(index, vertical_rp){
//				console.log(vertical_rp + '-sk--' + connection.askHasPathSk(vertical_rp, rp_sk));
				if(connection.askHasPathSk(vertical_rp, rp_sk)){
					if(connection.askQuery(rp, ["a"], "ont:FP")){
						connection.insertTriple(vertical_rp, "ont:mc", rp);
					}	
					rp = vertical_rp;
					return false;
				}
			});	
			
		}while(rp != rp_sk)
			
		console.log(rp);
		
	},

});