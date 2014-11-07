package virtuoso;

import instances.IndividualInstance;

import java.util.ArrayList;

public class Main {

	public static void main(String[] args) throws Exception  {
		
		SparqlQueries sparqlQueries = new SparqlQueries("http://www.semanticweb.org/ontologies/2014/5/ontology.owl/");
		
		ArrayList<IndividualInstance> instances = sparqlQueries.getAllIndividualInstances(false, false, false);
		
		System.out.println(instances);
	}

}