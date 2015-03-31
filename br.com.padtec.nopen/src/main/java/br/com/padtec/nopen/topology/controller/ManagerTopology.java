package br.com.padtec.nopen.topology.controller;

import java.util.HashSet;
import org.springframework.stereotype.Controller;
import br.com.padtec.nopen.model.NOpenQueries;

@Controller
public class ManagerTopology {

	public HashSet<String> getAllTemplateEquipment(){
		HashSet<String> equipments = new HashSet<String>();
		//equipments = NOpenQueries.getAllTemplateEquipment(/*como passar o modelo??*/);
		
		return equipments;
		
	}
}
