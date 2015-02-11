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
	TERMINATION_SINK_INPUT("Termination_Sink_Input"),
	ADAPTATION_SOURCE_INPUT("Adaptation_Source_Input"),
	SOURCE_A_FEP("Source_A-FEP"),
	SOURCE_A_FEP_BINDING("Source_A-FEP_Binding"),
	MATRIX_INPUT("Matrix_Input"),
	SOURCE_M_FEP("Source_M-FEP"),
	SOURCE_M_FEP_BINDING("Source-M-FEP_Binding"),
	SUBNETWORK_INPUT("Subnetwork_Input"),
	SOURCE_SN_FEP("Source_SN-FEP"),
	SOURCE_SN_FEP_BINDING("Source-SN-FEP_Binding"),
	LAYER_PROCESSOR_SOURCE_INPUT("Layer_Processor_Source_Input"),
	SOURCE_LPF_FEP("Source_LPF-FEP"),
	SOURCE_LPF_FEP_BINDING("Source_LPF-FEP Binding"),
	ADAPTATION_SINK_OUTPUT("Adaptation_Sink_Output"),
	SINK_A_FEP("Sink_A-FEP"),
	SINK_A_FEP_BINDING("Sink_A-FEP_Binding"),	
	TERMINATION_SINK_OUTPUT("Termination_Sink_Output"),
	SINK_M_FEP("Sink_M-FEP"),
	SINK_M_FEP_BINDING("Sink_M-FEP_Binding"),
	SINK_SN_FEP("Sink_SN-FEP"),	
	SINK_SN_FEP_BINDING("Sink_SN-FEP_Binding"),
	LAYER_PROCESSOR_SINK_INPUT("Layer_Processor_Sink_Input"),
	SINK_LPF_FEP("Sink_LPF-FEP"),	
	SINK_LPF_FEP_BINDING("Sink_LPF-FEP_Binding"),
	SOURCE_PM_FEP_BINDING("Source_PM-FEP_Binding"),
	SINK_PM_FEP_BINDING("Sink_PM-FEP_Binding"),
	ADAPTATION_SOURCE_OUTPUT("Adaptation_Source_Output"),
	SOURCE_AP("Source_AP"),
	SOURCE_AP_BINDING("Source_AP_Binding"),	
	SINK_AP("Sink_AP"),
	SINK_AP_BINDING("Sink_AP_Binding"),	
	LAYER_PROCESSOR_SOURCE_OUTPUT("Layer_Processor_Source_Output"),
	SOURCE_LP_FP("Source_LP-FP"),
	SOURCE_L_FP_BINDING("Source_L-FP_Binding"),	
	LAYER_PROCESSOR_SINK_OUTPUT("Layer_Processor_Sink_Output"),
	SINK_LP_FP("Sink_LP-FP"),
	SINK_L_FP_BINDING("Sink_L-FP_Binding"),		
	UNIDIRECTIONAL_A_FP("Unidirectional_A-FP"),
	A_FP_BINDING("A-FP_Binding"),	
	SUBNETWORK_OUTPUT("Subnetwork_Output"),
	SOURCE_SN_FP("Source_SN-FP"),
	SOURCE_SN_FP_BINDING("Source_SN-FP_Binding"),
	ADAPTATION_SINK_INPUT("Adaptation_Sink_Input"),
	SINK_SN_FP_BINDING("Sink_SN-FP_Binding"),
	MATRIX_OUTPUT("Matrix_Output"),
	SOURCE_M_FP_BINDING("Source_M-FP_Binding"),
	SOURCE_M_FP("Source_M-FP"),
	TERMINATION_SOURCE_INPUT("Termination_Source_Input"),
	SINK_M_FP_BINDING("Sink_M-FP_Binding"),
	TERMINATION_FUNCTION("Termination_Function"),
	ADAPTATION_FUNCTION("Adaptation_Function"),	
	SUBNETWORK("Subnetwork"),
	FORWARDING_END_POINT("Forwarding_End_Point"),
	FORWARDING_POINT("Forwarding_Point"),
	TRANSPORT_ENTITY("Transport_Entity"),
	LAYER_NETWORK("Layer_Network"),
	FORWARDING("Forwarding"),
	ADAPTATION_SINK_PROCESS("Adaptation_Sink_Process"),
	ADAPTATION_SOURCE_PROCESS("Adaptation_Source_Process"),
	TERMINATION_SINK_PROCESS("Termination_Sink_Process"),
	LAYER_PROCESSOR_PROCESS("Layer_Processor_Process"),
	TERMINATION_SOURCE_PROCESS("Termination_Source_Process");
	
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
