package br.com.padtec.nopen.model;

public enum ConceptEnum {

	Reference_Point("Reference_Point"),
	Trail_Termination_Function("Trail_Termination_Function"),
	PM_Input_Sk("PM_Input_Sk"),
	PM_Input_So("PM_Input_So"),
	Simple_Transport_Function("Simple_Transport_Function"),
	Matrix("Matrix"),
	FEP("FEP"),
	Trail_Termination_Function_Output("Trail_Termination_Function_Output"),
	Slot("Slot"),
	Trail_Termination_Function_Input("Trail_Termination_Function_Input"),
	Card_Layer("Card_Layer"),
	FP("FP"),
	Supervisor("Supervisor"),
	Adaptation_Function_Input("Adaptation_Function_Input"),
	Service("Service"),
	Card("Card"),
	Technology("Technology"),
	Output_Card("Output_Card"),
	Equipment("Equipment"),
	Adaptation_Function_Output("Adaptation_Function_Output"),
	TF_Card_Element("TF_Card_Element"),
	Physical_Media("Physical_Media"),
	AP("AP"),
	Transport_Function("Transport_Function"),
	Shelf("Shelf"),
	Rack("Rack"),
	Adaptation_Function("Adaptation_Function"),
	Matrix_Input("Matrix_Input"),
	Input("Input"),
	Layer_Type("Layer_Type"),
	Output("Output"),
	Matrix_Output("Matrix_Output"),
	Subslot("Subslot"),
	Input_Card("Input_Card"),
	Equipment_Holder("Equipment_Holder");

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