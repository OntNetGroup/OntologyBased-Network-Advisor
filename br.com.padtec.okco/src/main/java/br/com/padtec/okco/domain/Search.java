package br.com.padtec.okco.domain;
import java.util.ArrayList;
import java.util.List;

import br.com.padtec.common.queries.InfModelQueryUtil;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class Search 
{	
	
	public ArrayList<DtoDefinitionClass> GetSomeRelationsOfClass(InfModel infModel, String clsuri) {
		System.out.println("\nExecuting GetSomeRelationsOfClass()...");
		ArrayList<DtoDefinitionClass> dtoSomeList = new ArrayList<DtoDefinitionClass>();
		
		// Create a new query -- EQUIVALENT CLASS
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + infModel.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT ?x ?y ?z" +
		" WHERE {\n" +			
			" { " +
				" ?x " + "owl:equivalentClass" + " ?blank .\n " +
				" ?blank rdf:type owl:Class .\n"  +
				" ?blank owl:intersectionOf  ?list  .\n" +
				" ?list  rdf:rest*/rdf:first  ?member . \n"  +			
				" ?member " + "owl:someValuesFrom" + " ?z .\n " +
				" ?member " + "owl:onProperty ?y .\n" +	
				
				" FILTER( ?x = <" + clsuri + "> ) " +
			"} UNION {\n" +		
				" ?x " + "owl:equivalentClass" + " _:b0 .\n " +				
				" _:b0 " + "owl:someValuesFrom" + " ?z .\n " +
				" _:b0 " + "owl:onProperty ?y .\n" +
				
				" FILTER( ?x = <" + clsuri + "> ) " +
			" }\n" +	
				
			"UNION { " +
				" ?x " + "rdfs:subClassOf" + " ?blank .\n " +
				" ?blank rdf:type owl:Class .\n"  +
				" ?blank owl:intersectionOf  ?list  .\n" +
				" ?list  rdf:rest*/rdf:first  ?member . \n"  +			
				" ?member " + "owl:someValuesFrom" + " ?z .\n " +
				" ?member " + "owl:onProperty ?y .\n" +	
				
				" FILTER( ?x = <" + clsuri + "> ) " +
			"} UNION {\n" +		
				" ?x " + "rdfs:subClassOf" + " _:b1 .\n " +				
				" _:b1 " + "owl:someValuesFrom" + " ?z .\n " +
				" _:b1 " + "owl:onProperty ?y .\n" +
				
				" FILTER( ?x = <" + clsuri + "> ) " +
			" }\n" +			
		
		"}";
		
		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		ResultSet results = qe.execSelect();
		
		// Output query results 
		//ResultSetFormatter.out(System.out, results, query);
		
		DtoDefinitionClass itemList = null;
		while (results.hasNext()) {
			QuerySolution row= results.next();
		    RDFNode Source = row.get("x");
		    RDFNode Relation = row.get("y");
		    RDFNode Target = row.get("z");
		    
		    // jump the blank node if he exist
		    //if(! Target.toString().contains("#")){
		    //	continue;
		    //}
		    
		    //jump the blank nodes - Check blank node and signal '-'
		    String TargetStr = Target.toString();
		    String SourceStr = Source.toString();
		    if ( Character.isDigit(TargetStr.charAt(0)) || TargetStr.startsWith("-") || Character.isDigit(SourceStr.charAt(0)) || SourceStr.startsWith("-")) 
		    {
		        continue;
		    }
		    
			itemList = new DtoDefinitionClass();
			itemList.Source = Source.toString();
			itemList.Relation = Relation.toString();
			itemList.PropertyType = InfModelQueryUtil.getPropertyURIType(infModel, Relation.toString());
			itemList.TypeCompletness = EnumRelationTypeCompletness.SOME;
			itemList.Target = Target.toString();
			dtoSomeList.add(itemList);
		}
				
		// Create a new query -- SUB CLASS OF DE CLASSE DEFINIDA
		
		queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + infModel.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT ?x ?y" +
		" WHERE {\n" +
				" ?x " + "rdfs:subClassOf" + " ?y .\n " +
			"}";

		query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		qe = QueryExecutionFactory.create(query, infModel);
		results = qe.execSelect();
		
		// Output query results 
		// ResultSetFormatter.out(System.out, results, query);
		
		while (results.hasNext()) {
			
			QuerySolution row= results.next();
		    RDFNode Class = row.get("x");
		    RDFNode SuperClass = row.get("y");
		    
		    if(!Class.toString().contains(InfModelQueryUtil.w3URI) && !SuperClass.toString().contains(InfModelQueryUtil.w3URI) && Class.toString() != SuperClass.toString())
		    {		    	
		    	ArrayList<DtoDefinitionClass> dtoListWithSource = DtoDefinitionClass.getDtosWithSource(dtoSomeList, SuperClass.toString());
		    	if(dtoListWithSource != null)
		    	{
		    		for (DtoDefinitionClass dto : dtoListWithSource) {
			    		itemList = new DtoDefinitionClass();
						itemList.Source = Class.toString();
						itemList.Relation = dto.Relation;
						itemList.PropertyType = InfModelQueryUtil.getPropertyURIType(infModel, dto.Relation);
						itemList.TypeCompletness = EnumRelationTypeCompletness.SOME;
						itemList.Target = dto.Target;
						itemList.Cardinality = dto.Cardinality;
						dtoSomeList.add(itemList);
					}
		    	}
		    }
		}

		return dtoSomeList;
	}
	
	public ArrayList<DtoDefinitionClass> GetMinRelationsOfClass(InfModel infModel, String clsuri) {
		System.out.println("\nExecuting GetMinRelationsOfClass()...");
		ArrayList<DtoDefinitionClass> dtoMinList = new ArrayList<DtoDefinitionClass>();		
		
		// Create a new query
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + infModel.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT ?source ?relation ?cardinality ?target" +
		" WHERE {\n" +
			" { " +
				"?source " + "owl:equivalentClass" + " ?blank .\n " +
				"?blank rdf:type owl:Class ."  +
				"?blank owl:intersectionOf  ?list  ." +
				"?list  rdf:rest*/rdf:first  ?member ."  +			
				" ?member " + "owl:minQualifiedCardinality" + " ?cardinality .\n " +
				" ?member " + "owl:onProperty ?relation .\n" +
				" ?member " + "owl:onClass ?target" +		
				
				" FILTER( ?source = <" + clsuri + "> ) " +
			"} UNION {" +		
				"?source " + "owl:equivalentClass" + " ?blank .\n " +
				"?blank rdf:type owl:Class ."  +
				"?blank owl:intersectionOf  ?list     ." +
				"?list  rdf:rest*/rdf:first  ?member ."  +			
				" ?member " + "owl:minQualifiedCardinality" + " ?cardinality .\n " +
				" ?member " + "owl:onProperty ?relation .\n" +
				" ?member " + "owl:onDataRange ?target" +
				
				" FILTER( ?source = <" + clsuri + "> ) " +
			"} UNION {" +	
				" ?source " + "owl:equivalentClass" + " _:b0 .\n " +				
				" _:b0 " + "owl:minQualifiedCardinality" + " ?cardinality .\n " +
				" _:b0 " + "owl:onProperty ?relation .\n" +
				" _:b0 " + "owl:onClass ?target" +
				
				" FILTER( ?source = <" + clsuri + "> ) " +
			" } UNION { " +
				" ?source " + "owl:equivalentClass" + " _:b1 .\n " +				
				" _:b1 " + "owl:minQualifiedCardinality" + " ?cardinality .\n " +
				" _:b1 " + "owl:onProperty ?relation .\n" +
				" _:b1 " + "owl:onDataRange ?target" +
				
				" FILTER( ?source = <" + clsuri + "> ) " +
			"}" +
				
			" UNION { " +
				"?source " + "rdfs:subClassOf" + " ?blank .\n " +
				"?blank rdf:type owl:Class ."  +
				"?blank owl:intersectionOf  ?list  ." +
				"?list  rdf:rest*/rdf:first  ?member ."  +			
				" ?member " + "owl:minQualifiedCardinality" + " ?cardinality .\n " +
				" ?member " + "owl:onProperty ?relation .\n" +
				" ?member " + "owl:onClass ?target" +		
				
				" FILTER( ?source = <" + clsuri + "> ) " +
			"} UNION {" +		
				"?source " + "rdfs:subClassOf" + " ?blank .\n " +
				"?blank rdf:type owl:Class ."  +
				"?blank owl:intersectionOf  ?list     ." +
				"?list  rdf:rest*/rdf:first  ?member ."  +			
				" ?member " + "owl:minQualifiedCardinality" + " ?cardinality .\n " +
				" ?member " + "owl:onProperty ?relation .\n" +
				" ?member " + "owl:onDataRange ?target" +
				
				" FILTER( ?source = <" + clsuri + "> ) " +
			"} UNION {" +	
				" ?source " + "rdfs:subClassOf" + " _:b2 .\n " +				
				" _:b2 " + "owl:minQualifiedCardinality" + " ?cardinality .\n " +
				" _:b2 " + "owl:onProperty ?relation .\n" +
				" _:b2 " + "owl:onClass ?target" +	
				
				" FILTER( ?source = <" + clsuri + "> ) " +
			" } UNION { " +
				" ?source " + "rdfs:subClassOf" + " _:b3 .\n " +				
				" _:b3 " + "owl:minQualifiedCardinality" + " ?cardinality .\n " +
				" _:b3 " + "owl:onProperty ?relation .\n" +
				" _:b3 " + "owl:onDataRange ?target" +
				
				" FILTER( ?source = <" + clsuri + "> ) " +
			"}" +				
			
		"}";

		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		ResultSet results = qe.execSelect();
		
		// Output query results 
		//ResultSetFormatter.out(System.out, results, query);
		
		DtoDefinitionClass itemList = null;
		
		while (results.hasNext()) {
			QuerySolution row= results.next();
		    RDFNode Source = row.get("source");
		    RDFNode Relation = row.get("relation");
		    RDFNode Cardinality = row.get("cardinality");
		    RDFNode Target = row.get("target");
		    
		    //jump the blank nodes - Check blank node and signal '-'
		    String sourceStr = Source.toString();
		    if ( Character.isDigit(sourceStr.charAt(0)) || sourceStr.startsWith("-")) //
		    {
		        continue;
		    }
		    
			itemList = new DtoDefinitionClass();
			itemList.Source = Source.toString();
			itemList.Relation = Relation.toString();
			itemList.PropertyType = InfModelQueryUtil.getPropertyURIType(infModel, Relation.toString());
			itemList.TypeCompletness = EnumRelationTypeCompletness.MIN;
			itemList.Target = Target.toString();
			itemList.Cardinality = Cardinality.toString().split("\\^")[0];
			dtoMinList.add(itemList);
		}
		
		// Create a new query -- SUB CLASS OF -- DEFINED CLASS
		queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + infModel.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT ?x ?y" +
		" WHERE {\n" +
				" ?x " + "rdfs:subClassOf" + " ?y .\n " +
				//" _:b0 " + "owl:Class ?y .\n" +
			"}";

		query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		qe = QueryExecutionFactory.create(query, infModel);
		results = qe.execSelect();
		
		// Output query results 
		//ResultSetFormatter.out(System.out, results, query);
		
		while (results.hasNext()) {
			
			QuerySolution row= results.next();
		    RDFNode Class = row.get("x");
		    RDFNode SuperClass = row.get("y");
		    
		    if(!Class.toString().contains(InfModelQueryUtil.w3URI) && !SuperClass.toString().contains(InfModelQueryUtil.w3URI) && Class.toString() != SuperClass.toString())
		    {		    	
		    	ArrayList<DtoDefinitionClass> dtoListWithSource = DtoDefinitionClass.getDtosWithSource(dtoMinList, SuperClass.toString());
		    	if(dtoListWithSource != null)
		    	{
		    		for (DtoDefinitionClass dto : dtoListWithSource) {
			    		itemList = new DtoDefinitionClass();
						itemList.Source = Class.toString();
						itemList.Relation = dto.Relation;
						itemList.PropertyType = InfModelQueryUtil.getPropertyURIType(infModel, dto.Relation);
						itemList.TypeCompletness = EnumRelationTypeCompletness.MIN;
						itemList.Target = dto.Target;
						itemList.Cardinality = dto.Cardinality;
						dtoMinList.add(itemList);
					}
		    	}
		    }
		}
		
		return dtoMinList;
	}

	public ArrayList<DtoDefinitionClass> GetMaxRelationsOfClass(InfModel infModel, String clsuri) {

		System.out.println("\nExecuting GetMaxRelationsOfClass()...");
		ArrayList<DtoDefinitionClass> dtoMaxList = new ArrayList<DtoDefinitionClass>();
		
		// Create a new query
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + infModel.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT ?source ?relation ?cardinality ?target" +
		" WHERE {\n" +
			"{ " +
				"?source " + "owl:equivalentClass" + " ?blank .\n " +
				"?blank rdf:type owl:Class ."  +
				"?blank owl:intersectionOf  ?list     ." +
				"?list  rdf:rest*/rdf:first  ?member ."  +			
				" ?member " + "owl:maxQualifiedCardinality" + " ?cardinality .\n " +
				" ?member " + "owl:onProperty ?relation .\n" +
				" ?member " + "owl:onClass ?target" +		
				
				" FILTER( ?source = <" + clsuri + "> ) " +
			"} UNION {" +		
				"?source " + "owl:equivalentClass" + " ?blank .\n " +
				"?blank rdf:type owl:Class ."  +
				"?blank owl:intersectionOf  ?list     ." +
				"?list  rdf:rest*/rdf:first  ?member ."  +			
				" ?member " + "owl:maxQualifiedCardinality" + " ?cardinality .\n " +
				" ?member " + "owl:onProperty ?relation .\n" +
				" ?member " + "owl:onDataRange ?target" +
				
				" FILTER( ?source = <" + clsuri + "> ) " +
			"} UNION {" +		
				" ?source " + "owl:equivalentClass" + " _:b0 .\n " +				
				" _:b0 " + "owl:maxQualifiedCardinality" + " ?cardinality .\n " +
				" _:b0 " + "owl:onProperty ?relation .\n" +
				" _:b0 " + "owl:onClass ?target" +	
				
				" FILTER( ?source = <" + clsuri + "> ) " +
			" } UNION { " +
				" ?source " + "owl:equivalentClass" + " _:b1 .\n " +				
				" _:b1 " + "owl:maxQualifiedCardinality" + " ?cardinality .\n " +
				" _:b1 " + "owl:onProperty ?relation .\n" +
				" _:b1 " + "owl:onDataRange ?target" +
				
				" FILTER( ?source = <" + clsuri + "> ) " +
			"}" +
				
			" UNION { " +
				"?source " + "rdfs:subClassOf" + " ?blank .\n " +
				"?blank rdf:type owl:Class ."  +
				"?blank owl:intersectionOf  ?list     ." +
				"?list  rdf:rest*/rdf:first  ?member ."  +			
				" ?member " + "owl:maxQualifiedCardinality" + " ?cardinality .\n " +
				" ?member " + "owl:onProperty ?relation .\n" +
				" ?member " + "owl:onClass ?target" +	
				
				" FILTER( ?source = <" + clsuri + "> ) " +
			"} UNION {" +		
				"?source " + "rdfs:subClassOf" + " ?blank .\n " +
				"?blank rdf:type owl:Class ."  +
				"?blank owl:intersectionOf  ?list     ." +
				"?list  rdf:rest*/rdf:first  ?member ."  +			
				" ?member " + "owl:maxQualifiedCardinality" + " ?cardinality .\n " +
				" ?member " + "owl:onProperty ?relation .\n" +
				" ?member " + "owl:onDataRange ?target" +
				
				" FILTER( ?source = <" + clsuri + "> ) " +
			"} UNION {" +		
				" ?source " + "rdfs:subClassOf" + " _:b2 .\n " +				
				" _:b2 " + "owl:maxQualifiedCardinality" + " ?cardinality .\n " +
				" _:b2 " + "owl:onProperty ?relation .\n" +
				" _:b2 " + "owl:onClass ?target" +	
				
				" FILTER( ?source = <" + clsuri + "> ) " +
			" } UNION { " +
				" ?source " + "rdfs:subClassOf" + " _:b3 .\n " +				
				" _:b3 " + "owl:maxQualifiedCardinality" + " ?cardinality .\n " +
				" _:b3 " + "owl:onProperty ?relation .\n" +
				" _:b3 " + "owl:onDataRange ?target" +
				
				" FILTER( ?source = <" + clsuri + "> ) " +
			"}" +			
		"}";

		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		ResultSet results = qe.execSelect();
		
		// Output query results 
		// ResultSetFormatter.out(System.out, results, query);
		
		DtoDefinitionClass itemList = null;
		
		while (results.hasNext()) {
			
			QuerySolution row= results.next();
		    RDFNode Source = row.get("source");
		    RDFNode Relation = row.get("relation");
		    RDFNode Cardinality = row.get("cardinality");
		    RDFNode Target = row.get("target");
		    
		    String sourceStr = Source.toString();
		    if ( Character.isDigit(sourceStr.charAt(0)) || sourceStr.startsWith("-")) //Check blank node and signal '-'
		    {
		        continue;
		    }
		    
			itemList = new DtoDefinitionClass();
			itemList.Source = Source.toString();
			itemList.Relation = Relation.toString();
			itemList.PropertyType = InfModelQueryUtil.getPropertyURIType(infModel, Relation.toString());
			itemList.TypeCompletness = EnumRelationTypeCompletness.MAX;
			itemList.Target = Target.toString();
			itemList.Cardinality = Cardinality.toString().split("\\^")[0];
			dtoMaxList.add(itemList);
		}
		
		// Create a new query -- SUB CLASS OF -- DEFINED CLASS
		queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + infModel.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT ?x ?y" +
		" WHERE {\n" +
				" ?x " + "rdfs:subClassOf" + " ?y .\n " +
				//" _:b0 " + "owl:Class ?y .\n" +
			"}";

		query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		qe = QueryExecutionFactory.create(query, infModel);
		results = qe.execSelect();
		
		// Output query results 
		//ResultSetFormatter.out(System.out, results, query);
		
		while (results.hasNext()) {
			
			QuerySolution row= results.next();
		    RDFNode Class = row.get("x");
		    RDFNode SuperClass = row.get("y");
		    
		    if(!Class.toString().contains(InfModelQueryUtil.w3URI) && !SuperClass.toString().contains(InfModelQueryUtil.w3URI) && Class.toString() != SuperClass.toString())
		    {		    	
		    	ArrayList<DtoDefinitionClass> dtoListWithSource = DtoDefinitionClass.getDtosWithSource(dtoMaxList, SuperClass.toString());
		    	if(dtoListWithSource != null)
		    	{
		    		for (DtoDefinitionClass dto : dtoListWithSource) {
			    		itemList = new DtoDefinitionClass();
						itemList.Source = Class.toString();
						itemList.Relation = dto.Relation;
						itemList.PropertyType = InfModelQueryUtil.getPropertyURIType(infModel, dto.Relation);
						itemList.TypeCompletness = EnumRelationTypeCompletness.MAX;
						itemList.Target = dto.Target;
						itemList.Cardinality = dto.Cardinality;
						dtoMaxList.add(itemList);
					}
		    	}
		    }
		}
		
		return dtoMaxList;
	}
	
 	public ArrayList<DtoDefinitionClass> GetExactlyRelationsOfClass(InfModel infModel, String clsuri) {

 		System.out.println("\nExecuting GetExactlyRelationsOfClass()...");
		ArrayList<DtoDefinitionClass> dtoExactlyList = new ArrayList<DtoDefinitionClass>();
		
		// Create a new query
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + infModel.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT ?source ?relation ?cardinality ?target" +
		" WHERE {\n" +				
			" { " +
				"?source " + "owl:equivalentClass" + " ?blank .\n " +
				"?blank rdf:type owl:Class ."  +
				"?blank owl:intersectionOf  ?list     ." +
				"?list  rdf:rest*/rdf:first  ?member ."  +			
				" ?member " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				" ?member " + "owl:onProperty ?relation .\n" +
				" ?member " + "owl:onClass ?target" +
				
				" FILTER( ?source = <" + clsuri + "> ) " +
			"} UNION {" +		
				"?source " + "owl:equivalentClass" + " ?blank .\n " +
				"?blank rdf:type owl:Class ."  +
				"?blank owl:intersectionOf  ?list     ." +
				"?list  rdf:rest*/rdf:first  ?member ."  +			
				" ?member " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				" ?member " + "owl:onProperty ?relation .\n" +
				" ?member " + "owl:onDataRange ?target" +
				
				" FILTER( ?source = <" + clsuri + "> ) " +
			"} UNION {" +	
				" ?source " + "owl:equivalentClass" + " _:b0 .\n " +				
				" _:b0 " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				" _:b0 " + "owl:onProperty ?relation .\n" +
				" _:b0 " + "owl:onClass ?target" +	
				
				" FILTER( ?source = <" + clsuri + "> ) " +
			" } UNION { " +
				" ?source " + "owl:equivalentClass" + " _:b1 .\n " +				
				" _:b1 " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				" _:b1 " + "owl:onProperty ?relation .\n" +
				" _:b1 " + "owl:onDataRange ?target" +
				
				" FILTER( ?source = <" + clsuri + "> ) " +
			"}" +
				
			" UNION { " +
				"?source " + "rdfs:subClassOf" + " ?blank .\n " +
				"?blank rdf:type owl:Class ."  +
				"?blank owl:intersectionOf  ?list     ." +
				"?list  rdf:rest*/rdf:first  ?member ."  +			
				" ?member " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				" ?member " + "owl:onProperty ?relation .\n" +
				" ?member " + "owl:onClass ?target" +
				
				" FILTER( ?source = <" + clsuri + "> ) " +				
			"} UNION {" +		
				"?source " + "rdfs:subClassOf" + " ?blank .\n " +
				"?blank rdf:type owl:Class ."  +
				"?blank owl:intersectionOf  ?list     ." +
				"?list  rdf:rest*/rdf:first  ?member ."  +			
				" ?member " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				" ?member " + "owl:onProperty ?relation .\n" +
				" ?member " + "owl:onDataRange ?target" +
				
				" FILTER( ?source = <" + clsuri + "> ) " +
			"} UNION {" +	
				" ?source " + "rdfs:subClassOf" + " _:b2 .\n " +				
				" _:b2 " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				" _:b2 " + "owl:onProperty ?relation .\n" +
				" _:b2 " + "owl:onClass ?target" +
				
				" FILTER( ?source = <" + clsuri + "> ) " +
			" } UNION { " +
				" ?source " + "rdfs:subClassOf" + " _:b3 .\n " +				
				" _:b3 " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				" _:b3 " + "owl:onProperty ?relation .\n" +
				" _:b3 " + "owl:onDataRange ?target" +
				
				" FILTER( ?source = <" + clsuri + "> ) " +
			"}" +
		"}";

		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		ResultSet results = qe.execSelect();
		
		// Output query results 
		//ResultSetFormatter.out(System.out, results, query);
		
		DtoDefinitionClass itemList = null;
		
		while (results.hasNext()) {
			
			QuerySolution row= results.next();
		    RDFNode Source = row.get("source");
		    RDFNode Relation = row.get("relation");
		    RDFNode Cardinality = row.get("cardinality");
		    RDFNode Target = row.get("target");
		    
		    //jump the blank nodes - Check blank node and signal '-'
		    String sourceStr = Source.toString();
		    if ( Character.isDigit(sourceStr.charAt(0)) || sourceStr.startsWith("-")) //
		    {
		        continue;
		    }
		    
			itemList = new DtoDefinitionClass();
			itemList.Source = Source.toString();
			itemList.Relation = Relation.toString();
			itemList.PropertyType = InfModelQueryUtil.getPropertyURIType(infModel, Relation.toString());
			itemList.TypeCompletness = EnumRelationTypeCompletness.EXACTLY;
			itemList.Target = Target.toString();
			itemList.Cardinality = Cardinality.toString().split("\\^")[0];
			dtoExactlyList.add(itemList);
		}
		
		// Create a new query -- SUB CLASS OF -- DEFINED CLASS
		
		queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + infModel.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT ?x ?y" +
		" WHERE {\n" +
				" ?x " + "rdfs:subClassOf" + " ?y .\n " +
				//" _:b0 " + "owl:Class ?y .\n" +
			"}";

		query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		qe = QueryExecutionFactory.create(query, infModel);
		results = qe.execSelect();
		
		// Output query results 
		//ResultSetFormatter.out(System.out, results, query);
		
		while (results.hasNext()) {
			
			QuerySolution row= results.next();
		    RDFNode Class = row.get("x");
		    RDFNode SuperClass = row.get("y");
		    
		    if(!Class.toString().contains(InfModelQueryUtil.w3URI) && !SuperClass.toString().contains(InfModelQueryUtil.w3URI) && Class.toString() != SuperClass.toString())
		    {		    	
		    	ArrayList<DtoDefinitionClass> dtoListWithSource = DtoDefinitionClass.getDtosWithSource(dtoExactlyList, SuperClass.toString());
		    	if(dtoListWithSource != null)
		    	{
		    		for (DtoDefinitionClass dto : dtoListWithSource) {
			    		itemList = new DtoDefinitionClass();
						itemList.Source = Class.toString();
						itemList.Relation = dto.Relation;
						itemList.PropertyType = InfModelQueryUtil.getPropertyURIType(infModel, dto.Relation);
						itemList.TypeCompletness = EnumRelationTypeCompletness.EXACTLY;
						itemList.Target = dto.Target;
						itemList.Cardinality = dto.Cardinality;
						dtoExactlyList.add(itemList);
					}
		    	}
		    }
		}
		
		return dtoExactlyList;
	}

 	public ArrayList<DtoDefinitionClass> GetModelDefinitionsInInstances(ArrayList<Instance> listAllInstances,	InfModel InfModel) {

		ArrayList<DtoDefinitionClass> resultListDefinitions = new ArrayList<DtoDefinitionClass>();
		
		for (Instance instance : listAllInstances) 
		{
			for (String cls : instance.ListClasses) 
			{
				//DtoDefinitionClass aux = DtoDefinitionClass.getDtoWithSourceAndRelationAndTarget(resultListDefinitions, cls);
				
				DtoDefinitionClass aux = null;
				if(aux == null && ! cls.contains("Thing"))	//doesn't exist yet
				{
					ArrayList<DtoDefinitionClass> dtoSomeRelationsList = this.GetSomeRelationsOfClass(InfModel,cls);
					ArrayList<DtoDefinitionClass> dtoMinRelationsList = this.GetMinRelationsOfClass(InfModel,cls);
					ArrayList<DtoDefinitionClass> dtoMaxRelationsList = this.GetMaxRelationsOfClass(InfModel,cls);
					ArrayList<DtoDefinitionClass> dtoExactlyRelationsList = this.GetExactlyRelationsOfClass(InfModel,cls);	
					
					resultListDefinitions.addAll(dtoSomeRelationsList);
					resultListDefinitions.addAll(dtoMinRelationsList);
					resultListDefinitions.addAll(dtoMaxRelationsList);
					resultListDefinitions.addAll(dtoExactlyRelationsList);
				}			
				
			}		
			
		}		
		
		return resultListDefinitions;
	}
	
 	public ArrayList<DtoDefinitionClass> GetModelDefinitionsInInstances(String instanceURI, OntModel model, InfModel InfModel, ArrayList<Instance> listAllInstances, ManagerInstances manager) {

		Instance Instance = manager.getInstance(listAllInstances, instanceURI); // GET INTANCE on MODEL
		List<String> listInstancesDto = InfModelQueryUtil.getIndividualsURIFromAllClasses(InfModel);		
		for (String dto : listInstancesDto) {
			
			if(dto.equals(instanceURI))
			{				
				String nameSpace = dto.split("#")[0] + "#";
				String name = dto.split("#")[1];
				
				if (Instance == null)
				{					
					Instance = new Instance(nameSpace, name, InfModelQueryUtil.getClassesURI(InfModel, instanceURI), InfModelQueryUtil.getIndividualsURIDifferentFrom(InfModel, dto), InfModelQueryUtil.getIndividualsURISameAs(InfModel, dto),true);
					
				} else {
					
					//Update classes
					Instance.ListClasses = InfModelQueryUtil.getClassesURI(InfModel, instanceURI);
				}
			}
		}
	
		ArrayList<DtoDefinitionClass> resultListDefinitions = new ArrayList<DtoDefinitionClass>();

		for (String cls : Instance.ListClasses) 
		{
			DtoDefinitionClass aux = DtoDefinitionClass.getDtoWithSourceAndRelationAndTarget(resultListDefinitions, cls);
			if(aux == null && ! cls.contains("Thing"))	//don't exist yet
			{
				ArrayList<DtoDefinitionClass> dtoSomeRelationsList = this.GetSomeRelationsOfClass(InfModel,cls);
				ArrayList<DtoDefinitionClass> dtoMinRelationsList = this.GetMinRelationsOfClass(InfModel,cls);
				ArrayList<DtoDefinitionClass> dtoMaxRelationsList = this.GetMaxRelationsOfClass(InfModel,cls);
				ArrayList<DtoDefinitionClass> dtoExactlyRelationsList = this.GetExactlyRelationsOfClass(InfModel,cls);	
				
				resultListDefinitions.addAll(dtoSomeRelationsList);
				resultListDefinitions.addAll(dtoMinRelationsList);
				resultListDefinitions.addAll(dtoMaxRelationsList);
				resultListDefinitions.addAll(dtoExactlyRelationsList);
			}			
			
		}	
		
		//Add to list of intances
		//listAllInstances.add(Instance);
		
		//return
		return resultListDefinitions;
	}
 	
	/*
	 * Specializations search
	 */
	
	public List<DtoCompleteClass> GetCompleteSubClasses(String className, List<String> listClassesOfInstance, InfModel infModel)
	{
		System.out.println("\nExecuting GetCompleteSubClasses()...");
		List<DtoCompleteClass> ListCompleteClsAndSubCls = new ArrayList<DtoCompleteClass>();
		
		// Create a new query
		String queryString = 
				"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
				"PREFIX ns: <" + infModel.getNsPrefixURI("") + ">" +
				" SELECT DISTINCT ?x0 ?completeClass ?member" +
				" WHERE {\n" +
					"{ " +
				
						"?completeClass owl:equivalentClass ?cls ." +
						"?cls owl:intersectionOf ?nodeFather." +
		
						//one level
						"?nodeFather ?r1 ?x0 ."+
						"?x0 owl:unionOf  ?list ."+

						"?list  rdf:rest*/rdf:first  ?member ." +
						" FILTER( ?completeClass = <" + className + "> ) " +
						
					"} UNION {" +
					
						"?completeClass owl:equivalentClass ?cls ." +
						"?cls owl:intersectionOf ?nodeFather." +
		
						//two levels
						"?nodeFather ?r2 ?x1 ." +
						"?x1 ?r1 ?x0 ." +
						"?x0 owl:unionOf  ?list ." +
		
						"?list  rdf:rest*/rdf:first  ?member ." +
						" FILTER( ?completeClass = <" + className + "> ) " +
						
					"} UNION {" +
										
						"?completeClass owl:equivalentClass ?x0 ." +
						
						//zero levels
						"?x0 owl:unionOf  ?list ." +
						
						"?list  rdf:rest*/rdf:first  ?member ." +
						" FILTER( ?completeClass = <" + className + "> ) " +			
						
					"}" + 
				"}";

		/* The result is order by completClass */
		
		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		ResultSet results = qe.execSelect();
		
		// Output query results 
		// ResultSetFormatter.out(System.out, results, query);
		
		RDFNode blankNodeAux = null;	//Save last blank node
		DtoCompleteClass dto = null;
		while (results.hasNext()) {

			QuerySolution row= results.next();
			
			RDFNode blankNode = row.get("x0");
			RDFNode completeClass = row.get("completeClass");
		    RDFNode member = row.get("member");
		    
		    if(blankNodeAux == null)
		    {
		    	//first case blank node
		    	blankNodeAux = blankNode;    
		    	
		    	//Add here
		    	dto = new DtoCompleteClass();
		    	dto.CompleteClass = completeClass.toString();

		    	//check if member are disjoint of listClassesOfInstance
		    	
    			boolean ok = true;
    			List<String> listDisjointClassesOfMember = InfModelQueryUtil.getClassesURIDisjointWith(infModel,member.toString());
    			for (String disjointCls : listDisjointClassesOfMember) {
    				if(listClassesOfInstance.contains(disjointCls))
    				{
    					//not possible specialize
    					ok = false;
    					break;
    				}
				}
    			
    			//check if member are in listClassesOfInstace
    			if(listClassesOfInstance.contains(member.toString()))
    			{
    				// not necessary specialize
    				ok = false;
    			}
    			
    			if(ok == true)
    			{
    				dto.AddMember(member.toString());	
    			}	
		    	
		    } else {
		    	
		    	if(blankNode.equals(blankNodeAux))
		    	{
		    		//we are in the same blank node, same generalization set
		    		
		    		//add with not exist
		    		if(! dto.Members.contains(member.toString()))
		    		{
		    			//check if member are disjoint of listClassesOfInstance
		    			boolean ok = true;
		    			List<String> listDisjointClassesOfMember = InfModelQueryUtil.getClassesURIDisjointWith(infModel,member.toString());
		    			for (String disjointCls : listDisjointClassesOfMember) {
		    				if(listClassesOfInstance.contains(disjointCls))
		    				{
		    					//not possible specialize
		    					ok = false;
		    					break;
		    				}
						}
		    			
		    			//check if member are in listClassesOfInstace
		    			if(listClassesOfInstance.contains(member.toString()))
		    			{
		    				// not necessary specialize
		    				ok = false;
		    			}
		    			
		    			if(ok == true)
		    			{
		    				dto.AddMember(member.toString());	//ADD MEMBER
		    			}	
		    		}
		    		
		    	} else {
		    		
		    		//change generalization
		    		
		    		if(dto.Members.size() > 0)
		    			ListCompleteClsAndSubCls.add(dto);
		    	
		    		//new node
		    		//get only the not disjoint possibilities
		    		
		    		dto = new DtoCompleteClass();
		    		dto.CompleteClass = completeClass.toString();
		    		
		    		//check if member are disjoint of listClassesOfInstance
	    			boolean ok = true;
	    			List<String> listDisjointClassesOfMember = InfModelQueryUtil.getClassesURIDisjointWith(infModel,member.toString());
	    			for (String disjointCls : listDisjointClassesOfMember) {
	    				if(listClassesOfInstance.contains(disjointCls))
	    				{
	    					//not possible specialize
	    					ok = false;
	    					break;
	    				}
					}
	    			
	    			//check if member are in listClassesOfInstace
	    			if(listClassesOfInstance.contains(member.toString()))
	    			{
	    				// not necessary specialize
	    				ok = false;
	    			}
	    			
	    			if(ok == true)
	    			{
	    				dto.AddMember(member.toString());	
	    			}
			    	
			    	blankNodeAux = blankNode;
		    	} 	
		    }
		}
		
		//the last case
		if(! ListCompleteClsAndSubCls.contains(dto) && dto != null)
		{
			if(dto.Members.size() > 0)
				ListCompleteClsAndSubCls.add(dto);
		}
		
		return ListCompleteClsAndSubCls;
	}
	
}
