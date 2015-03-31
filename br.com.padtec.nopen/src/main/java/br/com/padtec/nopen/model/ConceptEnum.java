package br.com.padtec.nopen.model;

public enum ConceptEnum {
	
	TRANSPORT_FUNCTION("Transport_Function"), 
	LAYER("Layer"),
	TECHNOLOGY("Technology");
	
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
