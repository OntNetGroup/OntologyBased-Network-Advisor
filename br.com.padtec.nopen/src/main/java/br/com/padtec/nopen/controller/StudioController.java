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
	
	@RequestMapping("/pr-options")
	public String prOptionRequest(HttpServletRequest request) {		
		return "nopen-options/pr-options";
	}
	
	/**
	 * Procedure to delete Topology.
	 * @param filename
	 */	
	@RequestMapping("/deleteTopology")
	public @ResponseBody void deleteTopolopgy(@RequestParam("filename") String filename) {
		
		String equipmentsPath = NOpenFileUtil.replaceSlash(NOpenFileUtil.topologyJSONFolder + filename + "/equipments/");
		
		File equipmentsDir = new File(equipmentsPath);
		
		for(File file : equipmentsDir.listFiles()){ 
			file.delete();
		}
		
		equipmentsDir.delete();
		
		String path = NOpenFileUtil.replaceSlash(NOpenFileUtil.topologyJSONFolder + filename + "/");
		
		File dir = new File(path);
		
		for(File file : dir.listFiles()){ 
			file.delete();
		}
		
		dir.delete();
	}
	
	/**
	 * Procedure to delete a Template.
	 * @param filename
	 */	
	@RequestMapping("/deleteTemplate")
	public @ResponseBody void deleteTemplate(@RequestParam("filename") String filename) {
		
		String ituPath = NOpenFileUtil.replaceSlash(NOpenFileUtil.templateJSONFolder + filename + "/itu/");
		File ituDir = new File(ituPath);
		
		if(ituDir.exists()){
			for(File file : ituDir.listFiles()){ 
				file.delete();
			}
			
			ituDir.delete();
		}
		
		String path = NOpenFileUtil.replaceSlash(NOpenFileUtil.templateJSONFolder + filename + "/");
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
		
		String owlPath = NOpenFileUtil.replaceSlash(NOpenFileUtil.provisioningOWLFolder + filename + "/");
		File owlDir = new File(owlPath);
		
		for(File file : owlDir.listFiles()){ 
			file.delete();
		}
		
		owlDir.delete();
	}
	
}
