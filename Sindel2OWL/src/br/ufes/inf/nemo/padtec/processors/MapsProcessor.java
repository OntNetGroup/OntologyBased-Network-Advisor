package br.ufes.inf.nemo.padtec.processors;

import br.ufes.inf.nemo.padtec.Sindel2OWL;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Statement;

public class MapsProcessor {
	public static void processMaps(OntModel model, String ClassNS, String IndNS, String maps){
		Statement stmt;
		ObjectProperty rel,invRel;
		Individual a,b;

		String[] lin = maps.split(",");
		for (String s : lin) {
			String[] vars = s.split(":");
			
			a = model.getIndividual(IndNS+vars[0]);
			b = model.getIndividual(IndNS+vars[1]);
			
			String toB = Sindel2OWL.hashIndividuals.get(vars[1]);
			
			if(toB.equalsIgnoreCase("Input")){
				rel = model.getObjectProperty(ClassNS+"maps_input");
				invRel = model.getObjectProperty(ClassNS+"INV.maps_input");
			}else{
				rel = model.getObjectProperty(ClassNS+"maps_output");
				invRel = model.getObjectProperty(ClassNS+"INV.maps_output");
			}
			stmt = model.createStatement(a, rel, b);
			model.add(stmt);
			
			stmt = model.createStatement(b, invRel, a);
			model.add(stmt);
		}
	}
}
