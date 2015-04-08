package br.com.padtec.nopen.model;

public class DtoIElement {

	private String numberID = new String();
	private String name = new String();
	private ConceptEnum type;
	
	public DtoIElement (String numberID, String name, ConceptEnum type)
	{
		this.numberID = numberID;
		this.name = name;
		this.type = type;
	}
	
	public String getTypeName() { return type.toString(); }	
	public ConceptEnum getType() { return type; }
	
	public String getName() { return name; }	
	public String getID() { return numberID; }	
}
