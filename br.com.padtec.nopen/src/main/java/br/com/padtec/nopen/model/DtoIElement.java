package br.com.padtec.nopen.model;

public class DtoIElement {

	private String numberID = new String();
	private String name = new String();
	private String type;
	
	public String getNumberID() {
		return numberID;
	}

	public void setNumberID(String numberID) {
		this.numberID = numberID;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public DtoIElement (String numberID, String name, String type)
	{
		this.numberID = numberID;
		this.name = name;
		this.type = type;
	}
	
	public String getTypeName() { return type.toString(); }	
	public String getType() { return type; }
	
	public String getName() { return name; }	
	public String getID() { return numberID; }	
}
