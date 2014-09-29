package br.ufes.inf.nemo.okco.model.util;

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

public class InfModelUtil {

	/**
	 * Return all individuals URI of a given class URI of the ontology. This method is performed using SPARQL query.
	 * 
	 * @param model: jena.ontology.InfModel
	 * @param className: class URI
	 * 
	 * @author John Guerson
	 */
	static public List<String> getIndividualsURI(InfModel model, String classURI) 
	{	
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
		    //DateTimeHelper.printout("Individual URI: "+i.toString());
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
				//DateTimeHelper.printout("Different Individual URI: "+rdfY.toString()+" - From: "+individualURI);
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
				//DateTimeHelper.printout("Same Individual URI: "+rdfY.toString()+" - From: "+individualURI);
		    }
		}		
		return list;
	}
	
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
			//DateTimeHelper.printout("Individual URI: "+rdfInstance.toString()+" - From Relation Range: "+relationURI);
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
				//DateTimeHelper.printout("Individual URI: "+rdfInstance.toString()+" - From DataType Range: "+datatypePropertyURI);		    	 
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
		List<String> individualList = InfModelUtil.getIndividualsURIInRelationRange(model, individualURI, relationURI, rangeClassURI);
		if(individualList.size()>0) { return true; }						
		// check data property
		individualList = InfModelUtil.getIndividualsURIInDataTypeRelationRange(model, individualURI, relationURI, rangeClassURI);
		if(individualList.size()>0) { return true; }	
		return false;
	}
}
