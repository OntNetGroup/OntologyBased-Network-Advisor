package br.com.padtec.nopen.topology.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.nopen.topology.service.TopologyExporter;
import br.com.padtec.nopen.topology.service.TopologyImporter;

@Controller
public class TopologyController {

	@RequestMapping(value = "/exportTopology", method = RequestMethod.POST)
	protected @ResponseBody String exportTopology(@RequestParam("json") String json){
		
		TopologyExporter topology = new TopologyExporter();
		String xml = topology.exportTopology(json);
		
		return xml;
	}
	
	@RequestMapping(value = "/importTopology", method = RequestMethod.POST)
	protected @ResponseBody String importTopology(HttpServletRequest request){
			
		TopologyImporter topology = new TopologyImporter();
		String json = topology.importTopology(request);
		
		return json;
	}
	
}
