package br.com.padtec.common.dto.simple;

import java.util.ArrayList;
import java.util.List;

public class SimpleDtoInstance {
	
	public String Namespace;
	public String Name;
	public List<String> ListClassesBelong;
	public List<String> ListSameInstances;
	public List<String> ListDiferentInstances;
	public List<SimpleDtoRelation> ListImcompletenessRelationDefinitions;
	public List<SimpleDtoClass> ListImcompletenessClassDefinitions;
	
	public SimpleDtoInstance(String ns, String name, List<String> listClasses, List<String> listDifferent, List<String> listSame, List<SimpleDtoRelation> ListImcompletenessRelationDefinitions, List<SimpleDtoClass> ListImcompletenessClassDefinitions)
	{
		
		this.Namespace = ns;
		this.Name = name;
		this.ListClassesBelong = listClasses;
		
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
		
		if (ListImcompletenessRelationDefinitions == null)
		{
			this.ListImcompletenessRelationDefinitions = new ArrayList<SimpleDtoRelation>();
		}else{
			this.ListImcompletenessRelationDefinitions = new ArrayList<SimpleDtoRelation>(ListImcompletenessRelationDefinitions);
		}
		
		if (ListImcompletenessClassDefinitions == null)
		{
			this.ListImcompletenessClassDefinitions = new ArrayList<SimpleDtoClass>();
		}else{
			this.ListImcompletenessClassDefinitions = new ArrayList<SimpleDtoClass>(ListImcompletenessClassDefinitions);
		}
	}
	
	public void AddRelationDefinition(SimpleDtoRelation instanceRelationDefinition)
	{
		this.ListImcompletenessRelationDefinitions.add(instanceRelationDefinition);
	}
	
	public void AddClassDefinition(SimpleDtoClass instanceClassDefinition)
	{
		this.ListImcompletenessClassDefinitions.add(instanceClassDefinition);
	}
}
