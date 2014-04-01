package br.ufes.inf.padtec.tnokco.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class VisualizationController {

	@RequestMapping(method = RequestMethod.GET, value="/open_visualizator")
	public String open_visualizator(HttpServletRequest request) {
		return "open_visualizator";
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/allSites")
	public String allSites(HttpServletRequest request) {
		/* *
		 * valuesGraph
		 * subtitle
		 * width
		 * height
		 * 
		 * */
		
		return "open_visualizator";
	}
}
