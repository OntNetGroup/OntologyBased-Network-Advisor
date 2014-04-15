package br.ufes.inf.padtec.tnokco.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.inf.nemo.padtec.owl2arborjs.ArborParser;
import br.inf.nemo.padtec.tnokco.TNOKCOGraphPlotting;
import br.ufes.inf.nemo.okco.model.DtoResultAjax;
import br.ufes.inf.nemo.okco.model.Instance;
import br.ufes.inf.padtec.tnokco.business.Equipment;
import br.ufes.inf.padtec.tnokco.business.Provisioning;

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
			HashMap<String,ArrayList<String>> hash = new HashMap<String, ArrayList<String>>();
			for(Instance i : HomeController.ListAllInstances){
				hash.put(i.name,i.ListClasses);
			}

			valuesGraph = graphPlotting.getArborStructureFromClass(HomeController.InfModel,HomeController.NS+"Site",hash);
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

	@RequestMapping(method = RequestMethod.GET, value="/open_equipment_visualization_from_site")
	public String open_equipment_visualization_from_site(@RequestParam("site") String site, HttpServletRequest request) {

		ArrayList<Equipment> list = Provisioning.getEquipmentsConnectionsBinds();

		String arborStructure = "";
		String relationName = "";
		String target = "";
		String hashEquipIntIn = "";
		String hashEquipIntOut = "";
		String bindsBetween = "";


		for(Equipment equip: list){
			HashMap<ArrayList<String>, Equipment> binds = equip.getBinds();
			relationName = "interface_binds:";
			for(Map.Entry<ArrayList<String>, Equipment> x : binds.entrySet()){
				relationName += equip.getName()+"."+x.getKey().get(0)+"-"+x.getValue().getName()+"."+x.getKey().get(1)+",";
				target = x.getValue().getName();
			}
			relationName = relationName.substring(0, relationName.length()-1);
			arborStructure += ArborParser.getArborEdge("interface_binds", equip.getName(), target, false);
			hashEquipIntIn += getJSHash("hashEquipIntIn", equip.getName(), equip.getInputs());
			hashEquipIntOut += getJSHash("hashEquipIntOut", equip.getName(), equip.getOutputs());
		}

		hashEquipIntIn += "hashEquipIntIn['eq1']['eq1.in1'] = false;";
		hashEquipIntIn += "hashEquipIntIn['eq1']['eq1.in2'] = false;";
		hashEquipIntIn += "hashEquipIntIn['eq1']['eq1.in3'] = false;";
		hashEquipIntIn += "hashEquipIntIn['eq2']['eq2.in1'] = true;";
		hashEquipIntIn += "hashEquipIntIn['eq2']['eq2.in2'] = true;";
		hashEquipIntIn += "hashEquipIntIn['eq2']['eq2.in3'] = true;";
		hashEquipIntIn += "hashEquipIntIn['eq3']['eq3.in1'] = true;";
		hashEquipIntIn += "hashEquipIntIn['eq3']['eq3.in2'] = true;";
		hashEquipIntIn += "hashEquipIntIn['eq4']['eq4.in1'] = false;";

		hashEquipIntOut += "hashEquipIntOut['eq1']['eq1.out1'] = true;";
		hashEquipIntOut += "hashEquipIntOut['eq1']['eq1.out2'] = true;";
		hashEquipIntOut += "hashEquipIntOut['eq2']['eq2.out1'] = false;";
		hashEquipIntOut += "hashEquipIntOut['eq2']['eq2.out2'] = false;";
		hashEquipIntOut += "hashEquipIntOut['eq3']['eq3.out1'] = false;";
		hashEquipIntOut += "hashEquipIntOut['eq3']['eq3.out2'] = false;";
		hashEquipIntOut += "hashEquipIntOut['eq4']['eq4.out1'] = false;";

		arborStructure += "graph.addEdge(graph.addNode(\"eq1\", {shape:\"dot\",color:\"green\"}),graph.addNode(\"eq2\", {shape:\"dot\",color:\"green\"}), {name:'binds:eq1.out1-eq2.in1'})";
		arborStructure += "graph.addEdge(graph.addNode(\"eq1\", {shape:\"dot\",color:\"green\"}),graph.addNode(\"eq3\", {shape:\"dot\",color:\"green\"}), {name:'binds:eq1.out2-eq3.in1'})";
		arborStructure += "graph.addEdge(graph.addNode(\"eq3\", {shape:\"dot\",color:\"green\"}),graph.addNode(\"eq2\", {shape:\"dot\",color:\"green\"}), {name:'binds:eq3.out1-eq2.in2'})";
		arborStructure += "graph.addEdge(graph.addNode(\"eq4\", {shape:\"dot\",color:\"green\"}),graph.addNode(\"eq2\", {shape:\"dot\",color:\"green\"}), {name:'binds:eq4.out1-eq2.in3'})";


		int width  = 800;
		int height = 600;

		//session
		request.getSession().setAttribute("valuesGraph", arborStructure);
		request.getSession().setAttribute("width", width);
		request.getSession().setAttribute("height", height);
		request.getSession().setAttribute("hashEquipIntIn", hashEquipIntIn);
		request.getSession().setAttribute("hashEquipIntOut", hashEquipIntOut);

		return "equipmentVisualization";
	}

	@RequestMapping(method = RequestMethod.GET, value="/open_equipment_visualization")
	public String open_equipment_visualization(HttpServletRequest request) {

		ArrayList<Equipment> list = Provisioning.getEquipmentsConnectionsBinds();

		String arborStructure = "";
		String relationName = "";
		String target = "";
		String hashEquipIntIn = "";
		String hashEquipIntOut = "";

		for(Equipment equip: list){
			HashMap<ArrayList<String>, Equipment> binds = equip.getBinds();
			relationName = "interface_binds:";
			for(Map.Entry<ArrayList<String>, Equipment> x : binds.entrySet()){
				relationName += equip.getName()+"."+x.getKey().get(0)+"-"+x.getValue().getName()+"."+x.getKey().get(1)+",";
				target = x.getValue().getName();
			}
			relationName = relationName.substring(0, relationName.length()-1);
			arborStructure += ArborParser.getArborEdge("interface_binds", equip.getName(), target, false);
			hashEquipIntIn += getJSHash("hashEquipIntIn", equip.getName(), equip.getInputs());
			hashEquipIntOut += getJSHash("hashEquipIntOut", equip.getName(), equip.getOutputs());
		}

		hashEquipIntIn += "hashEquipIntIn['eq1']['eq1.in1'] = false;";
		hashEquipIntIn += "hashEquipIntIn['eq1']['eq1.in2'] = false;";
		hashEquipIntIn += "hashEquipIntIn['eq1']['eq1.in3'] = false;";
		hashEquipIntIn += "hashEquipIntIn['eq2']['eq2.in1'] = true;";
		hashEquipIntIn += "hashEquipIntIn['eq2']['eq2.in2'] = true;";
		hashEquipIntIn += "hashEquipIntIn['eq2']['eq2.in3'] = true;";
		hashEquipIntIn += "hashEquipIntIn['eq3']['eq3.in1'] = true;";
		hashEquipIntIn += "hashEquipIntIn['eq3']['eq3.in2'] = true;";
		hashEquipIntIn += "hashEquipIntIn['eq4']['eq4.in1'] = false;";

		hashEquipIntOut += "hashEquipIntOut['eq1']['eq1.out1'] = true;";
		hashEquipIntOut += "hashEquipIntOut['eq1']['eq1.out2'] = true;";
		hashEquipIntOut += "hashEquipIntOut['eq2']['eq2.out1'] = false;";
		hashEquipIntOut += "hashEquipIntOut['eq2']['eq2.out2'] = false;";
		hashEquipIntOut += "hashEquipIntOut['eq3']['eq3.out1'] = false;";
		hashEquipIntOut += "hashEquipIntOut['eq3']['eq3.out2'] = false;";
		hashEquipIntOut += "hashEquipIntOut['eq4']['eq4.out1'] = false;";

		arborStructure += "graph.addEdge(graph.addNode(\"eq1\", {shape:\"dot\",color:\"green\"}),graph.addNode(\"eq2\", {shape:\"dot\",color:\"green\"}), {name:'binds:eq1.out1-eq2.in1'})";
		arborStructure += "graph.addEdge(graph.addNode(\"eq1\", {shape:\"dot\",color:\"green\"}),graph.addNode(\"eq3\", {shape:\"dot\",color:\"green\"}), {name:'binds:eq1.out2-eq3.in1'})";
		arborStructure += "graph.addEdge(graph.addNode(\"eq3\", {shape:\"dot\",color:\"green\"}),graph.addNode(\"eq2\", {shape:\"dot\",color:\"green\"}), {name:'binds:eq3.out1-eq2.in2'})";
		arborStructure += "graph.addEdge(graph.addNode(\"eq4\", {shape:\"dot\",color:\"green\"}),graph.addNode(\"eq2\", {shape:\"dot\",color:\"green\"}), {name:'binds:eq4.out1-eq2.in3'})";


		int width  = 800;
		int height = 600;

		//session
		request.getSession().setAttribute("valuesGraph", arborStructure);
		request.getSession().setAttribute("width", width);
		request.getSession().setAttribute("height", height);
		request.getSession().setAttribute("hashEquipIntIn", hashEquipIntIn);
		request.getSession().setAttribute("hashEquipIntOut", hashEquipIntOut);

		return "equipmentVisualizer";
	}

	@RequestMapping(method = RequestMethod.GET, value="/connect_equip_binds")
	public DtoResultAjax connect_equip_binds(@RequestParam("equip_source") String equip_source,@RequestParam("interface_source") String interface_source,@RequestParam("equip_target") String equip_target,@RequestParam("interface_target") String interface_target , HttpServletRequest request) {
		boolean erro = false;
		DtoResultAjax dto = new DtoResultAjax();
		
		if(erro){
			dto.ok = false;
			dto.result = "Error! Something wrong happaned.";
			return dto;
		}
		
		System.out.println(equip_source+"#"+interface_source+" binds "+equip_target+"#"+interface_target);
		dto.ok = true;
		return dto;
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
