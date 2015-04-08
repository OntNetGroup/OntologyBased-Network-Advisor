package br.com.padtec.nopen.model;

public enum ConceptEnum {
	
	TRANSPORT_FUNCTION("Transport_Function"), 
	LAYER("Layer"),
	TECHNOLOGY("Technology"),
	EQUIPMENT("Equipment"),
	SERVICE("Service"),
	TTF("Trail_Termination_Function"),
	AF("Adaptation_Function");
	SERVICE("Service"),
	EQUIPMENT_HOLDER("Equipment Holder");
	
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
