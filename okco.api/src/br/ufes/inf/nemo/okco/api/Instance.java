package br.ufes.inf.nemo.okco.api;

import java.util.ArrayList;
import java.util.List;

public class Instance {
	
	public String Namespace;
	public String Name;
	public List<String> ListClassesBelong;
	public List<String> ListSameInstances;
	public List<String> ListDiferentInstances;
	public List<InstanceRelationDefinition> ListImcompletenessRelationDefinitions;
	public List<InstanceClassDefinition> ListImcompletenessClassDefinitions;
	
	public Instance(String ns, String name, List<String> listClasses, List<String> listDifferent, List<String> listSame, List<InstanceRelationDefinition> ListImcompletenessRelationDefinitions, List<InstanceClassDefinition> ListImcompletenessClassDefinitions)
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
			this.ListImcompletenessRelationDefinitions = new ArrayList<InstanceRelationDefinition>();
		}else{
			this.ListImcompletenessRelationDefinitions = new ArrayList<InstanceRelationDefinition>(ListImcompletenessRelationDefinitions);
		}
		
		if (ListImcompletenessClassDefinitions == null)
		{
			this.ListImcompletenessClassDefinitions = new ArrayList<InstanceClassDefinition>();
		}else{
			this.ListImcompletenessClassDefinitions = new ArrayList<InstanceClassDefinition>(ListImcompletenessClassDefinitions);
		}
	}
	
	public void AddRelationDefinition(InstanceRelationDefinition instanceRelationDefinition)
	{
		this.ListImcompletenessRelationDefinitions.add(instanceRelationDefinition);
	}
	
	public void AddClassDefinition(InstanceClassDefinition instanceClassDefinition)
	{
		this.ListImcompletenessClassDefinitions.add(instanceClassDefinition);
	}
}
