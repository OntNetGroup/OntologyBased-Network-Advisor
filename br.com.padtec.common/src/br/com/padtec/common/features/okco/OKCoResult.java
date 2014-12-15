package br.com.padtec.common.features.okco;

import java.util.ArrayList;

import br.com.padtec.common.dto.simple.SimpleDtoInstance;

public class OKCoResult {
	
	public ArrayList<SimpleDtoInstance> ListInstances;
	public ArrayList<String> ListErrors;
	
	public OKCoResult()
	{
		this.ListErrors = new ArrayList<String>();
		this.ListInstances = new ArrayList<SimpleDtoInstance>();
	}

}
