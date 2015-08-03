package br.com.padtec.nopen.provisioning.service;

import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.nopen.provisioning.model.PElement;
import br.com.padtec.nopen.provisioning.model.PLink;
import br.com.padtec.okco.core.application.OKCoUploader;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.jointjs.util.JointUtilManager;

public class ProvisioningManager {

	public void createElementsInOWL(String jsonElements, OKCoUploader repository) throws Exception {
		
		OntModel ontModel = repository.getBaseModel();
		String namespace = repository.getNamespace();
		
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
	
	public void createLinksInOWL(String jsonLinks, OKCoUploader repository) throws Exception {
		
		OntModel ontModel = repository.getBaseModel();
		String namespace = repository.getNamespace();
		
		PLink[] links = (PLink[]) JointUtilManager.getJavaFromJSON(jsonLinks, PLink[].class);
		for(PLink link : links) {
			
			String subject = namespace + link.getSource();
			
		}
		
	}
	
}
