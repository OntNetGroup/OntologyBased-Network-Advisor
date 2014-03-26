package br.ufes.inf.nemo.padtec.processors;

import br.ufes.inf.nemo.padtec.Sindel2OWL;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Statement;

public class ConnectsProcessor {
	private static Statement stmt;
	private static ObjectProperty rel;
	private static Individual a,b,x,k;
	private static OntClass o_class;
	
	public static void processCompositions(OntModel model, String ClassNS, String IndNS, String connects){
		o_class = model.getOntClass(ClassNS+"Information_Transfer");
		String[] lin = connects.split(";");
		for (String s : lin) {
			String[] bind = s.split(":");
			String[] vars = bind[1].split(",");

			a = model.getIndividual(IndNS+vars[0]);
			b = model.getIndividual(IndNS+vars[1]);
			if(vars.length == 2){
				//SimpleRelation
				processSimpleRelation(model, ClassNS);
			}else{
				//Assignable Relation
				x = model.getIndividual(IndNS+vars[2]);
				processAssignableRelation(model, ClassNS, IndNS);
			}
		}
	}
	
	//Simple
	private static void processSimpleRelation(OntModel model, String ClassNS){
		rel = model.getObjectProperty(ClassNS+"has_information_transfer");
		stmt = model.createStatement(a, rel, b);
		model.add(stmt);
	}
	
	//Assignable
	private static void processAssignableRelation(OntModel model, String ClassNS, String IndNS){
		String indName = "_ind_"+Sindel2OWL.getCont();
		k = o_class.createIndividual(IndNS+indName);

		Sindel2OWL.warning += "\nNew individual "+indName+" created of type Information_Transfer";
		Sindel2OWL.hashIndividuals.put(indName, "Information_Transfer");

		rel = model.getObjectProperty(ClassNS+"is_connecting");
		stmt = model.createStatement(k, rel, a);
		model.add(stmt);

		stmt = model.createStatement(k, rel, b);
		model.add(stmt);

		rel = model.getObjectProperty(ClassNS+"IT_is_represented_by");
		stmt = model.createStatement(k, rel, x);
		model.add(stmt);
	}
}
