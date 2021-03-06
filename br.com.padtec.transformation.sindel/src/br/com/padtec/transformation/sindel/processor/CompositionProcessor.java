package br.com.padtec.transformation.sindel.processor;

import br.com.padtec.trasnformation.sindel.Sindel2OWL;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Statement;

public class CompositionProcessor {
	public static void processCompositions(OntModel model, String ClassNS, String IndNS, String compositions){
		String[] lin = compositions.split(";");
		ObjectProperty componentOf = model.getObjectProperty(ClassNS+"componentOf");
		ObjectProperty invComponentOf = model.getObjectProperty(ClassNS+"INV.componentOf");
		ObjectProperty has_equipment  = model.getObjectProperty(ClassNS+"has_equipment");
		ObjectProperty invHas_equipment  = model.getObjectProperty(ClassNS+"INV.has_equipment");
		for (String s : lin) {
			String[] comp = s.split(":");
			String[] vars = comp[1].split(",");
			Individual src = model.getIndividual(IndNS+comp[0]);
			for (String var : vars) {
				Individual dst = model.getIndividual(IndNS+var);
				
				//CASE 3
				if((Sindel2OWL.hashIndividuals.get(comp[0]).equals("site") || Sindel2OWL.hashIndividuals.get(var).equals("equip"))){
					Statement stmt = model.createStatement(src, has_equipment, dst);
					model.add(stmt);

					stmt = model.createStatement(dst, invHas_equipment, src);
					model.add(stmt);	
					continue;
				}
				
				//CASE 2
				if((Sindel2OWL.hashSindelxG800.containsKey(Sindel2OWL.hashIndividuals.get(comp[0]))) && (Sindel2OWL.hashIndividuals.get(var).equals("input") || Sindel2OWL.hashIndividuals.get(var).equals("output"))){
					if(Sindel2OWL.hashIndividuals.get(var).equals("input"))
						dst.addOntClass(model.getOntClass(ClassNS+Sindel2OWL.hashSindelxG800.get(Sindel2OWL.hashIndividuals.get(comp[0]))+"_Input"));
					else
						dst.addOntClass(model.getOntClass(ClassNS+Sindel2OWL.hashSindelxG800.get(Sindel2OWL.hashIndividuals.get(comp[0]))+"_Output"));
				}
				
				Statement stmt = model.createStatement(src, componentOf, dst);
				model.add(stmt);

				stmt = model.createStatement(dst, invComponentOf, src);
				model.add(stmt);
			}
		}
	}
}
