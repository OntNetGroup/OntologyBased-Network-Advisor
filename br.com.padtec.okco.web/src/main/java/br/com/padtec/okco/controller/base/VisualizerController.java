package br.com.padtec.okco.controller.base;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.padtec.common.graph.BaseGraphPlotting;
import br.com.padtec.common.types.URIDecoder;
import br.com.padtec.okco.core.application.OKCoComponents;
import br.com.padtec.okco.core.graph.OKCoGraphPlotting;

/**
 * Controller responsible for the graph visualization.
 * See this class: {@link OKCoComponents} 
 */

public class VisualizerController {

	@RequestMapping(method = RequestMethod.GET, value="/graphVisualizer")
	public String graphVisualizer(@RequestParam("uri") String uri, @RequestParam("typeView") String typeView, HttpServletRequest request) 
	{
		/** Decode URIs First */
		uri = URIDecoder.decodeURI(uri);
		
		/** ==================================================
		 * Get Values of Graph
		 *  =================================================== */
		BaseGraphPlotting graphPlotting = new OKCoGraphPlotting();
		String valuesGraph = OKCoComponents.visualizer.getGraphValues(typeView,uri, graphPlotting);
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
