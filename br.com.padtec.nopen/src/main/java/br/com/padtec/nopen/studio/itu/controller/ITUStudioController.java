package br.com.padtec.nopen.studio.itu.controller;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jointjs.util.JointUtilManager;

import br.com.padtec.nopen.model.DtoJointElement;
import br.com.padtec.nopen.model.InstanceFabricator;
import br.com.padtec.nopen.model.SpecificDtoFabricator;
import br.com.padtec.nopen.provisioning.service.ProvisioningComponents;
import br.com.padtec.nopen.service.util.NOpenFileUtil;
import br.com.padtec.nopen.service.util.NOpenQueryUtil;
import br.com.padtec.nopen.studio.model.StudioSerializator;
import br.com.padtec.nopen.studio.service.PerformBind;
import br.com.padtec.nopen.studio.service.StudioComponents;

@Controller
public class ITUStudioController {
	
	//TODO: Remove after finished the application
	@RequestMapping("/itu-studio")
	public String networkTopologyRequest() 
	{
		return "itu-studio/itu-studio";
	}
	
	
	/* ======================================================================================
	 * GET
	 * ======================================================================================*/

	/** Procedure to get all the technologies */
	@RequestMapping(value = "/getTechnologies", method = RequestMethod.POST)
	public @ResponseBody String[] getTechnologies()
	{   
		return NOpenQueryUtil.getAllTechnologiesNames(StudioComponents.studioRepository.getBaseModel());		
	}
	
	/** Get all layers name */
	@RequestMapping(value = "/allLayers", method = RequestMethod.POST)
	public @ResponseBody String[][] getAllLayersNames() 
	{
		//it needs to be a base model because we will not run an inference each time the user creates a new layer.
		return NOpenQueryUtil.getAllLayerNames(ProvisioningComponents.provisioningRepository.getBaseModel());
	}
	
	/** Get layer names given a technology */
	@RequestMapping(value = "/techLayers", method = RequestMethod.POST)
	public @ResponseBody String[] getLayerNames(@RequestParam("techName") String techName) 
	{
		//it needs to be a base model because we will not run an inference each time the user creates a new layer.		
		return NOpenQueryUtil.getAllLayerNames(ProvisioningComponents.provisioningRepository.getBaseModel(), techName);
	}
	
	/* ======================================================================================
	 * Create
	 * ======================================================================================*/
		
	/** Insert content on a container */
	@RequestMapping(value = "/insertContainerITU", method = RequestMethod.POST)
	public @ResponseBody String insertContainer(@RequestParam("container") String container, @RequestParam("content") String content) 
	{	
		DtoJointElement dtoContainer = (DtoJointElement) JointUtilManager.getJavaFromJSON(container, DtoJointElement.class);
		DtoJointElement dtoContent = (DtoJointElement) JointUtilManager.getJavaFromJSON(content, DtoJointElement.class);
		
		try{
			InstanceFabricator.createComponentOfRelation(dtoContainer, dtoContent);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}		
		return "success";		
	}
		
	/** Bind the source element to the target element */
	/**
	 * @param sourceElement: id, name and type of the source element
	 * @param targetElement: id, name and type of the target element
	 * @param bind: id, name and type of the bind (name = id, type = "link")
	 * @return: "success" if the binding was successful
	 */
	@RequestMapping(value = "/performBind", method = RequestMethod.POST)
	public @ResponseBody String performBind(@RequestParam("sourceElement") String sourceElement, @RequestParam("targetElement") String targetElement,
	@RequestParam("bind") String bind) 
	{	
		DtoJointElement dtoSourceElement = (DtoJointElement) JointUtilManager.getJavaFromJSON(sourceElement, DtoJointElement.class);
		DtoJointElement dtoTargetElement = (DtoJointElement) JointUtilManager.getJavaFromJSON(targetElement, DtoJointElement.class);
		
		try{
			PerformBind.applyBinds(dtoSourceElement, dtoTargetElement, "ITU", StudioComponents.studioRepository);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}		

		return "success";		
	}
	
	/** Create a transport function on a layer or directly on a card */
	@RequestMapping(value = "/createTransportFunction", method = RequestMethod.POST)
	public @ResponseBody String createTransportFunction(@RequestParam("transportFunction") String transportFunction, @RequestParam("container") String container)
	{
		DtoJointElement dtoTransportFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(transportFunction, DtoJointElement.class);
		DtoJointElement dtoContainer = (DtoJointElement) JointUtilManager.getJavaFromJSON(container, DtoJointElement.class);

		try{
			SpecificDtoFabricator.createTransportFunction(StudioComponents.studioRepository,dtoTransportFunction, dtoContainer);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}		
		return "success";
	}
		
	
	/** Create a port (interface) of input or output connected to a transport function */
	@RequestMapping(value = "/createPort", method = RequestMethod.POST)
	public @ResponseBody String createPort(@RequestParam("port") String port, @RequestParam("transportFunction") String transportFunction) 
	{		
		DtoJointElement dtoTransportFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(transportFunction, DtoJointElement.class);
		DtoJointElement dtoPort = (DtoJointElement) JointUtilManager.getJavaFromJSON(port, DtoJointElement.class);
		 
		try{
			SpecificDtoFabricator.createPort(StudioComponents.studioRepository,dtoPort, dtoTransportFunction);		
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
		return "success";		
	}
		

	/** Create a connection between a source transport function and a target transport function */
	@RequestMapping(value = "/createLink", method = RequestMethod.POST)
	public @ResponseBody String createLink(@RequestParam("sourceTFunction") String sourceTFunction, @RequestParam("targetTFunction") String targetTFunction,
	@RequestParam("link") String link) 
	{
		DtoJointElement dtoSourceTFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(sourceTFunction, DtoJointElement.class);
		DtoJointElement dtoTargetTFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(targetTFunction, DtoJointElement.class);
		
		try{
			SpecificDtoFabricator.createLinkBetweenTFs(StudioComponents.studioRepository,dtoSourceTFunction, dtoTargetTFunction);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}	
		return "success";		
	}
	
	/* ======================================================================================
	 * Delete
	 * ======================================================================================*/
		
	/** Remove a container of a card */
	@RequestMapping(value = "/deleteContainer", method = RequestMethod.POST)
	public @ResponseBody String deleteContainer(@RequestParam("container") String container, @RequestParam("card") String card) 
	{
		
		DtoJointElement dtoContainer = (DtoJointElement) JointUtilManager.getJavaFromJSON(container, DtoJointElement.class);
		DtoJointElement dtoCard = (DtoJointElement) JointUtilManager.getJavaFromJSON(card, DtoJointElement.class);
		
		try{
			SpecificDtoFabricator.deleteLinkFromCardLayerToLayer(StudioComponents.studioRepository,dtoContainer, dtoCard);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}		
		return "success";
	}
	
	/** Remove a transport function */
	@RequestMapping(value = "/deleteTransportFunction", method = RequestMethod.POST)
	public @ResponseBody String deleteTransportFunction(@RequestParam("transportFunction") String transportFunction) 
	{	
		DtoJointElement dtoTransportFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(transportFunction, DtoJointElement.class);
		
		try{
			SpecificDtoFabricator.deleteTransportFunction(StudioComponents.studioRepository,dtoTransportFunction);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
		return "success";		
	}
	
	/** Remove a port (interface) of input or output */
	@RequestMapping(value = "/deletePort", method = RequestMethod.POST)
	public @ResponseBody String deletePort(@RequestParam("port") String port) 
	{
		DtoJointElement dtoPort = (DtoJointElement) JointUtilManager.getJavaFromJSON(port, DtoJointElement.class);
		try{
			SpecificDtoFabricator.deletePort(StudioComponents.studioRepository,dtoPort);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
		return "success";			
	}
		
	/** Remove a connection */
	@RequestMapping(value = "/deleteLink", method = RequestMethod.POST)
	public @ResponseBody String deleteLink(@RequestParam("sourceTFunction") String sourceTFunction, @RequestParam("targetTFunction") String targetTFunction,
	@RequestParam("link") String link) 
	{
		DtoJointElement dtoSourceTFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(sourceTFunction, DtoJointElement.class);
		DtoJointElement dtoTargetTFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(targetTFunction, DtoJointElement.class);
			
		try{
			SpecificDtoFabricator.deleteLinkBetweenTFs(StudioComponents.studioRepository, dtoSourceTFunction, dtoTargetTFunction); 
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
		return "success";			
	}
		
	/* ======================================================================================
	 * Name
	 * ======================================================================================*/
	
	/** Modify transport function name */
	@RequestMapping(value = "/setTransportFunctionName", method = RequestMethod.POST)
	public @ResponseBody String setTransportFunctionName(@RequestParam("transportFunction") String transportFunction)
	{
		DtoJointElement dtoTransportFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(transportFunction, DtoJointElement.class);

		try{
			SpecificDtoFabricator.setTransportFunctionName(dtoTransportFunction);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}	
		return "success";
	}
	
	/** Modify port name */
	@RequestMapping(value = "/setPortName", method = RequestMethod.POST)
	public @ResponseBody String setPortName(@RequestParam("port") String port) 
	{		
		DtoJointElement dtoPort = (DtoJointElement) JointUtilManager.getJavaFromJSON(port, DtoJointElement.class);
		 
		try{
			SpecificDtoFabricator.setPortName(dtoPort);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}	
		return "success";		
	}
	
	/* ======================================================================================
	 * Change
	 * ======================================================================================*/
	
	/** Change or remove (container=null) the container of a transport function */
	@RequestMapping(value = "/changeContainer", method = RequestMethod.POST)
	public @ResponseBody String changeContainer(@RequestParam("transportFunction") String transportFunction, @RequestParam("sourceContainer") String sourceContainer,
	@RequestParam("targetContainer") String targetContainer, @RequestParam("card") String card) 
	{
		DtoJointElement dtoTransportFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(transportFunction, DtoJointElement.class);
		DtoJointElement dtoSourceContainer = (DtoJointElement) JointUtilManager.getJavaFromJSON(sourceContainer, DtoJointElement.class);
		DtoJointElement dtoTargetContainer = (DtoJointElement) JointUtilManager.getJavaFromJSON(targetContainer, DtoJointElement.class);
		DtoJointElement dtoCard = (DtoJointElement) JointUtilManager.getJavaFromJSON(card, DtoJointElement.class);
		
		try{
			SpecificDtoFabricator.changeLayerOfTTF(StudioComponents.studioRepository,dtoTransportFunction, dtoSourceContainer, dtoTargetContainer, dtoCard); 
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
		return "success";		
	}
	
	/* ======================================================================================
	 * Verification
	 * ======================================================================================*/
		
	/** Verify if there are any elements without connection in the given card */
	@RequestMapping(value = "/verifyElementsOnCard", method = RequestMethod.POST)
	public @ResponseBody String[] verifyElementsOnCard(@RequestParam("card") String card)
	{
		DtoJointElement dtoCard = (DtoJointElement) JointUtilManager.getJavaFromJSON(card, DtoJointElement.class);
		
		String[] result = SpecificDtoFabricator.elementsWithNoConnection(dtoCard);		
		return result;
	}
	
	/** Checks if it is possible to create a transport function on a layer or directly to a card */
	@RequestMapping(value = "/canCreateTransportFunction", method = RequestMethod.POST)
	public @ResponseBody String canCreateTransportFunction(@RequestParam("transportFunction") String transportFunction, @RequestParam("container") String container)
	{
		DtoJointElement dtoTransportFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(transportFunction, DtoJointElement.class);
		DtoJointElement dtoContainer = (DtoJointElement) JointUtilManager.getJavaFromJSON(container, DtoJointElement.class);
		
		try{
			SpecificDtoFabricator.canCreateTransportFunction(dtoTransportFunction, dtoContainer);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}		
		return "true";
	}
		
	/** Check if it is possible to create a connection between a source transport function and a target transport function */
	@RequestMapping(value = "/canCreateLink", method = RequestMethod.POST)
	public @ResponseBody String canCreateLink(@RequestParam("sourceTFunction") String sourceTFunction, @RequestParam("targetTFunction") String targetTFunction) 
	{		
		DtoJointElement dtoSourceTFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(sourceTFunction, DtoJointElement.class);
		DtoJointElement dtoTargetTFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(targetTFunction, DtoJointElement.class);
		
		try{
			SpecificDtoFabricator.canCreateLinkBetweenTFs(StudioComponents.studioRepository,dtoSourceTFunction, dtoTargetTFunction);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}	
		return "true";		
	}
	
	/** Check if a bind can be performed from the source element to the target element (just check, don't perform the bind) */
	/**
	 * @param sourceElement: id, name and type of the source element
	 * @param targetElement: id, name and type of the target element
	 * @return: "true" if a bind can be performed
	 */
	@RequestMapping(value = "/canPerformBind", method = RequestMethod.POST)
	public @ResponseBody String canPerformBind(@RequestParam("sourceElement") String sourceElement, @RequestParam("targetElement") String targetElement) 
	{		
		DtoJointElement dtoSourceElement = (DtoJointElement) JointUtilManager.getJavaFromJSON(sourceElement, DtoJointElement.class);
		DtoJointElement dtoTargetElement = (DtoJointElement) JointUtilManager.getJavaFromJSON(targetElement, DtoJointElement.class);
		
		try{
			PerformBind.canCreateBind(dtoSourceElement, dtoTargetElement, StudioComponents.studioRepository);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}	
		
		return "true";		
	}
	
	/* ================================================
	 * Save & Load
	 * ================================================*/
	
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
	 * @return: atributos de TTF, para cada camada, no formato JSON
	 */
//	@RequestMapping(value = "/loadTTFAttributes", method = RequestMethod.POST)
//	public @ResponseBody String loadTTFAttributes()
//	{
//		String reference = "874.1"; //TODO: the reference must be gotten from a configuration file
//		String path = "itu-" + reference + "/";
//		path = NOpenFileUtil.replaceSlash(path);
//		NOpenFileUtil.createITUConfigurationRepository(path);
//		 
//		File file = new File(path + "ttf.json");
//
//		try {
//			if(!file.exists()){
//				String pathSource = "/attributes/itu-" + reference + "/ttf.json";
//				InputStream is = ITUStudioController.class.getResourceAsStream(pathSource);
//				StringWriter writer = new StringWriter();
//				IOUtils.copy(is, writer, "UTF-8");
//				String theString = writer.toString();
//				
//				File f = NOpenFileUtil.createFile(NOpenFileUtil.ituConfigurationJSONFolder + path, "ttf.json");
//				NOpenFileUtil.writeToFile(f, theString);
//			}
//		} catch (IOException e) {
//		    e.printStackTrace();
//		}
//		
//		return NOpenFileUtil.openItuConfigurationJSONFileAsString(path + "ttf.json");
//		
//	}
	
	/**
	 * @param transportFunction: id, name and type of a transport function
	 * @return: atributos of the given transport function in json format
	 */
	@RequestMapping(value = "/loadTransportFunctionAttributes", method = RequestMethod.POST)
	public @ResponseBody String loadTransportFunctionAttributes(@RequestParam("transportFunction") String transportFunction)
	{
		DtoJointElement dtoTransportFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(transportFunction, DtoJointElement.class);
		return new String();
	}
	
	/**
	 * @param fileName: nome do arquivo contendo o grafo desejado no formato JSON (nome do card que se deseja abrir, no caso)
	 * @return: conteudo do grafo no formato JSON or error
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
	 * Procedure to open a ITU file of a Equipment.
	 * @param path
	 * @param ituFilename
	 * @return
	 */
	@RequestMapping(value = "/openITUFileEquipment", method = RequestMethod.POST)
	protected @ResponseBody String openITUFileEquipment(@RequestParam("path") String path, @RequestParam("filename") String ituFilename)
	{		
		path = path + "/itu/" + ituFilename + ".json";
		path = NOpenFileUtil.replaceSlash(path);		
		return NOpenFileUtil.openEquipmentJSONFileAsString(path);		
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
	@RequestMapping("/deleteITUFileOfEquipmentFolder")
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
	@RequestMapping("/copyITUFilesOfEquipmentFolder")
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
