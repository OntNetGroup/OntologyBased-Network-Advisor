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
	
	/* ----- CRUD for Layer ----- */
	/** Insere uma camada no card cardID
	 * @param layer: camada a ser inserida (e.g., OTS)
	 * @param cardID: identificador do card que deverá ser composto da camada
	 * @return: success or error
	 */
	@RequestMapping(value = "/insertLayer", method = RequestMethod.POST)
	public @ResponseBody String insertLayer(@RequestParam("layer") String layer, @RequestParam("cardID") String cardID) {
		return "success";
	}
	
	/** Remove uma camada 
	 * @param layer: camada a ser removida (e.g., OTS)
	 * @param cardID: identificador do card que contém a camada
	 * @return: success or error
	 */
	@RequestMapping(value = "/deleteLayer", method = RequestMethod.POST)
	public @ResponseBody String deleteLayer(@RequestParam("layer") String layer, @RequestParam("cardID") String cardID) {
		return "success";
	}
	
	/* ----- CRUD for Transport Function ----- */
	/** Cria um tranport function transportFunctionID na camada layer, ou diretamente no card (layer=null)
	 * @param id: identificador do transport function a ser criado
	 * @param layer: camada sobre a qual o transport function deve ser criado (pode ser nulo)
	 * @return: success or error
	 */
	@RequestMapping(value = "/createTransportFunction", method = RequestMethod.POST)
	public @ResponseBody String createTransportFunction(@RequestParam("id") String id, @RequestParam("layer") String layer) 
	{
		/**===========================================================
		 * Create Transport Function
		 * =========================================================== */
		StudioFactory.createTransportFunction(id,layer);
		
		return "success";
	}
	
	/** Verifica se é possível criar um tranport function transportFunctionID na camada layer, ou diretamente no card (layer=null)
	 * @param id
	 * @param layer (pode ser nulo)
	 * @return
	 */
	@RequestMapping(value = "/canCreateTransportFunction", method = RequestMethod.POST)
	public @ResponseBody String canCreateTransportFunction(@RequestParam("id") String transportFunctionID, @RequestParam("layer") String layer) {
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
