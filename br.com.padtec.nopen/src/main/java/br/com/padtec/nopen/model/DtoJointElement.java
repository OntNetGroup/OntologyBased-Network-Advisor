package br.com.padtec.nopen.model;

public class DtoJointElement {

	private String 	id,
					name,
					type;
	
	//Introducing dummy constructor
    public DtoJointElement() {}
	
	public void setId(String id) { this.id = id; }
	public void setName(String name) { this.name = name; }
	public void setType(String type) { this.type = type; }
	
	public String getType() { return type; }
	public String getName() { return name; }	
	public String getId() { return id; }	
	
	@Override
	public String toString() {
		return "{ ID: " + id + ";\nname: " + name + ";\ntype: " + type + " ;}";
	}
}
