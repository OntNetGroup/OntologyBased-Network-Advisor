package br.ufes.inf.padtec.tnokco.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;
import br.ufes.inf.padtec.tnokco.business.ApplicationQueryUtil;
import br.ufes.inf.padtec.tnokco.business.DtoResultAjax;
import br.ufes.inf.padtec.tnokco.business.Equipment;
import br.ufes.inf.padtec.tnokco.business.InterfaceOutput;
import br.ufes.inf.padtec.tnokco.business.Provisioning;

@Controller
public class VisualizationController {
	private static HashMap<String,String> elements = null;
	 
	@RequestMapping(method = RequestMethod.GET, value="/open_visualizator")
	public String open_visualizator(HttpServletRequest request) {

		if(OKCoUploader.getBaseModel() == null)
			return "open_visualizator"; 

		List<String> sites = QueryUtil.getIndividualsURI(OKCoUploader.getInferredModel(), OKCoUploader.getNamespace()+"Site");
		List<String> equipments = QueryUtil.getIndividualsURI(OKCoUploader.getInferredModel(), OKCoUploader.getNamespace()+"Equipment");

		Provisioning.inferInterfaceConnections();
		Provisioning.getAllG800();
		HashMap<String, List<String>> g800List = Provisioning.ind_class;

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
			List<String> sites = Provisioning.getAllSitesAndConnections();
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
			List<String> g800s = Provisioning.getAllG800();
			ArrayList<String[]> triplas = Provisioning.triples_g800;
			HashMap<String, List<String>> hashIndv = Provisioning.ind_class;

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
			List<String> allInstances =QueryUtil.getIndividualsURIFromAllClasses(OKCoUploader.getInferredModel());
			System.out.println(allInstances);
			for (String instance : allInstances) {
				List<DtoInstanceRelation> targetList = ApplicationQueryUtil.GetInstanceRelations(OKCoUploader.getInferredModel(),instance);

				List<String> classes = QueryUtil.getClassesURI(OKCoUploader.getInferredModel(),instance);
				for (DtoInstanceRelation dtoInstanceRelation : targetList) {

					valuesGraph += "graph.addEdge(graph.addNode(\""+instance.substring(instance.indexOf("#")+1)+"\", ";
					valuesGraph += "{shape:\""+getG800Image(classes)+"_AZUL\"}),";
					valuesGraph += "graph.addNode(\""+dtoInstanceRelation.Target.substring(dtoInstanceRelation.Target.indexOf("#")+1)+"\", ";
					valuesGraph += "{shape:\""+getG800Image(QueryUtil.getClassesURI(OKCoUploader.getInferredModel(),dtoInstanceRelation.Target))+"_AZUL\"}), ";
					valuesGraph	+= "{name:'"+dtoInstanceRelation.Property.substring(dtoInstanceRelation.Property.indexOf("#")+1)+"'});";
					size++;
				}

				if(targetList.isEmpty()){
					valuesGraph += "graph.addNode(\""+instance.substring(instance.indexOf("#")+1)+"\", ";
					valuesGraph += "{shape:\""+getG800Image(QueryUtil.getClassesURI(OKCoUploader.getInferredModel(),instance))+"_AZUL\"});";
				}

				hashTypes += "hash[\""+instance.substring(instance.indexOf("#")+1)+"\"] = \"<b>"+instance.substring(instance.indexOf("#")+1)+" is an individual of classes: </b><br><ul>";
				for(String type : classes){
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
		request.getSession().setAttribute("nameSpace", OKCoUploader.getNamespace());
		request.getSession().setAttribute("size", size);

		return "showVisualization";
	}

	@RequestMapping(method = RequestMethod.GET, value="/open_equipment_visualization_from_site")
	public String open_equipment_visualization_from_site(@RequestParam("selected") String selected_site, HttpServletRequest request) {
		ArrayList<Equipment> equips = Provisioning.getEquipmentsFromSite(OKCoUploader.getNamespace()+selected_site);

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
		List<String> g800s = Provisioning.getG800FromEquipment(equip);
		ArrayList<String[]> triplas = Provisioning.triples_g800;
		HashMap<String, List<String>> hashIndv = Provisioning.ind_class;

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
				valuesGraph += "{shape:\""+getG800Image(QueryUtil.getClassesURI(OKCoUploader.getInferredModel(),stCon[0]))+"_AZUL\"}),";
				valuesGraph += "graph.addNode(\""+stCon[2].substring(stCon[2].indexOf("#")+1)+"\", ";
				valuesGraph += "{shape:\""+getG800Image(QueryUtil.getClassesURI(OKCoUploader.getInferredModel(),stCon[2]))+"_AZUL\"}), {name:'"+stCon[1].substring(stCon[1].indexOf("#")+1)+"'});";

				hashTypes += "hash[\""+stCon[0].substring(stCon[0].indexOf("#")+1)+"\"] = \"<b>"+stCon[0].substring(stCon[0].indexOf("#")+1)+" is an individual of classes: </b><br><ul>";
				for(String type : QueryUtil.getClassesURI(OKCoUploader.getInferredModel(),stCon[0])){
					if(type.contains("#"))
						hashTypes += "<li>"+type.substring(type.indexOf("#")+1)+"</li>";
				}
				hashTypes += "</ul>\";";

				hashTypes += "hash[\""+stCon[2].substring(stCon[2].indexOf("#")+1)+"\"] = \"<b>"+stCon[2].substring(stCon[2].indexOf("#")+1)+" is an individual of classes: </b><br><ul>";
				for(String type : QueryUtil.getClassesURI(OKCoUploader.getInferredModel(),stCon[2])){
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

	@RequestMapping(method = RequestMethod.GET, value="/do_connects")
	public @ResponseBody String do_connects(@RequestParam("rp_src") String rp_src,@RequestParam("rp_trg") String rp_trg, @RequestParam("rp_type") String rp_type, HttpServletRequest request) {
		try {
			Provisioning.connects(rp_src, rp_trg, rp_type);
			
			/*
			 * Verify the new possible connects
			 * */
			
			String hashAllowed = new String();
			ArrayList<String> equipsWithRps = new ArrayList<String>();
			ArrayList<String> connectsBetweenEqsAndRps = new ArrayList<String>();
			ArrayList<String> connectsBetweenRps = new ArrayList<String>();
			ArrayList<String[]> possibleConnections;
			
			ProvisioningController.getEquipmentsWithRPs(OKCoUploader.getInferredModel(), OKCoUploader.getNamespace(), equipsWithRps, connectsBetweenEqsAndRps, connectsBetweenRps);

			for (String connections : connectsBetweenEqsAndRps) {
				String src = connections.split("#")[0];
				String trg = connections.split("#")[1];

				possibleConnections = Provisioning.getPossibleConnects(src);
				if(!possibleConnections.isEmpty() && !hashAllowed.contains(src))
					hashAllowed += src+"#";

				possibleConnections = Provisioning.getPossibleConnects(trg);
				if(!possibleConnections.isEmpty() && !hashAllowed.contains(trg))
					hashAllowed += trg+"#";
			}
			
			for (String equipWithRP : equipsWithRps) {
				String equip = equipWithRP.split("#")[0];
				String rp = equipWithRP.split("#")[1];

				if(!equip.isEmpty()){
					if(!Provisioning.getPossibleConnects(rp).isEmpty() && !hashAllowed.contains(rp)){
						hashAllowed += equip+"#";
					}
				}
			}
			
			return hashAllowed;
		} catch (Exception e) {
			e.printStackTrace();
			return "false";
		}

	}

	@RequestMapping(method = RequestMethod.GET, value="/get_possible_connections")
	public @ResponseBody String get_possible_connections(@RequestParam("rp") String rp, HttpServletRequest request) {
		/*
		 * [0] = RP's name
		 * [1] = Connection type
		 * */
		ArrayList<String[]> list = Provisioning.getPossibleConnects(rp);

		String hashEquipIntIn = "";

		for(String[] line : list){
			hashEquipIntIn += line[0].substring(line[0].indexOf("#")+1)+"#"+line[1]+";";
		}

		return hashEquipIntIn;
	}

	@RequestMapping(method = RequestMethod.GET, value="/connects")
	public static String connects(HttpServletRequest request) {
		elementsInitialize();

		String arborStructure = new String();
		String hashEquipIntOut = new String();
		String hashTypes = new String();
		String hashAllowed = new String();
		String hashRPEquip = new String();

		int size = 0;
		int width  = 1000;
		int height = 800;

		ArrayList<String> equipsWithRps = new ArrayList<String>();
		ArrayList<String> connectsBetweenEqsAndRps = new ArrayList<String>();
		ArrayList<String> connectsBetweenRps = new ArrayList<String>();

		ProvisioningController.getEquipmentsWithRPs(OKCoUploader.getInferredModel(), OKCoUploader.getNamespace(), equipsWithRps, connectsBetweenEqsAndRps, connectsBetweenRps);

		/* *
		  	equipsWithRps
			[#skfep2, skeq2#skfep1, #sofep2, #sofep1, #skap3, #soap3, #skap1, soeq1#soap1, #skap2, #soap2]

			connectsBetweenEqsAndRps
			[skeq3#skfep2, pm1#skfep2, pm1#sofep2, soeq3#sofep2, soap2#sofep1, soap3#sofep1, soeq1#sofep1, skeq2#skap3, skeq4#skap3, soeq3#soap3, skeq1#skap1, skeq2#skap1, skeq2#skap2, skeq3#skap2, soeq3#soap2]
		 * */

		ArrayList<String> usedRPs = new ArrayList<String>();
		ArrayList<String[]> possibleConnections;
		for (String connections : connectsBetweenEqsAndRps) {
			String src = connections.split("#")[0];
			String trg = connections.split("#")[1];

			possibleConnections = Provisioning.getPossibleConnects(src);
			arborStructure += "graph.addEdge(graph.addNode(\""+src+"\", {shape:\""+getG800Image(QueryUtil.getClassesURI(OKCoUploader.getInferredModel(),OKCoUploader.getNamespace()+src))+"_"+(possibleConnections.isEmpty()?"ROXO":"VERDE")+"\"}),";
			if(!possibleConnections.isEmpty() && !hashAllowed.contains(src))
				hashAllowed += "hashAllowed.push(\""+src+"\");";

			possibleConnections = Provisioning.getPossibleConnects(trg);
			arborStructure += "graph.addNode(\""+trg+"\", {shape:\""+getG800Image(QueryUtil.getClassesURI(OKCoUploader.getInferredModel(),OKCoUploader.getNamespace()+trg))+"_"+(possibleConnections.isEmpty()?"ROXO":"VERDE")+"\"}), {name:' '});";
			if(!possibleConnections.isEmpty() && !hashAllowed.contains(trg))
				hashAllowed += "hashAllowed.push(\""+trg+"\");";

			size++;

			usedRPs.add(src);
			usedRPs.add(trg);

			hashTypes += "hash[\""+src+"\"] = \"<b>"+src+" is an individual of classes: </b><br><ul>";
			for(String type : QueryUtil.getClassesURI(OKCoUploader.getInferredModel(),OKCoUploader.getNamespace()+src)){
				if(type.contains("#"))
					hashTypes += "<li>"+type.substring(type.indexOf("#")+1)+"</li>";
			}
			hashTypes += "</ul>\";";

			hashTypes += "hash[\""+trg+"\"] = \"<b>"+trg+" is an individual of classes: </b><br><ul>";
			for(String type : QueryUtil.getClassesURI(OKCoUploader.getInferredModel(),OKCoUploader.getNamespace()+trg)){
				if(type.contains("#"))
					hashTypes += "<li>"+type.substring(type.indexOf("#")+1)+"</li>";
			}
			hashTypes += "</ul>\";";

		}

		HashMap<String, String> rpXequip = new HashMap<String, String>();

		for (String equipWithRP : equipsWithRps) {
			String equip = equipWithRP.split("#")[0];
			String rp = equipWithRP.split("#")[1];

			if(!equip.isEmpty()){
				Boolean situation;
				if(usedRPs.contains(rp)){
					situation = true;
				}else{
					situation = false;
					hashTypes += "hash[\""+equip+"\"] = \"<b>"+equip+" is an individual of classes: </b><br><ul><li>Equipment</li></ul>\";";
					if(!Provisioning.getPossibleConnects(rp).isEmpty() && !hashAllowed.contains(rp)){
						arborStructure += "graph.getNode(\""+equip+"\").data.shape = graph.getNode(\""+equip+"\").data.shape.split(\"_\")[0]+\"_VERDE\";";
						hashAllowed += "hashAllowed.push(\""+equip+"\");";
					}

				}

				hashRPEquip += "hashRPEquip['"+rp+"'] = \""+equip+"\";";

				hashEquipIntOut += "hashEquipIntOut['"+equip+"'] = new Array();";
				hashEquipIntOut += "hashEquipIntOut['"+equip+"']['"+rp+"'] = \""+situation.toString()+"\";";
				rpXequip.put(rp, equip);
			}
		}

		for (String rpXrp : connectsBetweenRps) {
			String srcRP = rpXrp.split("#")[0];
			String trgRP = rpXrp.split("#")[1];

			String srcNode = srcRP;
			String trgNode = trgRP;

			if(rpXequip.containsKey(srcRP)){ //src is inside a equip
				srcNode = rpXequip.get(srcRP);
			}
			if(rpXequip.containsKey(trgRP)){ //trg is inside a equip
				trgNode = rpXequip.get(trgRP);
			}
			possibleConnections = Provisioning.getPossibleConnects(srcNode);
			arborStructure += "graph.addEdge(graph.addNode(\""+srcNode+"\", {shape:\""+getG800Image(QueryUtil.getClassesURI(OKCoUploader.getInferredModel(),OKCoUploader.getNamespace()+srcNode))+"_"+(possibleConnections.isEmpty()?"ROXO":"VERDE")+"\"}),";

			possibleConnections = Provisioning.getPossibleConnects(trgNode);
			arborStructure += "graph.addNode(\""+trgNode+"\", {shape:\""+getG800Image(QueryUtil.getClassesURI(OKCoUploader.getInferredModel(),OKCoUploader.getNamespace()+trgNode))+"_"+(possibleConnections.isEmpty()?"ROXO":"VERDE")+"\"}), {name:'connects'});";
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
		request.getSession().setAttribute("hashRPEquip", hashRPEquip);
		request.getSession().setAttribute("size", size);

		return "connects";
	}

	//Binds Provisioning
	@RequestMapping(method = RequestMethod.GET, value="/binds")
	public static String bindsV(HttpServletRequest request) {
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

		for(Equipment equip : list){
			hashEquipIntOut += "hashEquipIntOut['"+equip.getName()+"'] = new Array();";
			hashTypes += "hash[\""+equip.getName()+"\"] = \"<b>"+equip.getName()+" is an individual of classes: </b><br><ul><li>Equipment</li></ul>\";";
			for(InterfaceOutput outs : equip.getOutputs()){
				hashEquipIntOut += "hashEquipIntOut['"+equip.getName()+"']['"+outs.getName()+"'] = \""+outs.isConnected()+"\";";
				if(hashAllowed.contains(equip.getName()))
					continue;
				ArrayList<String> possibleList = ProvisioningController.getCandidateInterfacesForConnection(outs.getName());
				for(String possibleConnection : possibleList){
					if(possibleConnection.contains("true")){
						hashAllowed += "hashAllowed[\""+equip.getName()+"\"] = \"VERDE\";";
						break;
					}
				}
			}

			for(Map.Entry<ArrayList<String>,Equipment> entry : equip.getBinds().entrySet()){
				arborStructure += "graph.addEdge(graph.addNode(\""+equip.getName()+"\", {shape:\"Equip_ROXO\"}),graph.addNode(\""+entry.getValue().getName()+"\", {shape:\""+getG800Image(QueryUtil.getClassesURI(OKCoUploader.getInferredModel(),OKCoUploader.getNamespace()+entry.getValue().getName()))+"_ROXO\"}), {name:'binds:";
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

		return "binds";
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
		elements.put("Forwarding_End_Point", "RP");
		elements.put("Forwarding_Point", "RP");
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
		//		elements.put("Forwarding_Rule", "FWR_RULE");
		elements.put("Equipment", "Equip");
		elements.put("Site", "SITE");
	}

	public static String getG800Image(List<String> elemTypes){
		for(String type: elemTypes){
			if(elements.containsKey(type.substring(type.indexOf("#")+1))){
				return elements.get(type.substring(type.indexOf("#")+1));
			}
		}
		return "dot";
	}

}

