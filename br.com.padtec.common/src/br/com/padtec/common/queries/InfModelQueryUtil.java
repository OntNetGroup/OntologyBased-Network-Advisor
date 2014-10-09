package br.com.padtec.common.queries;

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
		List<String> domainList = InfModelQueryUtil.getDomainURIs(infModel, propertyURI);
		boolean isDomainDisjointWithAll = true;
		for (String domain : domainList) 
		{						
			for (String classToCheck : classesURI)
			{
				if(!InfModelQueryUtil.isClassesURIDisjoint(infModel, domain, classToCheck)) isDomainDisjointWithAll = false;							
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
		List<String> rangeList = InfModelQueryUtil.getRangeURIs(infModel, propertyURI);
		boolean isDomainDisjointWithAll = true;
		for (String range : rangeList) 
		{						
			for (String classToCheck : classesURI)
			{
				if(!InfModelQueryUtil.isClassesURIDisjoint(infModel, range, classToCheck)) isDomainDisjointWithAll = false;							
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
		    if(subProp.toString().contains(model.getNsPrefixURI("")) && (!subProp.toString().equals(propertyURI)))
		    {
		    	result.add(subProp.toString());
		    	System.out.println("- SubProperty URI: "+subProp.toString());
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
		List<String> domainClassURIList = InfModelQueryUtil.getClassesURI(model,domainIndividualURI);
		List<String> rangeClassURIList = InfModelQueryUtil.getClassesURI(model,rangeIndividualURI);		
		List<String> subproperties = InfModelQueryUtil.getSubPropertiesURI(model, propertyURI, true, true);
		for (String subPropertyURI : subproperties) 
	    {
			boolean disjointDomain = InfModelQueryUtil.isDomainDisjointWithAll(model,subPropertyURI, domainClassURIList);
			boolean disjointRange = InfModelQueryUtil.isRangeDisjointWithAll(model,subPropertyURI, rangeClassURIList);
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
		List<String> listValues = InfModelQueryUtil.getIndividualsURIAtObjectPropertyRange(model, individualURI, propertyURI, rangeClassURI);		
		ArrayList<String> uniqueListValues = new ArrayList<String>();
		uniqueListValues.addAll(listValues);		
		for (String value : listValues)
		{			
			if(uniqueListValues.contains(value))
			{
				List<String> sameInstances = InfModelQueryUtil.getIndividualsURISameAs(model, value);
				for (String sameIns : sameInstances)
				{
					if(uniqueListValues.contains(sameIns)) uniqueListValues.remove(sameIns);
				}	
			}			
		}		
		counter = uniqueListValues.size();	
		System.out.println("- Count for Object Individuals: "+counter);
		List<String> dataTypeValues = InfModelQueryUtil.getIndividualsURIAtDataTypePropertyRange(model, individualURI, propertyURI, rangeClassURI);
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
		List<String> individualList = InfModelQueryUtil.getIndividualsURIAtObjectPropertyRange(model, individualURI, relationURI, rangeClassURI);
		if(individualList.size()>0) { return true; }						
		individualList = InfModelQueryUtil.getIndividualsURIAtDataTypePropertyRange(model, individualURI, relationURI, rangeClassURI);
		if(individualList.size()>0) { return true; }	
		return false;
	}
		
		
}
