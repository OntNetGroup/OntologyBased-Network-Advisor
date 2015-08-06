package br.com.padtec.common.factory;

import java.util.ArrayList;
import java.util.List;

import br.com.padtec.common.queries.QueryUtil;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDF;

public class FactoryUtil {
	public ArrayList<Statement> stmts = new ArrayList<Statement>();
	List<String> newIndividuals = new ArrayList<String>();
	
	public List<String> getNewIndividuals() {
		return newIndividuals;
	}
	
	public boolean newIndividualsContains(String individualURI) {
		return newIndividuals.contains(individualURI);
	}
	
	public void addNewIndividual(String individualURI){
		this.newIndividuals.add(individualURI);
	}
	/**
	 * Create an Instance of Data Property as from an Individual and enforce all dataTypeURI's super DP.
	 * 
	 * @param model: OntModel
	 * @param individualURI: new Individual URI
	 * @param classURI: class URI
	 * 
	 * @author Freddy Brasileiro
	 */
	static public void createInstanceAttribute(OntModel model, String individualURI, String dataPropertyURI, String value, String typeURI)
	{			
		createInstanceAttribute(model, individualURI, dataPropertyURI, value, typeURI, true);
	}
	
	/**
	 * Create an Instance of Data Property as from an Individual and enforce all dataTypeURI's super DP.
	 * 
	 * @param model: OntModel
	 * @param individualURI: new Individual URI
	 * @param classURI: class URI
	 * @param forceSuperTypes: force all super types
	 * 
	 * @author Freddy Brasileiro
	 */
	static public void createInstanceAttribute(OntModel model, String individualURI, String dataPropertyURI, String value, String typeURI, boolean forceSuperDP)
	{			
		//create the individualURI as from classURI
		Individual individual = model.getIndividual(individualURI);
		DatatypeProperty dataProperty = model.getDatatypeProperty(dataPropertyURI);
		Literal literal = model.createTypedLiteral(value, typeURI);
		individual.addLiteral(dataProperty, literal);
		
		if(forceSuperDP){
			enforceInstAttrSuper(model, individualURI, dataPropertyURI, value, typeURI);
		}		
	}
	
	/**
	 * Create an Instance of Data Property as from an Individual and enforce all dataTypeURI's super DP.
	 * 
	 * @param model: OntModel
	 * @param individualURI: new Individual URI
	 * @param classURI: class URI
	 * @param forceSuperTypes: force all super types
	 * 
	 * @author Freddy Brasileiro
	 */
	public void createInstanceAttributeStatement(OntModel model, String individualURI, String dataPropertyURI, String value, String typeURI)
	{			
		//create the individualURI as from classURI
		Individual individual = model.getIndividual(individualURI);
		DatatypeProperty dataProperty = model.getDatatypeProperty(dataPropertyURI);
		Literal literal = model.createTypedLiteral(value, typeURI);
		if(individual == null || dataProperty == null || literal == null){
			System.out.println();
		}
		individual.addLiteral(dataProperty, literal);
		Statement stmt = model.createStatement(individual, dataProperty, literal);
		this.stmts.add(stmt);				
	}
	
	/**
	 * Enforce all super types of an individualURI from a classURI's.
	 * 
	 * @param model: OntModel
	 * @param individualURI: new Individual URI
	 * @param classURI: class URI
	 * 
	 * @author Freddy Brasileiro
	 */
	static public void enforceInstAttrSuper(OntModel model, String individualURI, String dataPropertyURI, String value, String typeURI){
		//create the individualURI as from classURI
		Individual individual = model.getIndividual(individualURI);
		Literal literal = model.createTypedLiteral(value, typeURI);
		//also set the individualURI as from all super types of classURI
		List<String> superDPs = QueryUtil.getAllSuperProperties(model, dataPropertyURI);
		for (String superDP : superDPs) {
			DatatypeProperty superDataProperty = model.getDatatypeProperty(superDP);
			if(superDataProperty != null){
				individual.addLiteral(superDataProperty, literal);
			}				
		}
	}	
		
	/**
	 * Create an Individual as from some classURI and from all classURI's super types.
	 * 
	 * @param model: OntModel
	 * @param individualURI: new Individual URI
	 * @param classURI: class URI
	 * 
	 * @author Freddy Brasileiro
	 * @throws Exception 
	 */
	static public Individual createInstanceIndividual(OntModel model, String individualURI, String classURI) throws Exception
	{			
		return createInstanceIndividual(model, individualURI, classURI, true);		
	}
	
	/**
	 * Create an Individual as from some classURI and from all classURI's super types.
	 * 
	 * @param model: OntModel
	 * @param individualURI: new Individual URI
	 * @param classURI: class URI
	 * 
	 * @author Freddy Brasileiro
	 */
	static public void createAllDifferent(OntModel model, List<String> individualURIs)
	{		
		System.out.println("\nExecuting createAllDifferent()...");
		System.out.println("\nNumber of individuals: " + individualURIs.size() + "...");
		
		RDFNode[] nodes = new RDFNode[individualURIs.size()];
				
		for (int i = 0; i < individualURIs.size(); i++) {
			Individual indv = model.getIndividual(individualURIs.get(i));
			nodes[i] = indv;
		}
		RDFList rdfList = model.createList(nodes);
		
		model.createAllDifferent(rdfList);
	}
		
	/**
	 * Create an Individual as from some classURI and from all classURI's super types.
	 * 
	 * @param model: OntModel
	 * @param individualURI: new Individual URI
	 * @param classURI: class URI
	 * @param forceSuperTypes: force all super types
	 * 
	 * @author Freddy Brasileiro
	 * @throws Exception 
	 */
	static public Individual createInstanceIndividual(OntModel model, String individualURI, String classURI, boolean forceSuperTypes) throws Exception
	{			
		isValid(model, individualURI);
		
		System.out.println("\nExecuting createInstanceIndividual(" + individualURI + ")...");
		//create the individualURI as from classURI
		OntClass ontClass = model.getOntClass(classURI);
		Individual individual = model.createIndividual(individualURI, ontClass);
		individual.addOntClass(ontClass);
		
		if(forceSuperTypes){
			enforceInstIndvSuperTypes(model, individualURI, classURI);
		}		
		
		return individual;
	}
	
	/**
	 * Create an Individual as from some classURI and from all classURI's super types.
	 * 
	 * @param model: OntModel
	 * @param individualURI: new Individual URI
	 * @param classURI: class URI
	 * @param forceSuperTypes: force all super types
	 * 
	 * @author Freddy Brasileiro
	 * @throws Exception 
	 */
	public void createInstanceIndividualStatement(OntModel model, String individualURI, String classURI, boolean forceSuperTypes) throws Exception
	{			
//		System.out.println("\nExecuting createInstanceIndividualStatement(" + individualURI + ")...");
		//create the individualURI as from classURI
		Resource ontClass = model.createResource(classURI);
		Resource individual = model.createResource(individualURI);
		
		Statement stmt = model.createStatement(individual, RDF.type, ontClass);
		this.stmts.add(stmt);
		
		if(forceSuperTypes){
			List<String> superTypes = QueryUtil.getSupertypesURIs(model, classURI);
			for (String superType : superTypes) {
				Resource superClass = model.createResource(superType);
				Statement stmtSuper = model.createStatement(individual, RDF.type, superClass);
				this.stmts.add(stmtSuper);
			}
		}		
	}
	
	/**
	 * Enforce all super types of an individualURI from a classURI's.
	 * 
	 * @param model: OntModel
	 * @param individualURI: new Individual URI
	 * @param classURI: class URI
	 * 
	 * @author Freddy Brasileiro
	 */
	static public void enforceInstIndvSuperTypes(OntModel model, String individualURI, String classURI){
		//also set the individualURI as from all super types of classURI
		List<String> superTypes = QueryUtil.getSupertypesURIs(model, classURI);
		for (String superType : superTypes) {
			if(superType.contains("Thing")){
				System.out.println();
			}
			OntClass superClass = model.getOntClass(superType);
			Individual individual = model.createIndividual(individualURI, superClass);
			individual.addOntClass(superClass);
		}
	}
	
	/**
	 * Create an object property between two individuals, and all super OP and all inverses between the same two individuals.
	 * 
	 * @param model: OntModel
	 * @param indvSourceURI: source individual URI
	 * @param objectPropertyURI: object property URI
	 * @param indvTargetURI: target individual URI
	 * 
	 * @author Freddy Brasileiro
	 * @throws Exception 
	 */
	static public void createInstanceRelation(OntModel model, String indvSourceURI, String objectPropertyURI, String indvTargetURI) throws Exception{
		
		createInstanceRelation(model, indvSourceURI, objectPropertyURI, indvTargetURI, true, true, true);
	}
	
	static public boolean isValid(OntModel model, String elemURI) throws Exception
	{
		if(elemURI!=null && model != null){
			String ns = model.getNsPrefixURI("");
			if(ns!=null){
				if(elemURI.trim().equals(ns.trim())) throw new Exception("An element name cannot be empty.");
				if(elemURI.trim().equals(ns.trim()+"null")) throw new Exception("An element name cannot be null.");
			}
			if(elemURI.contains(" ")) throw new Exception("An element name cannot have a space character: "+elemURI.replace(model.getNsPrefixURI(""),""));
			return true;
		}
		if(model==null) throw new Exception("No ontology recognized at the creation of a new element. ");
		if(elemURI==null) throw new Exception("Cannot create a new element with a Null URI.");
		return false;
	}
	
	/**
	 * Create an object property between two individuals, and all super OP and all inverses between the same two individuals.
	 * 
	 * @param model: OntModel
	 * @param indvSourceURI: source individual URI
	 * @param objectPropertyURI: object property URI
	 * @param indvTargetURI: target individual URI
	 * @param forceSuperObjProp: force all super object properties
	 * @param forceInverses: force all inverses
	 * 
	 * @author Freddy Brasileiro
	 * @throws Exception 
	 */
	static public void createInstanceRelation(OntModel model, String indvSourceURI, String objectPropertyURI, String indvTargetURI, boolean forceSuperObjProp, boolean forceInverses, boolean forceCandSubRelation) throws Exception
	{			
		if(model == null){
			throw new Exception("The model is null.");
		}
		
		isValid(model, indvSourceURI);
		isValid(model, indvTargetURI);
		isValid(model, objectPropertyURI);

		System.out.println("\n"
							+ "Executing createInstanceRelation(" 
							+ indvSourceURI.replace(model.getNsPrefixURI(""), "") 
							+ ", " 
							+ objectPropertyURI.replace(model.getNsPrefixURI(""), "") 
							+ ", " 
							+ indvTargetURI.replace(model.getNsPrefixURI(""), "") 
							+ ")...");
		//Create an object property between two individuals
		Individual indvSource = model.getIndividual(indvSourceURI);
		Individual indvTarget = model.getIndividual(indvTargetURI);
		ObjectProperty objProp = model.getObjectProperty(objectPropertyURI);
		String ns = model.getNsPrefixURI("");
		if(indvSource == null){
			throw new Exception("Check your specification. The individual " + indvSourceURI.replace(ns, "") + " is UNDECLARED.");
		}else if(indvTarget == null){
			throw new Exception("Check your specification. The individual " + indvTargetURI.replace(ns, "") + " is UNDECLARED.");
		}else if(objProp == null){
			throw new Exception("Check your specification. The relation " + objectPropertyURI.replace(ns, "") + " is UNDECLARED.");
		}
		
		Statement stmt = model.createStatement(indvSource, objProp, indvTarget);
		model.add(stmt);
		
		if(forceInverses){
			enforceInstRelInv(model, indvSourceURI, objectPropertyURI, indvTargetURI);
		}		
		
		if(forceSuperObjProp){
			enforceInstRelSuper(model, indvSourceURI, objectPropertyURI, indvTargetURI, forceInverses);
		}
		
		if(forceCandSubRelation){
			enforceSubRelCandidates(model, indvSourceURI, objectPropertyURI, indvTargetURI, forceInverses);
		}
	}
	
	/**
	 * Create an object property between two individuals, and all super OP and all inverses between the same two individuals.
	 * 
	 * @param model: OntModel
	 * @param indvSourceURI: source individual URI
	 * @param objectPropertyURI: object property URI
	 * @param indvTargetURI: target individual URI
	 * @param forceSuperObjProp: force all super object properties
	 * @param forceInverses: force all inverses
	 * 
	 * @author Freddy Brasileiro
	 * @throws Exception 
	 */
	public void createInstanceRelationStatement(OntModel model, String indvSourceURI, String objectPropertyURI, String indvTargetURI, boolean forceCandSubRelation) throws Exception
	{			
		if(model == null){
			throw new Exception("The model is null.");
		}
		
		System.out.println("\n"
							+ "Executing createInstanceRelationStatement(" 
							+ indvSourceURI.replace(model.getNsPrefixURI(""), "") 
							+ ", " 
							+ objectPropertyURI.replace(model.getNsPrefixURI(""), "") 
							+ ", " 
							+ indvTargetURI.replace(model.getNsPrefixURI(""), "") 
							+ ")...");
		//Create an object property between two individuals
		Resource indvSource = model.createResource(indvSourceURI);
		Resource indvTarget = model.createResource(indvTargetURI);
		Property objProp = model.createProperty(objectPropertyURI);
		String ns = model.getNsPrefixURI("");
		if(indvSource == null){
			throw new Exception("Check your specification. The individual " + indvSourceURI.replace(ns, "") + " is UNDECLARED.");
		}else if(indvTarget == null){
			throw new Exception("Check your specification. The individual " + indvTargetURI.replace(ns, "") + " is UNDECLARED.");
		}else if(objProp == null){
			throw new Exception("Check your specification. The relation " + objectPropertyURI.replace(ns, "") + " is UNDECLARED.");
		}
		
		Statement stmt = model.createStatement(indvSource, objProp, indvTarget);
//		model.add(stmt);
		this.stmts.add(stmt);
		
		if(forceCandSubRelation){
			//create all super OP between the same two individuals
			List<String> candSubOPUris = QueryUtil.getPossibleSubRelations(model, indvSourceURI, objectPropertyURI, indvTargetURI);
			for (String candSubOPURI : candSubOPUris) {
				ObjectProperty candSubOP = model.getObjectProperty(candSubOPURI);
				if(candSubOP != null){
					Statement superStmt = model.createStatement(indvSource, candSubOP, indvTarget);
//					model.add(superStmt);
					this.stmts.add(superStmt);					
				}
			}
		}
	}
	
	/**
	 * Enforce super relations of an objectPropertyURI
	 * 
	 * @param model: OntModel
	 * @param indvSourceURI: source individual URI
	 * @param objectPropertyURI: object property URI
	 * @param indvTargetURI: target individual URI
	 * 
	 * @author Freddy Brasileiro
	 */
	static public void enforceInstRelSuper(OntModel model, String indvSourceURI, String objectPropertyURI, String indvTargetURI){
		enforceInstRelSuper(model, indvSourceURI, objectPropertyURI, indvTargetURI, true);
	}
	
	/**
	 * Enforce super relations of an objectPropertyURI
	 * 
	 * @param model: OntModel
	 * @param indvSourceURI: source individual URI
	 * @param objectPropertyURI: object property URI
	 * @param indvTargetURI: target individual URI
	 * 
	 * @author Freddy Brasileiro
	 */
	static public void enforceInstRelSuper(OntModel model, String indvSourceURI, String objectPropertyURI, String indvTargetURI, boolean forceInverses){
		Individual indvSource = model.getIndividual(indvSourceURI);
		Individual indvTarget = model.getIndividual(indvTargetURI);
		//create all super OP between the same two individuals
		List<String> superOPUris = QueryUtil.getAllSuperProperties(model, objectPropertyURI);
		for (String superOPURI : superOPUris) {
			ObjectProperty superOP = model.getObjectProperty(superOPURI);
			if(superOP != null){
				Statement superStmt = model.createStatement(indvSource, superOP, indvTarget);
				model.add(superStmt);
				
				if(forceInverses){
					enforceInstRelInv(model, indvSourceURI, superOPURI, indvTargetURI);
				}
			}
		}
	}
	
	/**
	 * Enforce candidates for sub-relations of an objectPropertyURI
	 * 
	 * @param model: OntModel
	 * @param indvSourceURI: source individual URI
	 * @param objectPropertyURI: object property URI
	 * @param indvTargetURI: target individual URI
	 * 
	 * @author Freddy Brasileiro
	 */
	static public void enforceSubRelCandidates(OntModel model, String indvSourceURI, String objectPropertyURI, String indvTargetURI, boolean forceInverses){
		Individual indvSource = model.getIndividual(indvSourceURI);
		Individual indvTarget = model.getIndividual(indvTargetURI);
		//create all super OP between the same two individuals
		List<String> candSubOPUris = QueryUtil.getPossibleSubRelations(model, indvSourceURI, objectPropertyURI, indvTargetURI);
		for (String candSubOPURI : candSubOPUris) {
			ObjectProperty candSubOP = model.getObjectProperty(candSubOPURI);
			if(candSubOP != null){
				Statement superStmt = model.createStatement(indvSource, candSubOP, indvTarget);
				model.add(superStmt);
				
				if(forceInverses){
					enforceInstRelInv(model, indvSourceURI, candSubOPURI, indvTargetURI);
				}
			}
		}
	}
	
	/**
	 * Enforce inverses of an objectPropertyURI
	 * 
	 * @param model: OntModel
	 * @param indvSourceURI: source individual URI
	 * @param objectPropertyURI: object property URI
	 * @param indvTargetURI: target individual URI
	 * 
	 * @author Freddy Brasileiro
	 */
	static public void enforceInstRelInv(OntModel model, String indvSourceURI, String objectPropertyURI, String indvTargetURI){
		Individual indvSource = model.getIndividual(indvSourceURI);
		Individual indvTarget = model.getIndividual(indvTargetURI);
		//create all inverses of the OP
		List<String> inverses = QueryUtil.getAllInverseOfURIs(model, objectPropertyURI);
		for (String invURI : inverses) {
			ObjectProperty invOP = model.getObjectProperty(invURI);
			Statement invStmt = model.createStatement(indvTarget, invOP, indvSource);
			model.add(invStmt);
		}
	}
	
	/**
	 * Create Statement. 
	 * 
	 * @param model: OntModel
	 * @param subjectURI 
	 * @param predicateURI
	 * @param objectURI 
	 * 
	 * @return
	 * @author John Guerson
	 */
	static public OntModel createStatement(OntModel model, String subjectURI, String predicateURI, String objectURI)
	{
		Individual a = null, b=null;
		if(objectURI!=null) a = model.getIndividual(objectURI);
		if(subjectURI!=null) b = model.getIndividual(subjectURI);				
		ObjectProperty rel = model.getObjectProperty(predicateURI);
		if(a!=null && b!=null)
		{
			Statement stmt = model.createStatement(b, rel, a);
			model.add(stmt);
		}
		return model;
	}
	
	//=========================================================================
	// These below are used at the OKCo application
	//=========================================================================
	
	/**
	 * Create Individual. Used at the OKCo application.
	 * 
	 * @param model: OntModel
	 * @param newIndividual: new Individual URI
	 * @param sameAsList: Same As List of the new Individual URI
	 * @param diffList: Diff List of the new Individual URI
	 * @param srcIndividualURI: source individual URI
	 * @param relationURI: relation URI 
	 * @param rangeClassURI: range class URI
	 * 
	 * @author John Guerson
	 */
	@Deprecated
	static public OntModel createIndividual(OntModel model, String individualURI, List<String> sameAsList, List<String> diffList, String srcIndividualURI, String relationURI, String rangeClassURI)
	{			
		Individual indInstance = model.getIndividual(srcIndividualURI);
		OntClass ClassImage = model.getOntClass(rangeClassURI);
		Property relation = model.getProperty(relationURI);		
		Individual newInstance = ClassImage.createIndividual(individualURI.replaceAll(" ", ""));		
		for (String s : diffList) 
		{
			Individual i = model.getIndividual(s);
			if(i!=null) i.setDifferentFrom(newInstance);
		}		
		for (String s : sameAsList) 
		{
			Individual i = model.getIndividual(s);
			if(i!=null) i.setSameAs(newInstance);
		}		
		indInstance.addProperty(relation, newInstance);				
		return model;
	}
	
	/**
	 * Delete Individual.
	 * 
	 * @param model: OntModel
	 * @param instance: Individual data transfer object
	 * 
	 * @author John Guerson
	 */
	static public OntModel deleteIndividual(OntModel model, String individualURI) 
	{
		Individual ind = model.getIndividual(individualURI);
		ind.remove();		
		ind = model.getIndividual(individualURI);
		return model;
	}
	
	/**
	 * Update Individual. Used at the OKCo application.
	 * 
	 * @param model: OntModel
	 * @param newIndividual: new Individual URI
	 * @param sameAsList: Same As List of the new Individual URI
	 * @param diffList: Diff List of the new Individual URI
	 * 
	 * @author John Guerson
	 */
	@Deprecated
	static public OntModel updateIndividual(OntModel model, String individualURI, List<String> sameAsList, List<String> diffList) 
	{				
		Individual indInstance = model.getIndividual(individualURI);		
		for (String s : QueryUtil.getIndividualsURIDifferentFrom(model, individualURI)) 
		{
			Individual i = model.getIndividual(s);
			indInstance.removeDifferentFrom(i);
		}
		for (String s : QueryUtil.getIndividualsURISameAs(model,individualURI)) 
		{
			Individual i = model.getIndividual(s);
			indInstance.removeSameAs(i);
		}		
		for (String s : diffList) 
		{
			Individual i = model.getIndividual(s);
			if(i!=null) i.setDifferentFrom(indInstance);
		}		
		for (String s : sameAsList) 
		{
			Individual i = model.getIndividual(s);
			if(i!=null) i.setSameAs(indInstance);
		}					
		return model;
	}
		
	/**
	 * Create a value at the range of the data property relation. Used at the OKCo application.
	 * 
	 * @param model: OntModel
	 * @param value: value
	 * @param individualURI: individual URI
	 * @param relationURI: relation URI
	 * @param rangeClassURI: range class URI
	 * 
	 * @author John Guerson
	 */
	@Deprecated
	static public OntModel createRangeDataPropertyValue(OntModel model, String value, String individualURI, String relationURI, String rangeClassURI) 
	{
		Individual indInstance = model.getIndividual(individualURI);
		Literal literal = model.createTypedLiteral(value,rangeClassURI);
		Property relation = model.getDatatypeProperty(relationURI);	
		indInstance.addProperty(relation, literal);		
		return model;
	}

	/**
	 * Delete a value from the range of the data property relation. Used at the OKCo application.
	 * 
	 * @param model: OntModel
	 * @param value: value
	 * @param individualURI: individual URI
	 * @param relationURI: relation URI
	 * @param rangeClassURI: range class URI
	 * 
	 * @author John Guerson
	 */
	static public OntModel deleteRangeDataPropertyValue(OntModel model, String value, String individualURI, String relationURI, String rangeClassURI) 
	{
		Individual indInstance = model.getIndividual(individualURI);
		Literal literal = model.createTypedLiteral(value,rangeClassURI);
		Property relation = model.getDatatypeProperty(relationURI);	
		indInstance.removeProperty(relation, literal);				
		return model;
	}

	/**
	 * Create an object property. Used at the OKCo application.
	 * 
	 * @param model: OntModel
	 * @param srcIndividualURI: source individual URI
	 * @param relationURI: relation URI
	 * @param tgtIndividualURI: target individual URI
	 * 
	 * @author John Guerson
	 */
	@Deprecated
	static public OntModel createObjectProperty(OntModel model, String srcIndividualURI, String relationURI, String tgtIndividualURI) 
	{		
		Individual indInstanceSource = model.getIndividual(srcIndividualURI);
		Individual indInstanceTarget = model.getIndividual(tgtIndividualURI);
		Property relation = model.getProperty(relationURI);
		indInstanceSource.addProperty(relation, indInstanceTarget);		
		
		List<String> allInverseOfURIs = QueryUtil.getAllInverseOfURIs(model, relationURI);
		
		for (String inverseOfURI : allInverseOfURIs) {
			Property inverse = model.getProperty(inverseOfURI);
			indInstanceTarget.addProperty(inverse, indInstanceSource);
		}
		
		return model;
	}
	
	/**
	 * Delete an object property.Used at the OKCo application.
	 * 
	 * @param model: OntModel
	 * @param srcIndividualURI: source individual URI
	 * @param relationURI: relation URI
	 * @param tgtIndividualURI: target individual URI
	 * 
	 * @author John Guerson
	 */
	static public OntModel deleteObjectProperty(OntModel model, String srcIndividual, String relationURI, String tgtIndividualURI) 
	{
		Individual indInstanceSource = model.getIndividual(srcIndividual);
		Individual indInstanceTarget = model.getIndividual(tgtIndividualURI);
		Property relation = model.getProperty(relationURI);	
		indInstanceSource.removeProperty(relation, indInstanceTarget);				
		return model;
	}

	/**
	 * Create an individual in a given class. Used at the OKCo application.
	 * 
	 * @param model: OntModel
	 * @param individualURI: individual URI
	 * @param classURI: class URI
	 * 
	 * @author John Guerson
	 */
	@Deprecated
	static public OntModel createIndividualOfClass(OntModel model, String individualURI, String classURI) 
	{	
		Individual indInstance = model.getIndividual(individualURI);
		OntClass cls = model.getOntClass(classURI);		
		indInstance.addOntClass(cls);		
		return model;
	}
	
	/**
	 * Delete an individual in a given class. Used at the OKCo application.
	 * 
	 * @param model: OntModel
	 * @param individualURI: individual URI
	 * @param classURI: class URI
	 * 
	 * @author John Guerson
	 */
	static public OntModel deleteIndividualOfClass(OntModel model, String individualURI, String classURI) 
	{	
		Individual indInstance = model.getIndividual(individualURI);
		OntClass cls = model.getOntClass(classURI);		
		indInstance.removeOntClass(cls);		
		return model;
	}
	
	/**
	 * Set Same As. Used at the OKCo application.
	 * 
	 * @param model: OntModel
	 * @param individualURI1: individual URI 1
	 * @param individualURI2: individual URI 2
	 * 
	 * @author John Guerson
	 */
	@Deprecated
	static public OntModel setSameAs(OntModel model, String individualURI1, String individualURI2)
	{
		Individual i1 = model.getIndividual(individualURI1);
		Individual i2 = model.getIndividual(individualURI2);		
		i1.setSameAs(i2);
		i2.setSameAs(i1);		
		return model;
	}

	/**
	 * Set Different From. Used at the OKCo application.
	 * 
	 * @param model: OntModel
	 * @param individualURI1: individual URI 1
	 * @param individualURI2: individual URI 2
	 * 
	 * @author John Guerson
	 */
	@Deprecated
	static public OntModel setDifferentFrom(OntModel model, String individualURI1, String individualURI2)
	{
		Individual i1 = model.getIndividual(individualURI1);
		Individual i2 = model.getIndividual(individualURI2);		
		i1.setDifferentFrom(i2);
		i2.setDifferentFrom(i1);		
		return model;
	}

	public void processStatements(OntModel model) {
		model.add(this.stmts);
		this.stmts.clear();
	}
	
}
