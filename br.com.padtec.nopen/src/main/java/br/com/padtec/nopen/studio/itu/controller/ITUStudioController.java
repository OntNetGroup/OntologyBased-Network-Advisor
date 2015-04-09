package br.com.padtec.nopen.studio.itu.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.nopen.model.DtoJointElement;
import br.com.padtec.nopen.service.util.NOpenQueries;
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
	
	@RequestMapping(value = "/allLayers", method = RequestMethod.POST)
	public @ResponseBody String[][] getAllLayersNames() 
	{
		return NOpenQueries.getLayerNames();
	}
	
	/* ----- CRUD for Container ----- */
	
	/** Insere um container no card
	 * @param container
	 * @param card
	 * @return
	 */
	@RequestMapping(value = "/insertContainer", method = RequestMethod.POST)
	public @ResponseBody String insertContainer(@RequestParam("container") String container, @RequestParam("container") String card) 
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
	 * @param card 
	 * @return
	 */
	@RequestMapping(value = "/createTransportFunction", method = RequestMethod.POST)
	public @ResponseBody String createTransportFunction(@RequestParam("transportFunction") String transportFunction, @RequestParam("container") String container,
	@RequestParam("card") String card)
	{
		DtoJointElement dtoTransportFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(transportFunction, DtoJointElement.class);
		DtoJointElement dtoContainer = (DtoJointElement) JointUtilManager.getJavaFromJSON(container, DtoJointElement.class);
		DtoJointElement dtoCard = (DtoJointElement) JointUtilManager.getJavaFromJSON(card, DtoJointElement.class);		

		try{
			StudioFactory.createTransportFunction(dtoTransportFunction, dtoContainer, dtoCard);
		}catch(Exception e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}		
		return "success";
	}
		
	/** Verifica se é possível criar o transport function sobre uma camada ou diretamente sobre o card
	 * @param transportFunction
	 * @param container
	 * @param card 
	 * @return
	 */
	@RequestMapping(value = "/canCreateTransportFunction", method = RequestMethod.POST)
	public @ResponseBody String canCreateTransportFunction(@RequestParam("transportFunction") String transportFunction, @RequestParam("container") String container,
	@RequestParam("card") String card)
	{
		DtoJointElement dtoTransportFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(transportFunction, DtoJointElement.class);
		DtoJointElement dtoContainer = (DtoJointElement) JointUtilManager.getJavaFromJSON(container, DtoJointElement.class);
		DtoJointElement dtoCard = (DtoJointElement) JointUtilManager.getJavaFromJSON(card, DtoJointElement.class);
		
//		TODO: boolean success = StudioFactory.canCreateTransportFunction(tFunctionID, tFunctionType, containerName, containerType, cardID);
		boolean success = true; //TODO: remover
		if(success) return "true";
		else return "false";
	}
	
	/** Remove um transport function
	 * @param transportFunction
	 * @return
	 */
	@RequestMapping(value = "/deleteTransportFunction", method = RequestMethod.POST)
	public @ResponseBody String deleteTransportFunction(@RequestParam("transportFunction") String transportFunction) 
	{	
		DtoJointElement dtoTransportFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(transportFunction, DtoJointElement.class);
		
//		TODO: boolean success = StudioFactory.deleteTransportFunction(id);
		boolean success = true; //TODO: remover
		if(success) return "success";
		else return "failed";
	}
		
	/** Troca ou retira (container=null) o container do transport function
	 * @param transportFunction
	 * @param container
	 * @param card 
	 * @return
	 */
	@RequestMapping(value = "/changeContainer", method = RequestMethod.POST)
	public @ResponseBody String changeContainer(@RequestParam("transportFunction") String transportFunction, @RequestParam("container") String container,
	@RequestParam("card") String card) 
	{
		DtoJointElement dtoTransportFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(transportFunction, DtoJointElement.class);
		DtoJointElement dtoContainer = (DtoJointElement) JointUtilManager.getJavaFromJSON(container, DtoJointElement.class);
		DtoJointElement dtoCard = (DtoJointElement) JointUtilManager.getJavaFromJSON(card, DtoJointElement.class);
		boolean success = true; //TODO: remover
//		TODO: boolean success = StudioFactory.changeContainer(tFunctionID, containerName, containerType, cardID);		
		if(success) return "success";
		else return "failed";
	}
		
	/* ----- CRUD for port ----- */
	
	/** Cria uma porta (interface) de entrada ou saída conectada a um transport function
	 * @param port
	 * @param transportFunction
	 * @return
	 */
	@RequestMapping(value = "/createPort", method = RequestMethod.POST)
	public @ResponseBody String createPort(@RequestParam("port") String port, @RequestParam("transportFunction") String transportFunction) 
	{		
		DtoJointElement dtoTransportFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(transportFunction, DtoJointElement.class);
		DtoJointElement dtoPort = (DtoJointElement) JointUtilManager.getJavaFromJSON(port, DtoJointElement.class);
		boolean success = true; //TODO: remover
//		TODO: boolean success = StudioFactory.createPort(portID, transportFunctionID);		
		if(success) return "success";
		else return "failed";
	}
		
	/** Remove uma porta (interface) de entrada ou saída
	 * @param port
	 * @return
	 */
	@RequestMapping(value = "/deletePort", method = RequestMethod.POST)
	public @ResponseBody String deletePort(@RequestParam("port") String port) 
	{
		DtoJointElement dtoPort = (DtoJointElement) JointUtilManager.getJavaFromJSON(port, DtoJointElement.class);
//		TODO: boolean success = StudioFactory.deletePort(id);
		boolean success = true; //TODO: remover
		if(success) return "success";
		else return "failed";	
	}
	
	/* ----- CRUD for link ----- */
	
	/** Cria uma conexão entre sourceTFunction e targetTFunction
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
		boolean success = true; //TODO: remover
//		TODO: boolean success = StudioFactory.createLink(sourceTFunctionID,targetTFunctionID);		
		if(success) return "success";
		else return "failed";
	}
	
	/** Verifica se é possível criar uma conexão entre sourceTFunction e targetTFunction
	 * @param sourceTFunction
	 * @param targetTFunction
	 * @return
	 */
	@RequestMapping(value = "/canCreateLink", method = RequestMethod.POST)
	public @ResponseBody String canCreateLink(@RequestParam("sourceTFunction") String sourceTFunction, @RequestParam("targetTFunction") String targetTFunction) 
	{		
		DtoJointElement dtoSourceTFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(sourceTFunction, DtoJointElement.class);
		DtoJointElement dtoTargetTFunction = (DtoJointElement) JointUtilManager.getJavaFromJSON(targetTFunction, DtoJointElement.class);
		boolean success = true; //TODO: remover
//		TODO: boolean success = StudioFactory.canCreateLink(sourceTFunctionID,targetTFunctionID);		
		if(success) return "true";
		else return "false";
	}
	
	/** Remove uma conexão
	 * @param link: identificador do link a ser deletado
	 * @return: success or error
	 */
	@RequestMapping(value = "/deleteLink", method = RequestMethod.POST)
	public @ResponseBody String deleteLink(@RequestParam("link") String link) 
	{
		DtoJointElement dtoLink = (DtoJointElement) JointUtilManager.getJavaFromJSON(link, DtoJointElement.class);
		boolean success = true; //TODO: remover
//		TODO: boolean success =  StudioFactory.deleteLink(id);		
		if(success) return "success";
		else return "failed";	
	}
	
	/* ----- Save/Load graph  ----- */
	/**
	 * @param graphJSON: conteúdo do grafo no formato JSON
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
	 * @param fileName: nome do arquivo contendo o grafo desejado no formato JSON (nome do card que se deseja abrir, no caso)
	 * @return: conteúdo do grafo no formato JSON or error
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
}
