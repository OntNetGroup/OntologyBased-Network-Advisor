package br.com.padtec.nopen.equipmentstudio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class EquipmentStudioController {
	
	@RequestMapping("/equipment-studio")
	public String equipmentStudioRequest() {
		return "equipment-studio/equipment-studio";
	}
}
