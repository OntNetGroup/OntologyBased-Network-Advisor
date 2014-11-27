package br.com.padtec.common.dto;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class DtoInstance implements Serializable {

	private static final long serialVersionUID = 1L;

	public String ns;
	public String name;
	public String uri;
	public String uriEncoded;
	public List<String> ListClasses;
	public List<String> ListSameInstances;
	public List<String> ListDiferentInstances;
	public boolean existInModel;	//
	public boolean isModified;		// Check is modified and not run the reasoner for him
	
	//Definitions respected by the instance
	
	public ArrayList<DtoDefinitionClass> ListSome;
	public ArrayList<DtoDefinitionClass> ListMin;
	public ArrayList<DtoDefinitionClass> ListMax;
	public ArrayList<DtoDefinitionClass> ListExactly;
	public ArrayList<DtoCompleteClass> ListCompleteClasses;
	public ArrayList<DtoPropertyAndSubProperties> ListSpecializationProperties;
	
	public DtoInstance(String ns, String name, List<String> listClasses, List<String> listDifferent, List<String> listSame, Boolean existInModel)
	{
		this.ns = ns;
		this.name = name;
		this.uri = ns+name;
		try {
			this.uriEncoded = URLEncoder.encode(this.uri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
		this.ListCompleteClasses = new ArrayList<DtoCompleteClass>();
		this.ListSpecializationProperties = new ArrayList<DtoPropertyAndSubProperties>();
		this.ListSome = new ArrayList<DtoDefinitionClass>();
		this.ListMin = new ArrayList<DtoDefinitionClass>();
		this.ListMax = new ArrayList<DtoDefinitionClass>();
		this.ListExactly = new ArrayList<DtoDefinitionClass>();
		if (listClasses == null)
		{
			this.ListClasses = new ArrayList<String>();
		}else{
			this.ListClasses = new ArrayList<String>(listClasses);
		}
		if (listSame == null)
		{
			this.ListSameInstances = new ArrayList<String>();
		}else{
			this.ListSameInstances = new ArrayList<String>(listSame);
		}
		
		if (listDifferent == null)
		{
			this.ListDiferentInstances = new ArrayList<String>();
		}else{
			this.ListDiferentInstances = new ArrayList<String>(listDifferent);
		}
		this.existInModel = existInModel;
		this.isModified = false;
	}
	
 	public void print()
	{
		System.out.println("\n-------------------------------------");
		System.out.println("- Instance name: " + this.ns + this.name);
		System.out.println("Identifier: "+uri);
		System.out.println("- ListOfSome: ");
		for (DtoDefinitionClass dto : ListSome) {
			System.out.println("      - " + dto.Relation + " -> " + dto.Target + " (" + dto.Cardinality + ")");
		}
		System.out.println("- ListOfMin: ");
		for (DtoDefinitionClass dto : ListMin) {
			System.out.println("      - " + dto.Relation + " -> " + dto.Target + " (" + dto.Cardinality + ")");
		}
		System.out.println("- ListOfMax: ");
		for (DtoDefinitionClass dto : ListMax) {
			System.out.println("      - " + dto.Relation + " -> " + dto.Target + " (" + dto.Cardinality + ")");
		}
		System.out.println("- ListOfExactly: ");
		for (DtoDefinitionClass dto : ListExactly) {
			System.out.println("      - " + dto.Relation + " -> " + dto.Target + " (" + dto.Cardinality + ")");
		}
		System.out.println("- ListOfComplete: ");
		for (DtoCompleteClass dto : ListCompleteClasses) {
			System.out.println("      		- " + dto.CompleteClass);
			for (String sub : dto.Members) {
				System.out.println("      				- " + sub);				
			}
		}
		System.out.println("- ListSpecializationProperties: ");
		for (DtoPropertyAndSubProperties dtoSpec : this.ListSpecializationProperties) {			
			System.out.println("	- " + dtoSpec.Property + " -> " + dtoSpec.iTargetName + " (" + dtoSpec.propertyType + ")");
			for (String propEsp : dtoSpec.SubProperties) {
				System.out.println("      			- " + propEsp);				
			}
		}
		System.out.println("- ListSame: ");
		for (String s : ListSameInstances) {
			System.out.println("      - " + s);
		}
		System.out.println("- ListDifferent: ");
		for (String s : ListDiferentInstances) {
			System.out.println("      - " + s);
		}
		System.out.println("");
	}
 	
 	public boolean is_Semi_Complete()
 	{ 		
 		boolean haveSpecializationProp = false;
 		for (DtoPropertyAndSubProperties dtoSpec : this.ListSpecializationProperties) {
			if(dtoSpec.SubProperties.size() > 0)
			{
				haveSpecializationProp = true;
				break;
			}				
		}
 		
 		if(haveSpecializationProp == true)
 		{
 			return true;
 			
 		} else 
 		{
 			return false;
 		}
 	}
 	
 	public boolean haveKnwologeToComplete()
 	{		
 		boolean haveComplete = false;
 		for (DtoCompleteClass dtoComplete : this.ListCompleteClasses) {
			if(dtoComplete.Members.size() > 0)
			{
				haveComplete = true;
				break;
			}
				
		}
 		
 		if(this.ListSome.size() > 0 || this.ListMin.size() > 0 || this.ListMax.size() > 0 || this.ListExactly.size() > 0 || haveComplete == true)
 		{
 			return true;
 			
 		} else 
 		{
 			return false;
 		} 		
 	}
 	
 	public void setModified(boolean value)
 	{		
 		this.isModified = value;
 		
 	}

	public static void removeFromList(ArrayList<DtoInstance> listNewInstances,	String uri) {

		for (DtoInstance instance : listNewInstances) {
			
			if(instance.uri.equals(uri))
			{
				listNewInstances.remove(instance);
				break;
			}
		}		
	}
	
	public List<DtoPropertyAndSubProperties> getPropertiesAndSubProperties()
	{
		return ListSpecializationProperties;
	}
	
	public List<DtoCompleteClass> getCompleteClasses()
	{
		return ListCompleteClasses;
	}
	
	public List<DtoDefinitionClass> getSomeDefinitionWithNoRepetition()
	{
		ArrayList<DtoDefinitionClass> result = new ArrayList<DtoDefinitionClass>();
		for (DtoDefinitionClass dto : ListSome) 
		{				
			boolean exist = false;
			for (DtoDefinitionClass dto2 : result) 
			{
				if(dto.sameAs(dto2)){exist = true; break; }
			}			
			if(!exist){ result.add(dto); }			
		}
		return result;
	}
	
	public List<DtoDefinitionClass> getMinDefinitionWithNoRepetition()
	{
		ArrayList<DtoDefinitionClass> result = new ArrayList<DtoDefinitionClass>();
		for (DtoDefinitionClass dto : ListMin) 
		{				
			boolean exist = false;
			for (DtoDefinitionClass dto2 : result) 
			{
				//Doesn't compare the source
				if(dto.Relation == dto2.Relation && dto.Target == dto2.Target && dto.Cardinality.equals(dto2.Cardinality) && dto.PropertyType.equals(dto2.PropertyType))					
				{ exist = true; break; }
			}			
			if(!exist){ result.add(dto); }			
		}
		return result;
	}

	public List<DtoDefinitionClass> getMaxDefinitionWithNoRepetition()
	{
		ArrayList<DtoDefinitionClass> result = new ArrayList<DtoDefinitionClass>();
		for (DtoDefinitionClass dto : ListMax) 
		{				
			boolean exist = false;
			for (DtoDefinitionClass dto2 : result) 
			{
				//Doesn't compare the source
				if(dto.Relation == dto2.Relation && dto.Target == dto2.Target && dto.Cardinality.equals(dto2.Cardinality) && dto.PropertyType.equals(dto2.PropertyType))					
				{ exist = true; break; }
			}			
			if(!exist){ result.add(dto); }			
		}
		return result;
	}	
	
	public List<DtoDefinitionClass> getExactDefinitionWithNoRepetition()
	{
		ArrayList<DtoDefinitionClass> result = new ArrayList<DtoDefinitionClass>();
		for (DtoDefinitionClass dto : ListExactly) 
		{				
			boolean exist = false;
			for (DtoDefinitionClass dto2 : result) 
			{
				//Doesn't compare the source
				if(dto.Relation == dto2.Relation && dto.Target == dto2.Target && dto.Cardinality.equals(dto2.Cardinality) && dto.PropertyType.equals(dto2.PropertyType))					
				{ exist = true; break; }
			}			
			if(!exist){ result.add(dto); }			
		}
		return result;
	}	

}
