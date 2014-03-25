package UserInterface;

import java.util.ArrayList;

import Business.FactoryInstances;
import Business.FactoryModel;
import Business.Search;
import Domain.DtoCompleteClass;
import Domain.DtoDefinitionClass;
import Domain.DtoDefinitionClassList;
import Domain.IFactory;
import Domain.IRepository;
import com.hp.hpl.jena.ontology.OntModel;

public class ConsoleApp1 {

	static IFactory Factory;
	static IRepository Repository;
	
	public static void main(String[] args) {

		Factory = new FactoryModel();
		Repository = Factory.GetRepository();
		String NS = "http://www.semanticweb.org/parentesco-simples.owl#";	
		String inputFileName = "C:/Users/Coras/Desktop/bateria1.owl";//"Input/bateria1.owl";
		
		OntModel model = Repository.Open(inputFileName);

		Search search = new Search(NS);
		search.QueryExample(model);		
		
		
		//ArrayList<DtoDefinitionClass> dtoMinRelationsList = search.GetSomeRelations(model);
		//for (DtoDefinitionClass dto : dtoMinRelationsList) {
		//	dto.print();
		//}
		
		//FactoryIndividuals factoryIndividuals = new FactoryIndividuals(NS);
		
		//search.GetIndividualRelations(model, NS + "f1");//(model);
		
		//ArrayList<DtoCompleteClass> dtoCompleteClasses = search.GetCompleteClasses(model);
		
		//ArrayList<String> disjointClassOfClassD = search.GetDisjointClassesOf(NS + "D", model);
		
		//ArrayList<DtoTripleResult> dtoSomeRelationsList = search.GetTripleSomeRelations(model);		
		//model = factoryIndividuals.CreateIndividualsForSomeRelations(dtoSomeRelationsList, model);
		
		//ArrayList<DtoTripleResultList> dtoSomeUnionRelationtList = search.GetTrilpleSomeRelationsWithUnionOf(model);
		
		//ArrayList<DtoTripleResult> dtoMinRelationsList = search.GetTripleMinRelations(model);
		//model = factoryIndividuals.CreateIndividualsForMinRestriction(dtoMinRelationsList, model);
		
		//ArrayList<DtoTripleResult> dtoExactlyRelationsList = search.GetTripleExactlyRelations(model);
		//model = factoryIndividuals.CreateIndividualsForExactlyRestriction(dtoExactlyRelationsList, model);
		
		
		
		//System.out.println("---");
		
		/*for (DtoTripleResult aux : dtoSomeRelationsList) {
			System.out.println(aux.Source + " -> " + aux.Relation + " -> " + aux.Cardinality + " " + aux.Target );
		}*/
		
		//Repository.SaveWithDialog(model);
	}
}
