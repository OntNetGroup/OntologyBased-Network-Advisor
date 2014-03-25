package Business;

import java.util.ArrayList;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;

import Domain.DtoDefinitionClass;

public class FactoryInstances {

	public Search Search;
	
	public FactoryInstances(Search s)
	{
		Search = s;
	}

	public OntModel CreateIndividualsForSomeRelations(ArrayList<DtoDefinitionClass> dtoSomeList, OntModel model) {

		//This one is use to capture all DtoDefinitionResult with new instance created
		ArrayList<DtoDefinitionClass> dtoSomeListNew = new ArrayList<DtoDefinitionClass>();
		
		for (DtoDefinitionClass dtoResult : dtoSomeList) {

			// List instances from Class Source
			ArrayList<String> listInstances = this.Search.GetInstancesFromClass(model, dtoResult.Source);
			System.out.println("-> " + dtoResult.Source.toString() + " - n individuos - " + listInstances.size());
			
			if(listInstances.size() > 0)	//Exist instances
			{
				//Check the list of instances
				for (String instance : listInstances) {
					
					System.out.println("\n\n#Checando o individuo: "+ instance +"#");
					
					//Here we check the instance and launch the reasoner
					System.out.println("-- REASONER ");
					boolean existInstanceTarget = this.Search.CheckExistInstanceTarget(model, instance, dtoResult.Relation, dtoResult.Target);
					if(existInstanceTarget)
					{
						//Do nothing
						System.out.println("---- Faz nada ");
						
					} else {
						
						System.out.println("---- Criando instância");
						
						//Add too dtoSomeListNew
						dtoSomeListNew.add(dtoResult);
						
						//Get instance, class, property
						Individual indInstance = model.getIndividual(instance);
						OntClass ClassImage = model.getOntClass(dtoResult.Target);
						Property relation = model.getProperty(dtoResult.Relation);				
						
						//Create individual
						String instanceName = dtoResult.Target + "-" + (this.Search.GetInstancesFromClass(model, dtoResult.Target).size() + 1);
						Individual newInstance = ClassImage.createIndividual(instanceName);
						
						//Add relation
						System.out.println("--------" + indInstance.getURI() + " -> "+ relation + " -> " + newInstance.getURI());
						indInstance.addProperty(relation, newInstance);
						System.out.println("---- Individuo " + newInstance.getURI());
					}
				}
			}
		}
		
		if(dtoSomeListNew.size() > 0)
		{
			//We need to check the list again because we create new individuals for than
			
			System.out.println("\n--------------------- Loop ------------------------\n");
			model = this.CreateIndividualsForSomeRelations(dtoSomeList, model);
		} else {
			
			return model;
		}
		
		return model;
	}
	
	public OntModel CreateIndividualsForSomeRelations_Single(String instanceSource, String newInstanceName, String Relation, String TargetClass, OntModel model)
	{
		System.out.println("---- Criando instância");		
		
		//Get instance, class, property
		Individual indInstance = model.getIndividual(instanceSource);
		OntClass ClassImage = model.getOntClass(TargetClass);
		Property relation = model.getProperty(Relation);				
		
		//Create individual
		
		Individual newInstance = ClassImage.createIndividual(newInstanceName);
		
		//Add relation
		System.out.println("--------" + indInstance.getURI() + " -> "+ relation + " -> " + newInstance.getURI());
		indInstance.addProperty(relation, newInstance);
		System.out.println("---- Individuo " + newInstance.getURI());
		
		return model;
	}
	
	public OntModel CreateIndividualsForMinRestriction(ArrayList<DtoDefinitionClass> dtoMinRelationsList, OntModel model) {

		//This one is use to capture all DtoTripleResult with new instance created
		ArrayList<DtoDefinitionClass> dtoMinListNew = new ArrayList<DtoDefinitionClass>();
		
		for (DtoDefinitionClass dtoTripleResult : dtoMinRelationsList) {
			
			// List instances from Class Source
			ArrayList<String> listInstances = this.Search.GetInstancesFromClass(model, dtoTripleResult.Source);
			System.out.println("-> " + dtoTripleResult.Source.toString() + " - n individuos - " + listInstances.size());
			
			if(listInstances.size() > 0)	//Exist instances
			{
				//Check the list of instances
				for (String instance : listInstances) {
				
					System.out.println("\n\n#Checando o individuo: "+ instance +"#");
					
					//Here we check the instance and launch the reasoner
					System.out.println("-- REASONER ");
					int quantityInstancesTarget = this.Search.CheckExistInstancesTargetCardinality(model, instance, dtoTripleResult.Relation, dtoTripleResult.Target, dtoTripleResult.Cardinality);
					
					System.out.println("Quantidade no target: " + quantityInstancesTarget);
					
					if(quantityInstancesTarget < Integer.parseInt(dtoTripleResult.Cardinality))
					{
						System.out.println("---- Criando instâncias - cardialidade desejada = " + dtoTripleResult.Cardinality);
						
						//Add too dtoSomeListNew
						dtoMinListNew.add(dtoTripleResult);
						
						for (int i = quantityInstancesTarget; i < Integer.parseInt(dtoTripleResult.Cardinality); i++) {
							
							//Get instance, class, property
							Individual indInstance = model.getIndividual(instance);
							OntClass ClassImage = model.getOntClass(dtoTripleResult.Target);
							Property relation = model.getProperty(dtoTripleResult.Relation);				
							
							//Create individual
							String instanceName = dtoTripleResult.Target + "-" + (this.Search.GetInstancesFromClass(model, dtoTripleResult.Target).size() + 1);
							Individual newInstance = ClassImage.createIndividual(instanceName);
							
							//Add relation
							indInstance.addProperty(relation, newInstance);
							System.out.println("---- #Criou Individuo " + newInstance.getURI());
						}

					} else {
						//Do nothing
						System.out.println("---- Faz nada ");
						
					}
				}
			}
		}
		
		return model;
	}	

	public OntModel CreateIndividualsForMinRestriction_Single(ArrayList<DtoDefinitionClass> dtoMinRelationsList, OntModel model)
	{
		return null;
	}
	
	public OntModel CreateIndividualsForExactlyRestriction(ArrayList<DtoDefinitionClass> dtoExactlyRelationsList, OntModel model)
	{
		//This one is use to capture all DtoTripleResult with new instance created
		ArrayList<DtoDefinitionClass> dtoExactlyListNew = new ArrayList<DtoDefinitionClass>();
		
		for (DtoDefinitionClass dtoTripleResult : dtoExactlyRelationsList) {
			
			// List instances from Class Source
			ArrayList<String> listInstances = this.Search.GetInstancesFromClass(model, dtoTripleResult.Source);
			System.out.println("-> " + dtoTripleResult.Source.toString() + " - n individuos - " + listInstances.size());
			
			if(listInstances.size() > 0)	//Exist instances
			{
				//Check the list of instances
				for (String instance : listInstances) {
				
					System.out.println("\n\n#Checando o individuo: "+ instance +"#");
					
					//Here we check the instance and launch the reasoner
					System.out.println("-- REASONER ");
					int quantityInstancesTarget = this.Search.CheckExistInstancesTargetCardinality(model, instance, dtoTripleResult.Relation, dtoTripleResult.Target, dtoTripleResult.Cardinality);
					
					System.out.println("Quantidade no target: " + quantityInstancesTarget);
					
					// Case 1 - same as min relation
					if(quantityInstancesTarget < Integer.parseInt(dtoTripleResult.Cardinality))
					{
						System.out.println("---- Criando instâncias - cardialidade desejada = " + dtoTripleResult.Cardinality);
						
						//Add too dtoSomeListNew
						dtoExactlyListNew.add(dtoTripleResult);
						
						for (int i = quantityInstancesTarget; i < Integer.parseInt(dtoTripleResult.Cardinality); i++) {
							
							//Get instance, class, property
							Individual indInstance = model.getIndividual(instance);
							OntClass ClassImage = model.getOntClass(dtoTripleResult.Target);
							Property relation = model.getProperty(dtoTripleResult.Relation);				
							
							//Create individual
							String instanceName = dtoTripleResult.Target + "-" + (this.Search.GetInstancesFromClass(model, dtoTripleResult.Target).size() + 1);
							Individual newInstance = ClassImage.createIndividual(instanceName);
							
							//Add relation
							indInstance.addProperty(relation, newInstance);
							System.out.println("---- #Criou Individuo " + newInstance.getURI());
						}

					// Case 2 - more individuals than necessary
					} else if (quantityInstancesTarget > Integer.parseInt(dtoTripleResult.Cardinality)) {

						System.out.println("---- Mais individuos do que o necessário. Excluir individuos ");
						
					} else {
						//Do nothing
						System.out.println("---- Faz nada ");
						
					}
				}
			}
		}
		
		return model;
	}

	public OntModel CreateIndividualsForExactlyRestriction_Single(ArrayList<DtoDefinitionClass> dtoExactlyRelationsList, OntModel model)
	{
		return null;
	}
}
