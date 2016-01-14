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

	namedGraph: "http://localhost:8080/nopen/provisioning.htm",

	initialize : function() {

		this.initializeConnection();
		this.initializeDB();

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

		var query = 'CLEAR GRAPH <' + namedGraph + '>';
		this.connection.query({
			"database" : database,
			"query": query,  
		},
		function () {});

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

		var namedGraph = this.namedGraph;

		var query = 'CLEAR GRAPH <' + newNamedGraph + '>';
		this.executeNamedGraphQuery(query);

		query = 'COPY <' + namedGraph + '> TO <' + newNamedGraph + '>';
		this.executeNamedGraphQuery(query);

	},

	loadNamedGraph : function(loadedNamedGraph) {

		var namedGraph = this.namedGraph;

		var query = 'CLEAR GRAPH <' + namedGraph + '>';
		this.executeNamedGraphQuery(query);

		query = 'COPY <' + loadedNamedGraph + '> TO <' + namedGraph + '>';
		this.executeNamedGraphQuery(query);

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
		console.log(query);

		connection.query({
			"database" : database,
			"query": query,
		},
		function (data) {});

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

	selectPortsOfMatrixType : function(layer){
		var query = 'SELECT ?o { GRAPH <' + this.namedGraph + '>  {' + layer + ' ont:INV.intermediates_up ?x . ?x rdf:type ont:Matrix . ?x ont:INV.is_interface_of.Output_Card.Transport_Function ?o . } } ORDER BY ?o';
		
		var result = this.executeQueryWithReasoning(query);

		var objects = [];

		$.each(result, function(index, object) {
			objects.push(object.o.value);
		})

		return objects;
	},
	
	selectCardLabelFromPort : function(card){
		var query = 'SELECT ?card_label { GRAPH <' + this.namedGraph + '>  {ont:' + card + ' ont:INV.A_Card_OutputCard ?card . ?card rdfs:label ?card_label } }';
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

		var result = this.executeAskQueryWithReasoning(query);

		return result;
	},

	executeQueryWithReasoning : function(query) {

		var result = undefined;

		jQuery.ajaxSetup({async:false});

		this.connection.query({
			database: "nopen",
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

		jQuery.ajaxSetup({async:false});

		this.connection.query({
			database: "nopen",
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

		jQuery.ajaxSetup({async:false});

		this.connection.query({
			database: "nopen",
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

		jQuery.ajaxSetup({async:false});

		this.connection.query({
			database: "nopen",
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

		var query = ' SELECT * WHERE { ?x ont:has_path ?y . } ';

		this.connection.query({
			database: "nopen",
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