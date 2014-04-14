package br.ufes.inf.padtec.tnokco.business;

public class InterfaceInput {

	private int id ;
	private String name;

	public InterfaceInput(String name){
		this.name= name;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
