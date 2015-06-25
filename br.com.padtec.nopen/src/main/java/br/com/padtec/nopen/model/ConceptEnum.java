package br.com.padtec.nopen.model;

public enum ConceptEnum {

	Technology("Technology"),
	Transport_Function("Transport_Function"),
	Output_Card("Output_Card"),
	Layer_Type("Layer_Type"),
	Adaptation_Function_Output("Adaptation_Function_Output"),
	Card("Card"),
	Equipment("Equipment"),
	Equipment_Holder("Equipment_Holder"),
	Adaptation_Function_Input("Adaptation_Function_Input"),
	AP("AP"),
	PM_Input_So("PM_Input_So"),
	Rack("Rack"),
	TF_Card_Element("TF_Card_Element"),
	Input("Input"),
	Trail_Termination_Function_Input("Trail_Termination_Function_Input"),
	Output("Output"),
	Matrix_Output("Matrix_Output"),
	Matrix_Input("Matrix_Input"),
	Shelf("Shelf"),
	Service("Service"),
	Subslot("Subslot"),
	Matrix("Matrix"),
	FEP("FEP"),
	Reference_Point("Reference_Point"),
	Slot("Slot"),
	FP("FP"),
	Trail_Termination_Function_Output("Trail_Termination_Function_Output"),
	PM_Input_Sk("PM_Input_Sk"),
	Card_Layer("Card_Layer"),
	Input_Card("Input_Card"),
	Trail_Termination_Function("Trail_Termination_Function"),
	Simple_Transport_Function("Simple_Transport_Function"),
	Physical_Media("Physical_Media"),
	Supervisor("Supervisor"),
	Adaptation_Function("Adaptation_Function");

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