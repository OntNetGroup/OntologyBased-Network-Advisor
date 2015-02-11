package br.com.padtec.advisor.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.padtec.advisor.application.AdvisorService;
import br.com.padtec.advisor.application.BindsVisualizator;
import br.com.padtec.advisor.application.ConnectsVisualizator;
import br.com.padtec.advisor.application.GeneralConnects;
import br.com.padtec.advisor.application.HTMLFigureMapper;
import br.com.padtec.advisor.application.Visualizator;
import br.com.padtec.advisor.application.queries.AdvisorQueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;

@Controller
public class VisualizationController {
	 
	@RequestMapping(method = RequestMethod.GET, value="/open-visualizer")
	public String open_visualizator(HttpServletRequest request) 
	{
		if(OKCoUploader.getBaseModel() == null) return "open-visualizer"; 

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

		return "open-visualizer";
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
		}
		else if(visualization.equals("allG800"))
		{			
			viz.setAllG800Config();

			request.getSession().setAttribute("canClick", false);
		}
		else if(visualization.equals("allElements"))
		{
			/**===========================================================
			 * Initialize figure mapper
			 * =========================================================== */
			HTMLFigureMapper.init();
			
			viz.setAllElementsConfig();
			
			request.getSession().setAttribute("canClick", false);
		}
		
		request.getSession().setAttribute("valuesGraph", viz.getValuesGraph());
		request.getSession().setAttribute("width", viz.getWidth());
		request.getSession().setAttribute("height", viz.getHeight());
		request.getSession().setAttribute("hashTypes", viz.getHashTypes());
		request.getSession().setAttribute("nameSpace", OKCoUploader.getNamespace());
		request.getSession().setAttribute("size", viz.getSize());
		
		return "show-visualization";
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
		
		return "show-visualization";
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
		
		return "show-visualization";
	}

	@RequestMapping(method = RequestMethod.GET, value="/connects")
	public static String connects(HttpServletRequest request) 
	{
		/**===========================================================
		 * Initialize figure mapper
		 * =========================================================== */
		HTMLFigureMapper.init();

		ConnectsVisualizator viz = new ConnectsVisualizator();
		
		request.getSession().setAttribute("valuesGraph", viz.getArborStructure());
		request.getSession().setAttribute("width", viz.getWidth());
		request.getSession().setAttribute("height", viz.getHeight());
		request.getSession().setAttribute("hashEquipIntOut", viz.getHashEquipIntOut());
		request.getSession().setAttribute("hashTypes", viz.getHashTypes());
		request.getSession().setAttribute("hashAllowed", viz.getHashAllowed());
		request.getSession().setAttribute("hashRPEquip", viz.getHashRPEquip());
		request.getSession().setAttribute("size", viz.getSize());

		return "connects";
	}

	@RequestMapping(method = RequestMethod.GET, value="/binds")
	public static String bindsV(HttpServletRequest request) 
	{
		/**===========================================================
		 * Initialize figure mapper
		 * =========================================================== */
		HTMLFigureMapper.init();
		
		/**===========================================================
		 * Executing Inference: Inferring Interface Connections...
		 * =========================================================== */
		GeneralConnects.inferInterfaceConnections();
		
		BindsVisualizator viz = new BindsVisualizator();
		viz.setConfig();

		request.getSession().setAttribute("valuesGraph", viz.getArborStructure());
		request.getSession().setAttribute("width", viz.getWidth());
		request.getSession().setAttribute("height", viz.getHeight());
		request.getSession().setAttribute("hashEquipIntOut", viz.getHashEquipIntOut());
		request.getSession().setAttribute("hashTypes", viz.getHashTypes());
		request.getSession().setAttribute("hashAllowed", viz.getHashAllowed());
		request.getSession().setAttribute("size", viz.getSize());

		return "binds";
	}	
}

