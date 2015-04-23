package br.com.padtec.nopen.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.nopen.service.util.NOpenFileUtil;

@Controller
public class StudioController {

	@RequestMapping("/nt-options")
	public String ntOptionRequest(HttpServletRequest request) {		
		return "nopen-options/nt-options";
	}
	
	@RequestMapping("/es-options")
	public String esOptionRequest(HttpServletRequest request) {		
		return "nopen-options/es-options";
	}
	
	@RequestMapping("/pv-options")
	public String pvOptionRequest(HttpServletRequest request) {		
		return "nopen-options/pv-options";
	}
	
	/**
	 * Procedure to delete Topology.
	 * @param filename
	 */	
	@RequestMapping("/deleteTopology")
	public @ResponseBody void deleteTopolopgy(@RequestParam("filename") String filename) {
		
		String path = NOpenFileUtil.replaceSlash(NOpenFileUtil.topologyJSONFolder + filename + "/");
		File dir = new File(path);
		
		for(File file : dir.listFiles()){ 
			file.delete();
		}
		
		dir.delete();
	}
	
	/**
	 * Procedure to delete Equipment.
	 * @param filename
	 */	
	@RequestMapping("/deleteEquipment")
	public @ResponseBody void deleteEquipment(@RequestParam("filename") String filename) {
		
		String ituPath = NOpenFileUtil.replaceSlash(NOpenFileUtil.equipmentJSONFolder + filename + "/itu/");
		File ituDir = new File(ituPath);
		
		if(ituDir.exists()){
			for(File file : ituDir.listFiles()){ 
				file.delete();
			}
			
			ituDir.delete();
		}
		
		String path = NOpenFileUtil.replaceSlash(NOpenFileUtil.equipmentJSONFolder + filename + "/");
		File dir = new File(path);
		
		for(File file : dir.listFiles()){ 
			file.delete();
		}
		
		dir.delete();
	}
	
	/**
	 * Procedure to delete Topology.
	 * @param filename
	 */	
	@RequestMapping("/deleteProvisioning")
	public @ResponseBody void deleteProvisioning(@RequestParam("filename") String filename) {
		
		String path = NOpenFileUtil.replaceSlash(NOpenFileUtil.provisioningJSONFolder + filename + "/");
		File dir = new File(path);
		
		for(File file : dir.listFiles()){ 
			file.delete();
		}
		
		dir.delete();
	}
	
}
