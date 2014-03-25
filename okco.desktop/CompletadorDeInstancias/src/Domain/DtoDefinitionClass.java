package Domain;

import java.util.ArrayList;

public class DtoDefinitionClass {
	
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
	public String Target;
	public String Cardinality;				// just in cases we need cardinality (max, min, exactly)
	public static final String sKey = "#&&#";	// separator key
	
	public DtoDefinitionClass()
	{
		this.Source = "";
		this.Relation = "";
		this.Target = "";
		this.Cardinality = "";
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

	public static DtoDefinitionClass get(ArrayList<DtoDefinitionClass> list, String dtoString)
	{
		String[] DtoArray = dtoString.split(sKey);
		
		for (DtoDefinitionClass aux : list) {	
			if(aux.Cardinality == null)
			{
				aux.Cardinality = "null";
			}
			if(DtoArray[0].equals(aux.Source) && DtoArray[1].equals(aux.Relation) && DtoArray[2].equals(aux.Target) && DtoArray[3].equals(aux.Cardinality)){
				return aux;
			}
		}
		return null;
	}

	public boolean sameAs(DtoDefinitionClass d) {
		
		if(this.Source == d.Source && this.Relation == d.Relation && this.Target == d.Target && this.Cardinality.equals(d.Cardinality))
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
		System.out.println(this.Source + " -> " + this.Relation + " - > " + this.Target + "(" + this.Cardinality + ")");
	}
	
	
	
	
}
