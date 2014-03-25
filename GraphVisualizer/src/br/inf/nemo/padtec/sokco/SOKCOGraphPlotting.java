package br.inf.nemo.padtec.sokco;

import java.util.HashMap;

import br.inf.nemo.padtec.graphplotting.GraphPlotting;

public class SOKCOGraphPlotting extends GraphPlotting{

	public SOKCOGraphPlotting() {
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
		elements.put("Trail_Termination_Function", "TTF");
		elements.put("Adaptation_Function", "AF");
		elements.put("Matrix", "M");
		elements.put("Subnetwork", "SN");
		elements.put("Physical_Media", "PM");
		elements.put("Input", "Input");
		elements.put("Output", "Output");
		elements.put("Reference_Point", "RP");
		elements.put("Transport_Entity", "TE");
		elements.put("Layer_Network", "Layer");
		elements.put("Binding", "Binding");
		elements.put("Information_Transfer", "InfTransfer");
		elements.put("Adaptation_Sink_Process", "Process");
		elements.put("Adaptation_Source_Process", "Process");
		elements.put("Trail_Termination_Sink_Process", "Process");
		elements.put("Trail_Termination_Source_Process", "Process");
	}

	@Override
	public String getSubtitle() {
		return "Subtitle_TNOKCO.png";
	}
}