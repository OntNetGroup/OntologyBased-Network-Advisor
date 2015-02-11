package br.com.padtec.advisor.application;

import java.util.HashMap;
import java.util.List;

public class HTMLFigureMapper {

	private static HashMap<String,String> elements = new HashMap<String,String>();
	
	public static void init()
	{	
		elements.clear();
		
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
		//elements.put("Forwarding_Rule", "FWR_RULE");
		elements.put("Equipment", "Equip");
		elements.put("Site", "SITE");
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
