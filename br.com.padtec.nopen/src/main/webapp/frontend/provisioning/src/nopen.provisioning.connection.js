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
	
	prefix : "onto",
	namespcae: "http://www.menthor.net/provisioning.owl#",
	
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
					"onto=http://www.menthor.net/provisioning.owl#",
				],
//				"icv.reasoning.enabled" : true,
			},
			"reasoning" : "SL",
			"files" : [],	
		};
		
		this.connection.createDB(
			options,
			function(data) {
				console.log('DATABASE CREATED!');
			});
		
	},
	
	save : function(triples, namedGraph) {
		
		var database = this.database;
		var connection = this.connection;
		
		var query = 'INSERT DATA {';
		
		$.each(triples, function(index, triple) {
			
//			var query = 'INSERT DATA { GRAPH <' + namedGraph + '> { ' + triple.s + ' ' + triple.p + ' ' + triple.o + ' . } }';
			query = query + ' ' + triple.s + ' ' + triple.p + ' ' + triple.o + ' . ';
			
		});
		
		query = query + '}';
		console.log(query);
		
		connection.query({
			"database" : database,
			"query": query,
//			"reasoning" : true,
		},
		function (data) {});
		
		
//		var query = 'CLEAR GRAPH <' + namedGraph + '>';
//		connection.query({
//			"database" : database,
//			"query": query,  
//		},
//		function () {
//			
//			
//			
//		});
		
	},
	
	insertQuery : function(graph, triples) {
		
		
		
		//document.
	},
	
	selectQuery : function() {
		
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
	
	testQuery : function() {
		
//		var options = {
//				"database" : this.database,
//			};  
//			
//			this.connection.getDB(options, function(data) {
//				console.log(JSON.stringify(data));
//				//return data;
//			});
//		
//			this.connection.getNamespaces(options, function(data) {
//				console.log('NS: ' + JSON.stringify(data));
//			});
			
//		this.connection.query({
//	        database: "testConnectionAPI",
//	        query: "select distinct * where { ?s ?p ?o }",  
//	        limit: 10,
//	        offset: 0,
//		},
//		function (data) {
//	        console.log(JSON.stringify(data.results.bindings));
//	    });
		
//		var query = ' INSERT DATA { np:Test rdf:type owl:Class .' + 
//					' np:Test rdfs:label "Test" . }';
			
		var query = ' SELECT * WHERE { ?x onto:has_path ?y . } ';
			
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
//			"database" : "testDB",
//		}, function(data) {
//			console.log('DATABASE DROPPED!');
//		});
		
	},

});