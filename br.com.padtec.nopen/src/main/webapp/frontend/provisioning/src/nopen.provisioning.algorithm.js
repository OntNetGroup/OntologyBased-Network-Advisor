nopen.provisioning.Algorithm = Backbone.Model.extend({
	app : undefined,
	connection: undefined,
	topology : undefined,

	initialize : function() {
		console.log("Algorithm started");
	},

	setApp : function(app) {
		this.app = app;
		this.connection = app.connection;
//		this.topology = app.getTopology();
	},
	setTopology : function(graph){
		this.topology = graph;
	},

	doProvisioning: function(type,equipments,ports) {
		var rps = this.connection.getConnectionBetweenReferencePoints(ports.source.id,ports.target.id);
		var paths = this.topology.shortestPath(equipments.source.id, equipments.target.id);
		if(!paths){
			console.log('god is the king of black cocade');
			return;
		}
		console.log(paths);
		if(type === 'Simple Connection'){
			this.simpleConnection(rps,ports,paths);
		}
	},

//	simpleConnection : function(rps,source,target){
//	var connection = this.connection;
//	var rp_so, rp_sk;

//	$.each(rps, function(index, rp){
//	rp_so = rp.rp_so;
//	rp_sk = rp.rp_sk;
//	});


//	var rp = rp_so;

//	do{
//	console.log(rp+" : "+this.connection.selectQuery(rp, ["a"]));
//	var vertical_rps = this.connection.selectQuery(rp, ["ont:vertical_path_so"]);

//	$.each(vertical_rps, function(index, vertical_rp){
////	console.log(vertical_rp + '-so--' + connection.askHasPathSo(vertical_rp, rp_sk));
//	if(connection.askHasPathSo(vertical_rp, rp_sk)){
//	if(connection.askQuery(vertical_rp, ["a"], "ont:FP")){
//	connection.insertTriple(rp, "ont:mc", vertical_rp);
//	}	
//	rp = vertical_rp;
//	return false;
//	}
//	});	

//	}while(!connection.askQuery(rp, ["ont:links_input", "a"], "ont:PM_Input_So"))

//	rp = this.connection.selectQuery(rp, ["ont:has_path"])[0];

//	do{
//	console.log(rp+" : "+this.connection.selectQuery(rp, ["a"]));
//	var vertical_rps = this.connection.selectQuery(rp, ["ont:vertical_path_sk"]);

//	$.each(vertical_rps, function(index, vertical_rp){
////	console.log(vertical_rp + '-sk--' + connection.askHasPathSk(vertical_rp, rp_sk));
//	if(connection.askHasPathSk(vertical_rp, rp_sk)){
//	if(connection.askQuery(rp, ["a"], "ont:FP")){
//	connection.insertTriple(vertical_rp, "ont:mc", rp);
//	}	
//	rp = vertical_rp;
//	return false;
//	}
//	});	

//	}while(rp != rp_sk)

//	console.log(rp);

//	},

	simpleConnection : function(rps,ports,p){
		var connection = this.connection;
		var rp_so, rp_sk;
		var paths = p.reverse();

		$.each(rps, function(index, rp){
			rp_so = rp.rp_so;
			rp_sk = rp.rp_sk;
		});

		var rp = "ont:"+rp_so;


		do{
			var vertical_rps = this.connection.selectQuery(rp, ["ont:vertical_path_so"]);

			$.each(vertical_rps, function(index, vertical_rp){
				if(connection.askNextEquipmentPath(vertical_rp, "ont:"+paths[0])){
					if(connection.askQuery(vertical_rp, ["a"], "ont:FP")){
						if(!connection.askQuery(rp, ["ont:mc"], vertical_rp))
							connection.insertTriple(rp, "ont:mc", vertical_rp);
					}	
					rp = vertical_rp;
					return false;
				}
			});	
		}while(!connection.askQuery(rp, ["ont:links_input", "a"], "ont:PM_Input_So"))

		paths.splice(0, 1);	
		rp = this.connection.selectQuery(rp, ["ont:physical_path"])[0];

		if(paths.length == 1){
			
		}
		
		do{
			var vertical_rps = this.connection.selectQuery(rp, ["ont:vertical_path_sk"]);

			$.each(vertical_rps, function(index, vertical_rp){
				if(connection.askQuery(rp, ["a"], "ont:FP")){
					var pccs = connection.getPossibleCrossConnections(rp);
					$.each(pccs, function(index, pcc){
						if(connection.askNextEquipmentPath(pcc, "ont:"+paths[0])){
							connection.insertTriple(rp, "ont:cc", pcc);
						}
					});
					
				}	
				rp = vertical_rp;
				return false;
			});	

		}while(rp != rp_sk)
			console.log(rp);

	},

});