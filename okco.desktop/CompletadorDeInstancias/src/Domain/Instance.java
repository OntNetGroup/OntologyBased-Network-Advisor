package Domain;

import java.util.ArrayList;

public class Instance {

	public String URI;	
	public ArrayList<String> ListSameInstances;
	public ArrayList<String> ListDiferentInstances;
	public boolean existInModel;
	
	//Definitions respected by the instance
	
	public ArrayList<DtoDefinitionClass> ListSome;
	public ArrayList<DtoDefinitionClass> ListMin;
	public ArrayList<DtoDefinitionClass> ListMax;
	public ArrayList<DtoDefinitionClass> ListExactly;
	public ArrayList<DtoDefinitionClass> ListComplete;
	
	public Instance(String URI, ArrayList<String> listDifferent, ArrayList<String> listSame, Boolean existInModel)
	{
		this.URI = URI;
		this.ListComplete = new ArrayList<DtoDefinitionClass>();
		this.ListSome = new ArrayList<DtoDefinitionClass>();
		this.ListMin = new ArrayList<DtoDefinitionClass>();
		this.ListMax = new ArrayList<DtoDefinitionClass>();
		this.ListExactly = new ArrayList<DtoDefinitionClass>();
		this.ListDiferentInstances = listDifferent;
		this.ListSameInstances = listSame;
		this.existInModel = existInModel;
	}
	
	public void print()
	{
		System.out.println("\n-------------------------------------");
		System.out.println("- Instance name: " + this.URI);
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
		for (DtoDefinitionClass dto : ListComplete) {
			System.out.println("      - " + dto.Relation + " -> " + dto.Target + " (" + dto.Cardinality + ")");
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

}
