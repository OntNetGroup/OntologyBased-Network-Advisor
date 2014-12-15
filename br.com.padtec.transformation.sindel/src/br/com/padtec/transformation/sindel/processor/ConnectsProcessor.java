package br.com.padtec.transformation.sindel.processor;

import br.com.padtec.trasnformation.sindel.Sindel2OWL;

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
	private static String toA,toB;
	
	public static void processConnects(OntModel model, String ClassNS, String IndNS, String connects){
		o_class = model.getOntClass(ClassNS+"Forwarding");
		String[] lin = connects.split(",");
		for (String s : lin) {
			String[] vars = s.split(":");

			if(vars.length == 2){
				//SimpleRelation
				a = model.getIndividual(IndNS+vars[0]);
				b = model.getIndividual(IndNS+vars[1]);
				
				toA = Sindel2OWL.hashIndividuals.get(vars[0]);
				toB = Sindel2OWL.hashIndividuals.get(vars[1]);
				
				processSimpleRelation(model, ClassNS);
			}else{
				//Assignable Relation
				x = model.getIndividual(IndNS+vars[0]);
				a = model.getIndividual(IndNS+vars[1]);
				b = model.getIndividual(IndNS+vars[2]);
				
				toA = Sindel2OWL.hashIndividuals.get(vars[1]);
				toB = Sindel2OWL.hashIndividuals.get(vars[2]);
				
				processAssignableRelation(model, ClassNS, IndNS);
			}
		}
	}
	
	//Simple
	private static void processSimpleRelation(OntModel model, String ClassNS){
		if(toA.equals("site") && toB.equals("site")){
			rel = model.getObjectProperty(ClassNS+"site_connects");
		}else{
			rel = model.getObjectProperty(ClassNS+"has_forwarding");				
		}
		
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
		
		rel = model.getObjectProperty(ClassNS+"has_forwarding");
		stmt = model.createStatement(a, rel, b);
		model.add(stmt);
	}
}
