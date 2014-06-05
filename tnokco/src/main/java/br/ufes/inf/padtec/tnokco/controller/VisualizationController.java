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

import br.ufes.inf.nemo.okco.model.DtoInstance;
import br.ufes.inf.nemo.okco.model.DtoInstanceRelation;
import br.ufes.inf.nemo.okco.model.DtoResultAjax;
import br.ufes.inf.padtec.tnokco.business.Equipment;
import br.ufes.inf.padtec.tnokco.business.InterfaceOutput;
import br.ufes.inf.padtec.tnokco.business.Provisioning;

@Controller
public class VisualizationController {
	private static HashMap<String,String> elements = null;
	
	@RequestMapping(method = RequestMethod.GET, value="/open_visualizator")
	public String open_visualizator(HttpServletRequest request) {
		
		if(HomeController.Model == null)
			return "open_visualizator"; 
		
		ArrayList<String> sites = HomeController.Search.GetInstancesFromClass(HomeController.Model, HomeController.InfModel, HomeController.NS+"Site");
		ArrayList<String> equipments = HomeController.Search.GetInstancesFromClass(HomeController.Model, HomeController.InfModel, HomeController.NS+"Equipment");

		Provisioning.inferInterfaceConnections();
		Provisioning.getAllG800();
		HashMap<String, ArrayList<String>> g800List = Provisioning.ind_class;
		
		//session
		request.getSession().setAttribute("sites", sites);
		request.getSession().setAttribute("equipments", equipments);
		request.getSession().setAttribute("g800", g800List);

		elementsInitialize();
		
		return "open_visualizator";
	}

	@RequestMapping(method = RequestMethod.GET, value="/open_network_visualization")
	public String open_network_visualization(@RequestParam("visualization") String visualization, HttpServletRequest request) {

		String valuesGraph = "";
		String hashTypes = "";
		int size = 0;
		int width  = 1000;
		int height = 800;
		
		if(visualization.equals("allSites")){
			ArrayList<String> sites = Provisioning.getAllSitesAndConnections();
			ArrayList<String[]> sitesConnections = Provisioning.connections;
			String rel = Provisioning.relation;

			for (String site : sites) {
				valuesGraph += "graph.addNode(\""+site.substring(site.indexOf("#")+1)+"\", {shape:\"SITE_AZUL\"});";
				hashTypes += "hash[\""+site.substring(site.indexOf("#")+1)+"\"] = \"<b>"+site.substring(site.indexOf("#")+1)+" is an individual of classes: </b><br><ul><li>Site</li></ul>\";";
				size++;
			}

			for(String[] stCon : sitesConnections){
				valuesGraph += "graph.addEdge(graph.addNode(\""+stCon[0].substring(stCon[0].indexOf("#")+1)+"\", {shape:\"SITE_AZUL\"}),graph.addNode(\""+stCon[1].substring(stCon[1].indexOf("#")+1)+"\", {shape:\"SITE_AZUL\"}), {name:'"+rel+"'});";
				size++;
			}
			request.getSession().setAttribute("canClick", true);
			request.getSession().setAttribute("targetURL", "open_equipment_visualization_from_site?selected=");
			request.getSession().setAttribute("popupMessage", "Go to Site\'s components");
		}else if(visualization.equals("allEquipments")){
			Provisioning.inferInterfaceConnections();
			ArrayList<Equipment> list = Provisioning.getEquipmentsConnectionsBinds();

			for(Equipment equip : list){
				hashTypes += "hash[\""+equip.getName()+"\"] = \"<b>"+equip.getName()+" is an individual of classes: </b><br><ul><li>Equipment</li></ul>\";";
				for(InterfaceOutput outs : equip.getOutputs()){
					valuesGraph += "graph.addEdge(graph.addNode(\""+outs.getName()+"\", {shape:\"INT_OUT_AZUL\"}),graph.addNode(\""+equip.getName()+"\", {shape:\"Equip_AZUL\"}), {name:'INV.componentOf'});";
					hashTypes += "hash[\""+outs.getName()+"\"] = \"<b>"+outs.getName()+" is an individual of classes: </b><br><ul><li>Output_Interface</li></ul>\";";
					size++;
				}
				
				for(Map.Entry<ArrayList<String>,Equipment> entry : equip.getBinds().entrySet()){
					valuesGraph += "graph.addEdge(graph.addNode(\""+entry.getKey().get(0)+"\", {shape:\"INT_OUT_AZUL\"}),graph.addNode(\""+entry.getKey().get(1)+"\", {shape:\"INT_IN_AZUL\"}), {name:'interface_binds'});";
					hashTypes += "hash[\""+entry.getKey().get(1)+"\"] = \"<b>"+entry.getKey().get(1)+" is an individual of classes: </b><br><ul><li>Input_Interface</li></ul>\";";
					valuesGraph += "graph.addEdge(graph.addNode(\""+entry.getValue().getName()+"\", {shape:\"Equip_AZUL\"}),graph.addNode(\""+entry.getKey().get(1)+"\", {shape:\"INT_IN_AZUL\"}), {name:'componentOf'});";
					size++;
				}
				if(equip.getBinds().isEmpty()){
					valuesGraph += "graph.addNode(\""+equip.getName()+"\", {shape:\"Equip_AZUL\"});";
					size++;
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
				size++;
			}

			for(String[] stCon : triplas){
				if(!hashIndv.containsKey(stCon[0]) || !hashIndv.containsKey(stCon[2]))
					continue;
				valuesGraph += "graph.addEdge(graph.addNode(\""+stCon[0].substring(stCon[0].indexOf("#")+1)+"\", ";
				valuesGraph += "{shape:\""+getG800Image(hashIndv.get(stCon[0]))+"_AZUL\"}),";
				valuesGraph += "graph.addNode(\""+stCon[2].substring(stCon[2].indexOf("#")+1)+"\", ";
				valuesGraph += "{shape:\""+getG800Image(hashIndv.get(stCon[2]))+"_AZUL\"}), {name:'"+stCon[1].substring(stCon[1].indexOf("#")+1)+"'});";
				size++;
			}
			
			request.getSession().setAttribute("canClick", false);
		}else if(visualization.equals("allElements")){
			elementsInitialize();
			ArrayList<DtoInstance> allInstances = HomeController.Search.GetAllInstancesWithClass(HomeController.Model, HomeController.InfModel);
			
			for (DtoInstance instance : allInstances) {
				ArrayList<DtoInstanceRelation> targetList = HomeController.Search.GetInstanceRelations(HomeController.InfModel,instance.Uri);
				
				for (DtoInstanceRelation dtoInstanceRelation : targetList) {
					
					valuesGraph += "graph.addEdge(graph.addNode(\""+instance.Uri.substring(instance.Uri.indexOf("#")+1)+"\", ";
					valuesGraph += "{shape:\""+getG800Image(instance.ClassNameList)+"_AZUL\"}),";
					valuesGraph += "graph.addNode(\""+dtoInstanceRelation.Target.substring(dtoInstanceRelation.Target.indexOf("#")+1)+"\", ";
					valuesGraph += "{shape:\""+getG800Image(HomeController.Search.GetClassesFrom(dtoInstanceRelation.Target, HomeController.InfModel))+"_AZUL\"}), ";
					valuesGraph	+= "{name:'"+dtoInstanceRelation.Property.substring(dtoInstanceRelation.Property.indexOf("#")+1)+"'});";
					size++;
				}
				
				if(targetList.isEmpty()){
					valuesGraph += "graph.addNode(\""+instance.Uri.substring(instance.Uri.indexOf("#")+1)+"\", ";
					valuesGraph += "{shape:\""+getG800Image(HomeController.Search.GetClassesFrom(instance.Uri, HomeController.InfModel))+"_AZUL\"});";
				}
				
				hashTypes += "hash[\""+instance.Uri.substring(instance.Uri.indexOf("#")+1)+"\"] = \"<b>"+instance.Uri.substring(instance.Uri.indexOf("#")+1)+" is an individual of classes: </b><br><ul>";
				for(String type : instance.ClassNameList){
					if(type.contains("#"))
						hashTypes += "<li>"+type.substring(type.indexOf("#")+1)+"</li>";
				}
				hashTypes += "</ul>\";";
			}
			
			request.getSession().setAttribute("canClick", false);
		}

		width  += 400 * (size / 100);
		height += 400 * (size / 100);
		
		//session
		request.getSession().setAttribute("valuesGraph", valuesGraph);
		request.getSession().setAttribute("width", width);
		request.getSession().setAttribute("height", height);
		request.getSession().setAttribute("hashTypes", hashTypes);
		request.getSession().setAttribute("nameSpace", HomeController.NS);
		request.getSession().setAttribute("size", size);

		return "showVisualization";
	}

	@RequestMapping(method = RequestMethod.GET, value="/open_equipment_visualization_from_site")
	public String open_equipment_visualization_from_site(@RequestParam("selected") String selected_site, HttpServletRequest request) {
		ArrayList<Equipment> equips = Provisioning.getEquipmentsFromSite(HomeController.NS+selected_site);

		String valuesGraph = "";
		String hashTypes = "";
		int size = 0;
		int width  = 1000;
		int height = 800;
		
		for(Equipment equip : equips){
			hashTypes += "hash[\""+equip.getName()+"\"] = \"<b>"+equip.getName()+" is an individual of classes: </b><br><ul><li>Equipment</li></ul>\";";
			for(InterfaceOutput outs : equip.getOutputs()){
				valuesGraph += "graph.addEdge(graph.addNode(\""+outs.getName()+"\", {shape:\"INT_OUT_AZUL\"}),graph.addNode(\""+equip.getName()+"\", {shape:\"Equip_AZUL\"}), {name:'INV.componentOf'});";
				hashTypes += "hash[\""+outs.getName()+"\"] = \"<b>"+outs.getName()+" is an individual of classes: </b><br><ul><li>Output_Interface</li></ul>\";";
				size++;
			}
			
			for(String in : equip.getInputs()){
				valuesGraph += "graph.addEdge(graph.addNode(\""+in+"\", {shape:\"INT_IN_AZUL\"}),graph.addNode(\""+equip.getName()+"\", {shape:\"Equip_AZUL\"}), {name:'INV.componentOf'});";
				hashTypes += "hash[\""+in+"\"] = \"<b>"+in+" is an individual of classes: </b><br><ul><li>Input_Interface</li></ul>\";";
				size++;
			}

			for(Map.Entry<ArrayList<String>,Equipment> entry : equip.getBinds().entrySet()){
				valuesGraph += "graph.addEdge(graph.addNode(\""+entry.getKey().get(0)+"\", {shape:\"INT_OUT_AZUL\"}),graph.addNode(\""+entry.getKey().get(1)+"\", {shape:\"INT_IN_AZUL\"}), {name:'interface_binds'});";
				hashTypes += "hash[\""+entry.getKey().get(1)+"\"] = \"<b>"+entry.getKey().get(1)+" is an individual of classes: </b><br><ul><li>Input_Interface</li></ul>\";";
				valuesGraph += "graph.addEdge(graph.addNode(\""+entry.getValue().getName()+"\", {shape:\"Equip_AZUL\"}),graph.addNode(\""+entry.getKey().get(1)+"\", {shape:\"INT_IN_AZUL\"}), {name:'componentOf'});";
				size++;
			}
			if(equip.getBinds().isEmpty()){
				valuesGraph += "graph.addNode(\""+equip.getName()+"\", {shape:\"Equip_AZUL\"});";
				size++;
			}
		}

		width  += 400 * (size / 10);
		height += 400 * (size / 10);
		
		//session
		request.getSession().setAttribute("canClick", true);
		request.getSession().setAttribute("targetURL", "open_g800_visualization_from_equip?selected=");
		request.getSession().setAttribute("popupMessage", "Go to Equipment\'s components");
		request.getSession().setAttribute("valuesGraph", valuesGraph);
		request.getSession().setAttribute("width", width);
		request.getSession().setAttribute("height", height);
		request.getSession().setAttribute("hashTypes", hashTypes);
		request.getSession().setAttribute("size", size);

		return "showVisualization";
	}

	@RequestMapping(method = RequestMethod.GET, value="/open_g800_visualization_from_equip")
	public String open_g800_visualization_from_equip(@RequestParam("selected") String equip, HttpServletRequest request) {
		ArrayList<String> g800s = Provisioning.getG800FromEquipment(equip);
		ArrayList<String[]> triplas = Provisioning.triples_g800;
		HashMap<String, ArrayList<String>> hashIndv = Provisioning.ind_class;

		String valuesGraph = "";
		String hashTypes = "";
		int size = 0;
		int width  = 1000;
		int height = 800;
		
		for (String g800 : g800s) {
			valuesGraph += "graph.addNode(\""+g800.substring(g800.indexOf("#")+1)+"\", {shape:\""+getG800Image(hashIndv.get(g800))+"_AZUL\"});";
			hashTypes += "hash[\""+g800.substring(g800.indexOf("#")+1)+"\"] = \"<b>"+g800.substring(g800.indexOf("#")+1)+" is an individual of classes: </b><br><ul>";
			for(String type : hashIndv.get(g800)){
				if(type.contains("#"))
					hashTypes += "<li>"+type.substring(type.indexOf("#")+1)+"</li>";
			}
			hashTypes += "</ul>\";";
			size++;
		}

		for(String[] stCon : triplas){
			if(!hashIndv.containsKey(stCon[0]) || !hashIndv.containsKey(stCon[2])){
				valuesGraph += "graph.addEdge(graph.addNode(\""+stCon[0].substring(stCon[0].indexOf("#")+1)+"\", ";
				valuesGraph += "{shape:\""+getG800Image(HomeController.Search.GetClassesFrom(stCon[0], HomeController.InfModel))+"_AZUL\"}),";
				valuesGraph += "graph.addNode(\""+stCon[2].substring(stCon[2].indexOf("#")+1)+"\", ";
				valuesGraph += "{shape:\""+getG800Image(HomeController.Search.GetClassesFrom(stCon[2], HomeController.InfModel))+"_AZUL\"}), {name:'"+stCon[1].substring(stCon[1].indexOf("#")+1)+"'});";
				
				hashTypes += "hash[\""+stCon[0].substring(stCon[0].indexOf("#")+1)+"\"] = \"<b>"+stCon[0].substring(stCon[0].indexOf("#")+1)+" is an individual of classes: </b><br><ul>";
				for(String type : HomeController.Search.GetClassesFrom(stCon[0], HomeController.InfModel)){
					if(type.contains("#"))
						hashTypes += "<li>"+type.substring(type.indexOf("#")+1)+"</li>";
				}
				hashTypes += "</ul>\";";
				
				hashTypes += "hash[\""+stCon[2].substring(stCon[2].indexOf("#")+1)+"\"] = \"<b>"+stCon[2].substring(stCon[2].indexOf("#")+1)+" is an individual of classes: </b><br><ul>";
				for(String type : HomeController.Search.GetClassesFrom(stCon[2], HomeController.InfModel)){
					if(type.contains("#"))
						hashTypes += "<li>"+type.substring(type.indexOf("#")+1)+"</li>";
				}
				hashTypes += "</ul>\";";
				
				continue;
			}
				
			valuesGraph += "graph.addEdge(graph.addNode(\""+stCon[0].substring(stCon[0].indexOf("#")+1)+"\", ";
			valuesGraph += "{shape:\""+getG800Image(hashIndv.get(stCon[0]))+"_AZUL\"}),";
			valuesGraph += "graph.addNode(\""+stCon[2].substring(stCon[2].indexOf("#")+1)+"\", ";
			valuesGraph += "{shape:\""+getG800Image(hashIndv.get(stCon[2]))+"_AZUL\"}), {name:'"+stCon[1].substring(stCon[1].indexOf("#")+1)+"'});";
			size++;
		}
		
		valuesGraph += "graph.pruneNode(\""+equip+"\");";
		
		width  += 400 * (size / 10);
		height += 400 * (size / 10);

		//session
		request.getSession().setAttribute("canClick", false);
		request.getSession().setAttribute("valuesGraph", valuesGraph);
		request.getSession().setAttribute("width", width);
		request.getSession().setAttribute("height", height);
		request.getSession().setAttribute("hashTypes", hashTypes);
		request.getSession().setAttribute("size", size);
		
		return "showVisualization";
	}

	@RequestMapping(method = RequestMethod.GET, value="/connect_equip_binds")
	public @ResponseBody String connect_equip_binds(@RequestParam("equip_source") String equip_source,@RequestParam("interface_source") String interface_source,@RequestParam("equip_target") String equip_target,@RequestParam("interface_target") String interface_target , HttpServletRequest request) {
		DtoResultAjax dto = ProvisioningController.binds(interface_target, interface_source, request, true, null);
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

	public static String getG800Image(ArrayList<String> elemTypes){
		for(String type: elemTypes){
			if(elements.containsKey(type.substring(type.indexOf("#")+1)))
				return elements.get(type.substring(type.indexOf("#")+1));
		}
		return "dot";
	}

	@RequestMapping(method = RequestMethod.GET, value="/connects_provisoning_visualization")
	public static String connects_provisoning_visualization(HttpServletRequest request) {
		
		
		
		return "connectsProvisioning";
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value="/provisoning_visualization")
	public static String provisoning_visualization(HttpServletRequest request) {
		ProvisioningController.getEquipmentsWithRPs(HomeController.InfModel, HomeController.NS, null, null);
		elementsInitialize();
		Provisioning.inferInterfaceConnections();
		ArrayList<Equipment> list = Provisioning.getEquipmentsConnectionsBinds();

		String arborStructure = "";
		String hashEquipIntOut = "";
		String hashTypes = "";
		String hashAllowed = "";
		
		int size = 0;
		int width  = 1000;
		int height = 800;
		boolean canConnect = false;
		
		for(Equipment equip : list){
			canConnect = false;
			hashEquipIntOut += "hashEquipIntOut['"+equip.getName()+"'] = new Array();";
			hashTypes += "hash[\""+equip.getName()+"\"] = \"<b>"+equip.getName()+" is an individual of classes: </b><br><ul><li>Equipment</li></ul>\";";
			for(InterfaceOutput outs : equip.getOutputs()){
				hashEquipIntOut += "hashEquipIntOut['"+equip.getName()+"']['"+outs.getName()+"'] = \""+outs.isConnected()+"\";";
				if(hashAllowed.contains(equip.getName()))
					continue;
				ArrayList<String> possibleList = ProvisioningController.getCandidateInterfacesForConnection(outs.getName());
				for(String possibleConnection : possibleList){
					if(possibleConnection.contains("true")){
						canConnect = true;
						hashAllowed += "hashAllowed[\""+equip.getName()+"\"] = \"VERDE\";";
						break;
					}
				}
			}

			for(Map.Entry<ArrayList<String>,Equipment> entry : equip.getBinds().entrySet()){
				arborStructure += "graph.addEdge(graph.addNode(\""+equip.getName()+"\", {shape:\"Equip_ROXO\"}),graph.addNode(\""+entry.getValue().getName()+"\", {shape:\""+getG800Image(HomeController.Search.GetClassesFrom(HomeController.NS+entry.getValue().getName(), HomeController.InfModel))+"_ROXO\"}), {name:'binds:";
				arborStructure += entry.getKey().get(0)+"-"+entry.getKey().get(1);
				arborStructure += "'});";
				size++;
			}
			
			if(equip.getBinds().isEmpty()){
				arborStructure += "graph.addNode(\""+equip.getName()+"\", {shape:\"Equip_ROXO\"});";
				size++;
			}
		}

		//Getting Physical medias
		
		ArrayList<String[]> pms = Provisioning.getAllPhysicalMediaAndBinds();
		/*
		 * pm[0] = connected equipment interface (output)
		 * pm[1] = connected pm port (input)
		 * pm[2] = equipment
		 * pm[3] = pm 
		 * pm[4] = connected equipment interface (input)
		 * pm[5] = connected pm port (output)
		 * pm[6] = equipment
		 * 
		 * */
		
		for(String[] pm : pms){
			hashEquipIntOut += "hashEquipIntOut['"+pm[3].substring(pm[3].indexOf("#")+1)+"'] = new Array();";
			hashTypes += "hash[\""+pm[3].substring(pm[3].indexOf("#")+1)+"\"] = \"<b>"+pm[3].substring(pm[3].indexOf("#")+1)+" is an individual of classes: </b><br><ul><li>Physical Media</li></ul>\";";
			
			if(pm[0] != null){
				arborStructure += "graph.addEdge(graph.addNode(\""+pm[3].substring(pm[3].indexOf("#")+1)+"\", {shape:\"PM_ROXO\"}),graph.addNode(\""+pm[2].substring(pm[2].indexOf("#")+1)+"\", {shape:\"Equip_ROXO\"}), {name:'binds:"+pm[0].substring(pm[0].indexOf("#")+1)+"-"+pm[1].substring(pm[1].indexOf("#")+1)+"'});";
				size++;
			}
			
			if(pm[5] != null){
				arborStructure += "graph.addEdge(graph.addNode(\""+pm[3].substring(pm[3].indexOf("#")+1)+"\", {shape:\"PM_ROXO\"}),graph.addNode(\""+pm[6].substring(pm[6].indexOf("#")+1)+"\", {shape:\"Equip_ROXO\"}), {name:'binds:"+pm[4].substring(pm[4].indexOf("#")+1)+"-"+pm[5].substring(pm[5].indexOf("#")+1)+"'});";
				hashEquipIntOut += "hashEquipIntOut['"+pm[3].substring(pm[3].indexOf("#")+1)+"']['"+pm[5]+"'] = \"true\";";
				size++;
			}
			
			if(pm[0] == null && pm[4] == null){
				arborStructure += "graph.addNode(\""+pm[3].substring(pm[3].indexOf("#")+1)+"\", {shape:\"PM_ROXO\"});";
			}
		}		
		
		width  += 400 * (size / 10);
		height += 400 * (size / 10);
		
		//session
		request.getSession().setAttribute("valuesGraph", arborStructure);
		request.getSession().setAttribute("width", width);
		request.getSession().setAttribute("height", height);
		request.getSession().setAttribute("hashEquipIntOut", hashEquipIntOut);
		request.getSession().setAttribute("hashTypes", hashTypes);
		request.getSession().setAttribute("hashAllowed", hashAllowed);
		request.getSession().setAttribute("size", size);

		return "equipmentVisualizer";
	}

	private static void elementsInitialize(){
		if(elements != null)
			return;
		
		elements = new HashMap<String, String>();
		
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
	}
	
}

