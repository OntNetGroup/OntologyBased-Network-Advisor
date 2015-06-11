package br.com.padtec.nopen.model;

public enum ConceptEnum {

	Service("Service"),
	Physical_Media("Physical_Media"),
	Matrix_Input("Matrix_Input"),
	PM_Input_Sk("PM_Input_Sk"),
	Shelf("Shelf"),
	Trail_Termination_Function_Output("Trail_Termination_Function_Output"),
	Input_Card("Input_Card"),
	Output("Output"),
	Matrix_Output("Matrix_Output"),
	Matrix("Matrix"),
	Card("Card"),
	Equipment("Equipment"),
	Adaptation_Function_Output("Adaptation_Function_Output"),
	TF_Card_Element("TF_Card_Element"),
	PM_Input_So("PM_Input_So"),
	Supervisor("Supervisor"),
	Technology("Technology"),
	AP("AP"),
	Output_Card("Output_Card"),
	Equipment_Holder("Equipment_Holder"),
	Trail_Termination_Function_Input("Trail_Termination_Function_Input"),
	Transport_Function("Transport_Function"),
	Adaptation_Function("Adaptation_Function"),
	Subslot("Subslot"),
	Slot("Slot"),
	FP("FP"),
	Adaptation_Function_Input("Adaptation_Function_Input"),
	Input("Input"),
	Trail_Termination_Function("Trail_Termination_Function"),
	Card_Layer("Card_Layer"),
	Simple_Transport_Function("Simple_Transport_Function"),
	FEP("FEP"),
	Reference_Point("Reference_Point"),
	Rack("Rack"),
	Layer_Type("Layer_Type");

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