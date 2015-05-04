package br.com.padtec.nopen.provisioning.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.nopen.provisioning.service.ProvisioningManager;
import br.com.padtec.nopen.service.util.NOpenFileUtil;

@Controller
public class ProvisioningController {

	@RequestMapping("/provisioning")
	public String provisioningRequest() 
	{
		return "provisioning/provisioning";
	}	
	
	@RequestMapping(value = "/openTopologyOnProvisioning", method = RequestMethod.POST)
	protected @ResponseBody String openTopologyOnProvisioning(@RequestParam("filename") String filename){
			
		filename = NOpenFileUtil.replaceSlash(filename + "/" + filename + ".json");
		
		ProvisioningManager provisioningManager = new ProvisioningManager();
		String json = provisioningManager.openProvisioning(filename);
		System.out.println(json);
		
		return json;
		
	}
	
	/**
	 * Procedure to check if a Provisioning file exist.
	 * @param filename
	 * @return
	 */
	@RequestMapping(value = "/checkProvisioningFile", method = RequestMethod.POST)
	protected @ResponseBody String checkProvisioningFile(@RequestParam("filename") String filename){

		filename = NOpenFileUtil.replaceSlash(filename + "/" + filename + ".json");
		if(NOpenFileUtil.checkProvisioningFileExist(filename)){
			return "exist";
		}
		
		return null;
	}
	
	/**
	 * Procedure to save a JSON Provisioning file.
	 * @param filename
	 * @param graph
	 */
	@RequestMapping(value = "/saveProvisioning", method = RequestMethod.POST)
	protected @ResponseBody void saveProvisioning(@RequestParam("filename") String filename, @RequestParam("graph") String graph){
		
		NOpenFileUtil.createProvisioningRepository(filename);
		filename = NOpenFileUtil.replaceSlash(filename + "/" + filename);
		
		try {
			File file = NOpenFileUtil.createProvisioningJSONFile(filename);
			NOpenFileUtil.writeToFile(file, graph);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Procedure to get all Provisioning saved files.
	 * @return
	 */
	@RequestMapping(value = "/getAllProvisioning", method = RequestMethod.GET)
	protected @ResponseBody String getAllProvisioning(){
			
		String[] provisioningElements = NOpenFileUtil.getAllProvisioningJSONFileNames();
		return NOpenFileUtil.parseStringToJSON("provisioning", provisioningElements);
		
	}
	
	/**
	 * Procedure to open a JSON Provisioning file.
	 * @param filename
	 * @return
	 */
	@RequestMapping(value = "/openProvisioning", method = RequestMethod.POST)
	protected @ResponseBody String openProvisioning(@RequestParam("filename") String filename){
		
		filename = NOpenFileUtil.replaceSlash(filename + "/" + filename + ".json");	
		return NOpenFileUtil.openProvisioningJSONFileAsString(filename);
		
	}
}
