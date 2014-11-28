package br.com.padtec.common.queries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.padtec.common.application.UploadApp;
import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.persistence.BaseModelRepository;
import br.com.padtec.common.types.OntPropertyEnum;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class QueryUtil {
	
	static public String w3URI  = "http://www.w3.org/";
	static public String owlURI = "http://www.w3.org/2002/07/owl";
	static public String rdfsURI = "http://www.w3.org/2000/01/rdf-schema";
	static public String rdfURI = "http://www.w3.org/1999/02/22-rdf-syntax-ns";
	
	/** 
	 * Return the URI of all classes of the ontology. This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel 
	 * 
	 * @author John Guerson
	 */
	static public List<String> getClassesURI(InfModel model) 
	{		
		System.out.println("\nExecuting getClassesURI()...");
		List<String> result = new ArrayList<String>();				
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + model.getNsPrefixURI("") + ">" +
		" SELECT *" +
		" WHERE {\n" +		
			" ?i rdf:type owl:Class .\n " +	
		    " FILTER(?i NOT IN (owl:Thing, owl:Nothing)) .\n"+
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
		    	System.out.println("- Class URI: "+i.toString()); 
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
		System.out.println("\nExecuting getClassesURI()...");
		System.out.println("- IndividualURI: "+individualURI);
		List<String> result = new ArrayList<String>();	
		//check if the individual is a data value
		if(individualURI.contains("http://www.w3.org/"))
		{
			String type = individualURI.split("\\^\\^")[1];
			result.add(type);
			return result;
		}
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
		"PREFIX ns: <" + model.getNsPrefixURI("") + "> \n" +
		" SELECT * \n" +
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
		    	System.out.println("- Class URI: "+i.toString()); 
		    	result.add(i.toString()); 		    
		    }
		}
		return result;
	}
	
	/**
	 * Check if a class is disjoint from another. This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel 
	 * @param classURI: Class URI
	 * @param checingClassURI: Class URI to be checked
	 * 
	 * @author John Guerson
	 */
	static public boolean isClassesURIDisjoint(InfModel model, String classURI, String checkingClassURI)
	{
		System.out.println("\nExecuting isClassesURIDisjoint()...");
		System.out.println("- Class URI: "+classURI);
		System.out.println("- Class URI to check: "+checkingClassURI);
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + model.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT *" +
		" WHERE {\n" +
				"<" + classURI + "> " + "owl:disjointWith" + " ?classD .\n " +
		"}";		
		Query query = QueryFactory.create(queryString);		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		// ResultSetFormatter.out(System.out, results, query);		
		while (results.hasNext()) 
		{			
			QuerySolution row= results.next();
		    RDFNode classD = row.get("classD");
		    String strClassD = classD.toString();		    
		    if(checkingClassURI.equals(strClassD)) {
		    	System.out.println("- Is Disjoint: true");
		    	return true; 
		    }		    	    		    
		}		
		return false;
	}
	
	/** 
	 * Return the type of this property URI. This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel 
	 * @param propertyURI: Property URI
	 * 
	 * @author John Guerson
	 */
	static public OntPropertyEnum getPropertyURIType(InfModel model, String propertyURI)
	{
		System.out.println("\nExecuting getPropertyURIType()...");
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + model.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT *" +
		" WHERE {\n" +
				"<" + propertyURI + "> " + " rdf:type " + " ?type .\n " +
		"}";		
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		//ResultSetFormatter.out(System.out, results, query);		
		while (results.hasNext()) 
		{			
			QuerySolution row= results.next();
		    RDFNode t = row.get("type");
		    String type = t.toString();		    
		    System.out.println("- Property URI Type: "+type);
		    if(type.contains("DatatypeProperty")) return OntPropertyEnum.DATA_PROPERTY;		    
		    if(type.contains("ObjectProperty")) return OntPropertyEnum.OBJECT_PROPERTY;
		    if(type.contains("TransitiveProperty")) return OntPropertyEnum.TRANSITIVE_PROPERTY;
		    if(type.contains("SymmetricProperty")) return OntPropertyEnum.SYMMETRIC_PROPERTY;
		    if(type.contains("InverseFunctionalProperty")) return OntPropertyEnum.INVERSE_FUNCTIONAL_PROPERTY;
		    if(type.contains("AnnotationProperty")) return OntPropertyEnum.ANNOTATION_PROPERTY;
		    if(type.contains("FunctionalProperty")) return OntPropertyEnum.FUNCTIONAL_PROPERTY;
		}		
		return null;
	}
	
	/** 
	 * Return true if the property have the domain classes disjoint of all classes in classesURIList at same time.
	 * This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel 
	 * @param propertyURI: Property URI
	 * @param classesURI: List of classes URI
	 * 
	 * @author John Guerson
	 */
	static public boolean isDomainDisjointWithAll(InfModel infModel, String propertyURI, List<String> classesURI) 
	{
		List<String> domainList = QueryUtil.getDomainURIs(infModel, propertyURI);
		boolean isDomainDisjointWithAll = true;
		for (String domain : domainList) 
		{						
			for (String classToCheck : classesURI)
			{
				if(!QueryUtil.isClassesURIDisjoint(infModel, domain, classToCheck)){
					isDomainDisjointWithAll = false;
					break;
				}
			}			
		}
		return isDomainDisjointWithAll;
	}

	/** 
	 * Return true if the property have the range classes disjoint of all classes in classesURIList at same time.
	 * This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel 
	 * @param propertyURI: Property URI
	 * @param classesURI: List of classes URI
	 * 
	 * @author John Guerson
	 */
	static public boolean isRangeDisjointWithAll(InfModel infModel, String propertyURI, List<String> classesURI) 
	{				
		List<String> rangeList = QueryUtil.getRangeURIs(infModel, propertyURI);
		boolean isDomainDisjointWithAll = true;
		for (String range : rangeList) 
		{						
			for (String classToCheck : classesURI)
			{
				if(!QueryUtil.isClassesURIDisjoint(infModel, range, classToCheck)){
					isDomainDisjointWithAll = false;
					break;
				}
			}			
		}
		return isDomainDisjointWithAll;
	}
	
	/**
	 * Return all the disjoint classes URI with this particular class in the ontology. This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel 
	 * @param classURI: Class URI
	 * 
	 * @author John Guerson
	 */
	static public List<String> getClassesURIDisjointWith(InfModel model,String classURI) 
	{		
		System.out.println("\nExecuting getClassesURIDisjointWith()...");
		System.out.println("- Class URI: "+classURI);
		List<String> result = new ArrayList<String>();		
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + model.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT *" +
		" WHERE {\n" +
				"<" + classURI + "> " + "owl:disjointWith" + " ?classDisjoint .\n " +
				" FILTER(?classDisjoint NOT IN (owl:Nothing)) .\n"+
		"}";		
		Query query = QueryFactory.create(queryString);		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		// ResultSetFormatter.out(System.out, results, query);		
		while (results.hasNext()) 
		{
			QuerySolution row= results.next();
		    RDFNode completeClass = row.get("classDisjoint");
		    result.add(completeClass.toString());		    	
		    System.out.println("- Disjoint Class URI: "+completeClass.toString());
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
	 * Return the URI of all datatype properties that this individual is linked to, in the ontology. This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel 
	 * @param individualURI: Individual URI
	 * 
	 * @author John Guerson
	 */
	static public List<String> getDatatypePropertiesURI(InfModel model, String individualURI)
	{
		System.out.println("\nExecuting getDatatypePropertiesURI()...");
		System.out.println("- Individual URI: "+individualURI);
		List<String> result = new ArrayList<String>();		
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + model.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT *" +
		" WHERE {\n" +		
			"<" + individualURI + ">" + " ?property" + " ?target .\n " +
			" ?property " + " rdf:type" + " owl:DatatypeProperty .\n " +
		"}";
		Query query = QueryFactory.create(queryString);		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		//ResultSetFormatter.out(System.out, results, query);
		while (results.hasNext())	
		{			
			QuerySolution row= results.next();
		    RDFNode property = row.get("property");	
		    if(isValidURI(property.toString())){
		    	System.out.println("- Datatype Property URI: "+property.toString()); 
		    	result.add(property.toString());
		    }
		}		
		return result;
	}
	
	/** 
	 * Return the URI of all object properties that this individual is linked to, in the ontology. This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel 
	 * @param individualURI: Individual URI
	 * 
	 * @author John Guerson
	 */
	static public List<String> getObjectPropertiesURI(InfModel model, String individualURI)
	{
		System.out.println("\nExecuting getObjectPropertiesURI()...");
		System.out.println("- Individual URI: "+individualURI);
		List<String> result = new ArrayList<String>();		
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + model.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT *" +
		" WHERE {\n" +		
			"<" + individualURI + ">" + " ?property" + " ?target .\n " +
			" ?property " + " rdf:type" + " owl:ObjectProperty .\n " +
		"}";
		Query query = QueryFactory.create(queryString);		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		//ResultSetFormatter.out(System.out, results, query);
		while (results.hasNext())	
		{			
			QuerySolution row= results.next();
		    RDFNode property = row.get("property");	
		    if(isValidURI(property.toString())){
		    	System.out.println("- Object Property URI: "+property.toString()); 
		    	result.add(property.toString());
		    }
		}		
		return result;
	}
	
	/** 
	 * Return the URI of all properties (both object and datatype properties) that this individual is linked to, in the ontology. This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel 
	 * @param individualURI: Individual URI
	 * 
	 * @author John Guerson
	 */
	static public List<String> getPropertiesURI(InfModel model, String individualURI)
	{
		System.out.println("\nExecuting getPropertiesURI()...");
		System.out.println("- Individual URI: "+individualURI);
		List<String> result = new ArrayList<String>();		
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + model.getNsPrefixURI("") + ">" +
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
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect(); 
		//ResultSetFormatter.out(System.out, results, query);
		while (results.hasNext()) 
		{			
			QuerySolution row= results.next();
		    RDFNode property = row.get("property");
		    if(isValidURI(property.toString())){
		    	System.out.println("- Property URI: "+property.toString()); 
		    	result.add(property.toString());
		    } 		    		    
		}		
		return result;
	}
	
	/** 
	 * Return the URI of all properties that are disjoint with this property URI in the ontology. This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel 
	 * @param propertyURI: Property URI
	 * 
	 * @author John Guerson
	 */
	static public List<String> getPropertiesURIDisjointWith(InfModel model, String propertyURI) 
	{
		System.out.println("Executing getPropertiesURIDisjointWith()...");
		System.out.println("- Property URI: "+propertyURI);
		List<String> result = new ArrayList<String>();
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + model.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT *" +
		" WHERE {\n" +
				"<" + propertyURI + "> " + "owl:propertyDisjointWith" + " ?propDisjoint .\n " +
		"}";		
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		// ResultSetFormatter.out(System.out, results, query);		
		while (results.hasNext()) 
		{
			QuerySolution row= results.next();
		    RDFNode prop = row.get("propDisjoint");
		    result.add(prop.toString());
		    System.out.println("- Disjoint Property URI: "+prop.toString());
		}		
		return result;
	}
	
	/** 
	 * Return the URI of all sub-properties of this property URI in the ontology. This sub-property might have undefined range and domain.
	 * This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel 
	 * @param propertyURI: Property URI
	 * 
	 * @author John Guerson
	 */
	static public List<String> getSubPropertiesURI(InfModel model, String propertyURI, boolean withDefinedDomain, boolean withDefinedRange)
	{
		System.out.println("\nExecuting getSubPropertiesURI()...");
		System.out.println("- Property URI: "+propertyURI);
		List<String> result = new ArrayList<String>();
		String definedDomainCode = "";
		if (withDefinedDomain) definedDomainCode = "?subProp rdfs:domain ?domainSubProp .";
		String definedRangeCode = "";
		if (withDefinedRange) definedRangeCode = "?subProp rdfs:range ?rangeSubProp .";
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + model.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT *" +
		" WHERE {\n" +
			"?subProp rdfs:subPropertyOf" + "<" + propertyURI + "> ." +
			definedDomainCode+
			definedRangeCode+
		"}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect(); 
		// ResultSetFormatter.out(System.out, results, query);		
		while (results.hasNext()) 
		{
			QuerySolution row= results.next();
		    RDFNode subProp = row.get("subProp");
		    if(!subProp.toString().contains(w3URI) && (!subProp.toString().equals(propertyURI)))
		    {
		    	if(!result.contains(subProp)) {
		    		result.add(subProp.toString());
		    		System.out.println("- SubProperty URI: "+subProp.toString());
		    	}		    	
		    }
		}		
		return result;
	}
	
	/** 
	 * Return the URI of all sub-properties of this property URI in the ontology but that are not contained in the subPropertyURIList given. 
	 * This sub-property has always defined its range and domain.
	 * This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel 
	 * @param propertyURI: Property URI
	 * 
	 * @author John Guerson
	 */
	static public List<String> getSubPropertiesURIExcluding(InfModel model, String domainIndividualURI, String propertyURI, String rangeIndividualURI, List<String> subPropertyURIList) 
	{
		System.out.println("\nExecuting getSubPropertiesURIExcluding()...");
		System.out.println("- Property URI: "+propertyURI);
		List<String> result = new ArrayList<String>();		
		List<String> domainClassURIList = QueryUtil.getClassesURI(model,domainIndividualURI);
		List<String> rangeClassURIList = QueryUtil.getClassesURI(model,rangeIndividualURI);		
		List<String> subproperties = QueryUtil.getSubPropertiesURI(model, propertyURI, true, true);		
		for (String subPropertyURI : subproperties) 
	    {							
			boolean disjointDomain = QueryUtil.isDomainDisjointWithAll(model,subPropertyURI, domainClassURIList);
			boolean disjointRange = QueryUtil.isRangeDisjointWithAll(model,subPropertyURI, rangeClassURIList);		
			if(disjointDomain && disjointRange)
	    	{
				if(!subPropertyURIList.contains(subPropertyURI)) {
					result.add(subPropertyURI);
					System.out.println("- SubProperty URI: "+subPropertyURI);
				}
	    	}
	    }
		return result;
	}
	
	/** 
	 * Return the URI of all sub-properties of this property URI in the ontology but that are not contained in the subPropertyURIList given. 
	 * This sub-property has always defined its range and domain.
	 * This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel 
	 * @param propertyURI: Property URI
	 * 
	 * @author John Guerson
	 */
	static public List<String> getRangeIndividualURI(InfModel model, String sourceIndividualURI, String propertyURI) 
	{
		List<String> result = new ArrayList<String>();
		Resource s = model.createResource(sourceIndividualURI);
		Property p = model.createProperty(propertyURI);
		StmtIterator statements = model.listStatements(s, p, (RDFNode) null);
		while(statements.hasNext()){
			Statement stm = statements.next();
			RDFNode o = stm.getObject();
		    result.add(o.toString());
		}
		return result;
	}

	/**
	 * Return the domain class of this property URI (it might be more than one) . This method is performded using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel 
	 * @param propertyURI: Property URI
	 * 
	 * @author John Guerson
	 */
	static public List<String> getDomainURIs(InfModel model, String propertyURI)
	{
		System.out.println("\nExecuting getDomainURIs()...");
		System.out.println("- Property URI: "+propertyURI);
		List<String> result = new ArrayList<String>();
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + model.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT *" +
		" WHERE {\n" +
			"<"+propertyURI+"> rdfs:domain ?domain ." +
		"}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect(); 
		// ResultSetFormatter.out(System.out, results, query);		
		while (results.hasNext()) 
		{
			QuerySolution row= results.next();
		    RDFNode domain = row.get("domain");
		    result.add(domain.toString());
		    System.out.println("- Domain URI: "+domain.toString());		    
		}		
		return result;
	}
	
	/**
	 * Return the first domain class of this property URI (since it might be more than one) . This method is performded using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel 
	 * @param propertyURI: Property URI
	 * 
	 * @author John Guerson
	 */
	static public List<String> getFirstDomainURI(InfModel model, String propertyURI)
	{
		System.out.println("\nExecuting getFirstDomainURI()...");
		System.out.println("- Property URI: "+propertyURI);
		List<String> result = new ArrayList<String>();
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + model.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT *" +
		" WHERE {\n" +
			"<"+propertyURI+"> rdfs:domain ?domain ." +
		"}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect(); 
		// ResultSetFormatter.out(System.out, results, query);		
		while (results.hasNext()) 
		{
			QuerySolution row= results.next();
		    RDFNode domain = row.get("domain");		    
		    result.add(domain.toString());
		    System.out.println("- Domain URI: "+domain.toString());
		    return result;		    		    
		}		
		return result;
	}
	
	/**
	 * Return the range class of this property URI (it might be more than one) . This method is performded using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel 
	 * @param propertyURI: Property URI
	 * 
	 * @author John Guerson
	 */
	static public List<String> getRangeURIs(InfModel model, String propertyURI)
	{
		System.out.println("\nExecuting getRangeURIs()...");
		System.out.println("- Property URI: "+propertyURI);
		List<String> result = new ArrayList<String>();
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + model.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT *" +
		" WHERE {\n" +
			"<"+propertyURI+"> rdfs:range ?range ." +
		"}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect(); 
		// ResultSetFormatter.out(System.out, results, query);		
		while (results.hasNext()) 
		{
			QuerySolution row= results.next();
		    RDFNode range = row.get("range");
		    result.add(range.toString());
		    System.out.println("- Range URI: "+range.toString());		    
		}		
		return result;
	}
		
	/**
	 * Return thefirst  range class of this property URI (since it might be more than one) . This method is performded using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel 
	 * @param propertyURI: Property URI
	 * 
	 * @author John Guerson
	 */
	static public List<String> getFirstRangeURI(InfModel model, String propertyURI)
	{
		System.out.println("\nExecuting getFirstRangeURI()...");
		System.out.println("- Property URI: "+propertyURI);
		List<String> result = new ArrayList<String>();
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + model.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT *" +
		" WHERE {\n" +
			"<"+propertyURI+"> rdfs:range ?range ." +
		"}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect(); 
		// ResultSetFormatter.out(System.out, results, query);		
		while (results.hasNext()) 
		{
			QuerySolution row= results.next();
		    RDFNode range = row.get("range");
		    result.add(range.toString());		    
		    System.out.println("- Range URI: "+range.toString());
		    return result;
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
		List<String> classes = QueryUtil.getClassesURI(infModel);		
		for (String classURI : classes) 
		{			
			if(classURI != null)
			{
				for(String i: QueryUtil.getIndividualsURI(infModel, classURI)){				
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
		System.out.println("\nExecuting getIndividualsURI()...");
		System.out.println("- Class URI: "+classURI);
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
		    System.out.println("- Individual URI: "+i.toString());
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
		System.out.println("\nExecuting getIndividualsURIDifferentFrom()...");
		System.out.println("- Individual URI: "+individualURI);
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
				System.out.println("- Different Individual URI: "+rdfY.toString());
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
		System.out.println("\nExecuting getIndividualsURISameAs()...");
		System.out.println("- Individual URI: "+individualURI);
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
				System.out.println("- Same Individual URI: "+rdfY.toString());
		    }
		}		
		return list;
	}
	
	/**
	 * Return all the individuals URI that are in the range of the given propertyURI, connected to the individualURI.
	 * This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel
	 * @param individualURI: Individual URI
	 * @param propertyURI: Property URI
	 * 
	 * @author John Guerson
	 */
	static public List<String> getIndividualsURIAtPropertyRange(InfModel model, String individualURI, String propertyURI)
	{
		System.out.println("\nExecuting getIndividualsURIInRange()...");
		System.out.println("- Individual URI: "+individualURI);
		System.out.println("- Property URI: "+propertyURI);
		List<String> result = new ArrayList<String>();		
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + model.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT *" +
		" WHERE {\n" +		
			"<" + individualURI + ">" + "<"+propertyURI+">"+ " ?target .\n "+			
		"}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect(); 
		//ResultSetFormatter.out(System.out, results, query);
		while (results.hasNext()) 
		{			
			QuerySolution row= results.next();
		    RDFNode property = row.get("target");
		    if(isValidURI(property.toString()))
		    {
		    	System.out.println("- Range Individual URI: "+property.toString()); 
		    	result.add(property.toString());
		    } 		    		    
		}		
		return result;
	}
	
	/**
	 * Return all the individuals URI that are in the range of the given propertyURI as instance of rangeClassURI and connected to the individualURI.
	 * This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel
	 * @param individualURI: Individual URI
	 * @param propertyURI: Property URI
	 * @param rangeClassURI: Rande Class URI
	 * 
	 * @author John Guerson
	 */
	static public List<String> getIndividualsURIAtObjectPropertyRange(InfModel model, String individualURI, String propertyURI, String rangeClassURI)
	{
		System.out.println("\nExecuting getIndividualsURIAtPropertyRange()...");
		System.out.println("- Individual URI: "+individualURI);
		System.out.println("- Property URI: "+propertyURI);
		System.out.println("- Range Class URI: "+rangeClassURI);
		List<String> result = new ArrayList<String>();		
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + model.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT *" +
		" WHERE {\n" +		
			"<" + individualURI + ">" + "<"+propertyURI+">"+ " ?target .\n "+
			" ?target" + " rdf:type" + " <"+ rangeClassURI + "> .\n " +
		"}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect(); 
		//ResultSetFormatter.out(System.out, results, query);
		while (results.hasNext()) 
		{			
			QuerySolution row= results.next();
		    RDFNode property = row.get("target");
		    if(isValidURI(property.toString()))
		    {
		    	System.out.println("- Range Individual URI: "+property.toString()); 
		    	result.add(property.toString());
		    } 		    		    
		}		
		return result;
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
	static public List<String> getIndividualsURIAtDataTypePropertyRange(InfModel model, String individualURI, String propertyURI, String rangeClassURI)
	{
		System.out.println("\nExecuting getIndividualsURIInDataTypeRelationRange()...");
		System.out.println("- Individual URI: "+individualURI);
		System.out.println("- Property URI: "+propertyURI);
		System.out.println("- Range Class URI: "+rangeClassURI);
		List<String> list = new ArrayList<String>();	
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <"+ model.getNsPrefixURI("") + ">" +
		"\n SELECT DISTINCT ?x" +
		" WHERE {\n" +
			" <" + individualURI + "> <" + propertyURI + "> ?x .\n " +
			" <" + propertyURI + "> rdf:type owl:DatatypeProperty .\n " +
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
				System.out.println("- Datatype Individual URI: "+rdfInstance.toString());		    	 
		    }
		}
		return list;
	}
	
	/**
	 * Count the individuals that are in the range of the given propertyURI as instance of rangeClassURI and connected to the individualURI.
	 * This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel
	 * @param individualURI: Individual URI
	 * @param propertyURI: Property URI
	 * @param rangeClassURI: Rande Class URI
	 * 
	 * @author John Guerson
	 */
	static public int countIndividualsURIAtPropertyRange(InfModel model, String individualURI, String propertyURI, String rangeClassURI) 
	{		
		System.out.println("\nExecuting countIndividualsURIAtPropertyRange()...");
		int counter = 0;		
		List<String> listValues = QueryUtil.getIndividualsURIAtObjectPropertyRange(model, individualURI, propertyURI, rangeClassURI);		
		ArrayList<String> uniqueListValues = new ArrayList<String>();
		uniqueListValues.addAll(listValues);		
		for (String value : listValues)
		{			
			if(uniqueListValues.contains(value))
			{
				List<String> sameInstances = QueryUtil.getIndividualsURISameAs(model, value);
				for (String sameIns : sameInstances)
				{
					if(uniqueListValues.contains(sameIns)) uniqueListValues.remove(sameIns);
				}	
			}			
		}		
		counter = uniqueListValues.size();	
		System.out.println("- Count for Object Individuals: "+counter);
		List<String> dataTypeValues = QueryUtil.getIndividualsURIAtDataTypePropertyRange(model, individualURI, propertyURI, rangeClassURI);
		counter += dataTypeValues.size();		
		System.out.println("- Count for Datatype Individuals: "+counter);
		return counter;
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
	static public boolean existsIndividualsAtPropertyRange(InfModel model, String individualURI, String relationURI, String rangeClassURI) 
	{				
		List<String> individualList = QueryUtil.getIndividualsURIAtObjectPropertyRange(model, individualURI, relationURI, rangeClassURI);
		if(individualList.size()>0) { return true; }						
		individualList = QueryUtil.getIndividualsURIAtDataTypePropertyRange(model, individualURI, relationURI, rangeClassURI);
		if(individualList.size()>0) { return true; }	
		return false;
	}
		
	/**
	 * It returns the triples in the format (domain,relation,range) of relations with some values.
	 * Should be more of a description here...
	 * 
	 *  @param mode: jena.ontology.InfModel
	 *  @param classURI: Class URI
	 *  
	 *  @author John Guerson
	 */
	static public List<String[]> getTuplesSomeValuesFrom(InfModel model, String classURI) 
	{
		System.out.println("\nExecuting getRelationsURIWithSomeValues()...");
		List<String[]> result = new ArrayList<String[]>();
		//classes
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + model.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT ?x ?y ?z" +
		" WHERE {\n" +			
			" { " +
				" ?x " + "owl:equivalentClass" + " ?blank .\n " +
				" ?blank rdf:type owl:Class .\n"  +
				" ?blank owl:intersectionOf  ?list  .\n" +
				" ?list  rdf:rest*/rdf:first  ?member . \n"  +			
				" ?member " + "owl:someValuesFrom" + " ?z .\n " +
				" ?member " + "owl:onProperty ?y .\n" +	
				
				" FILTER( ?x = <" + classURI + "> ) " +
			"} UNION {\n" +		
				" ?x " + "owl:equivalentClass" + " _:b0 .\n " +				
				" _:b0 " + "owl:someValuesFrom" + " ?z .\n " +
				" _:b0 " + "owl:onProperty ?y .\n" +
				
				" FILTER( ?x = <" + classURI + "> ) " +
			" }\n" +	
				
			"UNION { " +
				" ?x " + "rdfs:subClassOf" + " ?blank .\n " +
				" ?blank rdf:type owl:Class .\n"  +
				" ?blank owl:intersectionOf  ?list  .\n" +
				" ?list  rdf:rest*/rdf:first  ?member . \n"  +			
				" ?member " + "owl:someValuesFrom" + " ?z .\n " +
				" ?member " + "owl:onProperty ?y .\n" +	
				
				" FILTER( ?x = <" + classURI + "> ) " +
			"} UNION {\n" +		
				" ?x " + "rdfs:subClassOf" + " _:b1 .\n " +				
				" _:b1 " + "owl:someValuesFrom" + " ?z .\n " +
				" _:b1 " + "owl:onProperty ?y .\n" +
				
				" FILTER( ?x = <" + classURI + "> ) " +
			" }\n" +			
		
		"}";		
		Query query = QueryFactory.create(queryString);		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		//ResultSetFormatter.out(System.out, results, query);		
		while (results.hasNext()) 
		{
			QuerySolution row= results.next();
		    RDFNode Source = row.get("x");
		    RDFNode Relation = row.get("y");
		    RDFNode Target = row.get("z");		    
		    //jump the blank nodes - Check blank node and signal '-'
		    String TargetStr = Target.toString();
		    String SourceStr = Source.toString();
		    if ( Character.isDigit(TargetStr.charAt(0)) || TargetStr.startsWith("-") || Character.isDigit(SourceStr.charAt(0)) || SourceStr.startsWith("-")) 
		    {
		        continue;
		    }		    
			String[] triple = new String[]{Source.toString(), Relation.toString(), Target.toString() };	
			System.out.println("- Triple: \n");
			System.out.println("     "+Source.toString());
			System.out.println("     "+Relation.toString());
			System.out.println("     "+Target.toString());
			result.add(triple);
		}				
		//sub-classes		
		queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + model.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT ?x ?y" +
		" WHERE {\n" +
			" ?x " + "rdfs:subClassOf" + " ?y .\n " +
		"}";
		query = QueryFactory.create(queryString);	
		qe = QueryExecutionFactory.create(query, model);
		results = qe.execSelect();		
		// ResultSetFormatter.out(System.out, results, query);		
		while (results.hasNext()) 
		{			
			QuerySolution row= results.next();
		    RDFNode Class = row.get("x");
		    RDFNode SuperClass = row.get("y");		    
		    if(!Class.toString().contains(QueryUtil.w3URI) && !SuperClass.toString().contains(QueryUtil.w3URI) && Class.toString() != SuperClass.toString())
		    {		    	
		    	List<String[]> dtoListWithSource = retainOnly(result, SuperClass.toString());
		    	if(dtoListWithSource != null)
		    	{
		    		for (String[] dto : dtoListWithSource) 
		    		{		
		    			String[] newTriple = new String[3];
						newTriple[0] = Class.toString();
						newTriple[1] = dto[1];
						newTriple[2] = dto[2];						
						result.add(newTriple);
						System.out.println("- Triple: \n");
						System.out.println("     "+newTriple[0]);
						System.out.println("     "+newTriple[1]);
						System.out.println("     "+newTriple[2]);						
					}
		    	}
		    }
		}
		return result;
	}
	
	/**
	 * It retains only the triple which the first (domain class) is the given classURI.
	 * 
	 * @param tripleList: String Array with three positions i.e. domain, relationa and range, respectively.
	 * @param classURI: Class URI
	 * 
	 * @author John Guerson
	 */
	static private List<String[]> retainOnly(List<String[]> tripleList, String classURI)
	{		
		List<String[]> resultList = new ArrayList<String[]>();
		for (String[] triple : tripleList) 
		{
			if(triple[1].equals(classURI)) resultList.add(triple);			
		}
		return resultList;
	}
	
	/**
	 * It returns the triples in the format (domain,relation,cardinality,range) of relations with minimum cardianlity values.
	 * Should be more of a description here...
	 * 
	 *  @param mode: jena.ontology.InfModel
	 *  @param classURI: Class URI
	 *  
	 *  @author John Guerson
	 */
	static public List<String[]> getTuplesMinQualifiedCardinality(InfModel model, String classURI) 
	{
		System.out.println("\nExecuting getRelationsURIWithMinCardinality()...");
		List<String[]> result = new ArrayList<String[]>();		
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + model.getNsPrefixURI("") + ">" +
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
				
				" FILTER( ?source = <" + classURI + "> ) " +
			"} UNION {" +		
				"?source " + "owl:equivalentClass" + " ?blank .\n " +
				"?blank rdf:type owl:Class ."  +
				"?blank owl:intersectionOf  ?list     ." +
				"?list  rdf:rest*/rdf:first  ?member ."  +			
				" ?member " + "owl:minQualifiedCardinality" + " ?cardinality .\n " +
				" ?member " + "owl:onProperty ?relation .\n" +
				" ?member " + "owl:onDataRange ?target" +
				
				" FILTER( ?source = <" + classURI + "> ) " +
			"} UNION {" +	
				" ?source " + "owl:equivalentClass" + " _:b0 .\n " +				
				" _:b0 " + "owl:minQualifiedCardinality" + " ?cardinality .\n " +
				" _:b0 " + "owl:onProperty ?relation .\n" +
				" _:b0 " + "owl:onClass ?target" +
				
				" FILTER( ?source = <" + classURI + "> ) " +
			" } UNION { " +
				" ?source " + "owl:equivalentClass" + " _:b1 .\n " +				
				" _:b1 " + "owl:minQualifiedCardinality" + " ?cardinality .\n " +
				" _:b1 " + "owl:onProperty ?relation .\n" +
				" _:b1 " + "owl:onDataRange ?target" +
				
				" FILTER( ?source = <" + classURI + "> ) " +
			"}" +
				
			" UNION { " +
				"?source " + "rdfs:subClassOf" + " ?blank .\n " +
				"?blank rdf:type owl:Class ."  +
				"?blank owl:intersectionOf  ?list  ." +
				"?list  rdf:rest*/rdf:first  ?member ."  +			
				" ?member " + "owl:minQualifiedCardinality" + " ?cardinality .\n " +
				" ?member " + "owl:onProperty ?relation .\n" +
				" ?member " + "owl:onClass ?target" +		
				
				" FILTER( ?source = <" + classURI + "> ) " +
			"} UNION {" +		
				"?source " + "rdfs:subClassOf" + " ?blank .\n " +
				"?blank rdf:type owl:Class ."  +
				"?blank owl:intersectionOf  ?list     ." +
				"?list  rdf:rest*/rdf:first  ?member ."  +			
				" ?member " + "owl:minQualifiedCardinality" + " ?cardinality .\n " +
				" ?member " + "owl:onProperty ?relation .\n" +
				" ?member " + "owl:onDataRange ?target" +
				
				" FILTER( ?source = <" + classURI + "> ) " +
			"} UNION {" +	
				" ?source " + "rdfs:subClassOf" + " _:b2 .\n " +				
				" _:b2 " + "owl:minQualifiedCardinality" + " ?cardinality .\n " +
				" _:b2 " + "owl:onProperty ?relation .\n" +
				" _:b2 " + "owl:onClass ?target" +	
				
				" FILTER( ?source = <" + classURI + "> ) " +
			" } UNION { " +
				" ?source " + "rdfs:subClassOf" + " _:b3 .\n " +				
				" _:b3 " + "owl:minQualifiedCardinality" + " ?cardinality .\n " +
				" _:b3 " + "owl:onProperty ?relation .\n" +
				" _:b3 " + "owl:onDataRange ?target" +
				
				" FILTER( ?source = <" + classURI + "> ) " +
			"}" +			
		"}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		//ResultSetFormatter.out(System.out, results, query);
		while (results.hasNext()) 
		{
			QuerySolution row= results.next();
		    RDFNode Source = row.get("source");
		    RDFNode Relation = row.get("relation");
		    RDFNode Cardinality = row.get("cardinality");
		    RDFNode Target = row.get("target");		    
		    //jump the blank nodes - Check blank node and signal '-'
		    String sourceStr = Source.toString();
		    if (Character.isDigit(sourceStr.charAt(0)) || sourceStr.startsWith("-")) //
		    {
		        continue;
		    }		    
		    String[] triple = new String[]{Source.toString(), Relation.toString(), Cardinality.toString().split("\\^")[0], Target.toString() };	
			System.out.println("- Triple: \n");
			System.out.println("     "+Source.toString());
			System.out.println("     "+Relation.toString());
			System.out.println("     "+Cardinality.toString().split("\\^")[0]);
			System.out.println("     "+Target.toString());
			result.add(triple);
		}		
		//sub-classes
		queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + model.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT ?x ?y" +
		" WHERE {\n" +
			" ?x " + "rdfs:subClassOf" + " ?y .\n " +
			//" _:b0 " + "owl:Class ?y .\n" +
		"}";
		query = QueryFactory.create(queryString);
		qe = QueryExecutionFactory.create(query, model);
		results = qe.execSelect(); 
		//ResultSetFormatter.out(System.out, results, query);		
		while (results.hasNext()) 
		{
			QuerySolution row= results.next();
		    RDFNode Class = row.get("x");
		    RDFNode SuperClass = row.get("y");		    
		    if(!Class.toString().contains(QueryUtil.w3URI) && !SuperClass.toString().contains(QueryUtil.w3URI) && Class.toString() != SuperClass.toString())
		    {		    	
		    	List<String[]> dtoListWithSource = retainOnly(result, SuperClass.toString());
		    	if(dtoListWithSource != null)
		    	{
		    		for (String[] dto : dtoListWithSource) 
		    		{
		    			String[] newTriple = new String[4];
						newTriple[0] = Class.toString();
						newTriple[1] = dto[1];
						newTriple[2] = dto[2];
						newTriple[3] = dto[3];						
						result.add(newTriple);
						System.out.println("- Triple: \n");
						System.out.println("     "+newTriple[0]);
						System.out.println("     "+newTriple[1]);
						System.out.println("     "+newTriple[2]);
						System.out.println("     "+newTriple[3]);
					}
		    	}
		    }
		}		
		return result;
	}
	
	/**
	 * It returns the triples in the format (domain,relation,cardinality,range) of relations with maximum cardianlity values.
	 * Should be more of a description here...
	 * 
	 *  @param mode: jena.ontology.InfModel
	 *  @param classURI: Class URI
	 *  
	 *  @author John Guerson
	 */
	static public List<String[]> getTuplesMaxQualifiedCardinality(InfModel model, String classURI) 
	{
		System.out.println("\nExecuting getRelationsURIWithMaxCardinality()...");
		List<String[]> result = new ArrayList<String[]>();
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + model.getNsPrefixURI("") + ">" +
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
				
				" FILTER( ?source = <" + classURI + "> ) " +
			"} UNION {" +		
				"?source " + "owl:equivalentClass" + " ?blank .\n " +
				"?blank rdf:type owl:Class ."  +
				"?blank owl:intersectionOf  ?list     ." +
				"?list  rdf:rest*/rdf:first  ?member ."  +			
				" ?member " + "owl:maxQualifiedCardinality" + " ?cardinality .\n " +
				" ?member " + "owl:onProperty ?relation .\n" +
				" ?member " + "owl:onDataRange ?target" +
				
				" FILTER( ?source = <" + classURI + "> ) " +
			"} UNION {" +		
				" ?source " + "owl:equivalentClass" + " _:b0 .\n " +				
				" _:b0 " + "owl:maxQualifiedCardinality" + " ?cardinality .\n " +
				" _:b0 " + "owl:onProperty ?relation .\n" +
				" _:b0 " + "owl:onClass ?target" +	
				
				" FILTER( ?source = <" + classURI + "> ) " +
			" } UNION { " +
				" ?source " + "owl:equivalentClass" + " _:b1 .\n " +				
				" _:b1 " + "owl:maxQualifiedCardinality" + " ?cardinality .\n " +
				" _:b1 " + "owl:onProperty ?relation .\n" +
				" _:b1 " + "owl:onDataRange ?target" +
				
				" FILTER( ?source = <" + classURI + "> ) " +
			"}" +
				
			" UNION { " +
				"?source " + "rdfs:subClassOf" + " ?blank .\n " +
				"?blank rdf:type owl:Class ."  +
				"?blank owl:intersectionOf  ?list     ." +
				"?list  rdf:rest*/rdf:first  ?member ."  +			
				" ?member " + "owl:maxQualifiedCardinality" + " ?cardinality .\n " +
				" ?member " + "owl:onProperty ?relation .\n" +
				" ?member " + "owl:onClass ?target" +	
				
				" FILTER( ?source = <" + classURI + "> ) " +
			"} UNION {" +		
				"?source " + "rdfs:subClassOf" + " ?blank .\n " +
				"?blank rdf:type owl:Class ."  +
				"?blank owl:intersectionOf  ?list     ." +
				"?list  rdf:rest*/rdf:first  ?member ."  +			
				" ?member " + "owl:maxQualifiedCardinality" + " ?cardinality .\n " +
				" ?member " + "owl:onProperty ?relation .\n" +
				" ?member " + "owl:onDataRange ?target" +
				
				" FILTER( ?source = <" + classURI + "> ) " +
			"} UNION {" +		
				" ?source " + "rdfs:subClassOf" + " _:b2 .\n " +				
				" _:b2 " + "owl:maxQualifiedCardinality" + " ?cardinality .\n " +
				" _:b2 " + "owl:onProperty ?relation .\n" +
				" _:b2 " + "owl:onClass ?target" +	
				
				" FILTER( ?source = <" + classURI + "> ) " +
			" } UNION { " +
				" ?source " + "rdfs:subClassOf" + " _:b3 .\n " +				
				" _:b3 " + "owl:maxQualifiedCardinality" + " ?cardinality .\n " +
				" _:b3 " + "owl:onProperty ?relation .\n" +
				" _:b3 " + "owl:onDataRange ?target" +
				
				" FILTER( ?source = <" + classURI + "> ) " +
			"}" +			
		"}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect(); 
		// ResultSetFormatter.out(System.out, results, query);				
		while (results.hasNext()) 
		{
			QuerySolution row= results.next();
		    RDFNode Source = row.get("source");
		    RDFNode Relation = row.get("relation");
		    RDFNode Cardinality = row.get("cardinality");
		    RDFNode Target = row.get("target");		    
		    String sourceStr = Source.toString();
		    if(Character.isDigit(sourceStr.charAt(0)) || sourceStr.startsWith("-")) //Check blank node and signal '-'
		    {
		        continue;
		    }		  
		    String[] triple = new String[]{Source.toString(), Relation.toString(), Cardinality.toString().split("\\^")[0], Target.toString() };	
			System.out.println("- Triple: \n");
			System.out.println("     "+Source.toString());
			System.out.println("     "+Relation.toString());
			System.out.println("     "+Cardinality.toString().split("\\^")[0]);
			System.out.println("     "+Target.toString());
			result.add(triple);
		}		
		//sub-classes
		queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + model.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT ?x ?y" +
		" WHERE {\n" +
			" ?x " + "rdfs:subClassOf" + " ?y .\n " +
			//" _:b0 " + "owl:Class ?y .\n" +
		"}";
		query = QueryFactory.create(queryString);
		qe = QueryExecutionFactory.create(query, model);
		results = qe.execSelect();
		//ResultSetFormatter.out(System.out, results, query);		
		while (results.hasNext()) 
		{			
			QuerySolution row= results.next();
		    RDFNode Class = row.get("x");
		    RDFNode SuperClass = row.get("y");		    
		    if(!Class.toString().contains(QueryUtil.w3URI) && !SuperClass.toString().contains(QueryUtil.w3URI) && Class.toString() != SuperClass.toString())
		    {		    	
		    	List<String[]> dtoListWithSource = retainOnly(result, SuperClass.toString());
		    	if(dtoListWithSource != null)
		    	{
		    		for (String[] dto : dtoListWithSource) {
		    			String[] newTriple = new String[4];
						newTriple[0] = Class.toString();
						newTriple[1] = dto[1];
						newTriple[2] = dto[2];
						newTriple[3] = dto[3];						
						result.add(newTriple);	
						System.out.println("- Triple: \n");
						System.out.println("     "+newTriple[0]);
						System.out.println("     "+newTriple[1]);
						System.out.println("     "+newTriple[2]);
						System.out.println("     "+newTriple[3]);
					}
		    	}
		    }
		}		
		return result;
	}
	
	/**
	 * It returns the triples in the format (domain,relation,cardinality,range) of relations with exact cardianlity values.
	 * Should be more of a description here...
	 * 
	 *  @param mode: jena.ontology.InfModel
	 *  @param classURI: Class URI
	 *  
	 *  @author John Guerson
	 */
	static public List<String[]> getTuplesQualifiedCardinality(InfModel model, String classURI) {

 		System.out.println("\nExecuting getRelationsURIWithExactCardinality()...");
 		List<String[]> result = new ArrayList<String[]>();
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + model.getNsPrefixURI("") + ">" +
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
				
				" FILTER( ?source = <" + classURI + "> ) " +
			"} UNION {" +		
				"?source " + "owl:equivalentClass" + " ?blank .\n " +
				"?blank rdf:type owl:Class ."  +
				"?blank owl:intersectionOf  ?list     ." +
				"?list  rdf:rest*/rdf:first  ?member ."  +			
				" ?member " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				" ?member " + "owl:onProperty ?relation .\n" +
				" ?member " + "owl:onDataRange ?target" +
				
				" FILTER( ?source = <" + classURI + "> ) " +
			"} UNION {" +	
				" ?source " + "owl:equivalentClass" + " _:b0 .\n " +				
				" _:b0 " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				" _:b0 " + "owl:onProperty ?relation .\n" +
				" _:b0 " + "owl:onClass ?target" +	
				
				" FILTER( ?source = <" + classURI + "> ) " +
			" } UNION { " +
				" ?source " + "owl:equivalentClass" + " _:b1 .\n " +				
				" _:b1 " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				" _:b1 " + "owl:onProperty ?relation .\n" +
				" _:b1 " + "owl:onDataRange ?target" +
				
				" FILTER( ?source = <" + classURI + "> ) " +
			"}" +
				
			" UNION { " +
				"?source " + "rdfs:subClassOf" + " ?blank .\n " +
				"?blank rdf:type owl:Class ."  +
				"?blank owl:intersectionOf  ?list     ." +
				"?list  rdf:rest*/rdf:first  ?member ."  +			
				" ?member " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				" ?member " + "owl:onProperty ?relation .\n" +
				" ?member " + "owl:onClass ?target" +
				
				" FILTER( ?source = <" + classURI + "> ) " +				
			"} UNION {" +		
				"?source " + "rdfs:subClassOf" + " ?blank .\n " +
				"?blank rdf:type owl:Class ."  +
				"?blank owl:intersectionOf  ?list     ." +
				"?list  rdf:rest*/rdf:first  ?member ."  +			
				" ?member " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				" ?member " + "owl:onProperty ?relation .\n" +
				" ?member " + "owl:onDataRange ?target" +
				
				" FILTER( ?source = <" + classURI + "> ) " +
			"} UNION {" +	
				" ?source " + "rdfs:subClassOf" + " _:b2 .\n " +				
				" _:b2 " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				" _:b2 " + "owl:onProperty ?relation .\n" +
				" _:b2 " + "owl:onClass ?target" +
				
				" FILTER( ?source = <" + classURI + "> ) " +
			" } UNION { " +
				" ?source " + "rdfs:subClassOf" + " _:b3 .\n " +				
				" _:b3 " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				" _:b3 " + "owl:onProperty ?relation .\n" +
				" _:b3 " + "owl:onDataRange ?target" +
				
				" FILTER( ?source = <" + classURI + "> ) " +
			"}" +
		"}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		//ResultSetFormatter.out(System.out, results, query);
		while (results.hasNext()) 
		{			
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
		    String[] triple = new String[]{Source.toString(), Relation.toString(), Cardinality.toString().split("\\^")[0], Target.toString() };	
			System.out.println("- Triple: \n");
			System.out.println("     "+Source.toString());
			System.out.println("     "+Relation.toString());
			System.out.println("     "+Cardinality.toString().split("\\^")[0]);
			System.out.println("     "+Target.toString());
			result.add(triple);
		}		
		//sub-classes		
		queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + model.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT ?x ?y" +
		" WHERE {\n" +
			" ?x " + "rdfs:subClassOf" + " ?y .\n " +
			//" _:b0 " + "owl:Class ?y .\n" +
		"}";
		query = QueryFactory.create(queryString);
		qe = QueryExecutionFactory.create(query, model);
		results = qe.execSelect(); 
		//ResultSetFormatter.out(System.out, results, query);		
		while (results.hasNext()) 
		{			
			QuerySolution row= results.next();
		    RDFNode Class = row.get("x");
		    RDFNode SuperClass = row.get("y");		    
		    if(!Class.toString().contains(QueryUtil.w3URI) && !SuperClass.toString().contains(QueryUtil.w3URI) && Class.toString() != SuperClass.toString())
		    {		    	
		    	List<String[]> dtoListWithSource = retainOnly(result, SuperClass.toString());
		    	if(dtoListWithSource != null)
		    	{
		    		for (String[] dto : dtoListWithSource) {
		    			String[] newTriple = new String[4];
						newTriple[0] = Class.toString();
						newTriple[1] = dto[1];
						newTriple[2] = dto[2];
						newTriple[3] = dto[3];						
						result.add(newTriple);		
						System.out.println("- Triple: \n");
						System.out.println("     "+newTriple[0]);
						System.out.println("     "+newTriple[1]);
						System.out.println("     "+newTriple[2]);
						System.out.println("     "+newTriple[3]);
					}
		    	}
		    }
		}		
		return result;
	}

	/**
	 * It returns the complete classes in the format hash map <complete class, memberList>.
	 * Should be more of a description here...
	 * 
	 *  @param mode: jena.ontology.InfModel
	 *  @param classURI: Class URI
	 *  
	 *  @author John Guerson
	 */
	static public HashMap<String,List<String>> getCompleteClassesURI(String className, List<String> listClassesOfInstance, InfModel infModel)
	{
		System.out.println("\nExecuting getTuplesCompleteClasses()...");
		HashMap<String,List<String>> result = new HashMap<String,List<String>>();	
		String completeClassURI = new String();
		List<String> members = new ArrayList<String>();
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
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		ResultSet results = qe.execSelect(); 
		// ResultSetFormatter.out(System.out, results, query);		
		RDFNode blankNodeAux = null;	//Save last blank node		
		while (results.hasNext()) 
		{
			QuerySolution row= results.next();			
			RDFNode blankNode = row.get("x0");
			RDFNode completeClass = row.get("completeClass");
		    RDFNode member = row.get("member");		    
		    if(blankNodeAux == null)
		    {
		    	//first case blank node
		    	blankNodeAux = blankNode;		    	
		    	//Add here
		    	completeClassURI = completeClass.toString();
		    	//check if member are disjoint of listClassesOfInstance		    	
    			boolean ok = true;
    			List<String> listDisjointClassesOfMember = QueryUtil.getClassesURIDisjointWith(infModel,member.toString());
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
    				members.add(member.toString());    				    					
    			}		    	
		    }else{		    	
		    	if(blankNode.equals(blankNodeAux))
		    	{
		    		//we are in the same blank node, same generalization set		    		
		    		//add with not exist
		    		if(!members.contains(member.toString()))
		    		{
		    			//check if member are disjoint of listClassesOfInstance
		    			boolean ok = true;
		    			List<String> listDisjointClassesOfMember = QueryUtil.getClassesURIDisjointWith(infModel,member.toString());
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
		    				// add member
		    				members.add(member.toString());
		    			}	
		    		}		    		
		    	}else{		    		
		    		//change generalization		    		
		    		if(members.size() < 0) result.put(completeClassURI, members);
		    	
		    		//new node
		    		//get only the not disjoint possibilities		    				    		
		    		completeClassURI = completeClass.toString();
		    		members = new ArrayList<String>();
		    		
		    		//check if member are disjoint of listClassesOfInstance
	    			boolean ok = true;
	    			List<String> listDisjointClassesOfMember = QueryUtil.getClassesURIDisjointWith(infModel,member.toString());
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
	    				members.add(member.toString());	
	    			}			    	
			    	blankNodeAux = blankNode;
		    	} 	
		    }
		}		
		//the last case
		if(completeClassURI != null && !result.containsKey(completeClassURI))
		{
			if(members.size() > 0)
				result.put(completeClassURI,members);
		}
		//print out the result
		for(String key: result.keySet()){
			System.out.println("- Complete Class URI: "+key);
			for(String v: result.get(key)) System.out.println("    Member: "+v);
		}		
		return result;
	}
	

	/**
	 * 
	 * @author: Jordana Salamon
	 * @param: individual, list of relations from individual, model
	 */
	static public ArrayList<String> query_EndOfGraph(String individuo, ArrayList<String> Relacoes, InfModel model){
		// Create a new query
  		String var1 = null;
		String queryString = 
		 "PREFIX ont: <" + model.getNsPrefixURI("") + "> "
		+ "SELECT ?var" + Relacoes.size()
		+ " WHERE { ";
		if(Relacoes.size() == 1){
			var1 = "var"+Relacoes.size();
			queryString = queryString + "ont:" + individuo +  " ont:" + Relacoes.get(0) + " ?" + var1 + " }";
		}
		else {
			var1 = "var";
			int cont=1;
			queryString = queryString + "ont:" + individuo +  " ont:" + Relacoes.get(0) + " ?" + var1 +cont + ".";
			for (int i = 1; i< Relacoes.size(); i++) {
				String var2 = "var";
				int cont2=cont+1;
				queryString = queryString + "?" + var1 + cont  + " ont:" + Relacoes.get(i) + " ?" + var2 + cont2 + " .";
				cont++;
			}
			queryString = queryString + " }";
		}
		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		ArrayList<String> list = new ArrayList<String>();

		//ResultSetFormatter.out(System.out, results, query);
		
		while (results.hasNext()) {
			QuerySolution row = results.next();
		    
		    RDFNode rdfY = row.get("var"+Relacoes.size());
	    	list.add(rdfY.toString());
		}
		return list;
	}
	
	
}
