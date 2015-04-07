package br.com.padtec.nopen.studio.itu.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.nopen.studio.model.StudioFactory;
import br.com.padtec.nopen.studio.model.StudioSerializator;

@Controller
public class ITUStudioController {
	
	//REMOVER DEPOIS QUE TIVER TERMINADO
	@RequestMapping("/itu-studio")
	public String networkTopologyRequest() {
		return "itu-studio/itu-studio";
	}
	
	/* ----- CRUD for Container ----- */
	/** Insere um container no card cardID
	 * @param containerName: nome do container
	 * @param containerType: tipo do container
	 * @param cardID: identificador do card que deverá ser composto do container
	 * @return
	 */
	@RequestMapping(value = "/insertContainer", method = RequestMethod.POST)
	public @ResponseBody String insertContainer(@RequestParam("containerName") String containerName,
											@RequestParam("containerType") String containerType, 
											@RequestParam("cardID") String cardID) {
		return "success";
	}
	
	/** Remove um container do card cardID
	 * @param containerName: nome do container
	 * @param containerType: tipo do container
	 * @param cardID: identificador do card que contém a camada
	 * @return: success or error
	 */
	@RequestMapping(value = "/deleteContainer", method = RequestMethod.POST)
	public @ResponseBody String deleteContainer(@RequestParam("containerName") String containerName, @RequestParam("containerType") String containerType,
												@RequestParam("cardID") String cardID) {
		return "success";
	}
	
	/* ----- CRUD for Transport Function ----- */
	/** Cria um tranport function transportFunctionID na camada layer
	 * @param tFunctionID: identificador do transport function a ser criado
	 * @param tFunctionType: tipo do transport function a ser criado (e.g., TTF, AF etc.)
	 * @param containerName: nome do container sobre a qual o transport function deve ser criado (can be null)
	 * @param containerType: tipo do container sobre a qual o transport function deve ser criado (can be null)
	 * @param cardID: identificador do card 
	 * @return
	 */
	@RequestMapping(value = "/createTransportFunction", method = RequestMethod.POST)
	public @ResponseBody String createTransportFunction(@RequestParam("tFunctionID") String tFunctionID,
														@RequestParam("tFunctionType") String tFunctionType,
														@RequestParam("containerName") String containerName,
														@RequestParam("containerType") String containerType,
														@RequestParam("cardID") String cardID)
	{
		/**===========================================================
		 * Create Transport Function
		 * =========================================================== */
//		TODO: StudioFactory.createTransportFunction(tFunctionID, tFunctionType, containerName, containerType, cardID);
		
		return "success";
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
	public @ResponseBody String canCreateTransportFunction(	@RequestParam("tFunctionID") String tFunctionID,
															@RequestParam("tFunctionType") String tFunctionType,
															@RequestParam("containerName") String containerName,
															@RequestParam("containerType") String containerType,
															@RequestParam("cardID") String cardID)
	{
		return "true";
	}
	
	/** Remove o transport function cujo identificador é id
	 * @param id: identificador do transport function a ser deletado
	 * @return: success or error
	 */
	@RequestMapping(value = "/deleteTransportFunction", method = RequestMethod.POST)
	public @ResponseBody String deleteTransportFunction(@RequestParam("id") String id) 
	{
		/**===========================================================
		 * Delete Transport Function
		 * =========================================================== */
		StudioFactory.deleteTransportFunction(id);
		
		return "success";
	}
	
	
	/** Troca ou retira (container=null) o container do transport function
	 * @param tFunctionID: identificador do transport function que terá container será trocado
	 * @param containerName: nome do novo container do transport function (can be null)
	 * @param containerType: tipo do novo container do transport function (can be null)
	 * @param cardID: identificador do card
	 * @return
	 */
	@RequestMapping(value = "/changeContainer", method = RequestMethod.POST)
	public @ResponseBody String changeContainer(@RequestParam("tFunctionID") String tFunctionID,
												@RequestParam("containerName") String containerName,
												@RequestParam("containerType") String containerType,
												@RequestParam("cardID") String cardID) 
	{
		
		return "success";
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
		/**===========================================================
		 * Create Port
		 * =========================================================== */
		StudioFactory.createPort(portID, transportFunctionID);
		
		return "success";
	}
	
	/** Remove uma porta (interface) de entrada ou saída
	 * @param id: identificador da porta a ser deletada
	 * @return: success or error
	 */
	@RequestMapping(value = "/deletePort", method = RequestMethod.POST)
	public @ResponseBody String deletePort(@RequestParam("id") String id) 
	{
		/**===========================================================
		 * Delete Port
		 * =========================================================== */
		StudioFactory.deletePort(id);
		
		return "success";		
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
		
		/**===========================================================
		 * Create Link
		 * =========================================================== */
		
		return new String();
	}
	
	/** Verifica se é possível criar uma conexão entre sourceTFunctionID e targetTFunctionID
	 * @param sourceTFunctionID
	 * @param targetTFunctionID
	 * @return
	 */
	@RequestMapping(value = "/canCreateLink", method = RequestMethod.POST)
	public @ResponseBody String canCreateLink(@RequestParam("sourceTFunctionID") String sourceTFunctionID, @RequestParam("targetTFunctionID") String targetTFunctionID) {
		return "true";
	}
	
	/** Remove uma conexão
	 * @param id: identificador do link a ser deletado
	 * @return: success or error
	 */
	@RequestMapping(value = "/deleteLink", method = RequestMethod.POST)
	public @ResponseBody String deleteLink(@RequestParam("id") String id) 
	{
		/**===========================================================
		 * Delete Link
		 * =========================================================== */
		StudioFactory.deleteLink(id);
		
		return "success";	
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
			/**===========================================================
			 * Serialize JSON
			 * =========================================================== */			
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
			/**===========================================================
			 * Deserialize JSON
			 * =========================================================== */
			json = StudioSerializator.deserialize(fileName);
		
		} catch (IOException e) {
			errorMsg = "Serialize JOINT Error: "+ e.getMessage();	
			return errorMsg;
		}		
		return json;	
	}
}
