package br.ufes.inf.padtec.tnokco.business;

public class InterfaceOutput extends Interface {

	public boolean isConnected() {
		return connected;
	}
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	private boolean connected=false ;
	private String name;

	public boolean getId() {
		return connected;
	}
	public void setId(boolean value) {
		this.connected = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


}
