package Business;
import java.util.ArrayList;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import Domain.DtoCompleteClass;
import Domain.DtoDefinitionClass;
import Domain.DtoDefinitionClassList;
import Domain.DtoInstanceRelation;
import SwingUserInterface.Aplication;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class Search {

	public String NS;
	
	public Search(String NameSpace)
	{
		NS = NameSpace;
	}

	/*
	 * General search
	 */
	
	public ArrayList<String> GetProperties(OntModel model) {
		
		ArrayList<String> lista = new ArrayList<String>();
		ExtendedIterator i = model.listOntProperties();
		if( !i.hasNext() ) {
			//System.out.print( "none" );
		}
		else {
			while( i.hasNext() ) {
				Resource val = (Resource) i.next();
				lista.add( val.getURI() );
			}
		}
		return lista;
	}
	
	public ArrayList<String> GetClasses(OntModel model) {
		
		ArrayList<String> lista = new ArrayList<String>();
		ExtendedIterator i = model.listClasses();
		if( !i.hasNext() ) {
			//System.out.print( "none" );
		}
		else {
			while( i.hasNext() ) {
				Resource val = (Resource) i.next();
				lista.add( val.getURI() );
			}
		}
		return lista;
	}
	
	public ArrayList<String> GetInstancesFromClass(OntModel model,String className) {
		
		ArrayList<String> lista = new ArrayList<String>();
		OntClass Class = model.getOntClass(className);
		ExtendedIterator i = Class.listInstances();
		//System.out.println("hi");
		if( !i.hasNext() ) {
			//System.out.print( "none" );
		}
		else {
			while( i.hasNext() ) {
				Resource val = (Resource) i.next();
				lista.add( val.getURI() );
			}
		}
		return lista;
	}
	
	public ArrayList<String> GetAllInstances(OntModel model)
	{
		ArrayList<String> AllInstances = new ArrayList<String>();
		ArrayList<String> AllClasses = this.GetClasses(model);
		System.out.println("-> " + AllClasses.size());
		for (String className : AllClasses) {
			
			if(!(className == null)){
				ArrayList<String> InstancesClass = this.GetInstancesFromClass(model, className);
				System.out.println("->" + className);
				AllInstances.addAll(InstancesClass);
			}
		}
		
		return AllInstances;
	}

	
	/*
	 * Class and instance search
	 */
	
	public ArrayList<String> GetDifferentInstancesFrom(OntModel model, String instanceName)
	{		
		ArrayList<String> list = new ArrayList<String>();
		
		// create Pellet reasoner
		Reasoner r = PelletReasonerFactory.theInstance().create();		
		// create an inferencing model using the raw model
		InfModel infModel = ModelFactory.createInfModel(r, model);
		
		// Create a new query
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <http://www.semanticweb.org/parentesco-simples.owl#>" +
		" SELECT *" +
		" WHERE {\n" +		
			"<" + instanceName + "> owl:differentFrom" + " ?y .\n " +	
		"}";

		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		ResultSet results = qe.execSelect();
		
		// Output query results 
		//ResultSetFormatter.out(System.out, results, query);
		
		while (results.hasNext()) {
			QuerySolution row = results.next();
		    
		    RDFNode rdfY = row.get("y");
		    if(! instanceName.equals(rdfY.toString()))
		    {
		    	list.add(rdfY.toString());
		    }
		}		

		return list;
	}
	
	public ArrayList<String> GetSameInstancesFrom(OntModel model, String instanceName)
	{
		ArrayList<String> list = new ArrayList<String>();
		
		// create Pellet reasoner
		Reasoner r = PelletReasonerFactory.theInstance().create();		
		// create an inferencing model using the raw model
		InfModel infModel = ModelFactory.createInfModel(r, model);
		
		// Create a new query
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <http://www.semanticweb.org/parentesco-simples.owl#>" +
		" SELECT *" +
		" WHERE {\n" +		
			"<" + instanceName + "> owl:sameAs" + " ?y .\n " +	
		"}";
		System.out.println(queryString);
		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		ResultSet results = qe.execSelect();
		
		// Output query results 
		//ResultSetFormatter.out(System.out, results, query);
		
		while (results.hasNext()) {
			QuerySolution row = results.next();
		    
		    RDFNode rdfY = row.get("y");
		    if(! instanceName.equals(rdfY.toString()))
		    {
		    	list.add(rdfY.toString());
		    }
		}		

		return list;
	}
	
	public ArrayList<String> GetPropertiesFromClass(OntModel model,String className) {
		
		ArrayList<String> lista = new ArrayList<String>();
		
		// create Pellet reasoner
		Reasoner r = PelletReasonerFactory.theInstance().create();		
		// create an inferencing model using the raw model
		InfModel infModel = ModelFactory.createInfModel(r, model);
		
		System.out.println("\n");

		ExtendedIterator i = model.listClasses();
		if( !i.hasNext() ) {
			//System.out.print( "none" );
		}
		else {
			while( i.hasNext() ) {
				Resource val = (Resource) i.next();
				System.out.println( val.getURI());
			}
		}
		
		// Create a new query
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <http://www.semanticweb.org/parentesco-simples.owl#>" +
		" SELECT ?x ?y ?z" +
		" WHERE {\n" +
				" ?x " + "owl:equivalentClass" + " _:b0 .\n " +
				" _:b0 " + "owl:onProperty" + " ?y .\n " +
				" _:b0 " + "owl:someValuesFrom ?z . " +
				
				//" _:b rdf:type ?tipoX ." +
				//" ?tipoX rdfs:subClassOf owl:Thing . " +
				//"?r owl:onProperty ?p . " +
				
			"}";

		System.out.println(queryString + "\n");
		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		
		// Output query results 
		ResultSetFormatter.out(System.out, results, query);
		
		//while (results.hasNext()) {
		//	QuerySolution row= results.next();
		    //RDFNode rdfConcept = row.get("rec");
		    
		//}		
		return lista;
	}

	public boolean CheckExistInstanceTarget(OntModel model, String instance,
			String relation, String imageClass) {

		Boolean result = false;
		
		// create Pellet reasoner
		Reasoner r = PelletReasonerFactory.theInstance().create();		
		// create an inferencing model using the raw model
		InfModel infModel = ModelFactory.createInfModel(r, model);	

		// Create a new query
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <"+ NS + ">" +
		"\n SELECT ?x" +
		" WHERE {\n" +
				" <" + instance + "> <" + relation + "> ?x .\n " +
				" ?x" + " rdf:type" + " <"+ imageClass + "> .\n " +
			"}";

		System.out.println(queryString + "\n");
		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		ResultSet results = qe.execSelect();
		
		// Output query results 
		//ResultSetFormatter.out(System.out, results, query);
		
		while (results.hasNext()) {
			QuerySolution row = results.next();
		    RDFNode rdfInstance = row.get("x");
		    
		    //CHECAR O TIPO DO INDIVIDUAL OBTIDO		    
		    result = true;
		}		

		return result;
	}
	
	public int CheckExistInstancesTargetCardinality(OntModel model,
			String instance, String relation, String imageClass, String cardinality) {
		
		int result = 0;
		
		// create Pellet reasoner
		Reasoner r = PelletReasonerFactory.theInstance().create();		
		// create an inferencing model using the raw model
		InfModel infModel = ModelFactory.createInfModel(r, model);	

		// Create a new query
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <"+ NS + ">" +
		"\n SELECT ?x" +
		" WHERE {\n" +
				" <" + instance + "> <" + relation + "> ?x .\n " +
				" ?x" + " rdf:type" + " <"+ imageClass + "> .\n " +
			"}";

		System.out.println(queryString + "\n");
		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		ResultSet results = qe.execSelect();
		
		// Output query results 
		//ResultSetFormatter.out(System.out, results, query);
		
		while (results.hasNext()) {
			QuerySolution row = results.next();
		    RDFNode rdfInstance = row.get("x");
		    result++;
		}		

		return result;
	}

	public ArrayList<String> GetInstancesOfTargetWithRelation(OntModel model,
			String instance, String relation, String imageClass) {
		
		ArrayList<String> list = new ArrayList<String>();
		
		// create Pellet reasoner
		Reasoner r = PelletReasonerFactory.theInstance().create();		
		// create an inferencing model using the raw model
		InfModel infModel = ModelFactory.createInfModel(r, model);	

		// Create a new query
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <"+ NS + ">" +
		"\n SELECT ?x" +
		" WHERE {\n" +
				" <" + instance + "> <" + relation + "> ?x .\n " +
				" ?x" + " rdf:type" + " <"+ imageClass + "> .\n " +
			"}";

		System.out.println(queryString + "\n");
		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		ResultSet results = qe.execSelect();
		
		// Output query results 
		//ResultSetFormatter.out(System.out, results, query);
		
		while (results.hasNext()) {
			QuerySolution row = results.next();
		    RDFNode rdfInstance = row.get("x");
		    list.add(rdfInstance.toString());	    
		}		

		return list;
	}
	
	public ArrayList<DtoInstanceRelation> GetInstanceRelations(OntModel model, String individualUri)
	{
		ArrayList<DtoInstanceRelation> listIndividualRelations = new ArrayList<DtoInstanceRelation>();
		
		// create Pellet reasoner
		Reasoner r = PelletReasonerFactory.theInstance().create();		
		// create an inferencing model using the raw model
		InfModel infModel = ModelFactory.createInfModel(r, model);
		
		// Create a new query
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <http://www.semanticweb.org/parentesco-simples.owl#>" +
		" SELECT *" +
		" WHERE {\n" +		
				"<" + individualUri + ">" + " ?property" + " ?target .\n " +
				" ?property " + " rdfs:subPropertyOf" + " owl:topObjectProperty .\n " +
				" ?target " + " rdf:type" + " ?targetClass .\n " +
		"}";

		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		ResultSet results = qe.execSelect();
		
		// Output query results 
		//ResultSetFormatter.out(System.out, results, query);
		DtoInstanceRelation dtoItem = null;
		
		while (results.hasNext()) {
			QuerySolution row= results.next();
		    RDFNode property = row.get("property");
		    RDFNode target = row.get("target");
		    RDFNode targetClass = row.get("targetClass");
		    
		    if(targetClass.toString().contains(NS))
		    {
		    	dtoItem = new DtoInstanceRelation();
			    dtoItem.Property = property.toString();
			    dtoItem.Target = target.toString();
			    dtoItem.TargetClass = targetClass.toString();
			    
			    System.out.println(dtoItem.Property);
			    System.out.println(dtoItem.Target);
			    System.out.println(dtoItem.TargetClass + "\n");
			    
			    listIndividualRelations.add(dtoItem);	
		    }  
		    		    		    
		}
		
		return listIndividualRelations;
	}
	
	public void QueryExample(OntModel model) {

		// create Pellet reasoner
		Reasoner r = PelletReasonerFactory.theInstance().create();		
		// create an inferencing model using the raw model
		InfModel infModel = ModelFactory.createInfModel(r, model);
		
		// Create a new query
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <http://www.semanticweb.org/parentesco-simples.owl#>" +
		" SELECT *" +
		" WHERE {\n" +		
			" ?x " + "?r" + " ?y .\n " +
			/*" { " +
			"?source " + "owl:equivalentClass" + " ?blank .\n " +
			"?blank rdf:type owl:Class ."  +
			"?blank owl:intersectionOf  ?list     ." +

			" ?member " + "owl:maxQualifiedCardinality" + " ?cardinality .\n " +
			" ?member " + "owl:onProperty ?relation .\n" +
			" ?member " + "owl:onClass ?target" +
			
			"} UNION {" +
			
			" ?source " + "owl:equivalentClass" + " _:b0 .\n " +				
			" _:b0 " + "owl:minQualifiedCardinality" + " ?cardinality .\n " +
			" _:b0 " + "owl:onProperty ?relation .\n" +
			" _:b0 " + "owl:onClass ?target" +		
			" } " +*/
			
		"}";

		System.out.println(queryString);
		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		ResultSet results = qe.execSelect();
		
		// Output query results 
		ResultSetFormatter.out(System.out, results, query);
	}

	
	/*
	 * Relations search
	 */
	
	public ArrayList<DtoDefinitionClass> GetSomeRelations(OntModel model) {
		
		ArrayList<DtoDefinitionClass> dtoSomeList = new ArrayList<DtoDefinitionClass>();
		
		// create Pellet reasoner
		Reasoner r = PelletReasonerFactory.theInstance().create();		
		// create an inferencing model using the raw model
		InfModel infModel = ModelFactory.createInfModel(r, model);
		
		// Create a new query -- EQUIVALENT CLASS
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <http://www.semanticweb.org/parentesco-simples.owl#>" +
		" SELECT ?x ?y ?z" +
		" WHERE {\n" +
		
				" ?x " + "owl:equivalentClass" + " _:b0 .\n " +
				" _:b0 " + "owl:onProperty" + " ?y .\n " +
				" _:b0 " + "owl:someValuesFrom ?z . " +				
			"}";

		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
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
		    if(! Target.toString().contains("#")){
		    	continue;
		    }
		    
			itemList = new DtoDefinitionClass();
			itemList.Source = Source.toString();
			itemList.Relation = Relation.toString();
			itemList.Target = Target.toString();
			dtoSomeList.add(itemList);
		}
				
		// Create a new query -- SUB CLASS OF DE CLASSE DEFINIDA
		
		queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <http://www.semanticweb.org/parentesco-simples.owl#>" +
		" SELECT ?x ?y" +
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
		    
		    if(Class.toString().contains(NS) && SuperClass.toString().contains(NS) && Class.toString() != SuperClass.toString())
		    {		    	
		    	//System.out.println(Class.toString().split("#")[1] + " - " + SuperClass.toString().split("#")[1]);
		    	ArrayList<DtoDefinitionClass> dtoListWithSource = DtoDefinitionClass.getDtosWithSource(dtoSomeList, SuperClass.toString());
		    	if(dtoListWithSource != null)
		    	{
		    		for (DtoDefinitionClass dto : dtoListWithSource) {
			    		itemList = new DtoDefinitionClass();
						itemList.Source = Class.toString();
						itemList.Relation = dto.Relation;
						itemList.Target = dto.Target;
						itemList.Cardinality = dto.Cardinality;
						dtoSomeList.add(itemList);
					}
		    	}
		    }
		}

		return dtoSomeList;
	}

	public ArrayList<DtoDefinitionClassList> GetSomeRelationsWithUnionOf(OntModel model)
	{
		ArrayList<DtoDefinitionClassList> dtoSomeList = new ArrayList<DtoDefinitionClassList>();
		
		// create Pellet reasoner
		Reasoner r = PelletReasonerFactory.theInstance().create();		
		// create an inferencing model using the raw model
		InfModel infModel = ModelFactory.createInfModel(r, model);
		
		// Create a new query -- EQUIVALENT CLASS
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <http://www.semanticweb.org/parentesco-simples.owl#>" +
		" SELECT *" +
		" WHERE {\n" +
				" ?x " + "owl:equivalentClass" + " _:b0 .\n " +
				" _:b0 " + "owl:onProperty" + " ?y .\n " +
				" _:b0 " + "owl:someValuesFrom _:b1 . " +					
				"_:b1 owl:unionOf  ?list     ." +
				"?list  rdf:rest*/rdf:first  ?member ."  +		
			"}";

		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		ResultSet results = qe.execSelect();
		
		// Output query results 
		// ResultSetFormatter.out(System.out, results, query);
		
		DtoDefinitionClassList itemList = null;
		
		while (results.hasNext()) {
			QuerySolution row= results.next();
		    RDFNode Source = row.get("x");
		    RDFNode Relation = row.get("y");
		    RDFNode Target = row.get("member");			
			
		    itemList = DtoDefinitionClassList.GetDtoDefinitionListClass(dtoSomeList, Source.toString());
		    if(itemList == null)
		    {
		    	//New Class
		    	itemList = new DtoDefinitionClassList();
		    	itemList.Source = Source.toString();
		    	itemList.Relation = Relation.toString();
		    	itemList.AddMember(Target.toString());	
		    	dtoSomeList.add(itemList);
		    } else {
		    	itemList.AddMember(Target.toString());
		    }	
		}
		
		// Create a new query -- SUB CLASS OF -- ANONYNOUS CLASS
		queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <http://www.semanticweb.org/parentesco-simples.owl#>" +
		" SELECT *" +
		" WHERE {\n" +
				" ?x " + "rdfs:subClassOf" + " _:b0 .\n " +
				" _:b0 " + "owl:onProperty" + " ?y .\n " +
				" _:b0 " + "owl:someValuesFrom _:b1 . " +					
				"_:b1 owl:unionOf  ?list     ." +
				"?list  rdf:rest*/rdf:first  ?member ."  +		
			"}";

		query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		qe = QueryExecutionFactory.create(query, infModel);
		results = qe.execSelect();
		
		// Output query results 
		// ResultSetFormatter.out(System.out, results, query);
		
		itemList = null;
		
		while (results.hasNext()) {
			
			QuerySolution row= results.next();
		    RDFNode Source = row.get("x");
		    RDFNode Relation = row.get("y");
		    RDFNode Target = row.get("member");	
		    
		    // jump the blank node if he exist
		    if(! Source.toString().contains("#")){
		    	continue;
		    }
		    
		    itemList = DtoDefinitionClassList.GetDtoDefinitionListClass(dtoSomeList, Source.toString());
		    if(itemList == null)
		    {
		    	//New Class
		    	itemList = new DtoDefinitionClassList();
		    	itemList.Source = Source.toString();
		    	itemList.Relation = Relation.toString();
		    	itemList.AddMember(Target.toString());	
		    	dtoSomeList.add(itemList);
		    } else {
		    	itemList.AddMember(Target.toString());
		    }	
		}

		// Create a new query -- SUB CLASS OF -- DEFINED CLASS
		
		// FAZER ESSE
		
		for (DtoDefinitionClassList dto : dtoSomeList) {
			System.out.println(dto.Source + " -> " + dto.Relation + " ->");
			for (String string : dto.ListTarget) {
				System.out.println(" #" + string);
			}
			System.out.println(" ");
		}

		return dtoSomeList;
	}
	
	public ArrayList<DtoDefinitionClass> GetMinRelations(OntModel model) {

		ArrayList<DtoDefinitionClass> dtoMinList = new ArrayList<DtoDefinitionClass>();
		
		// create Pellet reasoner
		Reasoner r = PelletReasonerFactory.theInstance().create();		
		// create an inferencing model using the raw model
		InfModel infModel = ModelFactory.createInfModel(r, model);
		
		// Create a new query
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <http://www.semanticweb.org/parentesco-simples.owl#>" +
		" SELECT ?source ?relation ?cardinality ?target" +
		" WHERE {\n" +
			" { " +
				"?source " + "owl:equivalentClass" + " ?blank .\n " +
				"?blank rdf:type owl:Class ."  +
				"?blank owl:intersectionOf  ?list     ." +
				"?list  rdf:rest*/rdf:first  ?member ."  +			
				" ?member " + "owl:minQualifiedCardinality" + " ?cardinality .\n " +
				" ?member " + "owl:onProperty ?relation .\n" +
				" ?member " + "owl:onClass ?target" +			
			"} UNION {" +		
				" ?source " + "owl:equivalentClass" + " _:b0 .\n " +				
				" _:b0 " + "owl:minQualifiedCardinality" + " ?cardinality .\n " +
				" _:b0 " + "owl:onProperty ?relation .\n" +
				" _:b0 " + "owl:onClass ?target" +	
			" } " +
		"}";

		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
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
		    
			itemList = new DtoDefinitionClass();
			itemList.Source = Source.toString();
			itemList.Relation = Relation.toString();
			itemList.Target = Target.toString();
			itemList.Cardinality = Cardinality.toString().split("\\^")[0];
			dtoMinList.add(itemList);
		}
		
		// Create a new query -- SUB CLASS OF -- DEFINED CLASS
		queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <http://www.semanticweb.org/parentesco-simples.owl#>" +
		" SELECT ?x ?y" +
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
		    
		    if(Class.toString().contains(NS) && SuperClass.toString().contains(NS) && Class.toString() != SuperClass.toString())
		    {		    	
		    	//System.out.println(Class.toString().split("#")[1] + " - " + SuperClass.toString().split("#")[1]);
		    	ArrayList<DtoDefinitionClass> dtoListWithSource = DtoDefinitionClass.getDtosWithSource(dtoMinList, SuperClass.toString());
		    	if(dtoListWithSource != null)
		    	{
		    		for (DtoDefinitionClass dto : dtoListWithSource) {
			    		itemList = new DtoDefinitionClass();
						itemList.Source = Class.toString();
						itemList.Relation = dto.Relation;
						itemList.Target = dto.Target;
						itemList.Cardinality = dto.Cardinality;
						dtoMinList.add(itemList);
					}
		    	}
		    }
		}
		
		return dtoMinList;
	}

	public ArrayList<DtoDefinitionClass> GetMaxRelations(OntModel model) {

		ArrayList<DtoDefinitionClass> dtoMaxList = new ArrayList<DtoDefinitionClass>();
		
		// create Pellet reasoner
		Reasoner r = PelletReasonerFactory.theInstance().create();		
		// create an inferencing model using the raw model
		InfModel infModel = ModelFactory.createInfModel(r, model);
		
		// Create a new query
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <http://www.semanticweb.org/parentesco-simples.owl#>" +
		" SELECT ?source ?relation ?cardinality ?target" +
		" WHERE {\n" +
		" { " +
				"?source " + "owl:equivalentClass" + " ?blank .\n " +
				"?blank rdf:type owl:Class ."  +
				"?blank owl:intersectionOf  ?list     ." +
				"?list  rdf:rest*/rdf:first  ?member ."  +			
				" ?member " + "owl:maxQualifiedCardinality" + " ?cardinality .\n " +
				" ?member " + "owl:onProperty ?relation .\n" +
				" ?member " + "owl:onClass ?target" +			
			"} UNION {" +		
				" ?source " + "owl:equivalentClass" + " _:b0 .\n " +				
				" _:b0 " + "owl:maxQualifiedCardinality" + " ?cardinality .\n " +
				" _:b0 " + "owl:onProperty ?relation .\n" +
				" _:b0 " + "owl:onClass ?target" +	
			" } " +			
		"}";

		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
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
		    
			itemList = new DtoDefinitionClass();
			itemList.Source = Source.toString();
			itemList.Relation = Relation.toString();
			itemList.Target = Target.toString();
			itemList.Cardinality = Cardinality.toString().split("\\^")[0];
			dtoMaxList.add(itemList);
		}
		
		// Create a new query -- SUB CLASS OF -- DEFINED CLASS
		queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <http://www.semanticweb.org/parentesco-simples.owl#>" +
		" SELECT ?x ?y" +
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
		    
		    if(Class.toString().contains(NS) && SuperClass.toString().contains(NS) && Class.toString() != SuperClass.toString())
		    {		    	
		    	//System.out.println(Class.toString().split("#")[1] + " - " + SuperClass.toString().split("#")[1]);
		    	ArrayList<DtoDefinitionClass> dtoListWithSource = DtoDefinitionClass.getDtosWithSource(dtoMaxList, SuperClass.toString());
		    	if(dtoListWithSource != null)
		    	{
		    		for (DtoDefinitionClass dto : dtoListWithSource) {
			    		itemList = new DtoDefinitionClass();
						itemList.Source = Class.toString();
						itemList.Relation = dto.Relation;
						itemList.Target = dto.Target;
						itemList.Cardinality = dto.Cardinality;
						dtoMaxList.add(itemList);
					}
		    	}
		    }
		}
		
		return dtoMaxList;
	}
	
	public ArrayList<DtoDefinitionClass> GetExactlyRelations(OntModel model) {


		ArrayList<DtoDefinitionClass> dtoExactlyList = new ArrayList<DtoDefinitionClass>();
		
		// create Pellet reasoner
		Reasoner r = PelletReasonerFactory.theInstance().create();		
		// create an inferencing model using the raw model
		InfModel infModel = ModelFactory.createInfModel(r, model);
		
		// Create a new query
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <http://www.semanticweb.org/parentesco-simples.owl#>" +
		" SELECT ?source ?relation ?cardinality ?target" +
		" WHERE {\n" +				
			" { " +
				"?source " + "owl:equivalentClass" + " ?blank .\n " +
				"?blank rdf:type owl:Class ."  +
				"?blank owl:intersectionOf  ?list     ." +
				"?list  rdf:rest*/rdf:first  ?member ."  +			
				" ?member " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				" ?member " + "owl:onProperty ?relation .\n" +
				" ?member " + "owl:onClass ?target" +			
			"} UNION {" +		
				" ?source " + "owl:equivalentClass" + " _:b0 .\n " +				
				" _:b0 " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				" _:b0 " + "owl:onProperty ?relation .\n" +
				" _:b0 " + "owl:onClass ?target" +	
			" } " +
		"}";

		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
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
		    
			itemList = new DtoDefinitionClass();
			itemList.Source = Source.toString();
			itemList.Relation = Relation.toString();
			itemList.Target = Target.toString();
			itemList.Cardinality = Cardinality.toString().split("\\^")[0];
			dtoExactlyList.add(itemList);
		}
		
		// Create a new query -- SUB CLASS OF -- DEFINED CLASS
		
		queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <http://www.semanticweb.org/parentesco-simples.owl#>" +
		" SELECT ?x ?y" +
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
		    
		    if(Class.toString().contains(NS) && SuperClass.toString().contains(NS) && Class.toString() != SuperClass.toString())
		    {		    	
		    	//System.out.println(Class.toString().split("#")[1] + " - " + SuperClass.toString().split("#")[1]);
		    	ArrayList<DtoDefinitionClass> dtoListWithSource = DtoDefinitionClass.getDtosWithSource(dtoExactlyList, SuperClass.toString());
		    	if(dtoListWithSource != null)
		    	{
		    		for (DtoDefinitionClass dto : dtoListWithSource) {
			    		itemList = new DtoDefinitionClass();
						itemList.Source = Class.toString();
						itemList.Relation = dto.Relation;
						itemList.Target = dto.Target;
						itemList.Cardinality = dto.Cardinality;
						dtoExactlyList.add(itemList);
					}
		    	}
		    }
		}
		
		return dtoExactlyList;
	}

	public ArrayList<DtoCompleteClass> GetCompleteClasses(OntModel model)
	{
		ArrayList<DtoCompleteClass> listClasses = new ArrayList<DtoCompleteClass>();
		
		// create Pellet reasoner
		Reasoner r = PelletReasonerFactory.theInstance().create();		
		// create an inferencing model using the raw model
		InfModel infModel = ModelFactory.createInfModel(r, model);
		
		// Create a new query
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <http://www.semanticweb.org/parentesco-simples.owl#>" +
		" SELECT *" +
		" WHERE {\n" +
				"?completeClass " + "owl:equivalentClass" + " ?x .\n " +
				"?x rdf:type owl:Class ."  +
				"?x owl:unionOf  ?list     ." +
        		"?list  rdf:rest*/rdf:first  ?member ."  +
		"}";

		/* The result is order by completClass */
		
		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		ResultSet results = qe.execSelect();
		
		// Output query results 
		// ResultSetFormatter.out(System.out, results, query);
		
		DtoCompleteClass itemList = null;
		
		while (results.hasNext()) {

			QuerySolution row= results.next();
		    RDFNode completeClass = row.get("completeClass");
		    RDFNode member = row.get("member");
		    
		    itemList = DtoCompleteClass.GetDtoCompleteClass(listClasses, completeClass.toString());
		    if(itemList == null)
		    {
		    	//New Class
		    	itemList = new DtoCompleteClass();
		    	itemList.CompleteClass = completeClass.toString();
		    	itemList.AddMember(member.toString());	
		    	listClasses.add(itemList);
		    } else {
		    	itemList.AddMember(member.toString());
		    }		    
		}

		
		for (DtoCompleteClass dto : listClasses) {
			System.out.println("# " + dto.CompleteClass);
			for (String member : dto.Members) {
				System.out.print("-> " + member);
			}
			System.out.println("");
		}
		
		return listClasses;
	}

	public ArrayList<String> GetDisjointClassesOf(String className, OntModel model) {
		
		ArrayList<String> listDisjointClasses = new ArrayList<String>();
		
		// create Pellet reasoner
		Reasoner r = PelletReasonerFactory.theInstance().create();		
		// create an inferencing model using the raw model
		InfModel infModel = ModelFactory.createInfModel(r, model);
		
		// Create a new query
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <http://www.semanticweb.org/parentesco-simples.owl#>" +
		" SELECT *" +
		" WHERE {\n" +
				"<" + className + "> " + "owl:disjointWith" + " ?classDisjoint .\n " +
		"}";
		
		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		ResultSet results = qe.execSelect();
		
		// Output query results 
		// ResultSetFormatter.out(System.out, results, query);
		
		while (results.hasNext()) {
			QuerySolution row= results.next();
		    RDFNode completeClass = row.get("classDisjoint");
		    listDisjointClasses.add(completeClass.toString());		    		    
		}
		
		for (String string : listDisjointClasses) {
			System.out.println("-" + string);
		}
		
		return listDisjointClasses;

	}

}
