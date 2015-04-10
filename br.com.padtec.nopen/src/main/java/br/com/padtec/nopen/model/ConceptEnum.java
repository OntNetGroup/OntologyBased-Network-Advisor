package br.com.padtec.nopen.model;

public enum ConceptEnum {

	Input("Input"),
	Rack("Rack"),
	Manager_Element("Manager_Element"),
	Output_Interface("Output_Interface"),
	PM_Input_So("PM_Input_So"),
	Adaptation_Function("Adaptation_Function"),
	Input_Interface("Input_Interface"),
	Equipment("Equipment"),
	Matrix_Output("Matrix_Output"),
	Slot("Slot"),
	FEP("FEP"),
	Adaptation_Output("Adaptation_Output"),
	Card("Card"),
	FP("FP"),
	Matrix("Matrix"),
	Trail_Termination_Input("Trail_Termination_Input"),
	Reference_Point("Reference_Point"),
	AP("AP"),
	Trail_Termination_Output("Trail_Termination_Output"),
	Equipment_Holder("Equipment_Holder"),
	Adaptation_Input("Adaptation_Input"),
	Layer("Layer"),
	Physical_Media("Physical_Media"),
	Subslot("Subslot"),
	Output("Output"),
	Trail_Termination_Function("Trail_Termination_Function"),
	Technology("Technology"),
	Matrix_Input("Matrix_Input"),
	Transport_Function("Transport_Function"),
	PM_Input_Sk("PM_Input_Sk"),
	Shelf("Shelf"),
	Service("Service"),
	Simple_Transport_Function("Simple_Transport_Function");

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