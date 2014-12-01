package br.com.padtec.common.dto;

import java.util.ArrayList;

import br.com.padtec.common.types.OntPropertyEnum;

public class DtoPropertyAndSubProperties {
	
	public String Property;
	public ArrayList<String> SubProperties;
	public String iTargetNs;
	public String iTargetName;
	public OntPropertyEnum propertyType;
	
	public DtoPropertyAndSubProperties()
	{
		this.SubProperties = new ArrayList<String>();
	}
}
