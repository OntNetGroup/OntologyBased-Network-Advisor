package br.com.padtec.nopen.model;

public enum ProvisioningConceptEnum {

	Trail_Termination_Function_Output("Trail_Termination_Function_Output"),
	Input_Card("Input_Card"),
	Layer_Type("Layer_Type"),
	Card("Card"),
	Matrix_Output("Matrix_Output"),
	Transport_Function("Transport_Function"),
	Trail_Termination_Function_Input("Trail_Termination_Function_Input"),
	Matrix("Matrix"),
	Service("Service"),
	Output("Output"),
	Card_Layer("Card_Layer"),
	TF_Card_Element("TF_Card_Element"),
	PM_Input_Sk("PM_Input_Sk"),
	Output_Card("Output_Card"),
	Adaptation_Function_Input("Adaptation_Function_Input"),
	Input("Input"),
	PM_Input_So("PM_Input_So"),
	Adaptation_Function_Output("Adaptation_Function_Output"),
	AP("AP"),
	Reference_Point("Reference_Point"),
	Physical_Media("Physical_Media"),
	Equipment("Equipment"),
	FEP("FEP"),
	FP("FP"),
	Trail_Termination_Function("Trail_Termination_Function"),
	Adaptation_Function("Adaptation_Function"),
	Simple_Transport_Function("Simple_Transport_Function"),
	Technology("Technology"),
	Matrix_Input("Matrix_Input");

	private String concept;

	ProvisioningConceptEnum(String concept)
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
		for(ProvisioningConceptEnum c: ProvisioningConceptEnum.values()){
			System.out.println(c.concept);
		}
	}
}