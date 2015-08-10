package br.com.padtec.nopen.advisor.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.padtec.advisor.core.application.AdvisorComponents;
import br.com.padtec.advisor.core.application.HTMLFigureMapper;
import br.com.padtec.advisor.core.queries.AdvisorQueryUtil;
import br.com.padtec.common.util.PerformanceUtil;
import br.com.padtec.okco.core.application.OKCoComponents;

@Controller
public class VisualizationController {
	 
	@RequestMapping(method = RequestMethod.GET, value="/open-visualizer")
	public String open_visualizator(HttpServletRequest request) 
	{
		if(OKCoComponents.repository.getBaseModel() == null) return "open-visualizer"; 

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
		AdvisorComponents.connects.inferInterfaceConnections();
		
		/**===========================================================
		 * Get mappings of Individual x Classes from G800
		 * =========================================================== */
		List<String> allIndividuals = AdvisorComponents.connects.getAllIndividualsFromG800();				
		HashMap<String, List<String>> g800List = AdvisorComponents.connects.getIndividualVSClassesMap(allIndividuals);;				
		request.getSession().setAttribute("g800", g800List);

		return "advisor/views/open-visualizer";
	}

	@RequestMapping(method = RequestMethod.GET, value="/open_network_visualization")
	public String open_network_visualization(@RequestParam("visualization") String visualization, HttpServletRequest request) 
	{
		if(visualization.equals("allSites"))
		{			
			AdvisorComponents.visualizator.setAllSitesConfig();
			
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
			AdvisorComponents.connects.inferInterfaceConnections();

			AdvisorComponents.visualizator.setAllEquipmentsConfig();
			
			request.getSession().setAttribute("canClick", true);
			request.getSession().setAttribute("targetURL", "open_g800_visualization_from_equip?selected=");
			request.getSession().setAttribute("popupMessage", "Go to Equipment\'s components");
			
			request.getSession().setAttribute("visualizationType", "allEquipments");
		}
		else if(visualization.equals("allG800"))
		{			
			AdvisorComponents.visualizator.setAllG800Config();

			request.getSession().setAttribute("canClick", false);
			request.getSession().setAttribute("visualizationType", "allG800");
		}
		else if(visualization.equals("allElements"))
		{
			/**===========================================================
			 * Initialize figure mapper
			 * =========================================================== */
			HTMLFigureMapper.init();
			
			AdvisorComponents.visualizator.setAllElementsConfig();
			
			request.getSession().setAttribute("canClick", false);
			
			request.getSession().setAttribute("visualizationType", "allElements");
		}
		
		request.getSession().setAttribute("valuesGraph", AdvisorComponents.visualizator.getValuesGraph());
		request.getSession().setAttribute("width", AdvisorComponents.visualizator.getWidth());
		request.getSession().setAttribute("height", AdvisorComponents.visualizator.getHeight());
		request.getSession().setAttribute("hashTypes", AdvisorComponents.visualizator.getHashTypes());
		request.getSession().setAttribute("nameSpace", OKCoComponents.repository.getNamespace());
		request.getSession().setAttribute("size", AdvisorComponents.visualizator.getSize());
		
		return "advisor/views/show-visualization";
	}

	@RequestMapping(method = RequestMethod.GET, value="/open_equipment_visualization_from_site")
	public String open_equipment_visualization_from_site(@RequestParam("selected") String selected_site, HttpServletRequest request) 
	{	
		AdvisorComponents.visualizator.setAllEquipmentsConfing(selected_site);

		request.getSession().setAttribute("canClick", true);
		request.getSession().setAttribute("targetURL", "open_g800_visualization_from_equip?selected=");
		request.getSession().setAttribute("popupMessage", "Go to Equipment\'s components");
		request.getSession().setAttribute("valuesGraph", AdvisorComponents.visualizator.getValuesGraph());
		request.getSession().setAttribute("width", AdvisorComponents.visualizator.getWidth());
		request.getSession().setAttribute("height", AdvisorComponents.visualizator.getHeight());
		request.getSession().setAttribute("hashTypes", AdvisorComponents.visualizator.getHashTypes());
		request.getSession().setAttribute("size", AdvisorComponents.visualizator.getSize());
		
		request.getSession().setAttribute("visualizationType", "fromSite");
		request.getSession().setAttribute("site", selected_site);
		
		return "advisor/views/show-visualization";
	}

	@RequestMapping(method = RequestMethod.GET, value="/open_g800_visualization_from_equip")
	public String open_g800_visualization_from_equip(@RequestParam("selected") String equip, HttpServletRequest request) 
	{	
		AdvisorComponents.visualizator.setAllG800COnfig(equip);		
		
		request.getSession().setAttribute("canClick", false);
		request.getSession().setAttribute("valuesGraph", AdvisorComponents.visualizator.getValuesGraph());
		request.getSession().setAttribute("width", AdvisorComponents.visualizator.getWidth());
		request.getSession().setAttribute("height", AdvisorComponents.visualizator.getHeight());
		request.getSession().setAttribute("hashTypes", AdvisorComponents.visualizator.getHashTypes());
		request.getSession().setAttribute("size", AdvisorComponents.visualizator.getSize());
		
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
		
		AdvisorComponents.connectsVisualizator.setConfig();
		
		request.getSession().setAttribute("valuesGraph", AdvisorComponents.connectsVisualizator.getArborStructure());
		request.getSession().setAttribute("width", AdvisorComponents.connectsVisualizator.getWidth());
		request.getSession().setAttribute("height", AdvisorComponents.connectsVisualizator.getHeight());
		request.getSession().setAttribute("hashEquipIntOut", AdvisorComponents.connectsVisualizator.getHashEquipIntOut());
		request.getSession().setAttribute("hashTypes", AdvisorComponents.connectsVisualizator.getHashTypes());
		request.getSession().setAttribute("hashAllowed", AdvisorComponents.connectsVisualizator.getHashAllowed());
		request.getSession().setAttribute("hashRPEquip", AdvisorComponents.connectsVisualizator.getHashRPEquip());
		request.getSession().setAttribute("size", AdvisorComponents.connectsVisualizator.getSize());

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
		AdvisorComponents.connects.inferInterfaceConnections();
		
		AdvisorComponents.bindsVisualizator.createArborStruct();

		request.getSession().setAttribute("valuesGraph", AdvisorComponents.bindsVisualizator.getArborStructure());
		request.getSession().setAttribute("width", AdvisorComponents.bindsVisualizator.getWidth());
		request.getSession().setAttribute("height", AdvisorComponents.bindsVisualizator.getHeight());
		request.getSession().setAttribute("hashEquipIntOut", AdvisorComponents.bindsVisualizator.getHashEquipIntOut());
		request.getSession().setAttribute("hashTypes", AdvisorComponents.bindsVisualizator.getHashTypes());
		request.getSession().setAttribute("hashAllowed", AdvisorComponents.bindsVisualizator.getHashAllowed());
		request.getSession().setAttribute("size", AdvisorComponents.bindsVisualizator.getSize());

		PerformanceUtil.printExecutionTime("/binds", beginDate);
		
		return "advisor/views/binds";
	}	
}

