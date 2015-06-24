package br.com.padtec.nopen.model;

public enum ConceptEnum {

	Output("Output"),
	Card("Card"),	
	Layer_Type("Layer_Type"),
	Transport_Function("Transport_Function"),
	Shelf("Shelf"),	
	Reference_Point("Reference_Point"),
	Input_Card("Input_Card"),
	Adaptation_Function_Output("Adaptation_Function_Output"),	
	Slot("Slot"),
	Trail_Termination_Function_Input("Trail_Termination_Function_Input"),
	FP("FP"),
	Equipment_Holder("Equipment_Holder"),	
	Simple_Transport_Function("Simple_Transport_Function"),
	Rack("Rack"),
	TF_Card_Element("TF_Card_Element"),
	PM_Input_Sk("PM_Input_Sk"),
	Matrix_Output("Matrix_Output"),
	AP("AP"),
	Input("Input"),
	Adaptation_Function("Adaptation_Function"),
	Matrix_Input("Matrix_Input"),
	Technology("Technology"),
	Equipment("Equipment"),	
	Adaptation_Function_Input("Adaptation_Function_Input"),
	Subslot("Subslot"),
	Supervisor("Supervisor"),
	Matrix("Matrix"),	
	Trail_Termination_Function_Output("Trail_Termination_Function_Output"),
	Service("Service"),
	FEP("FEP"),
	PM_Input_So("PM_Input_So"),	
	Output_Card("Output_Card"),
	Card_Layer("Card_Layer"),
	Trail_Termination_Function("Trail_Termination_Function"),
	Physical_Media("Physical_Media");

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