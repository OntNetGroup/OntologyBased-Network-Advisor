package br.com.padtec.nopen.itustudio.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.nopen.itustudio.core.ITUStudioFactory;
import br.com.padtec.nopen.itustudio.core.ITUStudioSerializator;

@Controller
public class ITUStudioController {
	
	/* ----- CRUD for Transport Function ----- */
	/**
	 * @param id: identificador do transport function a ser criado
	 * @param layer: camada sobre a qual o transport function deve ser criado
	 * @return: success or error
	 */
	@RequestMapping(value = "/createTransportFunction", method = RequestMethod.POST)
	public @ResponseBody String createTransportFunction(@RequestParam("id") String id, @RequestParam("layer") String layer) 
	{
		/**===========================================================
		 * Create Transport Function
		 * =========================================================== */
		ITUStudioFactory.createTransportFunction(id,layer);
		
		return new String();
	}
	
	/**
	 * @param id: identificador do transport function a ser deletado
	 * @return: success or error
	 */
	@RequestMapping(value = "/deleteTransportFunction", method = RequestMethod.POST)
	public @ResponseBody String deleteTransportFunction(@RequestParam("id") String id) 
	{
		/**===========================================================
		 * Delete Transport Function
		 * =========================================================== */
		ITUStudioFactory.deleteTransportFunction(id);
		
		return new String();
	}
	
	
	/* ----- CRUD for port ----- */
	/**
	 * @param portID: identificador da porta a ser criada
	 * @param transportFunctionID: id do transport function no qual a porta será adicionada
	 * @return: success or error
	 */
	@RequestMapping(value = "/createPort", method = RequestMethod.POST)
	public @ResponseBody String createPort(@RequestParam("portID") String portID, @RequestParam("transportFunctionID") String transportFunctionID) 
	{
		/**===========================================================
		 * Create Port
		 * =========================================================== */
		ITUStudioFactory.createPort(portID, transportFunctionID);
		
		return new String();
	}
	
	/**
	 * @param id: identificador da porta a ser deletada
	 * @return: success or error
	 */
	@RequestMapping(value = "/deletePort", method = RequestMethod.POST)
	public @ResponseBody String deletePort(@RequestParam("id") String id) 
	{
		/**===========================================================
		 * Delete Port
		 * =========================================================== */
		ITUStudioFactory.deletePort(id);
		
		return new String();		
	}
	
	/* ----- CRUD for link ----- */
	/**
	 * @param linkID: identificador do link a ser criado
	 * @param sourcePortID: identificador da porta de origem do link
	 * @param targetPortID: identificador da porta destino do link
	 * @return: success or error
	 */
	@RequestMapping(value = "/createLink", method = RequestMethod.POST)
	public @ResponseBody String createLink(@RequestParam("linkID") String linkID, @RequestParam("sourcePortID") String sourcePortID, @RequestParam("targetPortID") String targetPortID) 
	{
		
		/**===========================================================
		 * Create Link
		 * =========================================================== */
		ITUStudioFactory.createLink(linkID, sourcePortID, targetPortID);
		
		return new String();	
	}
	
	/**
	 * @param id: identificador do link a ser deletado
	 * @return: success or error
	 */
	@RequestMapping(value = "/deleteLink", method = RequestMethod.POST)
	public @ResponseBody String deleteLink(@RequestParam("id") String id) 
	{
		/**===========================================================
		 * Delete Link
		 * =========================================================== */
		ITUStudioFactory.deleteLink(id);
		
		return new String();	
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
			ITUStudioSerializator.serialize(graphJSON, fileName);
		} catch (IOException e) {
			errorMsg = "Serialize JOINT Error: "+ e.getMessage();	
		}		
		return errorMsg;	
	}
	
	/**
	 * @param fileName: nome do arquivo contendo o grafo desejado no formato JSON (nome do card que se deseja abrir, no caso)
	 * @return: conteúdo do grafo no formato JSON
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
			json = ITUStudioSerializator.deserialize(fileName);
		
		} catch (IOException e) {
			errorMsg = "Serialize JOINT Error: "+ e.getMessage();	
			return errorMsg;
		}		
		return json;	
	}
}
