package br.com.padtec.nopen.topology.service;

import java.util.HashSet;

import org.springframework.stereotype.Controller;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.jointjs.util.JointUtilManager;

import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.nopen.model.ConceptEnum;
import br.com.padtec.nopen.provisioning.model.PElement;
import br.com.padtec.nopen.provisioning.model.PLink;
import br.com.padtec.nopen.service.util.NOpenQueryUtil;
import br.com.padtec.nopen.studio.service.StudioComponents;

@Controller
public class TopologyManager {

	public HashSet<String> getAllTemplateEquipment(){
		HashSet<String> equipments = new HashSet<String>();
		equipments = NOpenQueryUtil.getAllTemplateEquipment(StudioComponents.studioRepository.getBaseModel());		
		
		return equipments;
		
	}
	
	public void connectTFLayer(String idTF, String relation, String idLayer) throws Exception{
		//Save Transport Function
		String idTFURI = StudioComponents.studioRepository.getNamespace()+idTF;
		String TFURI = StudioComponents.studioRepository.getNamespace()+ConceptEnum.Transport_Function.toString();
		FactoryUtil.createInstanceIndividual(StudioComponents.studioRepository.getBaseModel(), idTFURI, TFURI);

		// Create relation between Transport Function and Layer
		String relationURI = StudioComponents.studioRepository.getNamespace()+relation; //ComponentOf7
		String layerURI = StudioComponents.studioRepository.getNamespace()+idLayer; //
		FactoryUtil.createInstanceRelation(StudioComponents.studioRepository.getBaseModel(),TFURI, relationURI, layerURI);

	}
	
	/**
	 * Procedure to parse json elements to owl
	 * @param jsonElements
	 * @throws Exception
	 * @author Lucas Bassetti
	 */
	public static void createElementsInOWL(String jsonElements) throws Exception {
		
		OntModel ontModel =  StudioComponents.studioRepository.getBaseModel();
		String namespace = StudioComponents.studioRepository.getNamespace();
		
		FactoryUtil factoryUtil = new FactoryUtil();
		
		PElement[] elements = (PElement[]) JointUtilManager.getJavaFromJSON(jsonElements, PElement[].class);		
		for(PElement element : elements) {
			
			String individualURI = namespace + element.getId();
			String classURI = namespace + element.getType();
			
			//create new individual
			factoryUtil.createInstanceIndividualStatement(ontModel, individualURI, classURI, false);
			
			//set individual label
			Resource individual = ontModel.createResource(individualURI);
			
			Statement stmt = ontModel.createStatement(individual, RDFS.label, ontModel.createLiteral(element.getName()));
			factoryUtil.stmts.add(stmt);
		}	
		
		factoryUtil.processStatements(ontModel);
		
	}
	
	/**
	 * Procedure to parse json links to owl
	 * @param jsonLinks
	 * @throws Exception
	 * @author Lucas Bassetti
	 */
	public static void createLinksInOWL(String jsonLinks) throws Exception {
		
		OntModel ontModel =  StudioComponents.studioRepository.getBaseModel();
		String namespace = StudioComponents.studioRepository.getNamespace();
		
		FactoryUtil factoryUtil = new FactoryUtil();
		
		PLink[] links = (PLink[]) JointUtilManager.getJavaFromJSON(jsonLinks, PLink[].class);
		for(PLink link : links) {
			
			String subject = namespace + link.getSubject();
			String predicate = namespace + link.getPredicate();
			String object = namespace + link.getObject();
			
			factoryUtil.createInstanceRelationStatement(ontModel, subject, predicate, object, false);
			
		}
		
		factoryUtil.processStatements(ontModel);
		
	}
	
}
