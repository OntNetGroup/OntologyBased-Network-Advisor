package br.com.padtec.advisor.application;

import java.util.HashMap;
import java.util.List;

import br.com.padtec.advisor.application.types.ConceptEnum;

public class HTMLFigureMapper {

	private static HashMap<String,String> elements = new HashMap<String,String>();
	
	public static void init()
	{	
		elements.clear();
		
		elements.put(ConceptEnum.TRANSPORT_FUNCTION.toString(), "TF");
		elements.put(ConceptEnum.ADAPTATION_FUNCTION.toString(), "AF");
		elements.put(ConceptEnum.MATRIX.toString(), "Matrix");
		elements.put(ConceptEnum.SUBNETWORK.toString(), "SN");
		elements.put(ConceptEnum.PHYSICAL_MEDIA.toString(), "PM");
		elements.put(ConceptEnum.INPUT.toString(), "Input");
		elements.put(ConceptEnum.OUTPUT.toString(), "Output");
		elements.put(ConceptEnum.REFERENCE_POINT.toString(), "RP");
		elements.put(ConceptEnum.FORWARDING_END_POINT.toString(), "RP");
		elements.put(ConceptEnum.FORWARDING_POINT.toString(), "RP");
		elements.put(ConceptEnum.TRANSPORT_ENTITY.toString(), "TE");
		elements.put(ConceptEnum.LAYER_NETWORK.toString(), "Layer");
		elements.put(ConceptEnum.BINDING.toString(), "Binding");
		elements.put(ConceptEnum.INPUT_INTERFACE.toString(), "INT_IN");
		elements.put(ConceptEnum.OUTPUT_INTERFACE.toString(), "INT_OUT");
		elements.put(ConceptEnum.FORWARDING.toString(), "FORWARDING");
		elements.put(ConceptEnum.ADAPTATION_SINK_PROCESS.toString(), "Process");
		elements.put(ConceptEnum.ADAPTATION_SOURCE_PROCESS.toString(), "Process");
		elements.put(ConceptEnum.TERMINATION_SINK_PROCESS.toString(), "Process");
		elements.put(ConceptEnum.LAYER_PROCESSOR_PROCESS.toString(), "Process");
		elements.put(ConceptEnum.TERMINATION_SOURCE_PROCESS.toString(), "Process");
		elements.put(ConceptEnum.EQUIPMENT.toString(), "Equip");
		elements.put(ConceptEnum.SITE.toString(), "SITE");
	}

	public static String getG800Image(List<String> elemTypes)
	{
		for(String type: elemTypes)
		{
			if(elements.containsKey(type.substring(type.indexOf("#")+1)))
			{
				return elements.get(type.substring(type.indexOf("#")+1));
			}
		}
		return "dot";
	}

}
