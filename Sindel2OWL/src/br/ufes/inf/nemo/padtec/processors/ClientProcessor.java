package br.ufes.inf.nemo.padtec.processors;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Statement;

public class ClientProcessor {
	public static void processClient(OntModel model, String ClassNS, String IndNS, String clients){
		Statement stmt;
		ObjectProperty rel;
		Individual a,b;

		String[] lin = clients.split(";");
		for (String s : lin) {
			String[] vars = s.split(":");
			
			a = model.getIndividual(IndNS+vars[0]);
			b = model.getIndividual(IndNS+vars[1]);

			rel = model.getObjectProperty(ClassNS+"client-server_layer_relationship");
			stmt = model.createStatement(a, rel, b);
			model.add(stmt);
		}
	}
}
