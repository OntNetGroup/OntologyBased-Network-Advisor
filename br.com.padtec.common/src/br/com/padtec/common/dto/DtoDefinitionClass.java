package br.com.padtec.common.dto;

import java.util.ArrayList;

import br.com.padtec.common.types.OntCardinalityEnum;
import br.com.padtec.common.types.OntPropertyEnum;

public class DtoDefinitionClass implements Comparable<DtoDefinitionClass>{
	
	/*
	 * Used in:
	 * 
	 * - some relations
	 * - minimal cardinality relation
	 * - maximal cardinality relation
	 * - exactly cardinality relation
	 * 
	 * */
	
	public String Source;
	public String Relation;
	public String uriRelationEncoded = new String();
	public OntPropertyEnum PropertyType;				//object or data
	public OntCardinalityEnum TypeCompletness;	//some,min,max...
	public String Target;
	public String Cardinality;					// just in cases we need cardinality (max, min, exactly)
	public static final String sKey = "#&&#";	// separator key
	
	public DtoDefinitionClass()
	{
		this.Source = "";
		this.Relation = "";
		this.Target = "";
		this.Cardinality = "";
		this.PropertyType = null;
		this.TypeCompletness = null;
	}
	
	public static ArrayList<DtoDefinitionClass> getDtosWithSource(ArrayList<DtoDefinitionClass> list, String source)
	{
		ArrayList<DtoDefinitionClass> listResult = new ArrayList<DtoDefinitionClass>();
		for (DtoDefinitionClass aux : list) {
			if(source.equals(aux.Source)){
				
				listResult.add(aux);
			}
		}
		return listResult;
	}
	
	@Override
	public String toString() {
		
		return this.Source + sKey + this.Relation + sKey + this.Target + sKey + this.Cardinality;
	
	}	

	public boolean sameAs(DtoDefinitionClass d) {
		
		if(this.Source == d.Source && this.Relation == d.Relation && this.Target == d.Target && this.Cardinality.equals(d.Cardinality) && this.PropertyType.equals(d.PropertyType))
		{
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean existDto(DtoDefinitionClass dto, ArrayList<DtoDefinitionClass> list) {
		
		for (DtoDefinitionClass d : list) {
			if(dto.sameAs(d))
			{
				return true;
			}
		}
		
		return false;
	}
	
 	public void print()
	{
		System.out.println(this.Source + " -> " + this.Relation + " (" + this.PropertyType + ") - > " + this.Target + "(" + this.Cardinality + ")");
	}

	public static DtoDefinitionClass getDtoWithSourceAndRelationAndTarget(ArrayList<DtoDefinitionClass> list, String source) {
		
		for (DtoDefinitionClass aux : list) {
			if(source.equals(aux.Source)){
				
				return aux;
			}
		}
		
		return null;
	}

	@Override
	public int compareTo(DtoDefinitionClass arg0) 
	{
		if(this.Source == arg0.Source && this.Relation == arg0.Relation && this.Target == arg0.Target && this.Cardinality.equals(arg0.Cardinality) && this.PropertyType.equals(arg0.PropertyType))
		{
			return 0;
		} else {
			return this.Relation.compareTo(arg0.Relation);
		}		
	}
	
	@Override
	public boolean equals(Object arg) {
		if(arg instanceof DtoDefinitionClass){
			DtoDefinitionClass arg0 = (DtoDefinitionClass)arg;
			if(this.Source == arg0.Source && this.Relation == arg0.Relation && this.Target == arg0.Target && this.Cardinality.equals(arg0.Cardinality) && this.PropertyType.equals(arg0.PropertyType))
			{
				return true;
			} else {
				return this.Relation.equals(arg0.Relation);
			}
		}
		return false;
	}
	
	
}
