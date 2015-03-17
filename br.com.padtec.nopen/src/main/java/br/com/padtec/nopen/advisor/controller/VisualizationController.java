package br.com.padtec.nopen.advisor.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.padtec.advisor.core.application.AdvisorService;
import br.com.padtec.advisor.core.application.BindsVisualizator;
import br.com.padtec.advisor.core.application.ConnectsVisualizator;
import br.com.padtec.advisor.core.application.GeneralConnects;
import br.com.padtec.advisor.core.application.HTMLFigureMapper;
import br.com.padtec.advisor.core.application.Visualizator;
import br.com.padtec.advisor.core.queries.AdvisorQueryUtil;
import br.com.padtec.advisor.core.util.PerformanceUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

@Controller
public class VisualizationController {
	 
	@RequestMapping(method = RequestMethod.GET, value="/open-visualizer")
	public String open_visualizator(HttpServletRequest request) 
	{
		if(OKCoUploader.getBaseModel() == null) return "advisor/views/open-visualizer"; 

		/**===========================================================
		 * Initialize figure mapper
		 * =========================================================== */
		HTMLFigureMapper.init();
		
		/**===========================================================
		 * Get Sites and Equipments
		 * =========================================================== */
		List<String> sites = AdvisorQueryUtil.getSitesURI();
		List<String> equipments = AdvisorQueryUtil.getEquipmentsURI();		
		request.getSession().setAttribute("sites", sites);
		request.getSession().setAttribute("equipments", equipments);
		
		/**===========================================================
		 * Executing Inference: Inferring Interface Connections...
		 * =========================================================== */
		GeneralConnects.inferInterfaceConnections();
		
		/**===========================================================
		 * Get mappings of Individual x Classes from G800
		 * =========================================================== */
		List<String> allIndividuals = AdvisorService.getAllIndividualsFromG800();				
		HashMap<String, List<String>> g800List = AdvisorService.getIndividualVSClassesMap(allIndividuals);;				
		request.getSession().setAttribute("g800", g800List);

		return "advisor/views/open-visualizer";
	}

	@RequestMapping(method = RequestMethod.GET, value="/open_network_visualization")
	public String open_network_visualization(@RequestParam("visualization") String visualization, HttpServletRequest request) 
	{
		Visualizator viz = new Visualizator();

		if(visualization.equals("allSites"))
		{			
			viz.setAllSitesConfig();
			
			request.getSession().setAttribute("canClick", true);
			request.getSession().setAttribute("targetURL", "open_equipment_visualization_from_site?selected=");
			request.getSession().setAttribute("popupMessage", "Go to Site\'s components");	
			request.getSession().setAttribute("visualizationType", "allSites");
		}
		else if(visualization.equals("allEquipments"))
		{
			/**===========================================================
			 * Executing Inference: Inferring Interface Connections...
			 * =========================================================== */
			GeneralConnects.inferInterfaceConnections();

			viz.setAllEquipmentsConfig();
			
			request.getSession().setAttribute("canClick", true);
			request.getSession().setAttribute("targetURL", "open_g800_visualization_from_equip?selected=");
			request.getSession().setAttribute("popupMessage", "Go to Equipment\'s components");
			
			request.getSession().setAttribute("visualizationType", "allEquipments");
		}
		else if(visualization.equals("allG800"))
		{			
			viz.setAllG800Config();

			request.getSession().setAttribute("canClick", false);
			request.getSession().setAttribute("visualizationType", "allG800");
		}
		else if(visualization.equals("allElements"))
		{
			/**===========================================================
			 * Initialize figure mapper
			 * =========================================================== */
			HTMLFigureMapper.init();
			
			viz.setAllElementsConfig();
			
			request.getSession().setAttribute("canClick", false);
			
			request.getSession().setAttribute("visualizationType", "allElements");
		}
		
		request.getSession().setAttribute("valuesGraph", viz.getValuesGraph());
		request.getSession().setAttribute("width", viz.getWidth());
		request.getSession().setAttribute("height", viz.getHeight());
		request.getSession().setAttribute("hashTypes", viz.getHashTypes());
		request.getSession().setAttribute("nameSpace", OKCoUploader.getNamespace());
		request.getSession().setAttribute("size", viz.getSize());
		
		return "advisor/views/show-visualization";
	}

	@RequestMapping(method = RequestMethod.GET, value="/open_equipment_visualization_from_site")
	public String open_equipment_visualization_from_site(@RequestParam("selected") String selected_site, HttpServletRequest request) 
	{
		Visualizator viz = new Visualizator();
		viz.setAllEquipmentsConfing(selected_site);

		request.getSession().setAttribute("canClick", true);
		request.getSession().setAttribute("targetURL", "open_g800_visualization_from_equip?selected=");
		request.getSession().setAttribute("popupMessage", "Go to Equipment\'s components");
		request.getSession().setAttribute("valuesGraph", viz.getValuesGraph());
		request.getSession().setAttribute("width", viz.getWidth());
		request.getSession().setAttribute("height", viz.getHeight());
		request.getSession().setAttribute("hashTypes", viz.getHashTypes());
		request.getSession().setAttribute("size", viz.getSize());
		
		request.getSession().setAttribute("visualizationType", "fromSite");
		request.getSession().setAttribute("site", selected_site);
		
		return "advisor/views/show-visualization";
	}

	@RequestMapping(method = RequestMethod.GET, value="/open_g800_visualization_from_equip")
	public String open_g800_visualization_from_equip(@RequestParam("selected") String equip, HttpServletRequest request) 
	{
		Visualizator viz = new Visualizator();
		viz.setAllG800COnfig(equip);		
		
		request.getSession().setAttribute("canClick", false);
		request.getSession().setAttribute("valuesGraph", viz.getValuesGraph());
		request.getSession().setAttribute("width", viz.getWidth());
		request.getSession().setAttribute("height", viz.getHeight());
		request.getSession().setAttribute("hashTypes", viz.getHashTypes());
		request.getSession().setAttribute("size", viz.getSize());
		
		request.getSession().setAttribute("visualizationType", "fromEquip");
		request.getSession().setAttribute("equip", equip);
		
		return "advisor/views/show-visualization";
	}

	@RequestMapping(method = RequestMethod.GET, value="/connects")
	public static String connects(HttpServletRequest request) 
	{
		/**===========================================================
		 * Initialize figure mapper
		 * =========================================================== */
		HTMLFigureMapper.init();

		ConnectsVisualizator viz = new ConnectsVisualizator();
		viz.setConfig();
				
		request.getSession().setAttribute("valuesGraph", viz.getArborStructure());
		request.getSession().setAttribute("width", viz.getWidth());
		request.getSession().setAttribute("height", viz.getHeight());
		request.getSession().setAttribute("hashEquipIntOut", viz.getHashEquipIntOut());
		request.getSession().setAttribute("hashTypes", viz.getHashTypes());
		request.getSession().setAttribute("hashAllowed", viz.getHashAllowed());
		request.getSession().setAttribute("hashRPEquip", viz.getHashRPEquip());
		request.getSession().setAttribute("size", viz.getSize());

		return "advisor/views/connects";
	}

	@RequestMapping(method = RequestMethod.GET, value="/binds")
	public static String bindsV(HttpServletRequest request) 
	{
		Date beginDate = new Date();
		/**===========================================================
		 * Initialize figure mapper
		 * =========================================================== */
		HTMLFigureMapper.init();
		
		/**===========================================================
		 * Executing Inference: Inferring Interface Connections...
		 * =========================================================== */
		GeneralConnects.inferInterfaceConnections();
		
		BindsVisualizator bindsViz = new BindsVisualizator();
		bindsViz.createArborStruct();

		request.getSession().setAttribute("valuesGraph", bindsViz.getArborStructure());
		request.getSession().setAttribute("width", bindsViz.getWidth());
		request.getSession().setAttribute("height", bindsViz.getHeight());
		request.getSession().setAttribute("hashEquipIntOut", bindsViz.getHashEquipIntOut());
		request.getSession().setAttribute("hashTypes", bindsViz.getHashTypes());
		request.getSession().setAttribute("hashAllowed", bindsViz.getHashAllowed());
		request.getSession().setAttribute("size", bindsViz.getSize());

		PerformanceUtil.printExecutionTime("/binds", beginDate);
		
		return "advisor/views/binds";
	}	
}

