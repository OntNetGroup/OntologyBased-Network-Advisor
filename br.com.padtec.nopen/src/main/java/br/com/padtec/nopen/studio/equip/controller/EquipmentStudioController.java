package br.com.padtec.nopen.studio.equip.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.nopen.service.util.NOpenFileUtil;

@Controller
public class EquipmentStudioController {

	@RequestMapping("/equipment-studio")
	public String equipmentStudioRequest() {
		return "equipment-studio/equipment-studio";
	}

	@RequestMapping("/checkEquipmentFile")
	public @ResponseBody String checkEquipmentFile(@RequestParam("filename") String fileName) {
		System.out.println("check");
		if(NOpenFileUtil.checkEquipmentFileExist(fileName)){
			return "OK";
		}
		return "exists";
	}

	@RequestMapping("/saveEquipment")
	public @ResponseBody void saveEquipment(@RequestParam("filename") String fileName, @RequestParam("graph") String graphValue) {
		/*
		 * MUDAR!!!!
		 * COLOCAR O WRITETOFILE RECEBENDO O FILENAME
		 * 
		 * */
		System.out.println("save");
		try {
			File file = NOpenFileUtil.createEquipmentJSONFile(fileName);
			NOpenFileUtil.writeToFile(file, graphValue);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
