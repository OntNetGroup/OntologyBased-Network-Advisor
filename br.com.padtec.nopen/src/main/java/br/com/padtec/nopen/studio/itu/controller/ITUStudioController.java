package br.com.padtec.nopen.studio.itu.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.nopen.model.DtoJointElement;
import br.com.padtec.nopen.model.NOpenQueries;
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
		

//		TODO: boolean success = StudioFactory.insertContainer(containerName, containerType, cardID);
		
		boolean success = true; //TODO: remover
		if(success) return "success";
		else return "failed";
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
		
		
//		TODO: boolean success = StudioFactory.deleteContainer(containerName, containerType, cardID);
		
		boolean success = true; //TODO: remover
		if(success) return "success";
		else return "failed";
	}
	
	/* ----- CRUD for Transport Function ----- */
	
	/** Cria um tranport function sobre uma camada ou diretamente sobre o card
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
		
//		TODO: boolean success = StudioFactory.createTransportFunction(tFunctionID, tFunctionType, containerName, containerType, cardID);
		
		boolean success = true; //TODO: remover
		if(success) return "success";
		else return "failed";
	}
		
	/** Verifica se é possível criar o tranport function
	 * @param tFunctionID: identificador do transport function a ser criado
	 * @param tFunctionType: tipo do transport function a ser criado (e.g., TTF, AF etc.)
	 * @param containerName: nome do container sobre a qual o transport function deve ser criado
	 * @param containerType: tipo do container sobre a qual o transport function deve ser criado
	 * @param cardID: identificador do card 
	 * @return
	 */
	@RequestMapping(value = "/canCreateTransportFunction", method = RequestMethod.POST)
	public @ResponseBody String canCreateTransportFunction(	@RequestParam("tFunctionID") String tFunctionID, @RequestParam("tFunctionType") String tFunctionType,
	@RequestParam("containerName") String containerName, @RequestParam("containerType") String containerType, @RequestParam("cardID") String cardID)
	{
		boolean success = StudioFactory.canCreateTransportFunction(tFunctionID, tFunctionType, containerName, containerType, cardID);		
		if(success) return "true";
		else return "false";
	}
	
	/** Remove o transport function cujo identificador é id
	 * @param id: identificador do transport function a ser deletado
	 * @return: success or error
	 */
	@RequestMapping(value = "/deleteTransportFunction", method = RequestMethod.POST)
	public @ResponseBody String deleteTransportFunction(@RequestParam("id") String id) 
	{	
		boolean success = StudioFactory.deleteTransportFunction(id);		
		if(success) return "success";
		else return "failed";
	}
		
	/** Troca ou retira (container=null) o container do transport function
	 * @param tFunctionID: identificador do transport function que terá container será trocado
	 * @param containerName: nome do novo container do transport function (can be null)
	 * @param containerType: tipo do novo container do transport function (can be null)
	 * @param cardID: identificador do card
	 * @return
	 */
	@RequestMapping(value = "/changeContainer", method = RequestMethod.POST)
	public @ResponseBody String changeContainer(@RequestParam("tFunctionID") String tFunctionID, @RequestParam("containerName") String containerName,
	@RequestParam("containerType") String containerType, @RequestParam("cardID") String cardID) 
	{
		boolean success = StudioFactory.changeContainer(tFunctionID, containerName, containerType, cardID);		
		if(success) return "success";
		else return "failed";
	}
		
	/* ----- CRUD for port ----- */
	
	/** Cria uma porta (interface) de entrada ou saída conectada a transportFunctionID
	 * @param portID: identificador da porta a ser criada
	 * @param transportFunctionID: id do transport function no qual a porta será conectada
	 * @return: success or error
	 */
	@RequestMapping(value = "/createPort", method = RequestMethod.POST)
	public @ResponseBody String createPort(@RequestParam("portID") String portID, @RequestParam("transportFunctionID") String transportFunctionID) 
	{		
		boolean success = StudioFactory.createPort(portID, transportFunctionID);		
		if(success) return "success";
		else return "failed";
	}
		
	/** Remove uma porta (interface) de entrada ou saída
	 * @param id: identificador da porta a ser deletada
	 * @return: success or error
	 */
	@RequestMapping(value = "/deletePort", method = RequestMethod.POST)
	public @ResponseBody String deletePort(@RequestParam("id") String id) 
	{
		boolean success = StudioFactory.deletePort(id);		
		if(success) return "success";
		else return "failed";	
	}
	
	/* ----- CRUD for link ----- */
	
	/** Cria uma conexão entre sourceTFunctionID e targetTFunctionID
	 * @param sourceTFunctionID
	 * @param targetTFunctionID
	 * @return: id do link criado or error
	 */
	@RequestMapping(value = "/createLink", method = RequestMethod.POST)
	public @ResponseBody String createLink(@RequestParam("sourceTFunctionID") String sourceTFunctionID, @RequestParam("targetTFunctionID") String targetTFunctionID) 
	{
		boolean success = StudioFactory.createLink(sourceTFunctionID,targetTFunctionID);		
		if(success) return "success";
		else return "failed";
	}
	
	/** Verifica se é possível criar uma conexão entre sourceTFunctionID e targetTFunctionID
	 * @param sourceTFunctionID
	 * @param targetTFunctionID
	 * @return
	 */
	@RequestMapping(value = "/canCreateLink", method = RequestMethod.POST)
	public @ResponseBody String canCreateLink(@RequestParam("sourceTFunctionID") String sourceTFunctionID, @RequestParam("targetTFunctionID") String targetTFunctionID) 
	{		
		boolean success = StudioFactory.canCreateLink(sourceTFunctionID,targetTFunctionID);		
		if(success) return "true";
		else return "false";
	}
	
	/** Remove uma conexão
	 * @param id: identificador do link a ser deletado
	 * @return: success or error
	 */
	@RequestMapping(value = "/deleteLink", method = RequestMethod.POST)
	public @ResponseBody String deleteLink(@RequestParam("id") String id) 
	{		
		boolean success =  StudioFactory.deleteLink(id);		
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
