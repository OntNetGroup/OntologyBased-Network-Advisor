/*
	==========================================
	HTTP Headers: Content-Type & Accept	
	==========================================
	RDF/XML	application/rdf+xml
	Turtle application/x-turtle or text/turtle
	N-Triples text/plain
	TriG application/x-trig
	TriX application/trix 
	NQuads text/x-nquads
	JSON-LD application/ld+json

	SPARQL XML Results Format application/sparql-results+xml
	SPARQL JSON Results Format application/sparql-results+json
	SPARQL Boolean Results text/boolean
	SPARQL Binary Results application/x-binary-rdf-results-table
 */		

nopen.provisioning.Connection = Backbone.Model.extend({

	connection : undefined,

	endpoint : "http://localhost:5820/",
	username : "admin",
	password : "admin",

	database : "nopen",

	prefix : "ont",
	namespace: "http://www.menthor.net/provisioning.owl#",

	namedGraph: "",
//	namedGraph: "http://localhost:8080/nopen/provisioning.htm",

	initialize : function() {
        
		this.initializeNamedGraph();
//		this.initializeConnection();
//		this.initializeDB();

	},

	initializeNamedGraph: function() {
	    
		$this = this;	
		
		var content = '<div id="save-dialog" title="Graph Name:">'
			+ 'Name: <input type="text" id="save-filename" value="' + $("#filename").val() + '"/>'
			+ '</div>'
			+ '<div id="name-error-message">' + 'Name cannot be empty!' + '</div>';
			
		var dialog = new joint.ui.Dialog({
			width: 300,
			type: 'neutral',
			title: 'Graph Name:',
			content: content,
			buttons: [
				{ action: 'cancel', content: 'Cancel', position: 'left' },
				{ action: 'save', content: 'Save', position: 'left' }
			]
		});
		
		dialog.on('action:save', check);
		dialog.on('action:cancel', cancel);

		dialog.open();
		
		function check(){
			
			var filename = $("#save-filename").val();
			
			if(filename == ""){
				$('#name-error-message').show();
			}
			else{
				$this.setNamedGraph(filename);
//				this.initializeConnection();
//				this.initializeDB();
				dialog.close();
			}
		}
		
		function cancel(){
			dialog.close();
		}
		
		
	},
	
	setNamedGraph: function(namedgraph,opt) {
		console.log(this.namedGraph);
		this.namedGraph = 'http://localhost:8080/'+namedgraph;
		console.log(this.namedGraph);
		if(opt){
			console.log('opt');
		}else{
			this.initializeConnection();
			this.initializeDB();
		}
	},
	
	initializeConnection : function() {

		var endpoint = this.endpoint;
		var username = this.username;
		var password = this.password;

		if(!this.connection) {
			this.connection = new Stardog.Connection();
			this.connection.setEndpoint(endpoint);
			this.connection.setCredentials(username, password);
		}

	},

	initializeDB : function() {

		var $this = this;
		var exist = false;

		var namedGraph = this.namedGraph;
		var database = this.database;

		this.connection.listDBs(function(data) {
			_.each(data.databases, function (dbName) {
				if(dbName === database) {
					console.log('Database: ' + dbName);
					exist = true;
				}
			});

			//if database not exist, create it!
			if(!exist) {
				$this.createDB();
			}
		}); 

//		var query = 'CLEAR GRAPH <' + namedGraph + '>';
//		this.connection.query({
//			"database" : database,
//			"query": query,  
//		},
//		function () {});

	},

	setDB : function(database) {
		this.database = database;
	},

	getDB : function() {
		return this.database;
	},

	createDB : function() {

		var database = this.database;

		var options = {
				"database" : database,
				"options" : {
					"database.namespaces" : [
					                         "ont=http://www.menthor.net/provisioning.owl#",
					                         ],
//					                         "icv.reasoning.enabled" : true,
				},
//				"reasoning" : "SL",
				"files" : [],	
		};

		this.connection.createDB(
				options,
				function(data) {
					console.log('DATABASE CREATED!');
				});

	},

	saveNamedGraph : function(newNamedGraph) {

//		var namedGraph = this.namedGraph;
//
//		var query = 'CLEAR GRAPH <' + newNamedGraph + '>';
//		this.executeNamedGraphQuery(query);
//
//		query = 'COPY <' + namedGraph + '> TO <' + newNamedGraph + '>';
//		this.executeNamedGraphQuery(query);

	},

	loadNamedGraph : function(loadedNamedGraph) {

//		var namedGraph = this.namedGraph;
//
//		var query = 'CLEAR GRAPH <' + namedGraph + '>';
//		this.executeNamedGraphQuery(query);
//
//		query = 'COPY <' + loadedNamedGraph + '> TO <' + namedGraph + '>';
//		this.executeNamedGraphQuery(query);

	},

	deleteNamedGraph : function(deletedNamedGraph) {

		var query = 'CLEAR GRAPH <' + loadedNamedGraph + '>';
		this.executeNamedGraphQuery(query);

	},

	save : function(triples, namedGraph) {

		var database = this.database;
		var connection = this.connection;

		var namedGraph = this.namedGraph;
		var query = 'INSERT DATA { GRAPH <' + namedGraph + '> {';

		$.each(triples, function(index, triple) {
			query = query + ' ' + triple.s + ' ' + triple.p + ' ' + triple.o + ' . ';

		});

		query = query + '} }';
//		console.log(query);
		jQuery.ajaxSetup({async:false});
		connection.query({
			"database" : database,
			"query": query,
		},
		function (data) {});
		jQuery.ajaxSetup({async:true});
	},

	selectAllLayers : function() {
		var query = 'SELECT DISTINCT ?label WHERE { ?layer rdf:type ont:Layer_Type . ?layer rdfs:label ?label . }'
			return this.selectLayer(query);	
	},

	selectTopLayers : function() {
		var query = 'SELECT DISTINCT ?label WHERE { ?layer rdf:type ont:Layer_Type . ?layer rdfs:label ?label . FILTER NOT EXISTS { ?layer ont:is_client ?x . } }'
			return this.selectLayer(query);	
	},

	selectBottomLayers : function() {
		var query = 'SELECT DISTINCT ?label WHERE { ?layer rdf:type ont:Layer_Type . ?layer rdfs:label ?label . FILTER NOT EXISTS { ?layer ont:INV.is_client ?x . } }'
			return this.selectLayer(query);	
	},

	selectLayer : function(query) {

		var layers = [];

		jQuery.ajaxSetup({async:false});

		var result = this.executeQuery(query);

		$.each(result, function(index, layer) {
			layers.push(layer.label.value);
		})

		console.log(JSON.stringify(layers));

		return layers;

	},

	selectLabelQuery : function(subject) {

		var namedGraph = this.namedGraph;
		query = 'SELECT ?label { GRAPH <' + namedGraph + '> { <' + subject + '> rdfs:label ?label . } }';

		var result = this.executeQuery(query);

		if(!result[0]){
			return "--";
		}
		var label = result[0].label.value;
		//label = label.replace(this.namespace, '');

		return label;

	},

	selectObjectsQuery : function(subject, predicates) {

		var namedGraph = this.namedGraph;
		var query = 'SELECT * { GRAPH <' + namedGraph + '> { ' + subject

		$.each(predicates, function(index, predicate){

			if(index === 0) {
				query = query + ' ' + predicate;
			}
			else {
				query = query + '/' + predicate;
			}

		});

		query = query + ' ?o . } }';

//		console.log('QUERY: ' + query);

		var result = this.executeQueryWithReasoning(query);

		var objects = [];

		$.each(result, function(index, object) {
			objects.push(object.o.value);
		})

		return objects;

	},
	getPossibleCrossConnections : function (rp){
		var namedGraph = this.namedGraph;
		var namespace = this.namespace;

		var query = 'select ?rps { GRAPH <' + namedGraph + '> {' +
				'  '+ rp +' ont:links_output.Reference_Point.Output/ont:INV.A_Matrix_MatrixOutput ?matrix .' +
				'  ?matrix ont:A_Matrix_MatrixOutput ?output .' +
				'  ?output ont:INV.links_output ?rps .' +
				'  FILTER (?rps != '+ rp +')' +
				'} }';
		
		var result = this.executeQueryWithReasoning(query);

		var objects = [];

		$.each(result, function(index, object) {
			objects.push(object.rps.value.replace(namespace, 'ont:'));
		})

		return objects;
	},
	selectQuery : function(subject, predicates) {

		var namedGraph = this.namedGraph;
		var query = 'SELECT * { GRAPH <' + namedGraph + '> { ' + subject
		var namespace = this.namespace;
		
		$.each(predicates, function(index, predicate){

			if(index === 0) {
				query = query + ' ' + predicate;
			}
			else {
				query = query + '/' + predicate;
			}

		});

		query = query + ' ?o . } }';

//		console.log('QUERY: ' + query);

		var result = this.executeQueryWithReasoning(query);

		var objects = [];

		$.each(result, function(index, object) {
			objects.push(object.o.value.replace(namespace, 'ont:'));
		})

		return objects;

	},

	selectTFOutFromTF: function(tf,port) {

		var query = 'SELECT * { GRAPH <' + this.namedGraph + '>  {ont:'+ tf +' ont:A_TransportFunction_Output ?tfout . ont:'+ port +' ont:binds.Output_Card.Output ?tfout	} }';

		var result = this.executeQuery(query);
		return result[0].tfout.value.replace(this.namespace, '');
	},

	selectTFInFromTF: function(tf,port) {

		var query = 'SELECT * { GRAPH <' + this.namedGraph + '>  {ont:'+ tf +' ont:A_TransportFunction_Input ?tfin .	ont:'+ port +' ont:binds.Input_Card.Input ?tfin	} }';

		var result = this.executeQuery(query);
		return result[0].tfin.value.replace(this.namespace, '');
	},

	getTFType: function(tf) {

		var query = 'SELECT * { GRAPH <' + this.namedGraph + '>  {ont:'+ tf +' rdf:type ?types }}';

		var namespace = this.namespace;
		var result = this.executeQueryWithReasoning(query);
		var toreturn;
		$.each(result, function(index, object) {
//			console.log(object.types.value);
			if(object.types.value === 'http://www.menthor.net/provisioning.owl#Adaptation_Function'){
				toreturn = object.types.value.replace(namespace, '');
				return false;
			}
			else if(object.types.value === 'http://www.menthor.net/provisioning.owl#Trail_Termination_Function'){
				toreturn = object.types.value.replace(namespace, '');
				return false;
			}
			else if(object.types.value === 'http://www.menthor.net/provisioning.owl#Matrix'){
				toreturn = object.types.value.replace(namespace, '');
				return false;
			}
		})
		return toreturn;
	},
	
	selectPortsOfMatrixType : function(layer){
//		var query = 'SELECT ?o { GRAPH <' + this.namedGraph + '>  {' + layer + ' ont:INV.intermediates_up ?x . ?x rdf:type ont:Matrix . ?x ont:INV.is_interface_of.Output_Card.Transport_Function ?o . } } ORDER BY ?o';
		var query = 'SELECT ?out_port { GRAPH <' + this.namedGraph + '>  {' + layer + ' ont:INV.A_Card_CardLayer/ont:A_Card_OutputCard ?out_port . } }';
		var result = this.executeQueryWithReasoning(query);

		var objects = [];

		$.each(result, function(index, object) {
			objects.push(object.out_port.value);
		})

		return objects;
	},
	/*
	 * A funcao recebe output_cards, pega os rp e verifica o has_path
	 * */
	getConnectionBetweenReferencePoints : function(outcard_source, outcard_sink){
		
		var namedGraph = this.namedGraph;
		var namespace = this.namespace;
		
		var query = 'SELECT ?rp_source ?rp_target { GRAPH <' + namedGraph + '> ' +
		'{	ont:' + outcard_source + ' ont:binds.Output_Card.Output/ont:INV.links_output ?rp_source . ' +
		'ont:' + outcard_sink + ' ont:binds.Output_Card.Output/ont:INV.links_output ?rp_target . ' +
		'} }';
		
		console.log(query);
		
		var result = this.executeQueryWithReasoning(query);
		
		var rp = [];

		$.each(result, function(index, object) {
			rp.push({
				'rp_so' : object.rp_source.value.replace(namespace, ''),
				'rp_sk' : object.rp_target.value.replace(namespace, '')
			});
		})

		return rp;
	
	},
	
	askNextEquipmentPath : function(rp,nextEquipment){
		
		var query = "ASK { GRAPH <"+this.namedGraph+">  { " +
				rp+" ont:vertical_path_so* ?rps. " +
				"?rps ont:links_input ?o . " +
				" { ?o a ont:PM_Input_So. ?rps ont:physical_path ?rpsk . } UNION { ?o a ont:PM_Input_Sk . ?rps ont:INV.physical_path ?rpsk . } . " +
				"?rpsk ont:links_output ?out . " +
				"?out ont:INV.componentOf* "+nextEquipment +
				"} } ";
		
		
		var result = this.executeAskQueryWithReasoning(query);

		return result.boolean;
	},

	connectAllOTSRp : function(equipment){
		var namedGraph = this.namedGraph;
		
		var query = 'SELECT ?rp { GRAPH <' + namedGraph + '> {' +	
			          '?layer a ont:Card_Layer .' +
					  '?layer rdfs:label ?layer_label.' +
					  'Filter (?layer_label = "OTS").'  +
					  '?layer ont:INV.A_Card_CardLayer ?card.' +
					  '?card ont:INV.A_Equipment_Card ?equip .' +
		               '?card rdfs:label ?card_label .' +
					  '?equip rdfs:label ?equip_label .' +
					  'FILTER (?equip_label = '+ equipment +').' +
					  '?card ont:A_Card_OutputCard ?output_card .' +
					  '?output_card ont:binds ?tf_out .' +
					  '?tf_out ont:INV.links_output ?rp .' +  
					  '} }';
		
		console.log(query);
		
       var result = this.executeQueryWithReasoning(query);
		
		var rp = [];

		$.each(result, function(index, object) {
			rp.push(object.rp.value.replace(namespace, 'ont:'));
		});
		
		for (var i = 0; i < rp.length; i++){
			for(var j = 0; j < rp.length; j++){
				if(i != j){
					
					this.insertTriple(rp[i], 'ont:has_path' , rp[j]);
				}
			}
		}
	

		
		
		return rp;
		
		
	},
	
	askHasPathSo : function(rp_so,rp_sk){
		var namedGraph = this.namedGraph;

		var query = 'ASK { GRAPH <' + namedGraph + '> { ' + 
			rp_so+' ont:vertical_path_so* ?pmso . '+
			rp_sk+' ont:vertical_path_so* ?pmsk . '+
			'?pmso ont:has_path ?pmsk} }';

//		console.log(query);
		
		var result = this.executeAskQueryWithReasoning(query);

		return result.boolean;

	},

	askHasPathSk : function(rp_so,rp_sk){
		var namedGraph = this.namedGraph;

		var query = 'ASK { GRAPH <' + namedGraph + '> { ' + 
			rp_so+' ont:vertical_path_sk* '+rp_sk+' } }';
			
//		console.log(query);
		
		var result = this.executeAskQueryWithReasoning(query);

		return result.boolean;

	},
	
	selectCardLabelFromPort : function(card){
		var query = 'SELECT ?card_label { GRAPH <' + this.namedGraph + '>  {?card ont:A_Card_OutputCard ont:' + card + ' . ?card rdfs:label ?card_label } }';
		var result = this.executeQueryWithReasoning(query);
		return result[0].card_label.value;
	},

	askIsConnectedPort : function(port) {

		var namedGraph = this.namedGraph;

		var isConnectedPort = false; 

		var query = 'SELECT ?port { GRAPH <' + namedGraph + '>  { ont:' + port + ' ont:vertical_links_to|ont:horizontal_links_to ?port . } }'; 
		var result = this.executeQueryWithReasoning(query);

		if(result.length > 0) {
			isConnectedPort = true;
		}
		else {
			query = 'SELECT ?port { GRAPH <' + namedGraph + '>  { ?port ont:vertical_links_to|ont:horizontal_links_to ont:' + port + ' . } }';
			result = this.executeQueryWithReasoning(query);

			if(result.length > 0) {
				isConnectedPort = true;
			}
		}

		return isConnectedPort;

	}, 

	askQuery : function(subject, predicates, object) {

		var namedGraph = this.namedGraph;

		var query = 'ASK { GRAPH <' + namedGraph + '> { ' + subject

		$.each(predicates, function(index, predicate){

			if(index === 0) {
				query = query + ' ' + predicate;
			}
			else {
				query = query + '/' + predicate;
			}

		});

		query = query + ' ' + object + ' . } }';

//		console.log(query);
		var result = this.executeAskQueryWithReasoning(query);

		return result.boolean;
	},
	
	insertTriple : function(subject, predicate, object){
		
		var database = this.database;
		var connection = this.connection;

		var namedGraph = this.namedGraph;
		var query = 'INSERT DATA { GRAPH <' + namedGraph + '> {';

		query = query + ' ' + subject + ' ' + predicate + ' ' + object + ' . ';

		query = query + '} }';
		console.log(query);
		jQuery.ajaxSetup({async:false});
		connection.query({
			"database" : database,
			"query": query,
		},
		function (data) {});
		jQuery.ajaxSetup({async:true});
		
	},

	executeQueryWithReasoning : function(query) {

		var result = undefined;
		var database = this.database;
		jQuery.ajaxSetup({async:false});

		this.connection.query({
			database: database,
			query: query,  
			reasoning: true,
		},
		function (data) {
//			console.log(JSON.stringify(data.results.bindings));
			result = data.results.bindings;
		});

		jQuery.ajaxSetup({async:true});

		return result;
	},

	executeAskQueryWithReasoning : function(query) {

		var result = undefined;
		var database = this.database;

		jQuery.ajaxSetup({async:false});

		this.connection.query({
			database: database,
			query: query,  
			reasoning: true,
		},
		function (data) {
			result = data;
		});

		jQuery.ajaxSetup({async:true});

		return result;
	},

	executeQuery : function(query) {

		var result = undefined;
		var database = this.database;
		jQuery.ajaxSetup({async:false});

		this.connection.query({
			database: database,
			query: query,  
		},
		function (data) {
//			console.log(JSON.stringify(data));
			result = data.results.bindings;
		});

		jQuery.ajaxSetup({async:true});

		return result;
	},

	executeNamedGraphQuery : function(query) {

		var result = undefined;
		var database = this.database;

		jQuery.ajaxSetup({async:false});

		this.connection.query({
			database: database,
			query: query,  
		},
		function (data) { });

		jQuery.ajaxSetup({async:true});

	},	


	testQuery : function() {

//		var options = {
//		"database" : this.database,
//		};  

//		this.connection.getDB(options, function(data) {
//		console.log(JSON.stringify(data));
//		//return data;
//		});

//		this.connection.getNamespaces(options, function(data) {
//		console.log('NS: ' + JSON.stringify(data));
//		});

//		this.connection.query({
//		database: "testConnectionAPI",
//		query: "select distinct * where { ?s ?p ?o }",  
//		limit: 10,
//		offset: 0,
//		},
//		function (data) {
//		console.log(JSON.stringify(data.results.bindings));
//		});

//		var query = ' INSERT DATA { np:Test rdf:type owl:Class .' + 
//		' np:Test rdfs:label "Test" . }';
		var database = this.database;

		var query = ' SELECT * WHERE { ?x ont:has_path ?y . } ';

		this.connection.query({
			database: database,
			query: query,  
			reasoning: true,
		},
		function (data) {
			console.log(JSON.stringify(data));
		});



		//this.connection.shutdownServer();

//		this.connection.dropDB({
//		"database" : "testDB",
//		}, function(data) {
//		console.log('DATABASE DROPPED!');
//		});

	},
	getRPInAndRPOut : function(){
		var namedGraph = this.namedGraph;
		var namespace = this.namespace;
		var query = 'SELECT ?rpin ?rpout  { GRAPH <'+ namedGraph +'>  {'+
		'?rpin ont:links_input.Reference_Point.Input ?RPlink_input . '+
		'?RPlink_inputcompose ont:A_TransportFunction_Input ?RPlink_input . '+
		'?RPlink_inputcompose ont:A_TransportFunction_Output ?RPlink_inputcompose_composed_out . '+
		'?rpout  ont:links_output.Reference_Point.Output ?RPlink_inputcompose_composed_out . '+
		'} }';

		var result = this.executeQueryWithReasoning(query);

		var objects = [];

		$.each(result, function(index, object) {
			var element ={
					'rpin' : object.rpin.value.replace(namespace, ''),
					'rpout' : object.rpout.value.replace(namespace, '')
			};
			objects.push(element);
		})

		return objects;
	},

	createHasPathfromPM: function(RpIn , RPOut){
		var connection = this.connection;
		var database = this.database;
		var namedGraph = this.namedGraph;

		var query = 'INSERT DATA { GRAPH <' + namedGraph + '> {ont:' + RpIn + ' ont:has_path ont:' + RPOut + ' . }}';
//		console.log(query);

		connection.query({
			"database" : database,
			"query": query,
		},
		function (data) {});
	},

	insertHasPath : function(hasPaths){
		var connection = this.connection;
		var database = this.database;

		var namedGraph = this.namedGraph;
		var query = 'INSERT DATA { GRAPH <' + namedGraph + '> {';

		$.each(hasPaths, function(index, triple) {
			query = query + ' ont:' + triple.rpin + ' ont:has_path ont:' + triple.rpout + ' . ';
		});

		query = query + '} }';
//		console.log(query);

		connection.query({
			"database" : database,
			"query": query,
		},
		function (data) {});
	},

	insertQuery : function(graph, triples) {


//		var query = 'CLEAR GRAPH <' + namedGraph + '>';
//		connection.query({
//		"database" : database,
//		"query": query,  
//		},
//		function () {



//		});

		//document.
	},

	selectQueryTest : function() {

		var query = 'SELECT * WHERE { GRAPH ?g { <' + document.URL + '> ?y ?z . } }';
		var result = "";

		this.connection.query({
			database: "testDB",
			query: query,  
		},
		function (data) {
			console.log(JSON.stringify(data));
			result = JSON.stringify(data);
		});

		return result;
	},

});