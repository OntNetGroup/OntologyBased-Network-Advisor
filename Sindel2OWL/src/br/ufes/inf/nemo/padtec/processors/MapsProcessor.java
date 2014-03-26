package br.ufes.inf.nemo.padtec.processors;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Statement;

public class MapsProcessor {
	public static void processMaps(OntModel model, String ClassNS, String IndNS, String maps){
		Statement stmt;
		ObjectProperty rel;
		Individual a,b;

		String[] lin = maps.split(";");
		for (String s : lin) {
			String[] bind = s.split(":");
			String[] vars = bind[1].split(",");
			
			a = model.getIndividual(IndNS+vars[0]);
			b = model.getIndividual(IndNS+vars[1]);

			rel = model.getObjectProperty(ClassNS+"maps");
			stmt = model.createStatement(a, rel, b);
			model.add(stmt);
		}
	}
}
