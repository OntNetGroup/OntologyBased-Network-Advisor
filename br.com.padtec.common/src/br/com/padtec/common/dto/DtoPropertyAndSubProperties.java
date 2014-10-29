package br.com.padtec.common.dto;

import java.util.ArrayList;

import br.com.padtec.common.queries.OntPropertyEnum;

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

	public static DtoPropertyAndSubProperties getInstance(ArrayList<DtoPropertyAndSubProperties> listSpecializationProperties,	String uriProperty) {
		
		for (DtoPropertyAndSubProperties dto : listSpecializationProperties) {
			if(dto.Property.equals(uriProperty))
			{
				return dto;
			}
		}
		
		return null;
	}

}
