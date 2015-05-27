package br.com.padtec.nopen.studio.itu.controller;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.nopen.model.DtoJointElement;
import br.com.padtec.nopen.provisioning.service.ProvisioningComponents;
import br.com.padtec.nopen.service.util.NOpenFileUtil;
import br.com.padtec.nopen.service.util.NOpenQueryUtil;
import br.com.padtec.nopen.studio.model.StudioFactory;
import br.com.padtec.nopen.studio.model.StudioSerializator;

import com.jointjs.util.JointUtilManager;

@Controller
public class ITUStudioController {
	
	//REMOVER DEPOIS QUE TIVER TERMINADO
	@RequestMapping("/itu-studio")
	public String networkTopologyRequest() {
		return "itu-studio/itu-studio";
	}
	
	/** Verify if there are any elements without connection in the given card
	 * @param card
	 * @return names of elements without connection
	 */
	@RequestMapping(value = "/verifyElementsOnCard", method = RequestMethod.POST)
	public @ResponseBody String[] verifyElementsOnCard(@RequestParam("card") String card)
	{
		DtoJointElement dtoCard = (DtoJointElement) JointUtilManager.getJavaFromJSON(card, DtoJointElement.class);
		
		String[] result = StudioFactory.elementsWithNoConnection(dtoCard);		
		return result;
	}
	
	/* ----- Search for Layers & Techs ----- */
	
	@RequestMapping(value = "/allLayers", method = RequestMethod.POST)
	public @ResponseBody String[][] getAllLayersNames() 
	{
		//it needs to be a base model because we will not run an inference each time the user creates a new layer.
		return NOpenQueryUtil.getAllLayerNames(ProvisioningComponents.provisioningRepository.getBaseModel());
	}
	
	/** Get layer names given a technology
	 * @param techName
	 * @return
	 */
	@RequestMapping(value = "/techLayers", method = RequestMethod.POST)
	public @ResponseBody String[] getLayerNames(@RequestParam("techName") String techName) 
	{
		//it needs to be a base model because we will not run an inference each time the user creates a new layer.		
		return NOpenQueryUtil.getAllLayerNames(ProvisioningComponents.provisioningRepository.getBaseModel(), techName);
	}
	
	/* ----- CRUD for Container ----- */
	
	/** Insere um container no card
	 * @param container
	 * @param card
	 * @return
	 */
	@RequestMapping(value = "/insertContainer", method = RequestMethod.POST)
	public @ResponseBody String insertContainer(@RequestParam("container") String container, @RequestParam("card") String card) 
	{	
		DtoJointElement dtoContainer = (DtoJointElement) JointUtilManager.getJavaFromJSON(container, DtoJointElement.class);
		DtoJointElement dtoCard = (DtoJointElement) JointUtilManager.getJavaFromJSON(card, DtoJointElement.class);
		
		try{
			StudioFactory.insertContainer(dtoContainer, dtoCard);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}		
		return "success";		
	}
	
	/** Remove um container do card
	 * @param container
	 * @param card
	 * @return: success or error
	 */
	@RequestMapping(value = "/deleteContainer", method = RequestMethod.POST)
	public @ResponseBody String deleteContainer(@RequestParam("container") String container, @RequestParam("card") String card) 
	{
		
		DtoJointElement dtoContainer = (DtoJointElement) JointUtilManager.getJavaFromJSON(container, DtoJointElement.class);
		DtoJointElement dtoCard = (DtoJointElement) JointUtilManager.getJavaFromJSON(card, DtoJointElement.class);
		
		try{
			StudioFactory.deleteContainer(dtoContainer, dtoCard);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}		
		return "success";
	}
	
	/* ----- CRUD for Transport Function ----- */
	
	/** Cria um transport function sobre uma camada ou diretamente sobre o card
	 * @param transportFunction
	 * @param container
	 * @return
	 */
	@RequestMapping(value = "/createTransportFunction", method = RequestMethod.POST)
	public @ResponseBody String createTransportFunction(@RequestParam("transportFunction") String transportFunction, @RequestParam("container") String container)
	{
		DtoJointElement dtoTransportFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(transportFunction, DtoJointElement.class);
		DtoJointElement dtoContainer = (DtoJointElement) JointUtilManager.getJavaFromJSON(container, DtoJointElement.class);

		try{
			StudioFactory.createTransportFunction(dtoTransportFunction, dtoContainer);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}		
		return "success";
	}
		
	/** Verifica se � poss�vel criar o transport function sobre uma camada ou diretamente sobre o card
	 * @param transportFunction
	 * @param container
	 * @return
	 */
	@RequestMapping(value = "/canCreateTransportFunction", method = RequestMethod.POST)
	public @ResponseBody String canCreateTransportFunction(@RequestParam("transportFunction") String transportFunction, @RequestParam("container") String container)
	{
		DtoJointElement dtoTransportFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(transportFunction, DtoJointElement.class);
		DtoJointElement dtoContainer = (DtoJointElement) JointUtilManager.getJavaFromJSON(container, DtoJointElement.class);
		
		try{
			StudioFactory.canCreateTransportFunction(dtoTransportFunction, dtoContainer);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}		
		return "true";
	}
	
	/** Remove um transport function
	 * @param transportFunction
	 * @return
	 */
	@RequestMapping(value = "/deleteTransportFunction", method = RequestMethod.POST)
	public @ResponseBody String deleteTransportFunction(@RequestParam("transportFunction") String transportFunction) 
	{	
		DtoJointElement dtoTransportFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(transportFunction, DtoJointElement.class);
		
		try{
			StudioFactory.deleteTransportFunction(dtoTransportFunction);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
		return "success";		
	}
		
	/** Troca ou retira (container=null) o container do transport function
	 * @param transportFunction
	 * @param sourceContainer
	 * @param targetContainer
	 * @param card 
	 * @return
	 */
	@RequestMapping(value = "/changeContainer", method = RequestMethod.POST)
	public @ResponseBody String changeContainer(@RequestParam("transportFunction") String transportFunction, @RequestParam("sourceContainer") String sourceContainer,
	@RequestParam("targetContainer") String targetContainer, @RequestParam("card") String card) 
	{
		DtoJointElement dtoTransportFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(transportFunction, DtoJointElement.class);
		DtoJointElement dtoSourceContainer = (DtoJointElement) JointUtilManager.getJavaFromJSON(sourceContainer, DtoJointElement.class);
		DtoJointElement dtoTargetContainer = (DtoJointElement) JointUtilManager.getJavaFromJSON(targetContainer, DtoJointElement.class);
		DtoJointElement dtoCard = (DtoJointElement) JointUtilManager.getJavaFromJSON(card, DtoJointElement.class);
		
		try{
			StudioFactory.changeContainer(dtoTransportFunction, dtoSourceContainer, dtoTargetContainer, dtoCard); 
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
		return "success";		
	}
	
	/** Modifica o nome de um transport function
	 * @param transportFunction
	 * @return
	 */
	@RequestMapping(value = "/setTransportFunctionName", method = RequestMethod.POST)
	public @ResponseBody String setTransportFunctionName(@RequestParam("transportFunction") String transportFunction)
	{
		DtoJointElement dtoTransportFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(transportFunction, DtoJointElement.class);

		try{
			StudioFactory.setTransportFunctionName(dtoTransportFunction);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}	
		return "success";
	}
	
	/* ----- CRUD for port ----- */
	
	/** Cria uma porta (interface) de entrada ou sa�da conectada a um transport function
	 * @param port
	 * @param transportFunction
	 * @return
	 */
	@RequestMapping(value = "/createPort", method = RequestMethod.POST)
	public @ResponseBody String createPort(@RequestParam("port") String port, @RequestParam("transportFunction") String transportFunction) 
	{		
		DtoJointElement dtoTransportFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(transportFunction, DtoJointElement.class);
		DtoJointElement dtoPort = (DtoJointElement) JointUtilManager.getJavaFromJSON(port, DtoJointElement.class);
		 
		try{
			StudioFactory.createPort(dtoPort, dtoTransportFunction);		
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
		return "success";		
	}
		
	/** Remove uma porta (interface) de entrada ou sa�da
	 * @param port
	 * @return
	 */
	@RequestMapping(value = "/deletePort", method = RequestMethod.POST)
	public @ResponseBody String deletePort(@RequestParam("port") String port) 
	{
		DtoJointElement dtoPort = (DtoJointElement) JointUtilManager.getJavaFromJSON(port, DtoJointElement.class);
		try{
			StudioFactory.deletePort(dtoPort);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
		return "success";			
	}
	
	/** Modifica o nome de uma interface
	 * @param port
	 * @return
	 */
	@RequestMapping(value = "/setPortName", method = RequestMethod.POST)
	public @ResponseBody String setPortName(@RequestParam("port") String port) 
	{		
		DtoJointElement dtoPort = (DtoJointElement) JointUtilManager.getJavaFromJSON(port, DtoJointElement.class);
		 
		try{
			StudioFactory.setPortName(dtoPort);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}	
		return "success";		
	}
	
	/* ----- CRUD for link ----- */
	
	/** Cria uma conex�o entre sourceTFunction e targetTFunction
	 * @param sourceTFunction
	 * @param targetTFunction
	 * @param link
	 * @return
	 */
	@RequestMapping(value = "/createLink", method = RequestMethod.POST)
	public @ResponseBody String createLink(@RequestParam("sourceTFunction") String sourceTFunction, @RequestParam("targetTFunction") String targetTFunction,
	@RequestParam("link") String link) 
	{
		DtoJointElement dtoSourceTFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(sourceTFunction, DtoJointElement.class);
		DtoJointElement dtoTargetTFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(targetTFunction, DtoJointElement.class);
		DtoJointElement dtoLink = (DtoJointElement) JointUtilManager.getJavaFromJSON(link, DtoJointElement.class);
		
		try{
			StudioFactory.createLink(dtoSourceTFunction, dtoTargetTFunction, dtoLink);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}	
		return "success";		
	}
	
	/** Verifica se � poss�vel criar uma conex�o entre sourceTFunction e targetTFunction
	 * @param sourceTFunction
	 * @param targetTFunction
	 * @return
	 */
	@RequestMapping(value = "/canCreateLink", method = RequestMethod.POST)
	public @ResponseBody String canCreateLink(@RequestParam("sourceTFunction") String sourceTFunction, @RequestParam("targetTFunction") String targetTFunction) 
	{		
		DtoJointElement dtoSourceTFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(sourceTFunction, DtoJointElement.class);
		DtoJointElement dtoTargetTFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(targetTFunction, DtoJointElement.class);
		
		try{
			StudioFactory.canCreateLink(dtoSourceTFunction, dtoTargetTFunction);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}	
		return "true";		
	}
	
	/** Remove uma conex�o
	 * @param sourceTFunction
	 * @param targetTFunction
	 * @param link: identificador do link a ser deletado
	 * @return: success or error
	 */
	@RequestMapping(value = "/deleteLink", method = RequestMethod.POST)
	public @ResponseBody String deleteLink(@RequestParam("sourceTFunction") String sourceTFunction, @RequestParam("targetTFunction") String targetTFunction,
	@RequestParam("link") String link) 
	{
		DtoJointElement dtoSourceTFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(sourceTFunction, DtoJointElement.class);
		DtoJointElement dtoTargetTFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(targetTFunction, DtoJointElement.class);
		DtoJointElement dtoLink = (DtoJointElement) JointUtilManager.getJavaFromJSON(link, DtoJointElement.class);
	
		try{
			StudioFactory.deleteLink(dtoLink, dtoSourceTFunction, dtoTargetTFunction); 
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
		return "success";			
	}
	
	/* ----- Save/Load graph  ----- */
	/**
	 * @param graphJSON: conte�do do grafo no formato JSON
	 * @param fileName: nome do arquivo no qual deve ser salvo o grafo (nome do card sendo editado, no caso)
	 * @return: success or error
	 */
	@RequestMapping(value = "/saveGraphJSON", method = RequestMethod.POST)
	public @ResponseBody String saveGraphJSON(@RequestParam("graphJSON") String graphJSON, @RequestParam("fileName") String fileName) 
	{		
		String errorMsg = new String();
		try {						
			StudioSerializator.serialize(graphJSON, fileName);
		} catch (IOException e) {
			errorMsg = "Serialize JOINT Error: "+ e.getMessage();	
		}		
		return errorMsg;	
	}
	
	/**
	 * @param fileName: nome do arquivo contendo os atributos de TTF
	 * @return: atributos de TTF, para cada camada, no formato JSON
	 */
	@RequestMapping(value = "/loadTTFAttributes", method = RequestMethod.POST)
	public @ResponseBody String loadTTFAttributes(@RequestParam("reference") String reference)
	{
		String path = "itu-" + reference + "/ttf.json";
		path = NOpenFileUtil.replaceSlash(path);		
		return NOpenFileUtil.openItuConfigurationJSONFileAsString(path);		

	}
	
	/**
	 * @param fileName: nome do arquivo contendo o grafo desejado no formato JSON (nome do card que se deseja abrir, no caso)
	 * @return: conte�do do grafo no formato JSON or error
	 */
	@RequestMapping(value = "/loadGraphJSON", method = RequestMethod.POST)
	public @ResponseBody String loadGraphJSON(@RequestParam("fileName") String fileName) 
	{
		String errorMsg = new String();
		String json = new String();
		try {			
			json = StudioSerializator.deserialize(fileName);
		
		} catch (IOException e) {
			errorMsg = "Serialize JOINT Error: "+ e.getMessage();	
			return errorMsg;
		}		
		return json;	
	}
	
	/**
	 * Procedure to open a ITU file of a Template.
	 * @param path
	 * @param ituFilename
	 * @return
	 */
	@RequestMapping(value = "/openITUFile", method = RequestMethod.POST)
	protected @ResponseBody String openITUFile(@RequestParam("path") String path, @RequestParam("filename") String ituFilename)
	{		
		path = path + "/itu/" + ituFilename + ".json";
		path = NOpenFileUtil.replaceSlash(path);		
		return NOpenFileUtil.openTemplateJSONFileAsString(path);		
	}
	
	/**
	 * Procedure to save all ITU files of a Equipment.
	 * @param path
	 * @param ituFilename
	 * @param graph
	 */
	@RequestMapping("/saveITUFile")
	public @ResponseBody void saveITUFile(@RequestParam("path") String path, @RequestParam("filename") String filename, @RequestParam("graph") String graph) 
	{		
		path = path + "/itu/";
		path = NOpenFileUtil.replaceSlash(path);
		NOpenFileUtil.createTemplateRepository(path);		
		try {
			File file = NOpenFileUtil.createTemplateJSONFile(path + filename);
			NOpenFileUtil.writeToFile(file, graph);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Procedure to delete a ITU file inside of a Equipment Folder.
	 * @param filename
	 */	
	@RequestMapping("/deleteITUFile")
	public @ResponseBody void deleteITUFile(@RequestParam("path") String path, @RequestParam("filename") String filename) {
		
		String ituPath = NOpenFileUtil.replaceSlash(NOpenFileUtil.templateJSONFolder + path + "/itu/" + filename + ".json");
		File ituFile = new File(ituPath);
		
		if(ituFile.exists()){
			ituFile.delete();
		}
		
	}
	
	/**
	 * Procedure to copy ITU files inside of a Equipment Folder.
	 * @param filename
	 */	
	@RequestMapping("/copyITUFiles")
	public @ResponseBody void copyITUFiles(@RequestParam("oldPath") String oldPath, @RequestParam("newPath") String newPath) {
		
		String ituOldPath = NOpenFileUtil.replaceSlash(NOpenFileUtil.templateJSONFolder + oldPath + "/itu/");
		String ituNewPath = NOpenFileUtil.replaceSlash(NOpenFileUtil.templateJSONFolder + newPath);
		NOpenFileUtil.createTemplateRepository(newPath);
		
		//DELETE ITU FILES IN NEW PATH
		String ituFilesInNewPath = NOpenFileUtil.replaceSlash(NOpenFileUtil.templateJSONFolder + newPath + "/itu/");
		File dir = new File(ituFilesInNewPath);
		if(dir.exists())
		{
			for(File file : dir.listFiles())
			{ 
				file.delete();
			}
		}
		
		File source = new File(ituOldPath);
		File dest = new File(ituNewPath);
				
		try {
			if(source.exists()){
				FileUtils.copyDirectoryToDirectory(source, dest);
			}
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
	}
}
