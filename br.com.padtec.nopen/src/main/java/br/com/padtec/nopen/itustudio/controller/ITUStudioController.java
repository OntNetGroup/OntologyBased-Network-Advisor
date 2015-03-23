package br.com.padtec.nopen.itustudio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ITUStudioController {
	
	/* ----- CRUD for Transport Function ----- */
	/**
	 * @param id: identificador do transport function a ser criado
	 * @param layer: camada sobre a qual o transport function deve ser criado
	 * @return
	 */
	@RequestMapping(value = "/createTransportFunction", method = RequestMethod.POST)
	public @ResponseBody String createTransportFunction(@RequestParam("id") String id, @RequestParam("layer") String layer) {
		return null;
	}
	
	/**
	 * @param id: identificador do transport function a ser deletado
	 * @return
	 */
	@RequestMapping(value = "/deleteTransportFunction", method = RequestMethod.POST)
	public @ResponseBody String deleteTransportFunction(@RequestParam("id") String id) {
		return null;
	}
	
	
	/* ----- CRUD for port ----- */
	/**
	 * @param portID: identificador da porta a ser criada
	 * @param transportFunctionID: id do transport function no qual a porta será adicionada
	 * @return
	 */
	@RequestMapping(value = "/createPort", method = RequestMethod.POST)
	public @ResponseBody String createPort(@RequestParam("portID") String portID, @RequestParam("transportFunctionID") String transportFunctionID) {
		return null;
	}
	
	/**
	 * @param id: identificador da porta a ser deletada
	 * @return
	 */
	@RequestMapping(value = "/deletePort", method = RequestMethod.POST)
	public @ResponseBody String deletePort(@RequestParam("id") String id) {
		return null;
	}
	
	/* ----- CRUD for link ----- */
	/**
	 * @param linkID: identificador do link a ser criado
	 * @param sourcePortID: identificador da porta de origem do link
	 * @param targetPortID: identificador da porta destino do link
	 * @return
	 */
	@RequestMapping(value = "/createLink", method = RequestMethod.POST)
	public @ResponseBody String createLink(@RequestParam("linkID") String linkID, @RequestParam("sourcePortID") String sourcePortID, @RequestParam("targetPortID") String targetPortID) {
		return null;
	}
	
	/**
	 * @param id: identificador do link a ser deletado
	 * @return
	 */
	@RequestMapping(value = "/deleteLink", method = RequestMethod.POST)
	public @ResponseBody String deleteLink(@RequestParam("id") String id) {
		return null;
	}
}
