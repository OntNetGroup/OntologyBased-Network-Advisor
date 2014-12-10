package br.com.padtec.okco.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.padtec.common.application.VisualizerApp;
import br.com.padtec.common.graph.GraphPlotting;
import br.com.padtec.common.graph.WOKCOGraphPlotting;
import br.com.padtec.common.types.URIDecoder;

/**
 * Controller responsible for the graph visualization.
 * See this class: {@link VisualizerApp} 
 */

@Controller
public class VisualizerController {

	@RequestMapping(method = RequestMethod.GET, value="/graphVisualizer")
	public String graphVisualizer(@RequestParam("uri") String uri, @RequestParam("typeView") String typeView, HttpServletRequest request) 
	{
		/** Decode URIs First */
		uri = URIDecoder.decodeURI(uri);
		
		/** ==================================================
		 * Get Values of Graph
		 *  =================================================== */
		GraphPlotting graphPlotting = new WOKCOGraphPlotting();
		String valuesGraph = VisualizerApp.getGraphValues(typeView,uri, graphPlotting);
		request.getSession().setAttribute("valuesGraph", valuesGraph);
	
		/** ==================================================
		 * Get Width, Height and Subtitle of Graph
		 *  =================================================== */
		int width  = graphPlotting.width;
		int height = graphPlotting.height;
		String subtitle = graphPlotting.getSubtitle();		
		request.getSession().setAttribute("width", width);
		request.getSession().setAttribute("height", height);
		request.getSession().setAttribute("subtitle", subtitle);
		
		return "graphVisualizer";
	}
}