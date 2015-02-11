package br.com.padtec.advisor.application.types;


public enum ConceptEnum {
	
	// ==============================
	// Concepts from the Ontology
	// ==============================
	
	EQUIPMENT("Equipment"),
	SITE("Site"),
	BINDING("Binding"),
	MATRIX("Matrix"),	
	INPUT("Input"),
	OUTPUT("Output"),	
	INPUT_INTERFACE("Input_Interface"),
	OUTPUT_INTERFACE("Output_Interface"),	
	PHYSICAL_MEDIA("Physical_Media"),
	PHYSICAL_MEDIA_INPUT("Physical_Media_Input"),
	PHYSICAL_MEDIA_OUTPUT("Physical_Media_Output"),
	TRANSPORT_FUNCTION("Transport_Function"),
	REFERENCE_POINT("Reference_Point"),
	SINK_PM_FEP("Sink_PM-FEP"),
	SOURCE_PM_FEP("Source_PM-FEP"),
	SOURCE_PATH_FEP("Source_Path_FEP"),
	PM_NC_FORWARDING("PM_NC_Forwarding"),
	UNIDIRECTIONAL_PM_NC("Unidirectional_PM_NC"),
	PATH_NC_FORWARDING("Path_NC_Forwarding"),
	UNIDIRECTIONAL_PATH_NC("Unidirectional_Path_NC"),
	AP_FORWARDING("AP_Forwarding"),
	UNIDIRECTIONAL_ACCESS_TRANSPORT_ENTITY("Unidirectional_Access_Transport_Entity"),
	DIRECTLY_BOUND_REFERENCE_POINT("Directly_Bound_Reference_Point"),
	TERMINATION_SOURCE_OUTPUT("Termination_Source_Output"),
	TERMINATION_SINK_INPUT("Termination_Sink_Input");
	
	// ==============================
	// New Concepts to be generated... 
	// ==============================
	
	private String concept;
	
	ConceptEnum(String concept)
	{
		this.concept = concept;
	}
	
	@Override
	public String toString() {
		return concept();
	}
	
	public String concept() { return concept; }  
	
	public static void main (String args[])
	{
		for(ConceptEnum c: ConceptEnum.values()){
			System.out.println(c.concept);
		}
	}
}
