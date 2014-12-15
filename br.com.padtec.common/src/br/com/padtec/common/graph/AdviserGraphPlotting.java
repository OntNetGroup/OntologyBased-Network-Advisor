package br.com.padtec.common.graph;

import java.util.ArrayList;
import java.util.HashMap;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;

public class AdviserGraphPlotting extends BaseGraphPlotting{

	public AdviserGraphPlotting() {
		super();
		createHash();
	}

	public AdviserGraphPlotting(HashMap<String, ArrayList<String>> hash) {
		super();
		createHash();
		this.hash = hash;
	}

	@Override
	public String getArborNode(String elem, boolean isCenterNode){
		String arborNode = "";
		String name;
		if(elem.contains("^^")){
			//Datatype
			name = elem.substring(0, elem.indexOf("^^"));
			arborNode = "graph.addNode(\""+name+"\", {shape:\"Datatype_AZUL\"})";
		}else{
			//Element
			name = elem.substring(elem.indexOf("#")+1);
			String shape,color;

			shape = getITUShape(elem);
			
			if(shape.equals("dot")){
				if(isClass(elem)){
					color = AZUL;
				}else{
					color = VERDE;
				}

				if(isCenterNode){
					color = ROXO;
				}

				arborNode += "graph.addNode(\""+name+"\", {shape:\""+shape+"\", color:\""+color+"\"})";	
			}else{
				
				if(isClass(elem)){
					color = "AZUL";
				}else{
					color = "VERDE";
				}

				if(isCenterNode){
					color = "ROXO";
				}

				arborNode += "graph.addNode(\""+name+"\", {shape:\""+shape+"_"+color+"\"})";
			}
		}

		return arborNode;
	}

	@Override
	public boolean isClass(String elem) {
		if(hash.get(elem) == null){
			return false;
		}
		return true;
	}


	public String getITUShape(String element){
		String type = element.substring(element.indexOf("#")+1);
		
		if(elements.containsKey(type))
			return elements.get(type);
		
		if(!hash.containsKey(type))
			return "dot";
		
		for(String owlType : hash.get(type)){
			String ituType = owlType.substring(owlType.indexOf("#")+1); 
			if(elements.containsKey(ituType))
				return elements.get(ituType);
		}
		return "dot";
	}

	private HashMap<String,String> elements = new HashMap<String, String>();

	private void createHash(){
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

	@Override
	public String getSubtitle() {
		return "Subtitle_TNOKCO.png";
	}
	
	public String getArborStructureFromClass(InfModel ontology, String cls, HashMap<String,ArrayList<String>> hashClasses){
		String query = QueryManager.getRelationsBetweenClass(cls);
		ResultSet resultSet = QueryManager.runQuery(ontology, query);

		ArborParser arborParser = new ArborParser(ontology,this,hashClasses);
		String arborStructure = arborParser.getArborJsString(resultSet,false);

		String arborHashStructure = arborParser.getArborHashStructure();

		return callBack(arborStructure, arborHashStructure);
	}

	public String getArborStructureFromEquipmentVisualization(OntModel model) {

		return null;
	}
	
}