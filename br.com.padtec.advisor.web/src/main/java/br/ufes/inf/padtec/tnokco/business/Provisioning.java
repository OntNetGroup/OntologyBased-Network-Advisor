package br.ufes.inf.padtec.tnokco.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.padtec.advisor.application.ProvisioningFunctionality;
import br.com.padtec.advisor.application.queries.AdvisorQueryUtil;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;
import br.com.padtec.transformation.sindel.processor.BindsProcessor;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Statement;


public class Provisioning {
	
	public static HashMap<String, List<String>> individual_classes_map= new HashMap<String, List<String>>();
	public static ArrayList<String[]> g800_triples = new ArrayList<String[]>();
	
	public Provisioning()
	{
		BindsProcessor.initValues();
	}

	public static void generateG800Mappings(List<String> g800IndividualsURI)
	{
		InfModel inferredModel = OKCoUploader.getInferredModel();				
		individual_classes_map.clear();
		g800_triples.clear();
		
		List<String> classesFromIndividual;
		for (String individualURI : g800IndividualsURI) 
		{
			classesFromIndividual= QueryUtil.getClassesURI(inferredModel,individualURI);
			individual_classes_map.put(individualURI, classesFromIndividual);
						
			List<String> propertiesURIList = QueryUtil.getPropertiesURI(OKCoUploader.getInferredModel(), individualURI);
			for(String propertyURI: propertiesURIList)
			{				
			    String[] triple = new String[3];
			    triple[0]=individualURI;
				triple[1]=propertyURI;				
			    List<String> ranges = QueryUtil.getRangeURIs(OKCoUploader.getInferredModel(), propertyURI);			    
			    if(ranges.size()>0) triple[2] = ranges.get(0);
			    else triple[2] = "";			    
				g800_triples.add(triple);
			}			
		}
	}

	public static List<String> filterG800ForEquipment(String equipmentName)
	{		
		InfModel inferredModel = OKCoUploader.getInferredModel();		
		String namespace = OKCoUploader.getNamespace();
		
		List<String> g800IndividualsList  = AdvisorQueryUtil.getTransportFunctionsURIAtComponentOfRange(namespace+equipmentName);
		
		List<String> outInterfacesList = AdvisorQueryUtil.getOutputInterfacesURIAtComponentOfRange(namespace+equipmentName);
		for (String interface_out : outInterfacesList) 
		{			
			g800IndividualsList.add(
				QueryUtil.getIndividualsURIAtObjectPropertyRange(inferredModel, namespace+interface_out, namespace+interface_out+"maps_output", namespace+interface_out+"Output").get(0)
			);			
		}
		
		List<String> inpInt = AdvisorQueryUtil.getInputInterfacesURIAtComponentOfRange(namespace+equipmentName);
		for (String interface_inp : inpInt) {
			try {
				g800IndividualsList.add(QueryUtil.getIndividualsURIAtObjectPropertyRange(inferredModel, namespace+interface_inp, namespace+"maps_input", namespace+"Input").get(0));
			} catch (Exception e) {				
			}			
		}
		
		/**====================================
		 * Generate class and relation mappings from G800
		 * ==================================== */
		generateG800Mappings(g800IndividualsList);
		
		return g800IndividualsList;
	}
	
	public static void bindsSpecific(Individual a, Individual b, String tipo_out, String tipo_inp) 
	{		
		OntModel baseModel = OKCoUploader.getBaseModel();
		String namespace = OKCoUploader.getNamespace();
		
		HashMap<String, String> key = new HashMap<String, String>();
		key.put("INPUT", tipo_inp);
		key.put("OUTPUT", tipo_out);
		
		try{
			HashMap<String, String> value = BindsProcessor.values.get(key);
			
			OntClass ClassImage = baseModel.getOntClass(namespace+value.get("RP"));
			Individual rp = baseModel.createIndividual(namespace+a.getLocalName()+"rp"+b.getLocalName(),ClassImage);
			//OKCoUploader.getBaseModel()=Model; #### weird construction!
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

	public static ArrayList<String[]> getAllPhysicalMediaAndBinds()
	{		
		List<String> mediasList = AdvisorQueryUtil.getPhysicalMediasURI();
		
		ArrayList<String[]> triples = new ArrayList<String[]>();

		for (String pm : mediasList) 
		{
			String[] triple = new String[7];
			
			String[] triple_aux = new String[3];
			
			triple_aux= ProvisioningFunctionality.getPhysicalMediaTriples("input", pm);
			
			if(triple_aux[0]!=null)
				triple[1]= triple_aux[0].split("#")[1];
			if(triple_aux[1]!=null)
				triple[0]= triple_aux[1].split("#")[1];
			if(triple_aux[2]!=null)
				triple[2]= triple_aux[2].split("#")[1];

			triple[3]=pm.split("#")[1];

			triple_aux= ProvisioningFunctionality.getPhysicalMediaTriples("output", pm);
			if(triple_aux[0]!=null){
				triple[4]= triple_aux[0].split("#")[1];
			}
			if(triple_aux[1]!=null){
				triple[5]= triple_aux[1].split("#")[1];
			}
			if(triple_aux[2]!=null)
				triple[6]= triple_aux[2].split("#")[1];

			triples.add(triple);
		}

		return triples;
	}




}
