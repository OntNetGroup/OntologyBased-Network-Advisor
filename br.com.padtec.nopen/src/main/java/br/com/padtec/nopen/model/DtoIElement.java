package br.com.padtec.nopen.model;

public class DtoIElement {

	private String id = null;
	private String name = null;
	private String type = null;
	
	public DtoIElement (String numberID, String name, String type)
	{
		this.id = numberID;
		this.name = name;
		this.type = type;
	}
	
	//Introducing the dummy constructor
    public DtoIElement() {
    }
	
	public void setId(String numberID) { this.id = numberID;}
	public void setName(String name) { this.name = name;}
	public void setType(String type) { this.type = type; }
	
	public String getType() { return type; }
	public String getName() { return name; }	
	public String getId() { return id; }	
	
	@Override
	public String toString() {
		return "{ID: "+id+";\nname: "+name+";\ntype: "+type+";}";
	}
}
