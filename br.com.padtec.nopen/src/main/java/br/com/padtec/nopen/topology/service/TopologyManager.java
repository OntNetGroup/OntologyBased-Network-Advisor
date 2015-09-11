package br.com.padtec.nopen.topology.service;

import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.stereotype.Controller;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.jointjs.util.JointUtilManager;

import br.com.padtec.common.dto.DtoInstance;
import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.common.queries.DtoQueryUtil;
import br.com.padtec.nopen.model.ConceptEnum;
import br.com.padtec.nopen.provisioning.model.PElement;
import br.com.padtec.nopen.provisioning.model.PLink;
import br.com.padtec.nopen.service.util.NOpenQueryUtil;
import br.com.padtec.nopen.studio.service.StudioComponents;

@Controller
public class TopologyManager {

	public HashSet<String> getAllTemplateEquipment(){
		HashSet<String> equipments = new HashSet<String>();
		equipments = NOpenQueryUtil.getAllTemplateEquipment(TopologyComponents.topologyRepository.getBaseModel());		
		
		return equipments;
		
	}
	
	public void connectTFLayer(String idTF, String relation, String idLayer) throws Exception{
		//Save Transport Function
		String idTFURI = TopologyComponents.topologyRepository.getNamespace()+idTF;
		String TFURI = TopologyComponents.topologyRepository.getNamespace()+ConceptEnum.Transport_Function.toString();
		FactoryUtil.createInstanceIndividual(TopologyComponents.topologyRepository.getBaseModel(), idTFURI, TFURI);

		// Create relation between Transport Function and Layer
		String relationURI = TopologyComponents.topologyRepository.getNamespace()+relation; //ComponentOf7
		String layerURI = TopologyComponents.topologyRepository.getNamespace()+idLayer; //
		FactoryUtil.createInstanceRelation(TopologyComponents.topologyRepository.getBaseModel(),TFURI, relationURI, layerURI);

	}
	
	/**
	 * Procedure to parse json elements to owl
	 * @param jsonElements
	 * @throws Exception
	 * @author Lucas Bassetti
	 */
	public static void createElementsInOWL(String jsonElements) throws Exception {
		
		OntModel ontModel =  TopologyComponents.topologyRepository.getBaseModel();
		String namespace = TopologyComponents.topologyRepository.getNamespace();
		
		FactoryUtil factoryUtil = new FactoryUtil();
		
		PElement[] elements = (PElement[]) JointUtilManager.getJavaFromJSON(jsonElements, PElement[].class);		
		for(PElement element : elements) {
			
			String individualURI = namespace + element.getId();
			String classURI = namespace + element.getType();
			System.out.println(classURI);
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
		
		OntModel ontModel =  TopologyComponents.topologyRepository.getBaseModel();
		String namespace = TopologyComponents.topologyRepository.getNamespace();
		
		FactoryUtil factoryUtil = new FactoryUtil();
		
		PLink[] links = (PLink[]) JointUtilManager.getJavaFromJSON(jsonLinks, PLink[].class);
		for(PLink link : links) {
			
			String subject = namespace + link.getSubject();
			String predicate = namespace + link.getPredicate();
			String object = namespace + link.getObject();
			
			factoryUtil.createInstanceRelationStatement(ontModel, subject, predicate, object, false);
			
		}
		
		factoryUtil.processStatements(ontModel);
		
		ArrayList<DtoInstance> instances = (ArrayList<DtoInstance>) DtoQueryUtil.getIndividualsFromClass(ontModel, "Card");
		
		for (DtoInstance dtoInstance : instances) {
			ArrayList<DtoInstanceRelation>rel= (ArrayList<DtoInstanceRelation>) DtoQueryUtil.getRelationsFrom(ontModel, dtoInstance.getNs()+dtoInstance.getName());
			System.err.println(rel);
		}
		
	}
	
}
