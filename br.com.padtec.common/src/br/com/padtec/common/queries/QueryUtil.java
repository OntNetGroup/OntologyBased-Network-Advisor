package br.com.padtec.common.queries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.types.OntPropertyEnum;

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

public class QueryUtil {
	
	static public String w3URI  = "http://www.w3.org/";
	static public String owl = "owl";
	static public String owlURI = "http://www.w3.org/2002/07/owl#";
	static public String rdfs = "rdfs";
	static public String rdfsURI = "http://www.w3.org/2000/01/rdf-schema#";
	static public String rdf = "rdf";
	static public String rdfURI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	static public String xml = "xsd";
	static public String xmlURI = "http://www.w3.org/2001/XMLSchema#";
	
	static public String PREFIXES = ""
			+ "PREFIX " + owl + ": <" + owlURI + ">\n"
			+ "PREFIX " + rdfs + ": <" + rdfsURI + ">\n"
			+ "PREFIX " + rdf + ": <" + rdfURI + ">\n"
			+ "PREFIX " + xml + ": <" + xmlURI + ">\n";
	
	/** 
	 * Return the URI of all classes of the ontology. This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel 
	 * 
	 * @author John Guerson
	 */
	static public boolean individualExists(InfModel model, String individualUri) 
	{		
		System.out.println("\nExecuting individualExists()...");
		String queryString = ""
				+ PREFIXES
				+ "ASK\n"
				+ "{\n"
				+ "\t<" + individualUri + "> rdf:type ?t  ;\n"
				+ "}\n";
		Query query = QueryFactory.create(queryString);
		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		boolean exist = qe.execAsk();		
		
		return exist;
	}
	
	/** 
	 * Return true if an individual is from a specific class
	 * 
	 * @param model: jena.ontology.InfModel 
	 * @param individualUri: individual URI 
	 * @param classURI: class URI 
	 * 
	 * @author Freddy Brasileiro
	 */
	static public boolean isIndividualFromClass(InfModel model, String individualUri, String classURI) 
	{		
		System.out.println("\nExecuting isIndividualFromClass()...");
		String queryString = ""
				+ PREFIXES
				+ "ASK\n"
				+ "{\n"
				+ "\t<" + individualUri + "> rdf:type <" + classURI + ">  ;\n"
				+ "}\n";
		Query query = QueryFactory.create(queryString);
		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		boolean isFromClass = qe.execAsk();		
		
		return isFromClass;
	}
	
	/** 
	 * Return true if an individual has a relation with another individual from a specific class
	 * 
	 * @param model: jena.ontology.InfModel 
	 * @param individualUri: source individual URI 
	 * @param relationURI: relation URI 
	 * @param tgtClassURI: class URI of the target individual 
	 * 
	 * @author Freddy Brasileiro
	 */
	static public boolean hasTargetIndividualFromClass(InfModel model, String srcIndividualUri, String relationURI, String tgtClassURI) 
	{		
		System.out.println("\nExecuting isTargetIndividualFromClass()...");
		String queryString = ""
				+ PREFIXES
				+ "ASK\n"
				+ "{\n"
				+ "<"+ srcIndividualUri + "> <" + relationURI + "> ?indv . \n"
				+ "	?indv rdf:type <" + tgtClassURI + "> .\n"
				+ "}\n";
		Query query = QueryFactory.create(queryString);
		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		boolean isFromClass = qe.execAsk();		
		
		return isFromClass;
	}
	
	/** 
	 * Return true if an individual has some data property with determined value
	 * 
	 * @param model: jena.ontology.InfModel 
	 * @param individualUri: source individual URI 
	 * @param relationURI: relation URI 
	 * @param tgtClassURI: class URI of the target individual 
	 * 
	 * @author Freddy Brasileiro
	 */
	static public boolean hasDataPropWithValue(InfModel model, String srcIndividualUri, String dataPropURI, String value) 
	{		
		System.out.println("\nExecuting isTargetIndividualFromClass()...");
		String queryString = ""
				+ PREFIXES
				+ "ASK\n"
				+ "WHERE {\n"
				+ "<" + srcIndividualUri + "> <" + dataPropURI + "> " + value + "\n"
				+ "}\n";
		Query query = QueryFactory.create(queryString);
		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		boolean hasDP = qe.execAsk();		
		
		return hasDP;
	}
	
	/** 
	 * Return the URI of individuals from some Class URI and related to other individual 
	 * 
	 * @param model: jena.ontology.InfModel
	 * @param knownIndvURI
	 * @param relationURI
	 * @param unknownIndvClassURI
	 * 
	 * @author Freddy
	 */
	static public List<String> getIndividualFromRelation(InfModel model, String knownIndvURI, String relationURI, String unknownIndvClassURI) 
	{		
		System.out.println("\nExecuting getIndividualFromRelation()...");
		List<String> result = new ArrayList<String>();				
		String queryString = "" 
				+ PREFIXES
				+ " SELECT *"
				+ " WHERE {\n" 		
				+	"\t{" 
				+	"\t\t<" + knownIndvURI + "> <" + relationURI + "> ?unknownIndv .\n " 	
				+    "\t\t?unknownIndv rdf:type <" + unknownIndvClassURI + "> .\n" 
				+    "\t}" 
				+    "\tUNION" 
				+    "\t{" 
				+    "\t\t?unknownIndv <" + relationURI + "> <" + knownIndvURI + "> .\n " 	
				+    "\t\t?unknownIndv rdf:type <" + unknownIndvClassURI + "> .\n" 
				+    "\t}" 
				+ "}";
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		while (results.hasNext()) 
		{			
			QuerySolution row = results.next();		    
		    RDFNode unknownIndv = row.get("unknownIndv");	
		    if(isValidURI(unknownIndv.toString()))
		    {
		    	System.out.println("- unknownIndv URI: "+unknownIndv.toString()); 
		    	result.add(unknownIndv.toString()); 
		    }
		}
		return result;
	}
	
	/** 
	 * Return the URI of types of individuals  
	 * 
	 * @param model: jena.ontology.InfModel
	 * @param indvSourceURI
	 * 
	 * @author Freddy
	 */
	static public List<String> getIndividualTypes(InfModel model, String indvSourceURI) 
	{		
		System.out.println("\nExecuting getIndividualTypes()...");
		List<String> result = new ArrayList<String>();				
		String queryString = "" 
				+ PREFIXES
				+ " SELECT *"
				+ " WHERE {\n" 		
				+    "\t<" + indvSourceURI + "> rdf:type ?type .\n" 
				+ "}";
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		while (results.hasNext()) 
		{			
			QuerySolution row = results.next();		    
		    RDFNode type = row.get("type");	
		    if(isValidURI(type.toString()))
		    {
		    	System.out.println("- type URI: "+type.toString()); 
		    	result.add(type.toString()); 
		    }
		}
		return result;
	}
	
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
		String queryString = "" 
				+ PREFIXES
				+ " SELECT *" 
				+ " WHERE {\n" 		
				+ 	" ?i rdf:type owl:Class .\n " 	
				+   " FILTER(?i NOT IN (owl:Thing, owl:Nothing)) .\n"
				+ "}";
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
	static public List<String> getClassesURIFromIndividual(InfModel model, String individualURI) 
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
		String queryString = ""
				+ PREFIXES
				+ " SELECT * \n" 
				+ " WHERE {\n" 	
				+	" <"+individualURI+"> rdf:type ?i .\n " 
				+	" ?i rdf:type owl:Class .\n" 
				+	" FILTER(?i NOT IN (owl:Thing,owl:Nothing)) .\n"
				+ "}";
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
		if(classURI.equals(checkingClassURI)){
			return false;
		}
		System.out.println("\nExecuting isClassesURIDisjoint()...");
		System.out.println("- Class URI: "+classURI);
		System.out.println("- Class URI to check: "+checkingClassURI);
		ArrayList<String> disjointClasses = getDisjointClassesURIs(model, classURI);
		for (String strClassD : disjointClasses) {
			if(checkingClassURI.equals(strClassD)) {
		    	System.out.println("- Is Disjoint: true");
		    	return true; 
		    }
		}
		return false;
	}
	
	/**
	 * Check if a class is disjoint from another. This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel 
	 * @param classURI: Class URI
	 * @param checingClassURI: Class URI to be checked
	 * 
	 * @author Freddy Brasileiro
	 */
	static public ArrayList<String> getDisjointClassesURIs(InfModel model, String classURI)
	{
		ArrayList<String> result = new ArrayList<String>();
		System.out.println("\nExecuting getDisjointClassesURIs()...");
		System.out.println("- Class URI: "+classURI);
		String queryString = 
				PREFIXES
				+ " SELECT DISTINCT ?classD \n" 
				+ " WHERE {\n" 
				+	"\t{\n" 
				+		"\t<" + classURI + "> " + "owl:disjointWith" + " ?classD .\n " 
				+	"\t}\n" 
				+	"\tUNION\n" 
				+	"\t{\n" 
				+		"\t?classD " + "owl:disjointWith" + " <" + classURI + "> .\n " 
				+	"\t}\n" 
				+	"\tUNION\n" 
				+	"\t{\n" 
				+		"\t?AllDisjointClassesNode rdf:type owl:AllDisjointClasses . \n"  
				+		"\t?AllDisjointClassesNode owl:members ?disjointMembersNode . \n" 
				+		"\t?disjointMembersNode rdf:rest*/rdf:first ?classD . \n" 
				+		"\tFILTER EXISTS { ?disjointMembersNode rdf:rest*/rdf:first <" + classURI + "> } . \n" 
				+	"\t}\n" 
				+ "}";		
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		// ResultSetFormatter.out(System.out, results, query);		
		while (results.hasNext()) 
		{			
			QuerySolution row= results.next();
		    RDFNode classD = row.get("classD");
		    result.add(classD.toString());	    	    		    
		}	
		result.remove(classURI);
		return result;
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
		String queryString = ""
				+ PREFIXES
				+ " SELECT DISTINCT *" 
				+ " WHERE {\n" 
				+		"<" + propertyURI + "> " + " rdf:type " + " ?type .\n " 
				+ "}";		
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
	 * @param objectPropertyURI: Property URI
	 * 
	 * @author Freddy Brasileiro
	 */
	static public List<String> getAllInverseOfURIs(InfModel model, String objectPropertyURI){
		System.out.println("\nExecuting getAllInverseOfURIs()...");
		System.out.println("- Object Property URI: " + objectPropertyURI);
		List<String> result = new ArrayList<String>();		
		String queryString = "" 
				+ PREFIXES
				+  " SELECT DISTINCT *\n"
				+ " WHERE\n"	
				+ "{ \n"
				+ "\t{\n"
				+ "\t\t<" + objectPropertyURI + "> owl:inverseOf ?inverse\n"
				+ "\t}\n" 
				+ "\tUNION\n"
				+ "\t{\n"
				+ "\t\t?inverse owl:inverseOf <" + objectPropertyURI + ">\n"
				+ "\t}\n"
				+ "}";
		Query query = QueryFactory.create(queryString);		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		//ResultSetFormatter.out(System.out, results, query);
		while (results.hasNext())	
		{			
			QuerySolution row= results.next();
		    RDFNode inverse = row.get("inverse");	
		    if(isValidURI(inverse.toString())){
		    	System.out.println("- Datatype Property URI: "+inverse.toString()); 
		    	result.add(inverse.toString());
		    }
		}		
		return result;
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
	static public boolean isDomainDisjointWithSome(InfModel infModel, String propertyURI, List<String> classesURI) 
	{
		//List<String> domainList = QueryUtil.getDomainURIs(infModel, propertyURI);
		List<String> domainList = QueryUtil.getDomainURIsWithSupertypes(infModel, propertyURI);
		boolean isDomainDisjointWithSome = false;
		for (String domain : domainList) 
		{						
			for (String classToCheck : classesURI)
			{
				if(QueryUtil.isClassesURIDisjoint(infModel, domain, classToCheck)){
					isDomainDisjointWithSome = true;
					break;
				}
			}			
		}
		return isDomainDisjointWithSome;
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
	static public boolean isRangeDisjointWithSome(InfModel infModel, String propertyURI, List<String> classesURI) 
	{				
		//List<String> rangeList = QueryUtil.getRangeURIs(infModel, propertyURI);
		List<String> rangeList = QueryUtil.getRangeURIsWithSupertypes(infModel, propertyURI);
		boolean isDomainDisjointWithSome = false;
		for (String range : rangeList) 
		{						
			for (String classToCheck : classesURI)
			{
				if(QueryUtil.isClassesURIDisjoint(infModel, range, classToCheck)){
					isDomainDisjointWithSome = true;
					break;
				}
			}			
		}
		return isDomainDisjointWithSome;
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
		String queryString = "" 
				+ PREFIXES
				+ " SELECT DISTINCT *" 
				+ " WHERE {\n" 
				+	"<" + individualURI + ">" + " ?property" + " ?target .\n " 
				+	" ?property " + " rdf:type" + " owl:DatatypeProperty .\n " 
				+ "}";
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
		String queryString = ""
				+ PREFIXES
				+ " SELECT DISTINCT *" 
				+ " WHERE {\n" 
				+	"<" + individualURI + ">" + " ?property" + " ?target .\n " 
				+	" ?property " + " rdf:type" + " owl:ObjectProperty .\n " 
				+ "}";
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
	
	static public List<String> getObjectPropertiesURI(InfModel model)
	{
		System.out.println("\nExecuting getPropertiesURI()...");		
		List<String> result = new ArrayList<String>();		
		String queryString = ""
				+ PREFIXES
				+ " SELECT DISTINCT *" 
				+ " WHERE {\n" 
				+	"?property " + " rdf:type " + " owl:ObjectProperty .\n " 			
				+ "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect(); 
		//ResultSetFormatter.out(System.out, results, query);
		while (results.hasNext()) 
		{			
			QuerySolution row= results.next();
			//RDFNode source = row.get("source");
		    RDFNode property = row.get("property");
		    //RDFNode target = row.get("target");
		    System.out.println("- Property URI: "+property.toString());
		    if(isValidURI(property.toString()))
		    {
		    	String triple = new String();	    	
		    	triple = property.toString();		    			    	 
		    	result.add(triple);
		    } 		    		    
		}		
		return result;
	}
	
	static public List<String> getDatatypePropertiesURI(InfModel model)
	{
		System.out.println("\nExecuting getPropertiesURI()...");		
		List<String> result = new ArrayList<String>();		
		String queryString = ""
				+ PREFIXES
				+ " SELECT DISTINCT *" 
				+ " WHERE {\n" 
				+	"?property " + " rdf:type " + " owl:DatatypeProperty .\n " 			
				+ "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect(); 
		//ResultSetFormatter.out(System.out, results, query);
		while (results.hasNext()) 
		{			
			QuerySolution row= results.next();
			//RDFNode source = row.get("source");
		    RDFNode property = row.get("property");
		    //RDFNode target = row.get("target");
		    System.out.println("- Property URI: "+property.toString());
		    if(isValidURI(property.toString()))
		    {
		    	String triple = new String();	    	
		    	triple = property.toString();		    			    	 
		    	result.add(triple);
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
				PREFIXES
				+ " SELECT DISTINCT *" 
				+ " WHERE {\n" 
				+	"{ " + "<" + individualURI + ">" + " ?property" + " ?target .\n " 
				+		" ?property " + " rdf:type" + " owl:ObjectProperty .\n " 
				+	"} UNION { " 
				+		"<" + individualURI + ">" + " ?property" + " ?target .\n " 
				+		" ?property " + " rdf:type" + " owl:DatatypeProperty.\n " 		
				+	"}" 
				+ "}";
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
	 * Return the URI of all properties (both object and datatype properties) and individuals that this individual is linked to, in the ontology. This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel 
	 * @param individualURI: Individual URI
	 * 
	 * @author Freddy Brasileiro
	 */
	static public List<DtoInstanceRelation> getPropertiesAndIndividualsURI(InfModel model, String individualURI)
	{
		System.out.println("\nExecuting getPropertiesURI()...");
		System.out.println("- Individual URI: "+individualURI);
		List<DtoInstanceRelation> result = new ArrayList<DtoInstanceRelation>();		
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		" SELECT DISTINCT *" +
		" WHERE {\n" +		
			"{ " + "<" + individualURI + ">" + " ?property" + " ?target .\n " +
				" ?property " + " rdf:type" + " owl:ObjectProperty .\n " +
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
		    RDFNode target = row.get("target");
		    if(isValidURI(property.toString())){
		    	DtoInstanceRelation dtoItem = new DtoInstanceRelation();
		    	dtoItem.Property = property.toString();
		    	dtoItem.Target = target.toString();
		    	System.out.println("- Property URI: "+property.toString()); 
		    	result.add(dtoItem);
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
		List<String> domainClassURIList = QueryUtil.getClassesURIFromIndividual(model,domainIndividualURI);
		List<String> rangeClassURIList = QueryUtil.getClassesURIFromIndividual(model,rangeIndividualURI);		
		List<String> subproperties = QueryUtil.getSubPropertiesURI(model, propertyURI, true, true);		
		for (String subPropertyURI : subproperties) 
	    {	
			boolean domainDisjointWithSome = QueryUtil.isDomainDisjointWithSome(model, subPropertyURI, domainClassURIList);
			boolean rangeDisjointWithSome = QueryUtil.isRangeDisjointWithSome(model, subPropertyURI, rangeClassURIList);		
			if(!domainDisjointWithSome && !rangeDisjointWithSome)
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
	 * Return the domain class of this property URI (it might be more than one) . This method is performed using SPARQL.
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
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
		" SELECT DISTINCT *\n" +
		" WHERE {\n" +
			"<"+propertyURI+"> rdfs:domain ?domain .\n" +
		"}\n";
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
	 * Return the domain class of this property URI (it might be more than one), including all supertypes of the domain class . This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel 
	 * @param propertyURI: Property URI
	 * 
	 * @author Freddy Brasileiro
	 */
	static public List<String> getDomainURIsWithSupertypes(InfModel model, String propertyURI)
	{
		System.out.println("\nExecuting getDomainURIsWithSupertypes()...");
		System.out.println("- Property URI: "+propertyURI);
		List<String> result = getDomainURIs(model, propertyURI);
		List<String> allSupertypes = new ArrayList<String>();
		for (String classURI : result) {
			List<String> supertypes = getSupertypesURIs(model, classURI);
			for (String supertype : supertypes) {
				if(!allSupertypes.contains(supertype)){
					allSupertypes.add(supertype);
				}
			}
		}		
		result.addAll(allSupertypes);
		return result;
	}
	
	/**
	 * Return the range class of this property URI (it might be more than one), including all supertypes of the domain class . This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel 
	 * @param propertyURI: Property URI
	 * 
	 * @author Freddy Brasileiro
	 */
	static public List<String> getRangeURIsWithSupertypes(InfModel model, String propertyURI)
	{
		System.out.println("\nExecuting getRangeURIsWithSupertypes()...");
		System.out.println("- Property URI: "+propertyURI);
		List<String> result = getRangeURIs(model, propertyURI);
		List<String> allSupertypes = new ArrayList<String>();
		for (String classURI : result) {
			List<String> supertypes = getSupertypesURIs(model, classURI);
			for (String supertype : supertypes) {
				if(!allSupertypes.contains(supertype)){
					allSupertypes.add(supertype);
				}
			}
		}		
		result.addAll(allSupertypes);
		return result;
	}
	
	/**
	 * Return superptypes of a this class URI (it might be more than one). This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel 
	 * @param classURI: Class URI
	 * 
	 * @author Freddy Brasileiro
	 */
	static public List<String> getSupertypesURIs(InfModel model, String classURI) {
		System.out.println("\nExecuting getSupertypesURIs()...");
		System.out.println("- Class URI: "+classURI);
		List<String> result = new ArrayList<String>();
		String queryString = ""
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "SELECT DISTINCT ?superClass "
				+ "WHERE {\n"
				+ "\t\t<" + classURI + "> rdfs:subClassOf*/rdfs:subClassOf ?superClass .\n"
				+ "}\n";
		
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect(); 
		while (results.hasNext()) 
		{
			QuerySolution row= results.next();
		    RDFNode superClass = row.get("superClass");
		    if(superClass.toString().contains(model.getNsPrefixURI("")) && !superClass.toString().equals(classURI)){
		    	result.add(superClass.toString());
			    System.out.println("- Domain URI: "+superClass.toString());
		    }
		    		    
		}		
		return result;
	}

	/**
	 * Return a list of possible subRelations of a subRelURI involving two individual URIs
	 * 
	 * @param model: jena.ontology.InfModel 
	 * @param indvSrcURI: individual source URI
	 * @param relationURI: relation URI
	 * @param indvTgtURI: individual target URI
	 * 
	 * @author Freddy Brasileiro
	 */
	static public List<String> getPossibleSubRelations(InfModel model, String indvSrcURI, String relationURI, String indvTgtURI) {
		System.out.println("\nExecuting getPossibleSubRelations()...");
		System.out.println("- relationURI: "+relationURI);
		List<String> result = new ArrayList<String>();
		String queryString = ""
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "SELECT DISTINCT ?subRelation \n"
				+ "WHERE {\n"
				+ "	?subRelation rdfs:subPropertyOf*/rdfs:subPropertyOf <" + relationURI + "> .\n"
				+ "	?subRelation rdfs:domain ?domain .\n"
				+ "	?subRelation rdfs:range ?range .\n"
				+ "	<" + indvSrcURI + "> <" + relationURI + "> <" + indvTgtURI + "> .\n"
				+ "	<" + indvSrcURI + "> rdf:type ?domain .\n"
				+ "	<" + indvTgtURI + "> rdf:type ?range .\n"
				+ "}";
		
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect(); 
		while (results.hasNext()) 
		{
			QuerySolution row = results.next();
		    RDFNode subRelation = row.get("subRelation");
		    if(subRelation.toString().contains(model.getNsPrefixURI(""))){
		    	result.add(subRelation.toString());
			    System.out.println("- subRelation URI: "+subRelation.toString());
		    }
		    		    
		}		
		return result;
	}

	/**
	 * Return the first domain class of this property URI (since it might be more than one) . This method is performed using SPARQL.
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
	 * Return the range class of this property URI (it might be more than one) . This method is performed using SPARQL.
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
	 * Return the first  range class of this property URI (since it might be more than one) . This method is performed using SPARQL.
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
	
	static public List<String[]> getDomainAndRangeURI(InfModel model, String propertyURI) 
	{		
		List<String[]> list = new ArrayList<String[]>();		
		String queryString =		
		" SELECT *" +
		" WHERE {\n" +		
			" ?source <" + propertyURI + "> ?target .\n " +	
		"}";
		Query query = QueryFactory.create(queryString);
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		while (results.hasNext()) 
		{
			String [] tupla = new String[2];
			QuerySolution row = results.next();		    
		    RDFNode source = row.get("source");
		    RDFNode target = row.get("target");		    
		    tupla[0] = source.toString();
		    tupla[1] = target.toString();	
		    list.add(tupla);
		}
		return list;		
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
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
		" SELECT DISTINCT * \n" +
		" WHERE {\n" +		
			"\t{ \n" + 
				"\t\t<" + individualURI + "> owl:differentFrom" + " ?differentIndividual .\n " +
			"\t} UNION { \n" +
				"\t\t ?differentIndividual owl:differentFrom <" + individualURI + "> .\n " +
			"\t } \n" +
			"\tUNION\n" +
			"\t{\n" +
				"\t\t?AllDifferentNode rdf:type owl:AllDifferent ." +
				"\t\t?AllDifferentNode owl:distinctMembers ?distinctMembers ." + 
				"\t\t?distinctMembers rdf:rest*/rdf:first ?differentIndividual ." +
				"\t\tFILTER EXISTS { ?distinctMembers rdf:rest*/rdf:first <" + individualURI + "> } . " +
			"\t}\n" +
		"}\n";
		Query query = QueryFactory.create(queryString);		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		//ResultSetFormatter.out(System.out, results, query);		
		while (results.hasNext()) 
		{
			QuerySolution row = results.next();		    
		    RDFNode differentIndividual = row.get("differentIndividual");
		    if(! individualURI.equals(differentIndividual.toString()))
		    {
		    	list.add(differentIndividual.toString());
				System.out.println("- Different Individual URI: "+differentIndividual.toString());
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
	 * @param rangeClassURI: Range Class URI
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
		" SELECT DISTINCT *" +
		" WHERE {\n" +		
			"<" + individualURI + ">" + "<"+propertyURI+">"+ " ?target .\n "+
			" ?target" + " rdf:type*/rdfs:subClassOf" + " <"+ rangeClassURI + "> .\n " +
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
	 * Return all the individuals URI that are in the domain of the given propertyURI as instance of domainClassURI and connected to the individualURI.
	 * This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel
	 * @param individualURI: Individual URI
	 * @param propertyURI: Property URI
	 * @param domainClassURI: Domain Class URI
	 * 
	 * @author Freddy Brasileiro
	 */
	static public List<String> getIndividualsURIAtObjectPropertyDomain(InfModel model, String individualURI, String propertyURI, String domainClassURI)
	{
		System.out.println("\nExecuting getIndividualsURIAtPropertyRange()...");
		System.out.println("- Individual URI: "+individualURI);
		System.out.println("- Property URI: "+propertyURI);
		System.out.println("- Range Class URI: "+domainClassURI);
		List<String> result = new ArrayList<String>();		
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
		"SELECT DISTINCT *\n" +
		"WHERE {\n" +		
			" ?domain " + "<"+propertyURI+"> " + "<" + individualURI + ">.\n" +
			" ?domain" + " rdf:type*/rdfs:subClassOf" + " <"+ domainClassURI + "> .\n" +
		"}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect(); 
		//ResultSetFormatter.out(System.out, results, query);
		while (results.hasNext()) 
		{			
			QuerySolution row= results.next();
		    RDFNode property = row.get("domain");
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
			if(triple[0].equals(classURI)) resultList.add(triple);			
		}
		return resultList;
	}
	
	/**
	 * It returns the definitions of properties with maximum cardianlity values.
	 * Should be more of a description here...
	 * 
	 *  @param mode: jena.ontology.InfModel
	 *  @param classURI: Class URI
	 *  
	 *  @author John Guerson
	 */
	static public List<String[]> getMaxCardinalityDefinitions(InfModel model, String classURI) 
	{
		System.out.println("\nExecuting getMaxCardinalityDefinitions()...");
		List<String[]> result = new ArrayList<String[]>();
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
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
		    	if((dtoListWithSource != null) && (dtoListWithSource.size() != 0))
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
	 * It returns the definitions of properties with exact cardinality values.
	 * Should be more of a description here...
	 * 
	 *  @param mode: jena.ontology.InfModel
	 *  @param classURI: Class URI
	 *  
	 *  @author John Guerson
	 */
	static public List<String[]> getExactlyCardinalityDefinitions(InfModel model, String classURI) {

 		System.out.println("\nExecuting getExactlyCardinalityDefinitions()...");
 		List<String[]> result = new ArrayList<String[]>();
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n " +
		" SELECT DISTINCT ?source ?relation ?cardinality ?target\n" +
		" WHERE {\n" +				
			" \t{ \n" +
				"\t\t?source " + "owl:equivalentClass" + " ?blank .\n " +
				"\t\t?blank rdf:type owl:Class .\n"  +
				"\t\t?blank owl:intersectionOf  ?list     .\n" +
				"\t\t?list  rdf:rest*/rdf:first  ?member .\n"  +			
				"\t\t ?member " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				"\t\t ?member " + "owl:onProperty ?relation .\n" +
				"\t\t ?member " + "owl:onClass ?target\n" +
				
				"\t\t FILTER( ?source = <" + classURI + "> ) \n" +
			"\t} UNION {\n" +		
				"\t\t?source " + "owl:equivalentClass" + " ?blank .\n " +
				"\t\t?blank rdf:type owl:Class .\n"  +
				"\t\t?blank owl:intersectionOf  ?list     .\n" +
				"\t\t?list  rdf:rest*/rdf:first  ?member .\n"  +			
				"\t\t ?member " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				"\t\t ?member " + "owl:onProperty ?relation .\n" +
				"\t\t ?member " + "owl:onDataRange ?target\n" +
				
				"\t\t FILTER( ?source = <" + classURI + "> ) \n" +
			"\t} UNION {\n" +	
				"\t\t ?source " + "owl:equivalentClass" + " _:b0 .\n \n" +				
				"\t\t _:b0 " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				"\t\t _:b0 " + "owl:onProperty ?relation .\n" +
				"\t\t _:b0 " + "owl:onClass ?target\n" +	
				
				"\t\t FILTER( ?source = <" + classURI + "> ) \n" +
			"\t } UNION { \n" +
				"\t\t ?source " + "owl:equivalentClass" + " _:b1 .\n " +				
				"\t\t _:b1 " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				"\t\t _:b1 " + "owl:onProperty ?relation .\n" +
				"\t\t _:b1 " + "owl:onDataRange ?target\n" +
				
				"\t\t FILTER( ?source = <" + classURI + "> ) \n" +
			"\t} UNION \n{ " +
				"\t\t?source " + "rdfs:subClassOf" + " ?blank .\n " +
				"\t\t?blank rdf:type owl:Class .\n"  +
				"\t\t?blank owl:intersectionOf  ?list     .\n" +
				"\t\t?list  rdf:rest*/rdf:first  ?member .\n"  +			
				"\t\t ?member " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				"\t\t ?member " + "owl:onProperty ?relation .\n" +
				"\t\t ?member " + "owl:onClass ?target\n" +
				
				"\t\t FILTER( ?source = <" + classURI + "> ) \n" +				
			"\t} UNION {\n" +		
				"\t\t?source " + "rdfs:subClassOf" + " ?blank .\n " +
				"\t\t?blank rdf:type owl:Class .\n"  +
				"\t\t?blank owl:intersectionOf  ?list     .\n" +
				"\t\t?list  rdf:rest*/rdf:first  ?member .\n"  +			
				"\t\t ?member " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				"\t\t ?member " + "owl:onProperty ?relation .\n" +
				"\t\t ?member " + "owl:onDataRange ?target\n" +
				
				"\t\t FILTER( ?source = <" + classURI + "> ) \n" +
			"\t} UNION {" +	
				"\t\t ?source " + "rdfs:subClassOf" + " _:b2 .\n " +				
				"\t\t _:b2 " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				"\t\t _:b2 " + "owl:onProperty ?relation .\n" +
				"\t\t _:b2 " + "owl:onClass ?target\n" +
				
				"\t\t FILTER( ?source = <" + classURI + "> ) \n" +
			"\t } UNION { \n" +
				"\t\t ?source " + "rdfs:subClassOf" + " _:b3 .\n " +				
				"\t\t _:b3 " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				"\t\t _:b3 " + "owl:onProperty ?relation .\n" +
				"\t\t _:b3 " + "owl:onDataRange ?targe\n" +
				
				"\t\t FILTER( ?source = <" + classURI + "> ) \n" +
			"\t}\n" +
		"}\n";
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
		    	if((dtoListWithSource != null) && (dtoListWithSource.size()>0))
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
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
		" SELECT DISTINCT ?x0 ?completeClass ?member\n" +
		" WHERE {\n" +
			"\t{ \n" +		
				"\t\t?completeClass owl:equivalentClass ?cls .\n" +
				"\t\t?cls owl:intersectionOf ?nodeFather.\n" +
				//one level
				"\t\t?nodeFather ?r1 ?x0 .\n"+
				"\t\t?x0 owl:unionOf  ?list .\n"+
				"\t\t?list  rdf:rest*/rdf:first  ?member .\n" +
				"\t\t FILTER( ?completeClass = <" + className + "> ) \n" +			
			"\t} UNION {\n" +		
				"\t\t?completeClass owl:equivalentClass ?cls .\n" +
				"\t\t?cls owl:intersectionOf ?nodeFather.\n" +
				//two levels
				"\t\t?nodeFather ?r2 ?x1 .\n" +
				"\t\t?x1 ?r1 ?x0 .\n" +
				"\t\t?x0 owl:unionOf  ?list .\n" +
				"\t\t?list  rdf:rest*/rdf:first  ?member .\n" +
				"\t\t FILTER( ?completeClass = <" + className + "> ) \n" +			
			"\t} UNION {\n" +							
				"\t\t?completeClass owl:equivalentClass ?x0 .\n" +			
				//zero levels
				"\t\t?x0 owl:unionOf  ?list .\n" +			
				"\t\t?list  rdf:rest*/rdf:first  ?member .\n" +
				"\t\t FILTER( ?completeClass = <" + className + "> ) \n" +			
			"\t}\n" + 
		"}\n";
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
    			
    			//List<String> listDisjointClassesOfMember = QueryUtil.getClassesURIDisjointWith(infModel,member.toString());
    			List<String> listDisjointClassesOfMember = QueryUtil.getDisjointClassesURIs(infModel, member.toString());
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
		    			//List<String> listDisjointClassesOfMember = QueryUtil.getClassesURIDisjointWith(infModel,member.toString());
		    			List<String> listDisjointClassesOfMember = QueryUtil.getDisjointClassesURIs(infModel, member.toString());
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
	    			List<String> listDisjointClassesOfMember = QueryUtil.getDisjointClassesURIs(infModel, member.toString());
	    			//List<String> listDisjointClassesOfMember = QueryUtil.getClassesURIDisjointWith(infModel,member.toString());
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
	 * It returns the end of the rdf/owl graph given the individual and the relations ahead of him 
	 * @author: Jordana Salamon
	 * @param: individual, list of relations from individual, model
	 */
	static public ArrayList<String> endOfGraph(InfModel model, String individualName, ArrayList<String> relationsNameList){
		// Create a new query
  		String var1 = null;
		String queryString = 
		 "PREFIX ont: <" + model.getNsPrefixURI("") + "> "
		+ "SELECT ?var" + relationsNameList.size()
		+ " WHERE { ";
		if(relationsNameList.size() == 1){
			var1 = "var"+relationsNameList.size();
			queryString = queryString + "ont:" + individualName.substring(individualName.indexOf("#")+1) +  " ont:" + relationsNameList.get(0) + " ?" + var1 + " }";
		}
		else {
			var1 = "var";
			int cont=1;
			queryString = queryString + "ont:" + individualName.substring(individualName.indexOf("#")+1) +  " ont:" + relationsNameList.get(0) + " ?" + var1 +cont + ".";
			for (int i = 1; i< relationsNameList.size(); i++) {
				String var2 = "var";
				int cont2=cont+1;
				queryString = queryString + "?" + var1 + cont  + " ont:" + relationsNameList.get(i) + " ?" + var2 + cont2 + " .";
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
		    
		    RDFNode rdfY = row.get("var"+relationsNameList.size());
	    	list.add(rdfY.toString());
		}
		return list;
	}
	
	/**
	 * It returns the end of the rdf/owl graph given the individual, the relations ahead of him and the ranges of the relations 
	 * @author: Jordana Salamon
	 * @param: individual, list of relations from individual, list of ranges of the relations, model
	 */
	static public ArrayList<String> endOfGraphWithRanges(InfModel model, String individualName, ArrayList<String> relationsNameList, ArrayList<String> rangesNameList){
  		// Create a new query
  		String var1 = null;
  		String queryString = 
  		"PREFIX ont: <" + model.getNsPrefixURI("") + "> " +
  		 "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
  		+ "SELECT ?var" + relationsNameList.size()
  		+ " WHERE { ";
  		if((relationsNameList.size() == 1) && (rangesNameList.size() == 1) && (rangesNameList.get(0) != " ")){
  			var1 = "var"+relationsNameList.size();
  			queryString = queryString + "ont:" + individualName.substring(individualName.indexOf("#")+1) +  " ont:" + relationsNameList.get(0) + " ?" + var1 + ".";
  			queryString = queryString + "?" + var1 +  " rdf:type" + " ont:" + rangesNameList.get(0) + "." + " }";
  		}
  		else {
  			var1 = "var";
  			int cont=1;
  			queryString = queryString + " ont:" + individualName.substring(individualName.indexOf("#")+1) +  " ont:" + relationsNameList.get(0) + " ?" + var1 +cont + " .";
  			if(rangesNameList.get(0) != " "){
  				queryString = queryString + " ?" + var1+cont +  " rdf:type" + " ont:" + rangesNameList.get(0) + " .";
  			}
  			for (int i = 1; i< relationsNameList.size(); i++) {
  				String var2 = "var";
  				int cont2=cont+1;
  				queryString = queryString + " ?" + var1 + cont  + " ont:" + relationsNameList.get(i) + " ?" + var2 + cont2 + " .";
  				if(rangesNameList.get(i) != " "){
  	  				queryString = queryString + " ?" + var2 + cont2 +  " rdf:type" + " ont:" + rangesNameList.get(i) + " .";
  	  			}
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
  		    
  		    RDFNode rdfY = row.get("var"+relationsNameList.size());
  	    	list.add(rdfY.toString());
  		}
  		return list;
  		}

	public static List<String> getAllSuperProperties(InfModel model, String objectPropertyURI){
		System.out.println("\nExecuting getAllSuperObjectProperties()...");
		System.out.println("- Object Property URI: " + objectPropertyURI);
		List<String> result = new ArrayList<String>();		
		String queryString = "" 
		+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
		+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
		+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
		+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
		+ "SELECT DISTINCT *"
		+ "WHERE "
		+ "{ "
		+ "\t<" + objectPropertyURI + "> rdfs:subPropertyOf*/rdfs:subPropertyOf ?superOP "
		+ "}";
		Query query = QueryFactory.create(queryString);		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		//ResultSetFormatter.out(System.out, results, query);
		while (results.hasNext())	
		{			
			QuerySolution row= results.next();
		    RDFNode superOP = row.get("superOP");	
		    if(isValidURI(superOP.toString())){
		    	System.out.println("- Object Property URI: "+superOP.toString()); 
		    	result.add(superOP.toString());
		    }
		}		
		return result;
	}
	
	public static boolean hasSubClass(InfModel model, String classID){
		String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "PREFIX ont: <http://nemo.inf.ufes.br/NewProject.owl#> "

				+ "ASK "
				+ "WHERE { ?subject rdfs:subClassOf <" + classID + "> ."
				+ "}";
		
		Query query = QueryFactory.create(queryString);
		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		boolean hasSubClass = qe.execAsk();		
		return hasSubClass;
	}
	
	public static ArrayList<String> SubClass(InfModel model, String classID){
		String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "PREFIX ont: <http://nemo.inf.ufes.br/NewProject.owl#> "

				+ "SELECT ?subject "
				+ "WHERE { ?subject rdfs:subClassOf <" + classID + "> ."
				+ "}";
		
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		
		ArrayList<String> SubClass = new ArrayList<String>();
		while (results.hasNext())	
		{			
			QuerySolution row= results.next();
		    RDFNode x = row.get("subject");	
		    	SubClass.add(x.toString());
		}			
		return SubClass;
	}
	
	/**
	 * It returns the definitions of properties with maximum cardianlity values.
	 * Should be more of a description here...
	 * 
	 *  @param mode: jena.ontology.InfModel
	 *  @param classURI: Class URI
	 *  
	 *  @author Jordana Salamon
	 */
	static public List<String[]> getMaxCardinalityDefinitionsClassTarget(InfModel model, String classURI) 
	{
		System.out.println("\nExecuting getMaxCardinalityDefinitions()...");
		List<String[]> result = new ArrayList<String[]>();
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
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
				
				" FILTER( ?target = <" + classURI + "> ) " +
			"} UNION {" +		
				"?source " + "owl:equivalentClass" + " ?blank .\n " +
				"?blank rdf:type owl:Class ."  +
				"?blank owl:intersectionOf  ?list     ." +
				"?list  rdf:rest*/rdf:first  ?member ."  +			
				" ?member " + "owl:maxQualifiedCardinality" + " ?cardinality .\n " +
				" ?member " + "owl:onProperty ?relation .\n" +
				" ?member " + "owl:onDataRange ?target" +
				
				" FILTER( ?target = <" + classURI + "> ) " +
			"} UNION {" +		
				" ?source " + "owl:equivalentClass" + " _:b0 .\n " +				
				" _:b0 " + "owl:maxQualifiedCardinality" + " ?cardinality .\n " +
				" _:b0 " + "owl:onProperty ?relation .\n" +
				" _:b0 " + "owl:onClass ?target" +	
				
				" FILTER( ?target = <" + classURI + "> ) " +
			" } UNION { " +
				" ?source " + "owl:equivalentClass" + " _:b1 .\n " +				
				" _:b1 " + "owl:maxQualifiedCardinality" + " ?cardinality .\n " +
				" _:b1 " + "owl:onProperty ?relation .\n" +
				" _:b1 " + "owl:onDataRange ?target" +
				
				" FILTER( ?target = <" + classURI + "> ) " +
			"}" +
				
			" UNION { " +
				"?source " + "rdfs:subClassOf" + " ?blank .\n " +
				"?blank rdf:type owl:Class ."  +
				"?blank owl:intersectionOf  ?list     ." +
				"?list  rdf:rest*/rdf:first  ?member ."  +			
				" ?member " + "owl:maxQualifiedCardinality" + " ?cardinality .\n " +
				" ?member " + "owl:onProperty ?relation .\n" +
				" ?member " + "owl:onClass ?target" +	
				
				" FILTER( ?target = <" + classURI + "> ) " +
			"} UNION {" +		
				"?source " + "rdfs:subClassOf" + " ?blank .\n " +
				"?blank rdf:type owl:Class ."  +
				"?blank owl:intersectionOf  ?list     ." +
				"?list  rdf:rest*/rdf:first  ?member ."  +			
				" ?member " + "owl:maxQualifiedCardinality" + " ?cardinality .\n " +
				" ?member " + "owl:onProperty ?relation .\n" +
				" ?member " + "owl:onDataRange ?target" +
				
				" FILTER( ?target = <" + classURI + "> ) " +
			"} UNION {" +		
				" ?source " + "rdfs:subClassOf" + " _:b2 .\n " +				
				" _:b2 " + "owl:maxQualifiedCardinality" + " ?cardinality .\n " +
				" _:b2 " + "owl:onProperty ?relation .\n" +
				" _:b2 " + "owl:onClass ?target" +	
				
				" FILTER( ?target = <" + classURI + "> ) " +
			" } UNION { " +
				" ?source " + "rdfs:subClassOf" + " _:b3 .\n " +				
				" _:b3 " + "owl:maxQualifiedCardinality" + " ?cardinality .\n " +
				" _:b3 " + "owl:onProperty ?relation .\n" +
				" _:b3 " + "owl:onDataRange ?target" +
				
				" FILTER( ?target = <" + classURI + "> ) " +
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
		    	if((dtoListWithSource != null) && (dtoListWithSource.size() != 0))
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
	 * It returns the definitions of properties with exact cardinality values.
	 * Should be more of a description here...
	 * 
	 *  @param mode: jena.ontology.InfModel
	 *  @param classURI: Class URI
	 *  
	 *  @author Jordana Salamon
	 */
	static public List<String[]> getExactlyCardinalityDefinitionsClassTarget(InfModel model, String classURI) {

 		System.out.println("\nExecuting getExactlyCardinalityDefinitions()...");
 		List<String[]> result = new ArrayList<String[]>();
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n " +
		" SELECT DISTINCT ?source ?relation ?cardinality ?target\n" +
		" WHERE {\n" +				
			" \t{ \n" +
				"\t\t?source " + "owl:equivalentClass" + " ?blank .\n " +
				"\t\t?blank rdf:type owl:Class .\n"  +
				"\t\t?blank owl:intersectionOf  ?list     .\n" +
				"\t\t?list  rdf:rest*/rdf:first  ?member .\n"  +			
				"\t\t ?member " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				"\t\t ?member " + "owl:onProperty ?relation .\n" +
				"\t\t ?member " + "owl:onClass ?target\n" +
				
				"\t\t FILTER( ?target = <" + classURI + "> ) \n" +
			"\t} UNION {\n" +		
				"\t\t?source " + "owl:equivalentClass" + " ?blank .\n " +
				"\t\t?blank rdf:type owl:Class .\n"  +
				"\t\t?blank owl:intersectionOf  ?list     .\n" +
				"\t\t?list  rdf:rest*/rdf:first  ?member .\n"  +			
				"\t\t ?member " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				"\t\t ?member " + "owl:onProperty ?relation .\n" +
				"\t\t ?member " + "owl:onDataRange ?target\n" +
				
				"\t\t FILTER( ?target = <" + classURI + "> ) \n" +
			"\t} UNION {\n" +	
				"\t\t ?source " + "owl:equivalentClass" + " _:b0 .\n \n" +				
				"\t\t _:b0 " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				"\t\t _:b0 " + "owl:onProperty ?relation .\n" +
				"\t\t _:b0 " + "owl:onClass ?target\n" +	
				
				"\t\t FILTER( ?target = <" + classURI + "> ) \n" +
			"\t } UNION { \n" +
				"\t\t ?source " + "owl:equivalentClass" + " _:b1 .\n " +				
				"\t\t _:b1 " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				"\t\t _:b1 " + "owl:onProperty ?relation .\n" +
				"\t\t _:b1 " + "owl:onDataRange ?target\n" +
				
				"\t\t FILTER( ?target = <" + classURI + "> ) \n" +
			"\t} UNION \n{ " +
				"\t\t?source " + "rdfs:subClassOf" + " ?blank .\n " +
				"\t\t?blank rdf:type owl:Class .\n"  +
				"\t\t?blank owl:intersectionOf  ?list     .\n" +
				"\t\t?list  rdf:rest*/rdf:first  ?member .\n"  +			
				"\t\t ?member " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				"\t\t ?member " + "owl:onProperty ?relation .\n" +
				"\t\t ?member " + "owl:onClass ?target\n" +
				
				"\t\t FILTER( ?target = <" + classURI + "> ) \n" +				
			"\t} UNION {\n" +		
				"\t\t?source " + "rdfs:subClassOf" + " ?blank .\n " +
				"\t\t?blank rdf:type owl:Class .\n"  +
				"\t\t?blank owl:intersectionOf  ?list     .\n" +
				"\t\t?list  rdf:rest*/rdf:first  ?member .\n"  +			
				"\t\t ?member " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				"\t\t ?member " + "owl:onProperty ?relation .\n" +
				"\t\t ?member " + "owl:onDataRange ?target\n" +
				
				"\t\t FILTER( ?target = <" + classURI + "> ) \n" +
			"\t} UNION {" +	
				"\t\t ?source " + "rdfs:subClassOf" + " _:b2 .\n " +				
				"\t\t _:b2 " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				"\t\t _:b2 " + "owl:onProperty ?relation .\n" +
				"\t\t _:b2 " + "owl:onClass ?target\n" +
				
				"\t\t FILTER( ?t = <" + classURI + "> ) \n" +
			"\t } UNION { \n" +
				"\t\t ?source " + "rdfs:subClassOf" + " _:b3 .\n " +				
				"\t\t _:b3 " + "owl:qualifiedCardinality" + " ?cardinality .\n " +
				"\t\t _:b3 " + "owl:onProperty ?relation .\n" +
				"\t\t _:b3 " + "owl:onDataRange ?targe\n" +
				
				"\t\t FILTER( ?target = <" + classURI + "> ) \n" +
			"\t}\n" +
		"}\n";
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
		    	if((dtoListWithSource != null) && (dtoListWithSource.size()>0))
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

}
