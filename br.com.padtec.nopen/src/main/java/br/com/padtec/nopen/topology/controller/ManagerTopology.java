package br.com.padtec.nopen.topology.controller;

import java.util.HashSet;
import org.springframework.stereotype.Controller;
import br.com.padtec.common.queries.QueryUtil;

@Controller
public class ManagerTopology {

	public HashSet<String> getAllTemplateEquipment(){
		HashSet<String> equipments = new HashSet<String>();
		//equipments = QueryUtil.getAllTemplateEquipment(/*como passar o modelo??*/);
		
		return equipments;
		
	}
}
