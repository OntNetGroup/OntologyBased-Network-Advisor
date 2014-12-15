package br.com.padtec.transformation.condel;
import java.io.InputStream;
import java.util.ArrayList;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class JavaAplicationGenerateCode {

	public static void main(String[] args) {		
				
		String inputFileName = "C://Users//fabio_000//Desktop//OntologiasOwl//G800Completa.owl";	
		String javaCodeProp = "";
		String javaCodeCls = "";
		
		OntModel model = null;
		model = ModelFactory.createOntologyModel();
		InputStream in = FileManager.get().open(inputFileName);
		if (in == null) {
		    throw new IllegalArgumentException("File: " + inputFileName + " not found");
		}		
		model.read(in,null);
		 
		
		//QueryExample(model, NS);
		
		ArrayList<String> lclasses = GetClasses(model);
		ArrayList<String> lprops = GetProperties(model);
		
		for (String prop : lprops) {
			javaCodeProp = javaCodeProp + " || instruction.contains(\" " + prop + " \")";
		}
		
		System.out.println("");
		
		for (String cls : lclasses) {
			javaCodeCls = javaCodeCls + " || instruction.contains(\"" + cls + ":\")";
			//if(! cls.contains("INV."))
			//	System.out.println("- " + cls);
		}
		
		System.out.println(javaCodeProp);
		System.out.println("");
		System.out.println(javaCodeCls);

	}
	
	public static void QueryExample(InfModel infModel, String NS) {
		
		String queryString = 
				"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
				"PREFIX ns: <" + NS + ">" +
				" SELECT *" +
				" WHERE {\n" +		
						"?x owl:equivalentClass ?y ." +
				"}";

		System.out.println(queryString);
		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		ResultSet results = qe.execSelect();
		
		// Output query results 
		ResultSetFormatter.out(System.out, results, query);
	}
	
	public static ArrayList<String> GetClasses(OntModel model) {
		
		ArrayList<String> lista = new ArrayList<String>();
		ExtendedIterator<OntClass> i = model.listClasses();
		if( !i.hasNext() ) {
			//System.out.print( "none" );
		}
		else {
			while( i.hasNext() ) {
				Resource val = (Resource) i.next();
				//System.out.println(val.getURI());
				if(val.getURI() != null)
				{
					lista.add( val.getLocalName() );
				}
			}
		}
		return lista;
	}
	
	public static ArrayList<String> GetProperties(OntModel model) {
		
		ArrayList<String> lista = new ArrayList<String>();
		ExtendedIterator<OntProperty> i = model.listOntProperties();
		if( !i.hasNext() ) {
			//System.out.print( "none" );
		}
		else {
			while( i.hasNext() ) {
				Resource val = (Resource) i.next();
				
				if(val != null && !(val.getURI().contains("http://www.w3.org/")))
				{
					lista.add( val.getLocalName() );
				}
			}
		}
		return lista;
	}

}
