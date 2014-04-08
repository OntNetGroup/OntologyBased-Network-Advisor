package br.inf.nemo.padtec.tnokco;

import java.util.HashMap;

import br.inf.nemo.padtec.graphplotting.GraphPlotting;
import br.inf.nemo.padtec.manager.query.QueryManager;
import br.inf.nemo.padtec.owl2arborjs.ArborParser;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;

public class TNOKCOGraphPlotting extends GraphPlotting{

	public TNOKCOGraphPlotting() {
		super();
		createHash();
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
		
		if(!hash.containsKey(element))
			return "dot";
		
		for(String owlType : hash.get(element)){
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
	}

	@Override
	public String getSubtitle() {
		return "Subtitle_TNOKCO.png";
	}
	
	public String getArborStructureFromClass(InfModel ontology, String cls){
		String query = QueryManager.getRelationsBetweenClass(cls);
		ResultSet resultSet = QueryManager.runQuery(ontology, query);

		ArborParser arborParser = new ArborParser(ontology,this);
		String arborStructure = arborParser.getArborJsString(resultSet);

		String arborHashStructure = arborParser.getArborHashStructure();

		return callBack(arborStructure, arborHashStructure);
	}

	public String getArborStructureFromEquipmentVisualization(OntModel model) {

		return null;
	}
	
}