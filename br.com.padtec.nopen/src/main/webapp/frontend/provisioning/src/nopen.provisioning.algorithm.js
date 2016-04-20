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
		console.log(rps);
		var paths = this.topology.shortestPath(equipments.source.id, equipments.target.id);
		console.log(paths);
		var s = "";
		var connection = this.connection;
		
		$.each(paths, function(i, p){
			s+="  "+connection.selectQuery("ont:"+p,["rdfs:label"]);
		});
		s+=connection.selectQuery("ont:"+equipments.source.id,["rdfs:label"]);
		console.log(s);
		if(!paths){
			console.log('god is the king of black cocade');
			return;
		}
		console.log(paths);
		if(type === 'Simple Connection'){
			this.simpleConnection(rps,ports,paths);
		}
		
	},

	
	
	
	simpleConnection : function(rps,ports,p){
		function vertical_path_so(rp,paths){
			
			do{
				var vertical_rps = connection.selectQuery(rp, ["ont:vertical_path_so"]);
             
				$.each(vertical_rps, function(index, vertical_rp){
					console.log(vertical_rps);
					if(connection.askNextEquipmentPath(vertical_rp, "ont:"+paths[0])){
						var tfk = connection.selectQuery(vertical_rp, ['ont:k'])[0];
						console.log(tfk);
						//se tfk for maior ou indefinido
						if (!tfk){
							//sempre que nao tiver k sera um FP sendo assim é mais facil usalo como referencia para pegar a matriz seguindo a ordem rf output matrix output e dela pegando a matriz
							
							if(connection.askQuery(vertical_rp, ["a"], "ont:FP")){
								if(!connection.askQuery(rp, ["ont:mc"], vertical_rp)) //guardar matriz(rp ?matriz?veto?hash?)
									connection.insertTriple(rp, "ont:mc", vertical_rp);
							}	
							rp = vertical_rp;
							return false;
						}
						
						else if(tfk >= provisioningK){
							if(connection.askQuery(vertical_rp, ["a"], "ont:FP")){
								if(!connection.askQuery(rp, ["ont:mc"], vertical_rp)) //guardar matriz(rp ?matriz?veto?hash?)
									connection.insertTriple(rp, "ont:mc", vertical_rp);
							}	
							rp = vertical_rp;
							return false;
						}else{
							
							//se tfk  < provisioningK voltar para a matriz anterior
							//caso nao enha matriz anterior ai vc retorna que nao é possivel
						}
//						else if(){
							//se tfk  < provisioningK voltar para a matriz anterior
							//caso nao enha matriz anterior ai vc retorna que nao é possivel
//						}
					}
				});	
			}while(!(connection.askQuery(rp, ["ont:links_input", "a"], "ont:PM_Input_So") || connection.askQuery(rp, ["ont:links_input", "a"], "ont:PM_Input_Sk")))
			var ret = connection.selectQuery(rp, ["ont:physical_path"]);
			if(ret.length == 0){
				ret = connection.selectQuery(rp, ["ont:INV.physical_path"]);
			}
			return ret[0];	
		}

		function vertical_path_intermediates(rp,paths){
			var rp_result = undefined;
			do{
				var vertical_rps = connection.selectQuery(rp, ["ont:vertical_path_sk"]);

				$.each(vertical_rps, function(index, vertical_rp){
					//var tfk = connection.selectQuery(vertical_rp, ['ont:k'])[0];
					//console.log(tfk);
					//se tfk for maior ou indefinido
					if(connection.askQuery(rp, ["a"], "ont:FP")){
						var pccs = connection.getPossibleCrossConnections(rp);
						$.each(pccs, function(index, pcc){
							if(connection.askNextEquipmentPath(pcc, "ont:"+paths[0])){
								connection.insertTriple(rp, "ont:cc", pcc);
								rp_result = pcc;
								return false;
							}
						});
						if(rp_result){
							return false;
						}
					}	
					rp = vertical_rp;
					return false;
				});	
				if(rp_result){
					return rp_result;
				}
			}while(true)
		}

		function vertical_path_sk(rp,rp_sk){
			do{
				var vertical_rps = connection.selectQuery(rp, ["ont:vertical_path_sk"]);
				$.each(vertical_rps, function(index, vertical_rp){
					if(connection.askHasPathSk(vertical_rp, rp_sk)){
						if(connection.askQuery(rp, ["a"], "ont:FP")){
							connection.insertTriple(vertical_rp, "ont:mc", rp);
						}	
						rp = vertical_rp;
						return false;
					}
				});	

			}while(rp != rp_sk)
			return rp;
		}
		//count time
		var start = new Date().getTime();
		
		var connection = this.connection;
		var rp_so, rp_sk;
		var paths = p.reverse();
		var provisioningk = 3;
		
		$.each(rps, function(index, rp){
			rp_so = "ont:"+rp.rp_so;
			rp_sk = "ont:"+rp.rp_sk;
		});

		var s = "";
		$.each(paths, function(i, pp){
			s+="  "+connection.selectQuery("ont:"+pp,["rdfs:label"]);
		});
		console.log(s);
		
		var rp = rp_so;

		do{
			console.log("vertical_path_so!"+ connection.selectQuery("ont:"+paths[0],["rdfs:label"]));
			rp = vertical_path_so(rp,paths);
			paths.splice(0, 1);	
			if(paths.length == 0){
				console.log("vertical_path_sk!");
				rp = vertical_path_sk(rp,rp_sk);
			}else{
				console.log("vertical_path_intermediates!"+ connection.selectQuery("ont:"+paths[0],["rdfs:label"]));
				rp = vertical_path_intermediates(rp,paths);	
			}
		}while(rp != rp_sk)

		var end = new Date().getTime();
		var time = (end - start)/1000;
		console.log("Provisioned in: "+time+"s");		
	},

	
	bandwithConnection: function(rps,ports,p , k){
		
		function vertical_path_so(rp,paths){
			 
			do{
				
				var vertical_rps = connection.selectQuery(rp, ["ont:vertical_path_so"]);

				$.each(vertical_rps, function(index, vertical_rp){
					if(connection.askNextEquipmentPath(vertical_rp, "ont:"+paths[0])){
                      var tfk = connection.selectQuery(vertical_rp, 'ont:k')[0];
                      console.log(tfk);
//					
//						return;
//					}
						if(connection.askQuery(vertical_rp, ["a"], "ont:FP")){
							if(!connection.askQuery(rp, ["ont:mc"], vertical_rp))
								connection.insertTriple(rp, "ont:mc", vertical_rp);
						}	
						rp = vertical_rp;
						return false;
					}
				});	
			}while(!(connection.askQuery(rp, ["ont:links_input", "a"], "ont:PM_Input_So") || connection.askQuery(rp, ["ont:links_input", "a"], "ont:PM_Input_Sk")))
			var ret = connection.selectQuery(rp, ["ont:physical_path"]);
			if(ret.length == 0){
				ret = connection.selectQuery(rp, ["ont:INV.physical_path"]);
			}
			return ret[0];	
		}

		function vertical_path_intermediates(rp,paths){
			var rp_result = undefined;
			do{
				var vertical_rps = connection.selectQuery(rp, ["ont:vertical_path_sk"]);

				$.each(vertical_rps, function(index, vertical_rp){
					if(connection.askQuery(rp, ["a"], "ont:FP")){
						var pccs = connection.getPossibleCrossConnections(rp);
						$.each(pccs, function(index, pcc){
							if(connection.askNextEquipmentPath(pcc, "ont:"+paths[0])){
								connection.insertTriple(rp, "ont:cc", pcc);
								rp_result = pcc;
								return false;
							}
						});
						if(rp_result){
							return false;
						}
					}	
					rp = vertical_rp;
					return false;
				});	
				if(rp_result){
					return rp_result;
				}
			}while(true)
		}

		function vertical_path_sk(rp,rp_sk){
			do{
				var vertical_rps = connection.selectQuery(rp, ["ont:vertical_path_sk"]);
				$.each(vertical_rps, function(index, vertical_rp){
					if(connection.askHasPathSk(vertical_rp, rp_sk)){
						if(connection.askQuery(rp, ["a"], "ont:FP")){
							connection.insertTriple(vertical_rp, "ont:mc", rp);
						}	
						rp = vertical_rp;
						return false;
					}
				});	

			}while(rp != rp_sk)
			return rp;
		}
		//count time
		var start = new Date().getTime();
		
		var provisioningk = k;
		var connection = this.connection;
		var rp_so, rp_sk;
		var paths = p.reverse();
		
		$.each(rps, function(index, rp){
			rp_so = "ont:"+rp.rp_so;
			rp_sk = "ont:"+rp.rp_sk;
		});

		var s = "";
		$.each(paths, function(i, pp){
			s+="  "+connection.selectQuery("ont:"+pp,["rdfs:label"]);
		});
		console.log(s);
		
		var rp = rp_so;

		do{
			
			// confirmar o k
			console.log("vertical_path_so!"+ connection.selectQuery("ont:"+paths[0],["rdfs:label"]));
		//desce	
			rp = vertical_path_so(rp,paths);
			
			paths.splice(0, 1);	
			
			if(paths.length == 0){
			
				console.log("vertical_path_sk!");
				//sobe
				rp = vertical_path_sk(rp,rp_sk);
			
			}else{
			
				console.log("vertical_path_intermediates!"+ connection.selectQuery("ont:"+paths[0],["rdfs:label"]));
				//troca de equipameno
				rp = vertical_path_intermediates(rp,paths);	
		
			}
		
		}while(rp != rp_sk)

		var end = new Date().getTime();
		var time = (end - start)/1000;
		console.log("Provisioned in: "+time+"s");		
		
	},
	
	
	
	
	
});