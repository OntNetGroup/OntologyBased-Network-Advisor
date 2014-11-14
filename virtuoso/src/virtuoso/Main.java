package virtuoso;

import instances.IndividualInstance;

import java.util.ArrayList;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

public class Main {

	public static void main(String[] args) throws Exception  {
		String ontologyUri = "http://www.semanticweb.org/ontologies/2014/5/ontology.owl";
		
		KAO kao = new KAO(ontologyUri);
		
		IndividualInstance eq = new IndividualInstance(ontologyUri+"#eq");
		eq.addListClasses(ontologyUri+"#Equipment");
		
		IndividualInstance eq2 = new IndividualInstance(ontologyUri+"#eq2");
		eq2.addListClasses(ontologyUri+"#Equipment");
		
		IndividualInstance in = new IndividualInstance(ontologyUri+"#input");
		in.addListClasses(ontologyUri+"#Input");
		
		IndividualInstance inpint = new IndividualInstance(ontologyUri+"#inpint");
		inpint.addListClasses(ontologyUri+"#Input_Interface");
		
		IndividualInstance out = new IndividualInstance(ontologyUri+"#out");
		out.addListClasses(ontologyUri+"#Output");
		
		IndividualInstance outint = new IndividualInstance(ontologyUri+"#outint");
		outint.addListClasses(ontologyUri+"#Output_Interface");
		//outint.addListClasses(ontologyUri+"#Mapped_Output_Interface");
		
		eq.addObjectPropertiesAsSource(kao, ontologyUri+"#componentOf", outint, true);
		eq2.addObjectPropertiesAsSource(kao, ontologyUri+"#componentOf", inpint, true);
		inpint.addObjectPropertiesAsSource(kao, ontologyUri+"#maps_input", in, true);
		out.addObjectPropertiesAsSource(kao, ontologyUri+"#binds", in, true);
		outint.addObjectPropertiesAsSource(kao, ontologyUri+"#maps_output", out, true);
		outint.addObjectPropertiesAsSource(kao, ontologyUri+"#INV.componentOf", eq, true);
		
		kao.insertInstance(eq);
		kao.insertInstance(eq2);
		kao.insertInstance(in);
		kao.insertInstance(inpint);
		kao.insertInstance(out);
		kao.insertInstance(outint);
		
		ArrayList<IndividualInstance> instances = kao.getAllIndividualInstances(true, true, true);
		
		System.out.println(instances);
	}
	
	public static void deleteRepository(String ontologyUri){
		/*			STEP 1			*/
		VirtGraph set = new VirtGraph (ConnectionFactory.getVirtuosourl(), "dba", "dba");

		/*			STEP 2			*/
		//System.out.println("\nexecute: CLEAR GRAPH <http://test1>");
        String str = "CLEAR GRAPH <" + ontologyUri + ">";
        VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(str, set);
        vur.exec();     
	}
	
	
}