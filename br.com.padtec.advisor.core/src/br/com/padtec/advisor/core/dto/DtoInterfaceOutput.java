package br.com.padtec.advisor.core.dto;

public class DtoInterfaceOutput{

	private boolean connected=false ;
	private String name;
	
	public boolean isConnected() 
	{
		return connected;
	}
	
	public void setConnected(boolean connected) 
	{
		this.connected = connected;
	}

	public boolean getId() 
	{
		return connected;
	}
	
	public void setId(boolean value) 
	{
		this.connected = value;
	}
	
	public String getName() 
	{
		return name;
	}
	
	public void setName(String name) 
	{
		this.name = name;
	}
}
