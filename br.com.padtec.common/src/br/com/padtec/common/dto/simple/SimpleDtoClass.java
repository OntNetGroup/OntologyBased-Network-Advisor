package br.com.padtec.common.dto.simple;

import java.util.ArrayList;

public class SimpleDtoClass {
	
	public String TopClass;
	public ArrayList<String> SubClassesToClassify;
	
	public SimpleDtoClass()
	{
		this.SubClassesToClassify = new ArrayList<String>();
	}

}
