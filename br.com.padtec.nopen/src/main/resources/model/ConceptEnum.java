package br.com.padtec.nopen.model;

public enum ConceptEnum {

	INPUT("Input"),
	RACK("Rack"),
	MANAGER_ELEMENT("Manager_Element"),
	OUTPUT_INTERFACE("Output_Interface"),
	PM_INPUT_SO("PM_Input_So"),
	ADAPTATION_FUNCTION("Adaptation_Function"),
	INPUT_INTERFACE("Input_Interface"),
	EQUIPMENT("Equipment"),
	MATRIX_OUTPUT("Matrix_Output"),
	SLOT("Slot"),
	FEP("FEP"),
	ADAPTATION_OUTPUT("Adaptation_Output"),
	CARD("Card"),
	FP("FP"),
	MATRIX("Matrix"),
	TRAIL_TERMINATION_INPUT("Trail_Termination_Input"),
	REFERENCE_POINT("Reference_Point"),
	AP("AP"),
	TRAIL_TERMINATION_OUTPUT("Trail_Termination_Output"),
	EQUIPMENT_HOLDER("Equipment_Holder"),
	ADAPTATION_INPUT("Adaptation_Input"),
	LAYER("Layer"),
	PHYSICAL_MEDIA("Physical_Media"),
	SUBSLOT("Subslot"),
	OUTPUT("Output"),
	TRAIL_TERMINATION_FUNCTION("Trail_Termination_Function"),
	TECHNOLOGY("Technology"),
	MATRIX_INPUT("Matrix_Input"),
	TRANSPORT_FUNCTION("Transport_Function"),
	PM_INPUT_SK("PM_Input_Sk"),
	SHELF("Shelf"),
	SERVICE("Service"),
	SIMPLE_TRANSPORT_FUNCTION("Simple_Transport_Function");

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