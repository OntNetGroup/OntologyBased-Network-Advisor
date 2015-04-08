package br.com.padtec.nopen.model;

public class DtoJointElement {

	private String id = null;
	private String name = null;
	private String type = null;
	
	//Introducing the dummy constructor
    public DtoJointElement() {
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
