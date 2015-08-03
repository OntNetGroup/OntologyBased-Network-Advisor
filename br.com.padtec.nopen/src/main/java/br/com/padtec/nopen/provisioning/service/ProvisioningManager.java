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
import com.jointjs.util.JointUtilManager;

public class ProvisioningManager {

	public OKCoUploader repository;
	
	public ProvisioningManager(OKCoUploader repository) {
		this.repository = repository;
	}
	
	/**
	 * 
	 * @param jsonElements
	 * @throws Exception
	 */
	public void createElementsInOWL(String jsonElements) throws Exception {
		
		OntModel ontModel = this.repository.getBaseModel();
		String namespace = this.repository.getNamespace();
		
		PElement[] elements = (PElement[]) JointUtilManager.getJavaFromJSON(jsonElements, PElement[].class);		
		for(PElement element : elements) {
			
			String individualURI = namespace + element.getId();
			String classURI = namespace + element.getType();
			
			//create new individual
			Individual individual = FactoryUtil.createInstanceIndividual(ontModel, individualURI, classURI);
			//set individual label
			individual.setLabel(element.getName(),"EN");	
			
		}		
		
	}
	/**
	 * 
	 * @param jsonLinks
	 * @throws Exception
	 */
	public void createLinksInOWL(String jsonLinks) throws Exception {
		
		OntModel ontModel = this.repository.getBaseModel();
		String namespace = this.repository.getNamespace();
		
		PLink[] links = (PLink[]) JointUtilManager.getJavaFromJSON(jsonLinks, PLink[].class);
		for(PLink link : links) {
			
			String subject = namespace + link.getSource();
			String predicate = getPredicateFromOWL(link.getSourceType(), link.getTargetType());
			String object = namespace + link.getTarget();
			
			FactoryUtil.createInstanceRelation(ontModel, subject, predicate, object);
			
		}
		
	}
	
	/**
	 * Procedure to get predicate from owl model
	 * @param sourceType
	 * @param targetType
	 * @return
	 */
	public String getPredicateFromOWL(String sourceType, String targetType) {
		
		OntModel ontModel = this.repository.getBaseModel();
		String namespace = this.repository.getNamespace();
		
		//Create string
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
		
		System.out.println(queryString);
		
		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, ontModel);
		ResultSet results = qe.execSelect();

		String predicate = "";
		boolean pred = true;
		
		while (results.hasNext()) {
			QuerySolution row = results.next();
		    
		    RDFNode predicateNode = row.get("?predicate");
		    String domain = row.get("?domain").toString().replace(namespace, "");
		    String range = row.get("?range").toString().replace(namespace, "");
		    
//		    System.out.println("domain: " + domain + " range: " + range);
//		    System.out.println("predicate: " + predicateNode.toString());
		    
		    //Set predicate with range == target type
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
		
		System.out.println("source: " + sourceType);
	    System.out.println("target: " + targetType);
		System.out.println(predicate);
		
		return predicate;
	}
	
}
