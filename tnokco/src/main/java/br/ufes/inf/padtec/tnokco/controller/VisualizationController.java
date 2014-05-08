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
import br.ufes.inf.padtec.tnokco.business.InterfaceInput;
import br.ufes.inf.padtec.tnokco.business.InterfaceOutput;
import br.ufes.inf.padtec.tnokco.business.Provisioning;

@Controller
public class VisualizationController {
	private HashMap<String,String> elements = new HashMap<String, String>();
	private int width  = 1200;
	private int height = 800;
	
	
	@RequestMapping(method = RequestMethod.GET, value="/open_visualizator")
	public String open_visualizator(HttpServletRequest request) {
		
		if(HomeController.Model == null)
			return "open_visualizator"; 
		
		ArrayList<String> sites = HomeController.Search.GetInstancesFromClass(HomeController.Model, HomeController.InfModel, HomeController.NS+"Site");

		//session
		request.getSession().setAttribute("sites", sites);

		elements.put("Termination_Function", "TF");
		elements.put("Adaptation_Function", "AF");
		elements.put("Matrix", "Matrix");
		elements.put("Subnetwork", "SN");
		elements.put("Physical_Media", "PM");
		elements.put("Input", "Input");
		elements.put("Output", "Output");
		elements.put("Reference_Point", "RP");
		elements.put("Transport_Entity", "TE");
		elements.put("Layer_Network", "Layer");
		elements.put("Binding", "Binding");
		elements.put("Input_Interface", "INT_IN");
		elements.put("Output_Interface", "INT_OUT");
		elements.put("Forwarding", "FORWARDING");
		elements.put("Adaptation_Sink_Process", "Process");
		elements.put("Adaptation_Source_Process", "Process");
		elements.put("Termination_Sink_Process", "Process");
		elements.put("Layer_Processor_Process", "Process");
		elements.put("Termination_Source_Process", "Process");
		elements.put("Forwarding_Rule", "FWR_RULE");
		elements.put("Equipment", "Equip");
		elements.put("Site", "SITE");
		
		return "open_visualizator";
	}

	@RequestMapping(method = RequestMethod.GET, value="/open_network_visualization")
	public String open_network_visualization(@RequestParam("visualization") String visualization, HttpServletRequest request) {

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
			request.getSession().setAttribute("canClick", true);
			request.getSession().setAttribute("targetURL", "open_equipment_visualization_from_site?selected=");
			request.getSession().setAttribute("popupMessage", "Go to Site\'s components");
		}else if(visualization.equals("allEquipments")){
			ArrayList<Equipment> list = Provisioning.getEquipmentsConnectionsBinds();

			for(Equipment equip : list){
				hashTypes += "hash[\""+equip.getName()+"\"] = \"<b>"+equip.getName()+" is an individual of classes: </b><br><ul><li>Equipment</li></ul>\";";
				for(InterfaceOutput outs : equip.getOutputs()){
					valuesGraph += "graph.addNode(\""+outs.getName()+"\", {shape:\"INT_OUT_AZUL\"}),graph.addEdge(graph.addNode(\""+equip.getName()+"\", {shape:\"Equip_AZUL\"}), {name:'INV.componentOf'});";
					hashTypes += "hash[\""+outs.getName()+"\"] = \"<b>"+outs.getName()+" is an individual of classes: </b><br><ul><li>Output_Interface</li></ul>\";";
				}
				
				for(String in : equip.getInputs()){
					valuesGraph += "graph.addNode(\""+in+"\", {shape:\"INT_IN_AZUL\"}),graph.addEdge(graph.addNode(\""+equip.getName()+"\", {shape:\"Equip_AZUL\"}), {name:'INV.componentOf'});";
					hashTypes += "hash[\""+in+"\"] = \"<b>"+in+" is an individual of classes: </b><br><ul><li>Input_Interface</li></ul>\";";
				}

				for(Map.Entry<ArrayList<String>,Equipment> entry : equip.getBinds().entrySet()){
					valuesGraph += "graph.addEdge(graph.addNode(\""+equip.getName()+"\", {shape:\"Equip_AZUL\"}),graph.addNode(\""+entry.getValue().getName()+"\", {shape:\"Equip_AZUL\"}), {name:'binds:";
					valuesGraph += entry.getKey().get(0)+"-"+entry.getKey().get(1);
					valuesGraph += "'});";
				}
				if(equip.getBinds().isEmpty()){
					valuesGraph += "graph.addNode(\""+equip.getName()+"\", {shape:\"Equip_AZUL\"});";
				}
			}
			request.getSession().setAttribute("canClick", true);
			request.getSession().setAttribute("targetURL", "open_g800_visualization_from_equip?selected=");
			request.getSession().setAttribute("popupMessage", "Go to Equipment\'s components");
		}else if(visualization.equals("allG800")){
			ArrayList<String> g800s = Provisioning.getAllG800();
			ArrayList<String[]> triplas = Provisioning.triples_g800;
			HashMap<String, ArrayList<String>> hashIndv = Provisioning.ind_class;

			for (String g800 : g800s) {
				valuesGraph += "graph.addNode(\""+g800.substring(g800.indexOf("#")+1)+"\", {shape:\""+getG800Image(hashIndv.get(g800))+"_AZUL\"});";
				hashTypes += "hash[\""+g800.substring(g800.indexOf("#")+1)+"\"] = \"<b>"+g800.substring(g800.indexOf("#")+1)+" is an individual of classes: </b><br><ul>";
				for(String type : hashIndv.get(g800)){
					if(type.contains("#"))
						hashTypes += "<li>"+type.substring(type.indexOf("#")+1)+"</li>";
				}
				hashTypes += "</ul>\";";
			}

			for(String[] stCon : triplas){
				if(!hashIndv.containsKey(stCon[0]) || !hashIndv.containsKey(stCon[2]))
					continue;
				valuesGraph += "graph.addEdge(graph.addNode(\""+stCon[0].substring(stCon[0].indexOf("#")+1)+"\", ";
				valuesGraph += "{shape:\""+getG800Image(hashIndv.get(stCon[0]))+"\"}),";
				valuesGraph += "graph.addNode(\""+stCon[2].substring(stCon[2].indexOf("#")+1)+"\", ";
				valuesGraph += "{shape:\""+getG800Image(hashIndv.get(stCon[2]))+"\"}), {name:'"+stCon[1].substring(stCon[1].indexOf("#")+1)+"'});";
			}
			
			request.getSession().setAttribute("canClick", false);
			
		}

		//session
		request.getSession().setAttribute("valuesGraph", valuesGraph);
		request.getSession().setAttribute("width", width);
		request.getSession().setAttribute("height", height);
		request.getSession().setAttribute("hashTypes", hashTypes);
		request.getSession().setAttribute("nameSpace", HomeController.NS);

		return "showVisualization";
	}

	@RequestMapping(method = RequestMethod.GET, value="/open_equipment_visualization_from_site")
	public String open_equipment_visualization_from_site(@RequestParam("selected") String selected_site, HttpServletRequest request) {
		ArrayList<Equipment> equips = Provisioning.getEquipmentsFromSite(HomeController.NS+selected_site);

		String valuesGraph = "";
		String hashTypes = "";

		for(Equipment equip : equips){
			hashTypes += "hash[\""+equip.getName()+"\"] = \"<b>"+equip.getName()+" is an individual of classes: </b><br><ul><li>Equipment</li></ul>\";";
			for(InterfaceOutput outs : equip.getOutputs()){
				valuesGraph += "graph.addNode(\""+outs.getName()+"\", {shape:\"INT_OUT_AZUL\"}),graph.addEdge(graph.addNode(\""+equip.getName()+"\", {shape:\"Equip_AZUL\"}), {name:'INV.componentOf'});";
				hashTypes += "hash[\""+outs.getName()+"\"] = \"<b>"+outs.getName()+" is an individual of classes: </b><br><ul><li>Output_Interface</li></ul>\";";
			}
			
			for(String in : equip.getInputs()){
				valuesGraph += "graph.addNode(\""+in+"\", {shape:\"INT_IN_AZUL\"}),graph.addEdge(graph.addNode(\""+equip.getName()+"\", {shape:\"Equip_AZUL\"}), {name:'INV.componentOf'});";
				hashTypes += "hash[\""+in+"\"] = \"<b>"+in+" is an individual of classes: </b><br><ul><li>Input_Interface</li></ul>\";";
			}

			for(Map.Entry<ArrayList<String>,Equipment> entry : equip.getBinds().entrySet()){
				valuesGraph += "graph.addEdge(graph.addNode(\""+equip.getName()+"\", {shape:\"Equip_AZUL\"}),graph.addNode(\""+entry.getValue().getName()+"\", {shape:\"Equip_AZUL\"}), {name:'binds:";
				valuesGraph += entry.getKey().get(0)+"-"+entry.getKey().get(1);
				valuesGraph += "'});";
			}
			if(equip.getBinds().isEmpty()){
				valuesGraph += "graph.addNode(\""+equip.getName()+"\", {shape:\"Equip_AZUL\"});";
			}
		}

		//session
		request.getSession().setAttribute("canClick", true);
		request.getSession().setAttribute("targetURL", "open_g800_visualization_from_equip?selected=");
		request.getSession().setAttribute("popupMessage", "Go to Equipment\'s components");
		request.getSession().setAttribute("valuesGraph", valuesGraph);
		request.getSession().setAttribute("width", width);
		request.getSession().setAttribute("height", height);
		request.getSession().setAttribute("hashTypes", hashTypes);

		return "showVisualization";
	}

	@RequestMapping(method = RequestMethod.GET, value="/open_g800_visualization_from_equip")
	public String open_g800_visualization_from_equip(@RequestParam("selected") String equip, HttpServletRequest request) {
		ArrayList<String> g800s = Provisioning.getG800FromEquipment(equip);
		ArrayList<String[]> triplas = Provisioning.triples_g800;
		HashMap<String, ArrayList<String>> hashIndv = Provisioning.ind_class;

		String valuesGraph = "";
		String hashTypes = "";

		for (String g800 : g800s) {
			valuesGraph += "graph.addNode(\""+g800.substring(g800.indexOf("#")+1)+"\", {shape:\""+getG800Image(hashIndv.get(g800))+"_AZUL\"});";
			hashTypes += "hash[\""+g800.substring(g800.indexOf("#")+1)+"\"] = \"<b>"+g800.substring(g800.indexOf("#")+1)+" is an individual of classes: </b><br><ul>";
			for(String type : hashIndv.get(g800)){
				if(type.contains("#"))
					hashTypes += "<li>"+type.substring(type.indexOf("#")+1)+"</li>";
			}
			hashTypes += "</ul>\";";
		}

		for(String[] stCon : triplas){
			if(!hashIndv.containsKey(stCon[0]) || !hashIndv.containsKey(stCon[2]))
				continue;
			valuesGraph += "graph.addEdge(graph.addNode(\""+stCon[0].substring(stCon[0].indexOf("#")+1)+"\", ";
			valuesGraph += "{shape:\""+getG800Image(hashIndv.get(stCon[0]))+"\"}),";
			valuesGraph += "graph.addNode(\""+stCon[2].substring(stCon[2].indexOf("#")+1)+"\", ";
			valuesGraph += "{shape:\""+getG800Image(hashIndv.get(stCon[2]))+"\"}), {name:'"+stCon[1].substring(stCon[1].indexOf("#")+1)+"'});";
		}

		//session
		request.getSession().setAttribute("canClick", false);
		request.getSession().setAttribute("valuesGraph", valuesGraph);
		request.getSession().setAttribute("width", width);
		request.getSession().setAttribute("height", height);
		request.getSession().setAttribute("hashTypes", hashTypes);
		return "showVisualization";
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

	private String getG800Image(ArrayList<String> elemTypes){
		for(String type: elemTypes){
			if(elements.containsKey(type.substring(type.indexOf("#")+1)))
				return elements.get(type.substring(type.indexOf("#")+1));
		}
		return "dot";
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

		//session
		request.getSession().setAttribute("valuesGraph", arborStructure);
		request.getSession().setAttribute("width", width);
		request.getSession().setAttribute("height", height);
		request.getSession().setAttribute("hashEquipIntOut", hashEquipIntOut);
		request.getSession().setAttribute("hashTypes", hashTypes);

		return "equipmentVisualizer";
	}

}
