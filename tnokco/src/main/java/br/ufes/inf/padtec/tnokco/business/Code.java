package br.ufes.inf.padtec.tnokco.business;

public class Code {
	
	public String version;
	public String name;
	public String codevalue;
	public int id;
	
	private int count = 0;
	
	public Code()
	{
		this.count++;
		id = count;
	}

}
