package br.com.padtec.common.transformation.condel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import br.com.padtec.common.dto.DtoInstanceRelation;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class OwlG805toCondel {

	public static String ns = "";
	public static final String w3String = "http://www.w3.org/";
	public static final String classIntegerValue = "http://www.w3.org/2001/XMLSchema#integer";
	
	public static ArrayList<String> transformToCondel(OntModel model)
	{	
		ArrayList<String> instructions = new ArrayList<String>();		
		ns = model.getNsPrefixURI("");	
		
		ArrayList<String> listInstances = new ArrayList<String>();
		listInstances = OwlG805toCondel.GetAllInstances(model, model);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		instructions.add("/* Condel code generated in "+ dateFormat.format(date) + " */");
		instructions.add("");
		instructions.add("");
		instructions.add("/* Instance declarations */");
		instructions.add("");
		
		// Instance declarations
		for (String ins : listInstances) 
		{				
			/* Create instances */
			ArrayList<String> listClasses = OwlG805toCondel.GetClassesFrom(ins, model);
			for (String cls : listClasses) {
				instructions.add(cls.replace(ns, "") + ":" + ins.replace(ns, "") + ";");
			}
		}	
		
		
		// Create Relations		
		for (String ins : listInstances) 
		{
			instructions.add("");
			instructions.add("/* " + ins + " */");
			instructions.add("");
			
			/* Create Relations */
			ArrayList<DtoInstanceRelation> listRelations = OwlG805toCondel.GetInstanceRelations(model, ins);
			for (DtoInstanceRelation dto : listRelations) {
				if(dto.Target.contains(classIntegerValue))
				{
					instructions.add(ins.replace(ns, "") + getCorrectAtributteDeclaration(dto.Property) + dto.Target.replace(ns, "").replace("^^http://www.w3.org/2001/XMLSchema#integer", "") + ";");
				} else {
					instructions.add(ins.replace(ns, "") + " " + dto.Property.replace(ns, "") + " " + dto.Target.replace(ns, "") + ";");
				}
			}
			
			/* Create Same */
			ArrayList<String> listSame = OwlG805toCondel.GetSameInstancesFrom(model, ins);
			for (String insS : listSame) {
				instructions.add(ins.replace(ns, "") + " " + "sameAs" + " " + insS.replace(ns, "") + ";");
			}

			/* Create Different */
			ArrayList<String> listDif = OwlG805toCondel.GetDifferentInstancesFrom(model, ins);
			for (String insD : listDif) {
				instructions.add(ins.replace(ns, "") + " " + "disjointWith" + " " + insD.replace(ns, "") + ";");
			}
		}
				
		return instructions;		
	}
	
	
	/* Auxiliary functions */
	
	public static ArrayList<String> GetAllInstances(OntModel model, InfModel infModel)
	{		
		ArrayList<String> AllInstances = new ArrayList<String>();
		ArrayList<String> AllClasses = GetClasses(model);
		//System.out.println("-> " + AllClasses.size());
		for (String className : AllClasses) {
			
			if(!(className == null)){
				ArrayList<String> InstancesFromClass = GetInstancesFromClass(model, infModel, className);
				for (String instance : InstancesFromClass) {
					if (!(AllInstances.contains(instance)))
						AllInstances.add(instance);
				}
				//System.out.println("->" + className);				
			}
		}
		
		return AllInstances;
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
				lista.add( val.getURI() );
			}
		}
		return lista;
	}
	
	public static ArrayList<String> GetInstancesFromClass(OntModel model, InfModel infModel, String className) {
		
		ArrayList<String> list = new ArrayList<String>();
		
		// Create a new query
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + ns + ">" +
		" SELECT *" +
		" WHERE {\n" +		
			" ?i rdf:type <" + className + "> .\n " +	
		"}";

		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		ResultSet results = qe.execSelect();
		
		// Output query results 
		//ResultSetFormatter.out(System.out, results, query);
		
		//System.out.println("-> " + instanceName);
		while (results.hasNext()) {
			
			QuerySolution row = results.next();
		    
		    RDFNode i = row.get("i");
		    list.add(i.toString());
		}	

		return list;		
	}
		
	public static ArrayList<String> GetDifferentInstancesFrom(InfModel infModel, String instanceName)
	{		
		ArrayList<String> list = new ArrayList<String>();
		
		// Create a new query
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + ns + ">" +
		" SELECT *" +
		" WHERE {\n" +		
			"{ " + 
				"<" + instanceName + "> owl:differentFrom" + " ?y .\n " +
			"} UNION { " +
				" ?y owl:differentFrom <" + instanceName + "> .\n " +
			" } " +
		"}";

		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		ResultSet results = qe.execSelect();
		
		// Output query results 
		//ResultSetFormatter.out(System.out, results, query);
		
		//System.out.println("-> " + instanceName);
		while (results.hasNext()) {
			QuerySolution row = results.next();
		    
		    RDFNode rdfY = row.get("y");
		    if(! instanceName.equals(rdfY.toString()))
		    {
		    	list.add(rdfY.toString());
		    	//System.out.println("-------> " + rdfY.toString());
		    }
		}	
		//System.out.println("");

		return list;
	}
	
	public static ArrayList<String> GetSameInstancesFrom(InfModel infModel, String instanceName)
	{
		ArrayList<String> list = new ArrayList<String>();
		
		// Create a new query
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + ns + ">" +
		" SELECT *" +
		" WHERE {\n" +		
			"{ " + 
				"<" + instanceName + "> owl:sameAs" + " ?y .\n " +
			"} UNION { " +
				" ?y owl:sameAs <" + instanceName + "> .\n " +
			" } " +
		"}";

		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		ResultSet results = qe.execSelect();
		
		// Output query results 
		//ResultSetFormatter.out(System.out, results, query);
		
		while (results.hasNext()) {
			QuerySolution row = results.next();
		    
		    RDFNode rdfY = row.get("y");
		    if(! instanceName.equals(rdfY.toString()))
		    {
		    	list.add(rdfY.toString());
		    }
		}		

		return list;
	}
	
	public static ArrayList<DtoInstanceRelation> GetInstanceRelations(InfModel infModel, String individualUri)
	{
		ArrayList<DtoInstanceRelation> listIndividualRelations = new ArrayList<DtoInstanceRelation>();
		
		// Create a new query
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + ns + ">" +
		" SELECT DISTINCT *" +
		" WHERE {\n" +		
			"{ " + "<" + individualUri + ">" + " ?property" + " ?target .\n " +
				" ?property " + " rdf:type" + " owl:ObjectProperty .\n " +
			"} UNION { " +
				"<" + individualUri + ">" + " ?property" + " ?target .\n " +
				" ?property " + " rdf:type" + " owl:DatatypeProperty.\n " +		
			"}" +
		"}";

		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		ResultSet results = qe.execSelect();
		
		// Output query results 
		//ResultSetFormatter.out(System.out, results, query);
		DtoInstanceRelation dtoItem = null;
		
		while (results.hasNext()) {
			
			QuerySolution row= results.next();
		    RDFNode property = row.get("property");
		    RDFNode target = row.get("target"); 
		    
		    dtoItem = new DtoInstanceRelation();
		    dtoItem.Property = property.toString();
		    dtoItem.Target = target.toString();
		    
			listIndividualRelations.add(dtoItem);		    		    		    
		}
		
		return listIndividualRelations;
	}

	public static ArrayList<String> GetClassesFrom(String instanceName, InfModel infModel) {
		
		ArrayList<String> listClasses = new ArrayList<String>();
		
		//check if instance is a data value
		if(instanceName.contains("http://www.w3.org/"))
		{
			String type = instanceName.split("\\^\\^")[1];
			listClasses.add(type);
			return listClasses;
		}
		
		// Create a new query
		String queryString = 
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX ns: <" + ns + ">" +
		" SELECT *" +
		" WHERE {\n" +
				"<" + instanceName + "> " + "rdf:type" + " ?class .\n " +
		"}";
		
		Query query = QueryFactory.create(queryString); 
		
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		ResultSet results = qe.execSelect();
		
		// Output query results 
		// ResultSetFormatter.out(System.out, results, query);
		
		while (results.hasNext()) {
			QuerySolution row= results.next();
		    RDFNode cls = row.get("class");
		    if((cls.toString().contains(ns)) && (!cls.toString().contains(w3String)))
		    {
		    	listClasses.add(cls.toString());	
		    }		    	    		    
		}
		
		return listClasses;

	}
	
	public static String getCorrectAtributteDeclaration(String property)
	{
		String relationName = "";
		
		if(property.contains("Trail_Termination_Function.type")) 
		{			
			relationName = ".type:";				
		}
		if(property.contains("Geographical_Element_With_Alias.location")) 
		{
			relationName = ".location:";
		}
		if(property.contains("Defined_Geographical_Element.latitude.degree")) 
		{
			relationName = ".lat.deg:";			
		}
		if(property.contains("Defined_Geographical_Element.latitude.minute")) 
		{
			relationName = ".lat.min:";
		}
		if(property.contains("Defined_Geographical_Element.latitude.second")) 
		{
			relationName = ".lat.sec:";			
		}
		if(property.contains("Defined_Geographical_Element.longitude.degree")) 
		{
			relationName = ".lon.deg:";
		}
		if(property.contains("Defined_Geographical_Element.longitude.minute")) 
		{
			relationName = ".lon.min:";
		}
		if(property.contains("Defined_Geographical_Element.longitude.second")) 
		{
			relationName = ".lon.sec:";
		}		
		
		return relationName;
		
	}
}
