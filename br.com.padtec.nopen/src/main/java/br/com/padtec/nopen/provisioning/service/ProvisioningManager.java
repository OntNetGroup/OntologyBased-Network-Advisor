package br.com.padtec.nopen.provisioning.service;

import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.nopen.provisioning.model.PElement;
import br.com.padtec.nopen.provisioning.model.PLink;
import br.com.padtec.okco.core.application.OKCoUploader;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.shared.Lock;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.jointjs.util.JointUtilManager;

public class ProvisioningManager {

	public OKCoUploader repository;
	
	public ProvisioningManager(OKCoUploader repository) {
		this.repository = repository;
	}
	
	/**
	 * Procedure to parse json elements to owl
	 * @param jsonElements
	 * @throws Exception
	 * @author Lucas Bassetti
	 */
	public void createElementsInOWL(String jsonElements) throws Exception {
		
		OntModel ontModel = this.repository.getBaseModel();
		String namespace = this.repository.getNamespace();
		
		FactoryUtil factoryUtil = new FactoryUtil();
		
		PElement[] elements = (PElement[]) JointUtilManager.getJavaFromJSON(jsonElements, PElement[].class);		
		for(PElement element : elements) {
			
			String individualURI = namespace + element.getId();
			String classURI = namespace + element.getType();
			
			//create new individual
			//Individual individual = 
			factoryUtil.createInstanceIndividualStatement(ontModel, individualURI, classURI, false);
			//set individual label
			//individual.setLabel(element.getName(),"EN");	
			
			Resource individual = ontModel.createResource(individualURI);
			
			Statement stmt = ontModel.createStatement(individual, RDFS.label, ontModel.createLiteral(element.getName()));
			factoryUtil.stmts.add(stmt);
		}	
		
		ontModel.enterCriticalSection(Lock.READ);
		try {
			factoryUtil.processStatements(ontModel);
			System.out.println("TERMINOU1");
		} finally {
			ontModel.leaveCriticalSection();
		}
		
		
	}
	
	/**
	 * Procedure to parse json links to owl
	 * @param jsonLinks
	 * @throws Exception
	 * @author Lucas Bassetti
	 */
	public void createLinksInOWL(String jsonLinks) throws Exception {
		
		
		OntModel ontModel = this.repository.getBaseModel();
		String namespace = this.repository.getNamespace();
		
		FactoryUtil factoryUtil = new FactoryUtil();
		
		PLink[] links = (PLink[]) JointUtilManager.getJavaFromJSON(jsonLinks, PLink[].class);
		for(PLink link : links) {
			
			String subject = namespace + link.getSource();
			String predicate = getPredicateFromOWL(link.getSourceType(), link.getTargetType());
			String object = namespace + link.getTarget();
			
			factoryUtil.createInstanceRelationStatement(ontModel, subject, predicate, object, false);
			
		}
		
		ontModel.enterCriticalSection(Lock.READ);
		try {
			factoryUtil.processStatements(ontModel);
			System.out.println("TERMINOU2");
		} finally {
			ontModel.leaveCriticalSection();
		}
		
	}
	
	
	/**
	 * Procedure to get predicate from owl model
	 * @param sourceType
	 * @param targetType
	 * @return
	 * @author Lucas Bassetti
	 */
	public String getPredicateFromOWL(String sourceType, String targetType) {
		
		OntModel ontModel = this.repository.getBaseModel();
		String namespace = this.repository.getNamespace();
		
		//Create query string
		String prefix = "PREFIX ont: <" + namespace + "> " + 
						"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
						"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
						"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";
		String queryString = prefix +
						"SELECT DISTINCT * WHERE { " +
							"?predicate rdfs:domain ?domain . " +
							"?predicate rdfs:range ?range . " +
							"ont:" + sourceType + " rdfs:subClassOf*/owl:intersectionOf*/rdf:rest*/rdf:first*/rdfs:subClassOf* ?domain . " +
							"ont:" + targetType + " rdfs:subClassOf*/owl:intersectionOf*/rdf:rest*/rdf:first*/rdfs:subClassOf* ?range . " +
						 "}";
		
//		System.out.println(queryString);
		
		//execute query string
		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, ontModel);
		ResultSet results = qe.execSelect();

		//get predicate from query result
		String predicate = "";
		boolean pred = true;
		
		while (results.hasNext()) {
			QuerySolution row = results.next();
		    
		    RDFNode predicateNode = row.get("?predicate");
		    String domain = row.get("?domain").toString().replace(namespace, "");
		    String range = row.get("?range").toString().replace(namespace, "");
		    
		    if(sourceType.equals(domain) && targetType.equals(range)) {
		    	predicate = predicateNode.toString();
		    	break;
		    }
		    else if(sourceType.equals(domain) || targetType.equals(range)) {
		    	predicate = predicateNode.toString();
		    	pred = false;
		    }
		    else if (pred){
		    	predicate = predicateNode.toString();
		    }
		}
		
//		System.out.println("source: " + sourceType);
//	    System.out.println("target: " + targetType);
//		System.out.println(predicate);
		
		return predicate;
	}
	
}
