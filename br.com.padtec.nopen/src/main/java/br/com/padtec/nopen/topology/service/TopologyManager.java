package br.com.padtec.nopen.topology.service;

import java.util.HashSet;

import org.springframework.stereotype.Controller;

import br.com.padtec.common.factory.FactoryUtil;
import br.com.padtec.nopen.model.ConceptEnum;
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
	
}
