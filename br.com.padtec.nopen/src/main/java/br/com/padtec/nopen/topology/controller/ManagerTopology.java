package br.com.padtec.nopen.topology.controller;

import java.util.HashSet;
import br.com.padtec.nopen.core.queries.NOpenQueryUtil;
import org.springframework.stereotype.Controller;

@Controller
public class ManagerTopology {

	public HashSet<String> getAllTemplateEquipment(){
		HashSet<String> equipments = new HashSet<String>();
		
		equipments = NOpenQueryUtil.getAllTemplateEquipment();
		
		return equipments;
		
	}
}
