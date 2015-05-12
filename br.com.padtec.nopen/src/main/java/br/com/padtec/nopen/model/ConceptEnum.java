package br.com.padtec.nopen.model;

public enum ConceptEnum {

	Input_Card("Input_Card"),
	Adaptation_Function_Output("Adaptation_Function_Output"),
	Trail_Termination_Function_Output("Trail_Termination_Function_Output"),
	Input("Input"),
	Trail_Termination_Function_Input("Trail_Termination_Function_Input"),
	Rack("Rack"),
	PM_Input_So("PM_Input_So"),
	Adaptation_Function("Adaptation_Function"),
	Equipment("Equipment"),
	Supervisor("Supervisor"),
	Matrix_Output("Matrix_Output"),
	Slot("Slot"),
	FEP("FEP"),
	Adaptation_Function_Input("Adaptation_Function_Input"),
	Card("Card"),
	FP("FP"),
	Matrix("Matrix"),
	Reference_Point("Reference_Point"),
	AP("AP"),
	Equipment_Holder("Equipment_Holder"),
	Layer("Layer"),
	Physical_Media("Physical_Media"),
	Subslot("Subslot"),
	Output("Output"),
	Trail_Termination_Function("Trail_Termination_Function"),
	Technology("Technology"),
	Matrix_Input("Matrix_Input"),
	Transport_Function("Transport_Function"),
	Output_Card("Output_Card"),
	PM_Input_Sk("PM_Input_Sk"),
	Shelf("Shelf"),
	Service("Service"),
	Simple_Transport_Function("Simple_Transport_Function"),
	TF_Card_Element("TF_Card_Element");

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