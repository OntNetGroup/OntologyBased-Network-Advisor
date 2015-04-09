package br.com.padtec.nopen.topology.service;

import java.util.HashSet;

import org.springframework.stereotype.Controller;

import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.nopen.model.ConceptEnum;
import br.com.padtec.nopen.service.util.NOpenQueries;
import br.com.padtec.nopen.studio.service.StudioComponents;

@Controller
public class TopologyManager {

	public HashSet<String> getAllTemplateEquipment(){
		HashSet<String> equipments = new HashSet<String>();
		equipments = NOpenQueries.getAllTemplateEquipment(StudioComponents.studioRepository.getBaseModel());		
		
		return equipments;
		
	}
	
	public void connectTFLayer(String idTF, String relation, String idLayer){
		//Save Transport Function
		String idTFURI = StudioComponents.studioRepository.getNamespace()+idTF;
		String TFURI = StudioComponents.studioRepository.getNamespace()+ConceptEnum.TRANSPORT_FUNCTION.toString();
		FactoryUtil.createInstanceIndividual(StudioComponents.studioRepository.getBaseModel(), idTFURI, TFURI);

		// Create relation between Transport Function and Layer
		String relationURI = StudioComponents.studioRepository.getNamespace()+relation; //ComponentOf7
		String layerURI = StudioComponents.studioRepository.getNamespace()+idLayer; //
		FactoryUtil.createInstanceRelation(StudioComponents.studioRepository.getBaseModel(),TFURI, relationURI, layerURI);

	}
	
}
