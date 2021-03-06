package br.com.padtec.nopen.provisioning.model;

public class PElement {

	String 	type,
			id,
			name;

	public PElement() {
		
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString(){
		return id +"("+name+") : "+type;
	}
	
}
