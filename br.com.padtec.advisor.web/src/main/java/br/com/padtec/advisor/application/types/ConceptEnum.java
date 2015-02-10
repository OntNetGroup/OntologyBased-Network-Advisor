package br.com.padtec.advisor.application.types;

public enum ConceptEnum {
	
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
	TRANSPORT_FUNCTION("Transport_Function"),
	REFERENCE_POINT("Reference_Point");
	
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
