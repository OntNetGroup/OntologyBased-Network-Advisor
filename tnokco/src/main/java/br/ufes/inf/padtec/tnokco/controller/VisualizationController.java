package br.ufes.inf.padtec.tnokco.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.inf.nemo.padtec.tnokco.TNOKCOGraphPlotting;

@Controller
public class VisualizationController {

	@RequestMapping(method = RequestMethod.GET, value="/open_visualizator")
	public String open_visualizator(HttpServletRequest request) {
		return "open_visualizator";
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/open_network_visualization")
	public String open_network_visualization(@RequestParam("visualization") String visualization, HttpServletRequest request) {
		/* *
		 * valuesGraph
		 * subtitle
		 * width
		 * height
		 * 
		 * */
		
		TNOKCOGraphPlotting graphPlotting = new TNOKCOGraphPlotting();
		
		String valuesGraph = "";
		
		if(visualization.equals("allSites")){
//			valuesGraph = graphPlotting.getArborStructureFromClass(HomeController.InfModel,HomeController.NS+"Site");	
			valuesGraph = graphPlotting.getArborStructureFromClass(HomeController.Model,HomeController.NS+"Site");
		}
		
		int width  = graphPlotting.width;
		int height = graphPlotting.height;
		String subtitle = graphPlotting.getSubtitle();
		
		//session
		request.getSession().setAttribute("valuesGraph", valuesGraph);
		request.getSession().setAttribute("width", width);
		request.getSession().setAttribute("height", height);
		request.getSession().setAttribute("subtitle", subtitle);
		
		return "showVisualization";
	}
}
