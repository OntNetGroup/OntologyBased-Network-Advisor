package br.com.padtec.advisor.application;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.padtec.okco.core.application.OKCoUploader;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Statement;

public class GeneralBinds extends AdvisorService {

	public static PossibleBindsMap possibleBinds = new PossibleBindsMap();
	
	//==============================================
	//These are factory methods because they modify the ontology
	//==============================================
	
	/**
	 * Bind specifics.
	 * 
	 * @param a: Individual
	 * @param b: Individual
	 * @param tipo_out: ?
	 * @param tipo_inp: ?
	 */
	public static void bindsSpecific(Individual a, Individual b, String tipo_out, String tipo_inp) 
	{		
		OntModel baseModel = OKCoUploader.getBaseModel();
		String namespace = OKCoUploader.getNamespace();
		
		HashMap<String, String> key = new HashMap<String, String>();
		key.put("INPUT", tipo_inp);
		key.put("OUTPUT", tipo_out);
		
		try{
			HashMap<String, String> value = possibleBinds.getMap().get(key);
			
			OntClass ClassImage = baseModel.getOntClass(namespace+value.get("RP"));
			Individual rp = baseModel.createIndividual(namespace+a.getLocalName()+"rp"+b.getLocalName(),ClassImage);
			Individual binding= baseModel.createIndividual(namespace+a.getLocalName()+"binding"+b.getLocalName(),baseModel.getResource(namespace+value.get("RP_BINDING")));
			
			ArrayList<Statement> stmts = new ArrayList<Statement>();
			
			stmts.add(baseModel.createStatement(binding, baseModel.getProperty(namespace+value.get("RP_RELATION")), rp));
			stmts.add(baseModel.createStatement(binding, baseModel.getProperty(namespace+value.get("RP_BINDING_REL_IN")), b));
			stmts.add(baseModel.createStatement(binding, baseModel.getProperty(namespace+value.get("RP_BINDING_REL_OUT")), a));
			
			OKCoUploader.getBaseModel().add(stmts);
			
		}catch(Exception e){
			e = new Exception("not bound");
		}
	}
}
