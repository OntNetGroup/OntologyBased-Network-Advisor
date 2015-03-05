package br.com.padtec.advisor.core.dto;

public class DtoInterfaceInput{

	private int id ;
	private String name;

	public DtoInterfaceInput(String name)
	{
		this.name= name;
	}
	
	public int getId() 
	{
		return id;
	}
	
	public void setId(int id) 
	{
		this.id = id;
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
