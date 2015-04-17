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
	public @ResponseBody String checkEquipmentFile(@RequestParam("filename") String filename) {
		System.out.println("check");
		
		filename = NOpenFileUtil.replaceSlash(filename + "/" + filename);
		if(NOpenFileUtil.checkEquipmentFileExist(filename)){
			return "exist";
		}
		return null;
	}

	@RequestMapping("/saveEquipment")
	public @ResponseBody void saveEquipment(@RequestParam("filename") String filename, @RequestParam("graph") String graph) {

		NOpenFileUtil.createEquipmentRepository(filename);
		filename = NOpenFileUtil.replaceSlash(filename + "/" + filename);
		
		try {
			File file = NOpenFileUtil.createEquipmentJSONFile(filename);
			NOpenFileUtil.writeToFile(file, graph);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@RequestMapping("/saveITUFiles")
	public @ResponseBody void saveITUFiles(@RequestParam("path") String path, @RequestParam("filename") String ituFilename, @RequestParam("graph") String graph) {
		
		path = path + "/itu/";
		path = NOpenFileUtil.replaceSlash(path);
		NOpenFileUtil.createEquipmentRepository(path);
		
		try {
			File file = NOpenFileUtil.createEquipmentJSONFile(path + ituFilename);
			NOpenFileUtil.writeToFile(file, graph);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
