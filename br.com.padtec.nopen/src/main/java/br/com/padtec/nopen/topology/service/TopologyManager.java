package br.com.padtec.nopen.topology.service;

import java.util.HashSet;

import org.springframework.stereotype.Controller;

import br.com.padtec.nopen.model.NOpenQueries;
import br.com.padtec.nopen.studio.service.StudioComponents;

@Controller
public class TopologyManager {

	public HashSet<String> getAllTemplateEquipment(){
		HashSet<String> equipments = new HashSet<String>();
		equipments = NOpenQueries.getAllTemplateEquipment(StudioComponents.studioRepository.getBaseModel());		
		
		return equipments;
		
	}
}
