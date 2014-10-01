package br.ufes.inf.nemo.okco.model.queries;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class InfModelQueryUtil {
	
	/** 
	 * Return the URI of all classes of the ontology. This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel 
	 * 
	 * @author John Guerson
	 */
	static public List<String> getClassesURI(InfModel model) 
	{		
		System.out.println("Executing getClassesURI(model)");
		List<String> result = new ArrayList<String>();				
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + model.getNsPrefixURI("") + ">" +
		" SELECT *" +
		" WHERE {\n" +		
			" ?i rdf:type owl:Class .\n " +	
		    " FILTER(?i NOT IN (owl:Thing,owl:Nothing)) .\n"+
		"}";
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		// ResultSetFormatter.out(System.out, results, query);
		while (results.hasNext()) 
		{			
			QuerySolution row = results.next();		    
		    RDFNode i = row.get("i");	
		    if(isValidURI(i.toString()))
		    {
		    	System.out.println("Class URI: "+i.toString()); 
		    	result.add(i.toString()); 
		    }
		}
		return result;
	}
	
	/** 
	 * Return the URI of all classes of this specific individual in the ontology. This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel 
	 * @param individualURI: Individual URI
	 * 
	 * @author John Guerson
	 */
	static public List<String> getClassesURI(InfModel model, String individualURI) 
	{		
		System.out.println("Executing getClassesURI(model, individualURI)");
		List<String> result = new ArrayList<String>();				
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + model.getNsPrefixURI("") + ">" +
		" SELECT *" +
		" WHERE {\n" +		
			" <"+individualURI+"> rdf:type ?i .\n " +
			" ?i rdf:type owl:Class .\n" +
			" FILTER(?i NOT IN (owl:Thing,owl:Nothing)) .\n"+
		"}";
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		// ResultSetFormatter.out(System.out, results, query);
		while (results.hasNext()) 
		{			
			QuerySolution row = results.next();		    
		    RDFNode i = row.get("i");
		    if(isValidURI(i.toString())) {
		    	System.out.println("Class URI: "+i.toString()+" - IndividualURI: "+individualURI); 
		    	result.add(i.toString()); 		    
		    }
		}
		return result;
	}
	
	/**
	 * Check if a URI is valid.
	 * 
	 * @author John Guerson
	 */
	static public boolean isValidURI(String uri)
	{
		return uri.contains("http");
	}
			
	/** 
	 * Return the URI of all datatype relations that this individual is linked to, in the ontology. This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel 
	 * @param individualURI: Individual URI
	 * 
	 * @author John Guerson
	 */
	static public List<String> getDatatypeRelationsURI(InfModel infModel, String individualURI)
	{
		System.out.println("Executing getDatatypeRelationsURI(model, individualURI)");
		ArrayList<String> result = new ArrayList<String>();		
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + infModel.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT *" +
		" WHERE {\n" +		
			"<" + individualURI + ">" + " ?property" + " ?target .\n " +
			" ?property " + " rdf:type" + " owl:DatatypeProperty .\n " +
		"}";
		Query query = QueryFactory.create(queryString);		
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		ResultSet results = qe.execSelect();
		//ResultSetFormatter.out(System.out, results, query);
		while (results.hasNext())	
		{			
			QuerySolution row= results.next();
		    RDFNode property = row.get("property");	
		    if(isValidURI(property.toString())){
		    	System.out.println("Datatype Relation URI: "+property.toString()+" - IndividualURI: "+individualURI); 
		    	result.add(property.toString());
		    }
		}		
		return result;
	}
	
	/** 
	 * Return the URI of all object relations that this individual is linked to, in the ontology. This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel 
	 * @param individualURI: Individual URI
	 * 
	 * @author John Guerson
	 */
	static public List<String> getObjectRelationsURI(InfModel infModel, String individualURI)
	{
		System.out.println("Executing getObjectRelationsURI(model, individualURI)");
		ArrayList<String> result = new ArrayList<String>();		
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + infModel.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT *" +
		" WHERE {\n" +		
			"<" + individualURI + ">" + " ?property" + " ?target .\n " +
			" ?property " + " rdf:type" + " owl:ObjectProperty .\n " +
		"}";
		Query query = QueryFactory.create(queryString);		
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		ResultSet results = qe.execSelect();
		//ResultSetFormatter.out(System.out, results, query);
		while (results.hasNext())	
		{			
			QuerySolution row= results.next();
		    RDFNode property = row.get("property");	
		    if(isValidURI(property.toString())){
		    	System.out.println("Object Relation URI: "+property.toString()+" - IndividualURI: "+individualURI); 
		    	result.add(property.toString());
		    }
		}		
		return result;
	}
	
	/** 
	 * Return the URI of all relations (both object and datatype relations) that this individual is linked to, in the ontology. This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel 
	 * @param individualURI: Individual URI
	 * 
	 * @author John Guerson
	 */
	public List<String> getRelationsURI(InfModel infModel, String individualURI)
	{
		System.out.println("Executing getRelationsURI(model, individualURI)");
		ArrayList<String> result = new ArrayList<String>();		
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + infModel.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT *" +
		" WHERE {\n" +		
			"{ " + "<" + individualURI + ">" + " ?property" + " ?target .\n " +
				" ?property " + " rdf:type" + " owl:ObjectProperty .\n " +
			"} UNION { " +
				"<" + individualURI + ">" + " ?property" + " ?target .\n " +
				" ?property " + " rdf:type" + " owl:DatatypeProperty.\n " +		
			"}" +
		"}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		ResultSet results = qe.execSelect(); 
		//ResultSetFormatter.out(System.out, results, query);
		while (results.hasNext()) 
		{			
			QuerySolution row= results.next();
		    RDFNode property = row.get("property");
		    if(isValidURI(property.toString())){
		    	System.out.println("Relation URI: "+property.toString()+" - IndividualURI: "+individualURI); 
		    	result.add(property.toString());
		    } 		    		    
		}		
		return result;
	}
	
	/** 
	 * Return all individuals URI of all the classes of the ontology. This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel 
	 * 
	 * @author John Guerson
	 */
	static public List<String> getIndividualsURIFromAllClasses(InfModel infModel)
	{	
		List<String> individuals = new ArrayList<String>();		
		List<String> classes = InfModelQueryUtil.getClassesURI(infModel);		
		for (String classURI : classes) 
		{			
			if(classURI != null)
			{
				for(String i: InfModelQueryUtil.getIndividualsURI(infModel, classURI)){				
					if(!individuals.contains(i)) individuals.add(i);
				}
			}
		}		
		return individuals;
	}
	
	/**
	 * Return all individuals URI of a given class URI of the ontology. This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel
	 * @param className: class URI
	 * 
	 * @author John Guerson
	 */
	static public List<String> getIndividualsURI(InfModel model, String classURI) 
	{	
		System.out.println("Executing getIndividualsURI(model, classURI)");
		List<String> list = new ArrayList<String>();		
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + model.getNsPrefixURI("") + ">" +
		" SELECT *" +
		" WHERE {\n" +		
			" ?i rdf:type <" + classURI + "> .\n " +	
		"}";
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		// ResultSetFormatter.out(System.out, results, query);
		while (results.hasNext()) 
		{			
			QuerySolution row = results.next();		    
		    RDFNode i = row.get("i");		    
		    list.add(i.toString());	
		    System.out.println("Individual URI: "+i.toString()+" - Class URI: "+classURI);
		}
		return list;		
	}
	
	/**
	 * Return all individuals URI that are different from the given individual URI in the ontology.
	 * This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel
	 * @param individualURI: Individual URI
	 * 
	 * @author John Guerson
	 */	
	static public List<String> getIndividualsURIDifferentFrom(InfModel model, String individualURI)
	{		
		System.out.println("Executing getIndividualsURIDifferentFrom(model, individualURI)");
		List<String> list = new ArrayList<String>();
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + model.getNsPrefixURI("")  + ">" +
		" SELECT DISTINCT ?y " +
		" WHERE {\n" +		
			"{ " + 
				"<" + individualURI + "> owl:differentFrom" + " ?y .\n " +
			"} UNION { " +
				" ?y owl:differentFrom <" + individualURI + "> .\n " +
			" } " +
		"}";
		Query query = QueryFactory.create(queryString);		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		//ResultSetFormatter.out(System.out, results, query);		
		while (results.hasNext()) 
		{
			QuerySolution row = results.next();		    
		    RDFNode rdfY = row.get("y");
		    if(! individualURI.equals(rdfY.toString()))
		    {
		    	list.add(rdfY.toString());
				System.out.println("Different Individual URI: "+rdfY.toString()+" - From: "+individualURI);
		    }
		}	
		return list;
	}
	
	/**
	 * Return all individuals URI that are the same as the given individual URI in the ontology.
	 * This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel
	 * @param individualURI: Individual URI
	 * 
	 * @author John Guerson
	 */	
	static public List<String> getIndividualsURISameAs(InfModel model, String individualURI)
	{
		System.out.println("Executing getIndividualsURISameAs(model, individualURI)");
		List<String> list = new ArrayList<String>();
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + model.getNsPrefixURI("")  + ">" +
		" SELECT DISTINCT ?y" +
		" WHERE {\n" +
			"{ " + 
				"<" + individualURI + "> owl:sameAs" + " ?y .\n " +
			"} UNION { " +
				" ?y owl:sameAs <" + individualURI + "> .\n " +
			" } " +
		"}";
		Query query = QueryFactory.create(queryString);		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		//ResultSetFormatter.out(System.out, results, query);		
		while (results.hasNext()) 
		{
			QuerySolution row = results.next();		    
		    RDFNode rdfY = row.get("y");
		    if(! individualURI.equals(rdfY.toString()))
		    {
		    	list.add(rdfY.toString());
				System.out.println("Same Individual URI: "+rdfY.toString()+" - From: "+individualURI);
		    }
		}		
		return list;
	}
		
	//======================================================================
	//These methods below are quite weird. We will try to fix them
	//======================================================================
	
	/**
	 * Return all the individuals URI that are instance of rangeClassURI and is related to the given individualURI via relationURI.
	 * This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel
	 * @param individualURI: Individual URI
	 * @param relationURI: Relation URI
	 * @param rangeClassURI: Range Class URI
	 * 
	 * @author John Guerson
	 */
	static public List<String> getIndividualsURIInRelationRange(InfModel model, String individualURI, String relationURI, String rangeClassURI) 
	{		
		System.out.println("Executing getIndividualsURIInRelationRange()");
		List<String> list = new ArrayList<String>();		
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <"+ model.getNsPrefixURI("") + ">" +
		"\n SELECT DISTINCT ?x" +
		" WHERE {\n" +
			" <" + individualURI + "> <" + relationURI + "> ?x .\n " +
			" ?x" + " rdf:type" + " <"+ rangeClassURI + "> .\n " +
		"}";
		Query query = QueryFactory.create(queryString);		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		//ResultSetFormatter.out(System.out, results, query);		
		while (results.hasNext()) 
		{
			QuerySolution row = results.next();
		    RDFNode rdfInstance = row.get("x");
		    list.add(rdfInstance.toString());
			System.out.println("Individual URI: "+rdfInstance.toString()+" - From Relation Range: "+relationURI);
		}
		return list;
	}
	
	/**
	 * Return all the individuals URI that are instance of rangeClassURI and is related to the given individualURI via datatypeRelationURI.
	 * This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel
	 * @param individualURI: Individual URI
	 * @param relationURI: Datatype Relation URI
	 * @param rangeClassURI: Range Class URI
	 * 
	 * @author John Guerson
	 */
	static public List<String> getIndividualsURIInDataTypeRelationRange(InfModel model, String individualURI, String relationURI, String rangeClassURI)
	{
		System.out.println("Executing getIndividualsURIInDataTypeRelationRange()");
		List<String> list = new ArrayList<String>();	
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <"+ model.getNsPrefixURI("") + ">" +
		"\n SELECT DISTINCT ?x" +
		" WHERE {\n" +
			" <" + individualURI + "> <" + relationURI + "> ?x .\n " +
			" <" + relationURI + "> rdf:type owl:DatatypeProperty .\n " +
		"}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		//ResultSetFormatter.out(System.out, results, query);
		while (results.hasNext()) 
		{
			QuerySolution row = results.next();
		    RDFNode rdfInstance = row.get("x");		    
		    if(rdfInstance.toString().contains(rangeClassURI))
		    {
		    	 list.add(rdfInstance.toString());
				System.out.println("Individual URI: "+rdfInstance.toString()+" - From DataType Range: "+relationURI);		    	 
		    }
		}
		return list;
	}
	
	/**
	 * Check if there is at least one individual URI that is instance of rangeClassURI and is related to the given individualURI via relationURI.
	 * This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel
	 * @param individualURI: Individual URI
	 * @param relationURI: Relation URI
	 * @param rangeClassURI: Range Class URI
	 * 
	 * @author John Guerson
	 */
	static public boolean existsIndividualsInRelationRange(InfModel model, String individualURI, String relationURI, String rangeClassURI) 
	{				
		List<String> individualList = InfModelQueryUtil.getIndividualsURIInRelationRange(model, individualURI, relationURI, rangeClassURI);
		if(individualList.size()>0) { return true; }						
		// check data property
		individualList = InfModelQueryUtil.getIndividualsURIInDataTypeRelationRange(model, individualURI, relationURI, rangeClassURI);
		if(individualList.size()>0) { return true; }	
		return false;
	}
}
