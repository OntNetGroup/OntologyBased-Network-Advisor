package br.com.padtec.nopen.provisioning.service;

import java.util.ArrayList;

import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.nopen.model.RelationEnum;
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
	
	public void createLinksInOWL(String jsonLinks) throws Exception {
		
		OntModel ontModel = this.repository.getBaseModel();
		String namespace = this.repository.getNamespace();
		
		PLink[] links = (PLink[]) JointUtilManager.getJavaFromJSON(jsonLinks, PLink[].class);
		for(PLink link : links) {
			
			String subject = namespace + link.getSource();
			String predicate = namespace + getPredicate(link.getSourceType(), link.getTargetType());
			String object = namespace + link.getTarget();
			
			FactoryUtil.createInstanceRelation(ontModel, subject, predicate, object);
			
		}
		
	}
	
	public String getPredicate(String sourceType, String targetType) {
		
		OntModel ontModel = this.repository.getBaseModel();
		String namespace = this.repository.getNamespace();
		String prefix = "PREFIX ont: <" + namespace + ">";
		
		String queryString = prefix +
				" SELECT ?predicate WHERE { " +
				"?predicate rdfs:domain ont:" + sourceType + " . " +
				"?predicate rdfs:range ont:" + targetType + " }";
		
		System.out.println(queryString);
		
		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, ontModel);
		ResultSet results = qe.execSelect();

		String predicate = "";
		
		while (results.hasNext()) {
			QuerySolution row = results.next();
		    
		    RDFNode predicateNode = row.get("?predicate");
		    predicate = predicateNode.toString();
		}
		
		System.out.println(predicate);

		return predicate;
	}
	
}
