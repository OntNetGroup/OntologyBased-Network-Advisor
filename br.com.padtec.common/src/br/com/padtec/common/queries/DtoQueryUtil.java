package br.com.padtec.common.queries;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import br.com.padtec.common.dto.DtoDefinitionClass;
import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.types.OntCardinalityEnum;
import br.com.padtec.common.types.OntPropertyEnum;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;

public class DtoQueryUtil {

	public static List<DtoInstance> getIndividualsFromClass(InfModel model, String className)
	{
		List<String> classNamesWithoutNameSpace = new ArrayList<String>();
		
		classNamesWithoutNameSpace.add(className);
		
		List<DtoInstance> result = getIndividualsFromClasses(model, classNamesWithoutNameSpace);
		
		return result;
	}
	
	public static List<DtoInstance> getIndividualsFromClasses(InfModel model, List<String> classNamesList)
	{
		List<DtoInstance> result = new ArrayList<DtoInstance>();		
		String ns = model.getNsPrefixURI("");
		for (String className : classNamesList)
		{
			List<String> individuals = QueryUtil.getIndividualsURI(model, ns+className);
			for (String indv : individuals) 
			{
				DtoInstance dtoIndv = DtoQueryUtil.getIndividualByName(model, indv, true, false, false);
				if(!result.contains(dtoIndv)) result.add(dtoIndv);
			}		
		}		
		return result;
	}
	
	/** 
	 * Return a individual by its name.
	 * It returns also all the classes of an individual as well as all the other individuals different and the same as this one.
	 *  
	 * @author Freddy Brasileiro
	 *
	 * @param model Identify in which model you are searching
	 * @param individualURI Identify which the URI (with namespace) from the individual you are searching
	 * @param clsEager Defines when the classes of an individual must be got eagerly 
	 * @param diffFromEager Defines when the "different from individuals" of an individual must be got eagerly
	 * @param sameAsEager Defines when the "same as individuals" of an individual must be got eagerly
	 */
	public static DtoInstance getIndividualByName(InfModel model, String individualURI, Boolean classesEager, Boolean diffFromEager, Boolean sameAsEager)
	{
		if(!individualURI.contains("#")){ throw new RuntimeException("Entity namespace problem. The " + individualURI +" have to followed by \"#\"."); }
		List<String> classesURIList = null;
		List<String> diffURIList = null;
		List<String> sameAsURIList = null;
		if(classesEager)
		{
			System.out.println();
			System.out.print("Getting classes eagerly");
			classesURIList = QueryUtil.getClassesURIFromIndividual(model, individualURI);
		}
		if(diffFromEager)
		{
			System.out.println();
			System.out.print("Getting \"different from individuals\" eagerly");
			diffURIList = QueryUtil.getIndividualsURIDifferentFrom(model, individualURI);
		}    		
		if(sameAsEager)
		{
			System.out.println();
			System.out.print("Getting \"same as individuals\" eagerly");
			sameAsURIList = QueryUtil.getIndividualsURISameAs(model, individualURI);
		}    		
		String nameSpace = individualURI.split("#")[0] + "#";
		String name = individualURI.split("#")[1];		
		DtoInstance individual = new DtoInstance(nameSpace, name, classesURIList, diffURIList, sameAsURIList, true);		
		return individual;
	}
		
	/** 
	 * Return the list of all individuals from the ontology.
	 * It returns also all the classes of an individual as well as all the other individuals different and the same as this one.
	 *  
	 * @throws OKCoNameSpaceException
	 * 
	 * @author John Guerson
	 * 
	 * @param model
	 * @param clsEager Defines when the classes of an individual must be got eagerly 
	 * @param diffFromEager Defines when the "different from individuals" of an individual must be got eagerly
	 * @param sameAsEager Defines when the "same as individuals" of an individual must be got eagerly
	 */
	static public List<DtoInstance> getIndividuals(InfModel model, Boolean classesEager, Boolean diffFromEager, Boolean sameAsEager) throws RuntimeException 
	{		
		List<DtoInstance> result = new ArrayList<DtoInstance>();				
		List<String> individualsURIList = QueryUtil.getIndividualsURIFromAllClasses(model);		
    	for (String indivURI : individualsURIList)
    	{    		    		
    		DtoInstance individual = getIndividualByName(model, indivURI, classesEager, diffFromEager, sameAsEager);
    		result.add(individual);
		}		
		return result;
	}
	
	/** 
	 * Return all the relations from a particular individual from the ontology.
	 * In other words, the relations in which this individual is the source object.
	 * This method also returns the first range(target) of these relations.
	 * 
	 * @author John Guerson
	 */
	static public List<DtoInstanceRelation> getRelationsFrom(InfModel model, String individualURI)
	{		
		List<DtoInstanceRelation> result = new ArrayList<DtoInstanceRelation>();
		List<String> propertiesURIList = QueryUtil.getPropertiesURI(model, individualURI);
		for(String propertyURI: propertiesURIList)
		{
			List<String> rangeIndividuals = QueryUtil.getRangeIndividualURI(model, individualURI, propertyURI);
			for (String rngIdv : rangeIndividuals) 
			{
				DtoInstanceRelation dtoItem = new DtoInstanceRelation();
			    dtoItem.Property = propertyURI;
			    dtoItem.Target = rngIdv;
			    if(!result.contains(dtoItem)){
			    	result.add(dtoItem);
			    }			    
			}
			
		}
		return result;
	}	
	
	static public List<DtoInstanceRelation> getRelationsFrom(InfModel model, DtoInstance dtoIndividual)
	{	
		return getRelationsFrom(model,dtoIndividual.ns+dtoIndividual.name);
	}
	
	/** 
	 * Return all the relations from/to a particular individual from the ontology.
	 * In other words, the relations in which this individual is the source and target object.
	 * This method also returns the first range(target) of these relations.
	 * 
	 * @author John Guerson
	 */
	public static List<DtoInstanceRelation> getRelationsFromAndTo(InfModel model, String individualURI)
	{
		List<DtoInstanceRelation> result = getRelationsFrom(model, individualURI);
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + model.getNsPrefixURI("") + ">" +
		" SELECT DISTINCT *" +
		" WHERE {\n" +		
			"{ " + " ?domain " + " ?property " + "<" + individualURI + ">" + " .\n " +
				" ?property " + " rdf:type" + " owl:ObjectProperty .\n " +
			"} " +
		"}";
		Query query = QueryFactory.create(queryString);		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		//ResultSetFormatter.out(System.out, results, query);
		DtoInstanceRelation dtoItem = null;		
		while (results.hasNext()) 
		{			
			QuerySolution row= results.next();
		    ResourceImpl property = (ResourceImpl) row.get("property");
		    String propertyUri = property.getURI();		    
		    propertyUri = propertyUri.replace(property.getNameSpace(), "");		    
		    if(propertyUri.startsWith("INV.")) propertyUri.replaceFirst("INV.", "");
		    else propertyUri = "INV." + propertyUri;		    
		    propertyUri = property.getNameSpace() + propertyUri;		    
		    RDFNode domain = row.get("domain");		    
		    dtoItem = new DtoInstanceRelation();
		    dtoItem.Property = propertyUri;		    
		    /**since I change the relation name (including or removing the "INV." prefix), the domain result changes to target*/
		    dtoItem.Target = domain.toString();		    
		    if(!result.contains(dtoItem))
		    {
		    	result.add(dtoItem);
		    }					    		    		    
		}		
		return result;
	}
	
	/**
	 * It returns the cardinality definitions of a property.
	 * Should be more of a description here...
	 * 
	 *  @param mode: jena.ontology.InfModel
	 *  @param classURI: Class URI
	 *  
	 *  @author Freddy Brasileiro
	 */
	static public List<DtoDefinitionClass> getSomeCardinalityDefinitionsFrom(InfModel model, String classURI) 
	{
		System.out.println("\nExecuting getSomeCardinalityDefinitions()...");
		List<DtoDefinitionClass> result = new ArrayList<DtoDefinitionClass>();
		//classes
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
		"PREFIX ns: <" + model.getNsPrefixURI("") + ">\n" +
		" SELECT DISTINCT ?source ?property ?target ?propertyType\n" +
		" WHERE {\n" +			
			" \t{ \n" +
				"\t\t ?source " + "owl:equivalentClass" + " ?blank .\n " +
				"\t\t ?blank rdf:type owl:Class .\n"  +
				"\t\t ?blank owl:intersectionOf  ?list  .\n" +
				"\t\t ?list  rdf:rest*/rdf:first  ?member . \n"  +			
				"\t\t ?member " + "owl:" + OntCardinalityEnum.SOME.getOwlAxiom() + " ?target .\n " +
				"\t\t ?member " + "owl:onProperty ?property .\n" +	
				"\t\t ?property rdf:type ?propertyType ." +
				"\t\t FILTER( ?source = <" + classURI + "> ) \n" +
			"\t} UNION {\n" +		
				"\t\t ?source " + "owl:equivalentClass" + " _:b0 .\n " +				
				"\t\t _:b0 " + "owl:" + OntCardinalityEnum.SOME.getOwlAxiom() + " ?target .\n " +
				"\t\t _:b0 " + "owl:onProperty ?property .\n" +
				"\t\t ?property rdf:type ?propertyType ." +
				"\t\t FILTER( ?source = <" + classURI + "> ) \n" +
			"\t }UNION { \n" +
				"\t\t ?source " + "rdfs:subClassOf" + " ?blank .\n " +
				"\t\t ?blank rdf:type owl:Class .\n"  +
				"\t\t ?blank owl:intersectionOf  ?list  .\n" +
				"\t\t ?list  rdf:rest*/rdf:first  ?member . \n"  +			
				"\t\t ?member " + "owl:" + OntCardinalityEnum.SOME.getOwlAxiom() + " ?target .\n " +
				"\t\t ?member " + "owl:onProperty ?property .\n" +	
				"\t\t ?property rdf:type ?propertyType ." +
				"\t\t FILTER( ?source = <" + classURI + "> ) \n" +
			"\t} UNION {\n" +		
				"\t\t ?source " + "rdfs:subClassOf" + " _:b1 .\n " +				
				"\t\t _:b1 " + "owl:" + OntCardinalityEnum.SOME.getOwlAxiom() + " ?target .\n " +
				"\t\t _:b1 " + "owl:onProperty ?property .\n" +
				"\t\t ?property rdf:type ?propertyType ." +
				"\t\t FILTER( ?source = <" + classURI + "> ) \n" +
			" }\n" +			
		
		"}";		
		Query query = QueryFactory.create(queryString);		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		//ResultSetFormatter.out(System.out, results, query);		
		while (results.hasNext()) 
		{
			QuerySolution row= results.next();
		    RDFNode source = row.get("source");
		    RDFNode relation = row.get("property");
		    RDFNode target = row.get("target");		
		    RDFNode propertyType = row.get("propertyType");
		    String targetStr = target.toString();
		    DtoDefinitionClass defClass = new DtoDefinitionClass();
		    defClass.Source = source.toString();
		    if(propertyType.toString().contains("ObjectProperty")){
		    	defClass.PropertyType = OntPropertyEnum.OBJECT_PROPERTY;
		    }else{
		    	defClass.PropertyType = OntPropertyEnum.DATA_PROPERTY;
		    }		    
		    defClass.Relation = relation.toString();
			try {
				defClass.uriRelationEncoded = URLEncoder.encode(defClass.Relation, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}	
		    defClass.Target = target.toString();
		    try {
				defClass.uriTargetEncoded = URLEncoder.encode(defClass.Target, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		    defClass.TypeCompletness = OntCardinalityEnum.SOME;
		    if(Character.isDigit(targetStr.charAt(0))) 
		    {
		    	String cardinality = targetStr.split("http")[0];
		    	cardinality = cardinality.substring(0, cardinality.length()-2); 
		    	defClass.Cardinality = cardinality;
		    }		    
			System.out.println("- Triple: \n");
			System.out.println("     "+source.toString());
			System.out.println("     "+relation.toString());
			System.out.println("     "+target.toString());
			if(!result.contains(defClass)){
				result.add(defClass);				
			}
			
		}	
		/*
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
		*/
		return result;
	}
	
	/**
	 * It returns the definitions of properties with min cardianlity values.
	 * Should be more of a description here...
	 * 
	 *  @param mode: jena.ontology.InfModel
	 *  @param classURI: Class URI
	 *  
	 *  @author John Guerson
	 * @throws OKCoException 
	 */
	
	static public List<DtoDefinitionClass> getCardinalityDefinitionsFrom(InfModel model, String classURI, OntCardinalityEnum typeCompletness) 
	{
		if(typeCompletness.equals(OntCardinalityEnum.SOME)) return new ArrayList<DtoDefinitionClass>();
		System.out.println("\nExecuting getCardinalityDefinitionsFrom()...");
		System.out.println("\nClass: " + classURI);
		System.out.println("\ttypeCompletness: " + typeCompletness);
		List<DtoDefinitionClass> result = new ArrayList<DtoDefinitionClass>();		
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
		"PREFIX ns: <" + model.getNsPrefixURI("") + ">\n" +
		" SELECT DISTINCT ?source ?relation ?cardinality ?target ?propertyType\n" +
		" WHERE {\n" +
			" \t{ \n" +
				"\t\t?source " + "owl:equivalentClass" + " ?blank .\n " +
				"\t\t?blank rdf:type owl:Class .\n"  +
				"\t\t?blank owl:intersectionOf  ?list  .\n" +
				"\t\t?list  rdf:rest*/rdf:first  ?member .\n"  +			
				"\t\t ?member " + "owl:" + typeCompletness.getOwlAxiom() + " ?cardinality .\n " +
				"\t\t ?member " + "owl:onProperty ?relation .\n" +
				"\t\t ?relation rdf:type ?propertyType .\n" +
				"\t\t ?member " + "owl:onClass ?target\n" +		
				
				"\t\t FILTER( ?source = <" + classURI + "> )\n" +
			"\t} UNION {\n" +		
				"\t\t?source " + "owl:equivalentClass" + " ?blank .\n " +
				"\t\t?blank rdf:type owl:Class .\n"  +
				"\t\t?blank owl:intersectionOf  ?list     .\n" +
				"\t\t?list  rdf:rest*/rdf:first  ?member .\n"  +			
				"\t\t ?member " + "owl:" + typeCompletness.getOwlAxiom() + " ?cardinality .\n " +
				"\t\t ?member " + "owl:onProperty ?relation .\n" +
				"\t\t ?relation rdf:type ?propertyType .\n" +
				"\t\t ?member " + "owl:onDataRange ?target\n" +
				
				"\t\t FILTER( ?source = <" + classURI + "> ) \n" +
			"\t} UNION {\n" +	
				"\t\t ?source " + "owl:equivalentClass" + " _:b0 .\n " +				
				"\t\t _:b0 " + "owl:" + typeCompletness.getOwlAxiom() + " ?cardinality .\n " +
				"\t\t _:b0 " + "owl:onProperty ?relation .\n" +
				"\t\t ?relation rdf:type ?propertyType .\n" +
				"\t\t _:b0 " + "owl:onClass ?target\n" +
				
				"\t\t FILTER( ?source = <" + classURI + "> ) \n" +
			"\t } UNION { \n" +
				"\t\t ?source " + "owl:equivalentClass" + " _:b1 .\n " +				
				"\t\t _:b1 " + "owl:" + typeCompletness.getOwlAxiom() + " ?cardinality .\n " +
				"\t\t _:b1 " + "owl:onProperty ?relation .\n" +
				"\t\t ?relation rdf:type ?propertyType .\n" +
				"\t\t _:b1 " + "owl:onDataRange ?target\n" +
				
				"\t\t FILTER( ?source = <" + classURI + "> ) \n" +
			"\t}UNION { \n" +
				"\t\t?source " + "rdfs:subClassOf" + " ?blank .\n " +
				"\t\t?blank rdf:type owl:Class .\n"  +
				"\t\t?blank owl:intersectionOf  ?list  .\n" +
				"\t\t?list  rdf:rest*/rdf:first  ?member .\n"  +			
				"\t\t ?member " + "owl:" + typeCompletness.getOwlAxiom() + " ?cardinality .\n " +
				"\t\t ?member " + "owl:onProperty ?relation .\n" +
				"\t\t ?relation rdf:type ?propertyType .\n" +
				"\t\t ?member " + "owl:onClass ?target\n" +		
				
				"\t\t FILTER( ?source = <" + classURI + "> ) \n" +
			"\t} UNION {\n" +		
				"\t\t?source " + "rdfs:subClassOf" + " ?blank .\n " +
				"\t\t?blank rdf:type owl:Class .\n"  +
				"\t\t?blank owl:intersectionOf  ?list     .\n" +
				"\t\t?list  rdf:rest*/rdf:first  ?member .\n"  +			
				"\t\t ?member " + "owl:" + typeCompletness.getOwlAxiom() + " ?cardinality .\n " +
				"\t\t ?member " + "owl:onProperty ?relation .\n" +
				"\t\t ?relation rdf:type ?propertyType .\n" +
				"\t\t ?member " + "owl:onDataRange ?target\n" +
				
				"\t\t FILTER( ?source = <" + classURI + "> ) \n" +
			"\t} UNION {\n" +	
				"\t\t ?source " + "rdfs:subClassOf" + " _:b2 .\n " +				
				"\t\t _:b2 " + "owl:" + typeCompletness.getOwlAxiom() + " ?cardinality .\n " +
				"\t\t _:b2 " + "owl:onProperty ?relation .\n" +
				"\t\t ?relation rdf:type ?propertyType .\n" +
				"\t\t _:b2 " + "owl:onClass ?target\n" +	
				
				"\t\t FILTER( ?source = <" + classURI + "> ) \n" +
			"\t } UNION { \n" +
				"\t\t ?source " + "rdfs:subClassOf" + " _:b3 .\n " +				
				"\t\t _:b3 " + "owl:" + typeCompletness.getOwlAxiom() + " ?cardinality .\n " +
				"\t\t _:b3 " + "owl:onProperty ?relation .\n" +
				"\t\t ?relation rdf:type ?propertyType .\n" +
				"\t\t _:b3 " + "owl:onDataRange ?target\n" +
				
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
		    RDFNode source = row.get("source");
		    RDFNode relation = row.get("relation");
		    RDFNode cardinality = row.get("cardinality");
		    RDFNode target = row.get("target");
		    RDFNode propertyType = row.get("propertyType");
		    DtoDefinitionClass defClass = new DtoDefinitionClass();
		    defClass.Source = source.toString();
		    if(propertyType.toString().contains("ObjectProperty")){
		    	defClass.PropertyType = OntPropertyEnum.OBJECT_PROPERTY;
		    }else{
		    	defClass.PropertyType = OntPropertyEnum.DATA_PROPERTY;
		    }		    
		    defClass.Relation = relation.toString();
		    try {
				defClass.uriRelationEncoded = URLEncoder.encode(defClass.Relation, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		    defClass.Target = target.toString();
		    try {
				defClass.uriTargetEncoded = URLEncoder.encode(defClass.Target, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		    defClass.TypeCompletness = OntCardinalityEnum.SOME;
		    String cardinalityStr = cardinality.toString();
		    if(Character.isDigit(cardinalityStr.charAt(0))) 
		    {
		    	cardinalityStr = cardinalityStr.split("http")[0];
		    	cardinalityStr = cardinalityStr.substring(0, cardinalityStr.length()-2); 
		    	
		    }
		    defClass.Cardinality = cardinalityStr;	    
			System.out.println("- Triple: \n");
			System.out.println("     "+source.toString());
			System.out.println("     "+relation.toString());
			System.out.println("     "+target.toString());
			if(!result.contains(defClass)){
				result.add(defClass);				
			}
		}		
		/*
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
		*/
		return result;
	}

	/**
	 * Return all the individuals transfer objects that are in the range of the given propertyURI as instance of rangeClassURI and connected to the individualURI.
	 * This method is performed using SPARQL.
	 * 
	 * @param model: jena.ontology.InfModel
	 * @param individualURI: Individual URI
	 * @param propertyURI: Property URI
	 * @param rangeClassURI: Rande Class URI
	 * 
	 * @author John Guerson
	 */
	static public List<DtoInstance> getIndividualsAtObjectPropertyRange(InfModel model, String individualURI, String propertyURI, String rangeClassURI)
	{
		List<DtoInstance> result = new ArrayList<DtoInstance>();
		List<String> individualsURIs = QueryUtil.getIndividualsURIAtObjectPropertyRange(model, individualURI, propertyURI,rangeClassURI);
		for(String uri: individualsURIs)
		{
			DtoInstance dtoInstance = getIndividualByName(model, uri,true,true,true);
			if(dtoInstance!=null) result.add(dtoInstance);
		}
		return result;
	}		
}
