package br.com.padtec.common.features;

import java.util.ArrayList;

import br.com.padtec.common.dto.simple.SimpleDtoInstance;

public class OKCoResultFromFile {
	
	public ArrayList<SimpleDtoInstance> ListInstances;
	public ArrayList<String> ListErrors;
	public String owlFile;
	
	public OKCoResultFromFile()
	{
		this.ListInstances = new ArrayList<SimpleDtoInstance>();
		this.ListErrors = new ArrayList<String>();
	}

}
