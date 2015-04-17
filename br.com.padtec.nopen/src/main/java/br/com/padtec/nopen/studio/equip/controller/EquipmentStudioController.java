package br.com.padtec.nopen.studio.equip.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
		
		filename = NOpenFileUtil.replaceSlash(filename + "/" + filename + ".json");
		if(NOpenFileUtil.checkEquipmentFileExist(filename)){
			return "exist";
		}
		return null;
	}

	
	@RequestMapping("/deleteITUFiles")
	public @ResponseBody void deleteITUFiles(@RequestParam("filename") String filename) {
		File dir = new File(NOpenFileUtil.equipmentJSONFolder + filename + "/itu/");
		System.out.println(NOpenFileUtil.equipmentJSONFolder + filename + "/itu/");

		for(File file : dir.listFiles()){ 
			file.getName();
			file.delete();
		}
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
	
	@RequestMapping(value = "/getAllEquipments", method = RequestMethod.GET)
	protected @ResponseBody String getAllEquipments(){
			
		String[] equipments = NOpenFileUtil.getAllEquipmentJSONFileNames();
		return NOpenFileUtil.parseStringToJSON("equipment", equipments);
		
	}
	
	@RequestMapping(value = "/openEquipment", method = RequestMethod.POST)
	protected @ResponseBody String openEquipment(@RequestParam("filename") String filename){
		
		filename = NOpenFileUtil.replaceSlash(filename + "/" + filename + ".json");	
		return NOpenFileUtil.openEquipmentJSONFileAsString(filename);
		
	}
	
	@RequestMapping(value = "/openITUFile", method = RequestMethod.POST)
	protected @ResponseBody String openITUFile(@RequestParam("path") String path, @RequestParam("filename") String ituFilename){
		
		path = path + "/itu/" + ituFilename + ".json";
		path = NOpenFileUtil.replaceSlash(path);	
		
		return NOpenFileUtil.openEquipmentJSONFileAsString(path);
		
	}

}
