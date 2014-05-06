package br.ufes.inf.padtec.tnokco.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.inf.nemo.padtec.tnokco.TNOKCOGraphPlotting;
import br.ufes.inf.nemo.okco.model.DtoResultAjax;
import br.ufes.inf.nemo.okco.model.Instance;
import br.ufes.inf.padtec.tnokco.business.Equipment;
import br.ufes.inf.padtec.tnokco.business.InterfaceOutput;
import br.ufes.inf.padtec.tnokco.business.Provisioning;

@Controller
public class VisualizationController {

	@RequestMapping(method = RequestMethod.GET, value="/open_visualizator")
	public String open_visualizator(HttpServletRequest request) {
		if(HomeController.Model == null)
			return "open_visualizator"; 
		ArrayList<String> sites = HomeController.Search.GetInstancesFromClass(HomeController.Model, HomeController.InfModel, HomeController.NS+"Site");
		
		//session
		request.getSession().setAttribute("sites", sites);
		
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
		String hashTypes = "";

		if(visualization.equals("allSites")){
			ArrayList<String> sites = Provisioning.getAllSitesAndConnections();
			ArrayList<String[]> sitesConnections = Provisioning.connections;
			String rel = Provisioning.relation;
			
			for (String site : sites) {
				valuesGraph += "graph.addNode(\""+site.substring(site.indexOf("#")+1)+"\", {shape:\"SITE_AZUL\"});";
				hashTypes += "hash[\""+site.substring(site.indexOf("#")+1)+"\"] = \"<b>"+site.substring(site.indexOf("#")+1)+" is an individual of classes: </b><br><ul><li>Site</li></ul>\";";
			}
			
			for(String[] stCon : sitesConnections){
				valuesGraph += "graph.addEdge(graph.addNode(\""+stCon[0].substring(stCon[0].indexOf("#")+1)+"\", {shape:\"SITE_AZUL\"}),graph.addNode(\""+stCon[1].substring(stCon[1].indexOf("#")+1)+"\", {shape:\"SITE_AZUL\"}), {name:'"+rel+"'});";
			}
			
			
		}else if(visualization.equals("allEquipments")){
			ArrayList<Equipment> list = Provisioning.getEquipmentsConnectionsBinds();
			
			for(Equipment equip : list){
				hashTypes += "hash[\""+equip.getName()+"\"] = \"<b>"+equip.getName()+" is an individual of classes: </b><br><ul><li>Equipment</li></ul>\";";
				
				for(Map.Entry<ArrayList<String>,Equipment> entry : equip.getBinds().entrySet()){
					valuesGraph += "graph.addEdge(graph.addNode(\""+equip.getName()+"\", {shape:\"Equip_AZUL\"}),graph.addNode(\""+entry.getValue().getName()+"\", {shape:\"Equip_AZUL\"}), {name:'binds:";
					valuesGraph += entry.getKey().get(0)+"-"+entry.getKey().get(1);
					valuesGraph += "'});";
				}
				if(equip.getBinds().isEmpty()){
					valuesGraph += "graph.addNode(\""+equip.getName()+"\", {shape:\"Equip_AZUL\"});";
				}
			}
		}else if(visualization.equals("allG800")){
			
		}

		int width  = graphPlotting.width;
		int height = graphPlotting.height;
		String subtitle = graphPlotting.getSubtitle();

		//session
		request.getSession().setAttribute("valuesGraph", valuesGraph);
		request.getSession().setAttribute("width", width);
		request.getSession().setAttribute("height", height);
		request.getSession().setAttribute("subtitle", subtitle);
		request.getSession().setAttribute("hashTypes", hashTypes);
		request.getSession().setAttribute("nameSpace", HomeController.NS);

		return "showVisualization";
	}

	@RequestMapping(method = RequestMethod.GET, value="/open_equipment_visualization_from_site")
	public String open_equipment_visualization_from_site(@RequestParam("selected_site") String selected_site, HttpServletRequest request) {
		ArrayList<Equipment> equips = Provisioning.getEquipmentsFromSite(HomeController.NS+selected_site);

		String arborStructure = "";
		String hashEquipIntOut = "";
		String hashTypes = "";
		
		for(Equipment equip : equips){
			hashEquipIntOut += "hashEquipIntOut['"+equip.getName()+"'] = new Array();";
			hashTypes += "hash[\""+equip.getName()+"\"] = \"<b>"+equip.getName()+" is an individual of classes: </b><br><ul><li>Equipment</li></ul>\";";
			for(InterfaceOutput outs : equip.getOutputs()){
				hashEquipIntOut += "hashEquipIntOut['"+equip.getName()+"']['"+outs.getName()+"'] = "+outs.isConnected()+";";		
			}
			
			for(Map.Entry<ArrayList<String>,Equipment> entry : equip.getBinds().entrySet()){
				arborStructure += "graph.addEdge(graph.addNode(\""+equip.getName()+"\", {shape:\"Equip_AZUL\"}),graph.addNode(\""+entry.getValue().getName()+"\", {shape:\"Equip_AZUL\"}), {name:'binds:";
				arborStructure += entry.getKey().get(0)+"-"+entry.getKey().get(1);
				arborStructure += "'});";
			}
			if(equip.getBinds().isEmpty()){
				arborStructure += "graph.addNode(\""+equip.getName()+"\", {shape:\"Equip_AZUL\"});";
			}
		}
		
		int width  = 800;
		int height = 600;

		//session
		request.getSession().setAttribute("valuesGraph", arborStructure);
		request.getSession().setAttribute("width", width);
		request.getSession().setAttribute("height", height);
		request.getSession().setAttribute("hashEquipIntOut", hashEquipIntOut);
		request.getSession().setAttribute("hashTypes", hashTypes);

		return "equipmentVisualizer";
	}

	@RequestMapping(method = RequestMethod.GET, value="/provisoning_visualization")
	public String provisoning_visualization(HttpServletRequest request) {

		ArrayList<Equipment> list = Provisioning.getEquipmentsConnectionsBinds();
		
		String arborStructure = "";
		String hashEquipIntOut = "";
		String hashTypes = "";
		
		for(Equipment equip : list){
			hashEquipIntOut += "hashEquipIntOut['"+equip.getName()+"'] = new Array();";
			hashTypes += "hash[\""+equip.getName()+"\"] = \"<b>"+equip.getName()+" is an individual of classes: </b><br><ul><li>Equipment</li></ul>\";";
			for(InterfaceOutput outs : equip.getOutputs()){
				hashEquipIntOut += "hashEquipIntOut['"+equip.getName()+"']['"+outs.getName()+"'] = "+outs.isConnected()+";";		
			}
			
			for(Map.Entry<ArrayList<String>,Equipment> entry : equip.getBinds().entrySet()){
				arborStructure += "graph.addEdge(graph.addNode(\""+equip.getName()+"\", {shape:\"Equip_AZUL\"}),graph.addNode(\""+entry.getValue().getName()+"\", {shape:\"Equip_AZUL\"}), {name:'binds:";
				arborStructure += entry.getKey().get(0)+"-"+entry.getKey().get(1);
				arborStructure += "'});";
			}
			if(equip.getBinds().isEmpty()){
				arborStructure += "graph.addNode(\""+equip.getName()+"\", {shape:\"Equip_AZUL\"});";
			}
		}
		
		int width  = 800;
		int height = 600;

		//session
		request.getSession().setAttribute("valuesGraph", arborStructure);
		request.getSession().setAttribute("width", width);
		request.getSession().setAttribute("height", height);
		request.getSession().setAttribute("hashEquipIntOut", hashEquipIntOut);
		request.getSession().setAttribute("hashTypes", hashTypes);

		return "equipmentVisualizer";
	}

	@RequestMapping(method = RequestMethod.GET, value="/open_tfs_from_equipment")
	public String open_tfs_from_equipment(@RequestParam("equip") String equip, HttpServletRequest request) {

		return "equipmentVisualizer";
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/connect_equip_binds")
	public @ResponseBody String connect_equip_binds(@RequestParam("equip_source") String equip_source,@RequestParam("interface_source") String interface_source,@RequestParam("equip_target") String equip_target,@RequestParam("interface_target") String interface_target , HttpServletRequest request) {
		DtoResultAjax dto = ProvisioningController.binds(interface_target, interface_source, request);
		return dto.ok+"";
	}

	@RequestMapping(method = RequestMethod.GET, value="/get_input_interfaces_from")
	public @ResponseBody String get_input_interfaces_from(@RequestParam("equip") String equip, @RequestParam("interf") String interf, HttpServletRequest request) {
		
		ArrayList<String> list = ProvisioningController.getCandidateInterfacesForConnection(interf);
		String hashEquipIntIn = "";
		
		for(String line : list){
			hashEquipIntIn += line;
		}
		return hashEquipIntIn;
	}
	
	private String getJSHash(String hashName, String key, ArrayList<String> values){
		String ret = "\n"+hashName+"[\""+key+"\"] = ";
		for(String value:values){
			ret += "\""+value+"\",";
		}
		ret = ret.substring(0, ret.length()-1);
		return ret;
	}

}
