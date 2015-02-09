package br.ufes.inf.padtec.tnokco.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mindswap.pellet.exceptions.InconsistentOntologyException;

import br.com.padtec.advisor.application.queries.AdvisorQueryUtil;
import br.com.padtec.advisor.application.types.ConceptEnum;
import br.com.padtec.advisor.application.types.RelationEnum;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;
import br.com.padtec.okco.core.exception.OKCoExceptionInstanceFormat;
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
	
	public static ArrayList<String[]> getPossibleConnects(String rp)
	{
		InfModel inferredModel = OKCoUploader.getInferredModel();
		OntModel baseModel = OKCoUploader.getBaseModel();
		String namespace = OKCoUploader.getNamespace();
		
			ArrayList<String[]> result = new ArrayList<String[]>();
			
			List<String> classes_from_rp=QueryUtil.getClassesURI(inferredModel,namespace+rp);
			ArrayList<String> relations = new ArrayList<String>();
			ArrayList<String> rp_sink = new ArrayList<String>();
			ArrayList<String> rp_so = new ArrayList<String>();
			ArrayList<String> ranges = new ArrayList<String>();

			if(classes_from_rp.contains(namespace+"Source_PM-FEP"))
			{
				relations.add("INV.binding_is_represented_by");
				ranges.add(" ");
				relations.add("is_binding");
				ranges.add("Input");
				relations.add("INV.componentOf");
				ranges.add(" ");
				relations.add("componentOf");
				ranges.add("Output");
				relations.add("INV.is_binding");
				ranges.add("Binding");
				relations.add("binding_is_represented_by");
				ranges.add("Sink_PM-FEP");
				
				rp_sink = QueryUtil.query_EndOfGraphWithRanges(rp, relations, ranges, inferredModel);
				for(int i = 0; i < rp_sink.size(); i++){
					if(!QueryUtil.getIndividualsURIAtObjectPropertyRange(inferredModel, namespace+rp,namespace+"has_forwarding", namespace+"Reference_Point").contains(rp_sink.get(i))){
						String[] tuple = new String[2];
						tuple[0] = rp_sink.get(i);
						tuple[1] = "pm_nc";
						result.add(tuple);
					}
				}
				System.out.println();

			}else{
				relations.add("INV.binding_is_represented_by");
				ranges.add(" ");
				relations.add("is_binding");
				ranges.add("Input");
				relations.add("INV.componentOf");
				ranges.add(" ");
				relations.add("componentOf");
				ranges.add("Output");
				relations.add("INV.is_binding");
				ranges.add("Binding");
				relations.add("binding_is_represented_by");
				ranges.add("Reference_Point");
				rp_so = QueryUtil.query_EndOfGraphWithRanges(rp, relations, ranges, inferredModel);
				relations.add("has_forwarding");
				ranges.add("Reference_Point");
				relations.add("INV.binding_is_represented_by");
				ranges.add("Binding");
				relations.add("is_binding");
				ranges.add("Input");
				relations.add("INV.componentOf");
				ranges.add(" ");
				relations.add("componentOf");
				ranges.add("Output");
				relations.add("INV.is_binding");
				ranges.add("Binding");
				relations.add("binding_is_represented_by");
				ranges.add("Reference_Point");

				rp_sink = QueryUtil.query_EndOfGraphWithRanges(rp, relations, ranges, inferredModel);
				
				for(int i = 0; i < rp_sink.size(); i++){
					if(!QueryUtil.getIndividualsURIAtObjectPropertyRange(inferredModel, namespace+rp,namespace+"has_forwarding", namespace+"Reference_Point").contains(rp_sink.get(i))){
						String[] tuple = new String[2];
						tuple[0]= rp_sink.get(i);
						for(int j = 0; j < rp_so.size(); j++){
							if(rp_so.get(j).equals(namespace+"Source_PM-FEP")|| rp_so.get(j).equals(namespace+"Source_Path_FEP")){
								tuple[1]="nc";
							}else{
								tuple[1]="trail";
								result.add(tuple);
							}
						}
					}
				}
				System.out.println();
			}
			return result;
	}


	public static void connects(String rp, String rp_2, String type) throws InconsistentOntologyException, OKCoExceptionInstanceFormat
	{
		OKCoUploader.substituteInferredModelFromBaseModel(false);
		
		InfModel inferredModel = OKCoUploader.getInferredModel();
		OntModel baseModel = OKCoUploader.getBaseModel();
		String namespace = OKCoUploader.getNamespace();
				
		ArrayList<Statement> stmts = new ArrayList<Statement>();
		Individual forwarding;
		Individual te;
		if(type.equals("pm_nc")){
			forwarding = OKCoUploader.getBaseModel().createIndividual(namespace+rp+"_fw_"+rp_2,OKCoUploader.getBaseModel().getResource(namespace+"PM_NC_Forwarding"));
			te = OKCoUploader.getBaseModel().createIndividual(namespace+rp+"_ate_"+rp_2,OKCoUploader.getBaseModel().getResource(namespace+"Unidirectional_PM_NC"));
			stmts.add(OKCoUploader.getBaseModel().createStatement(forwarding, OKCoUploader.getBaseModel().getProperty(namespace+"is_represented_by_Uni_Access_Transport_Entity"), te));
			stmts.add(OKCoUploader.getBaseModel().createStatement(forwarding, OKCoUploader.getBaseModel().getProperty(namespace+"Forwarding_from_Uni_PM_NC"), OKCoUploader.getBaseModel().getIndividual(namespace+rp)));
			stmts.add(OKCoUploader.getBaseModel().createStatement(forwarding, OKCoUploader.getBaseModel().getProperty(namespace+"Forwarding_to_Uni_PM_NC"), OKCoUploader.getBaseModel().getIndividual(namespace+rp_2)));	
			
		}else{
			if(type.equals("nc")){
				forwarding = OKCoUploader.getBaseModel().createIndividual(namespace+rp+"_fw_"+rp_2,OKCoUploader.getBaseModel().getResource(namespace+"Path_NC_Forwarding"));
				te = OKCoUploader.getBaseModel().createIndividual(namespace+rp+"_ate_"+rp_2,OKCoUploader.getBaseModel().getResource(namespace+"Unidirectional_Path_NC"));
				stmts = new ArrayList<Statement>();
				stmts.add(OKCoUploader.getBaseModel().createStatement(forwarding, OKCoUploader.getBaseModel().getProperty(namespace+"is_represented_by_Uni_Path_NC"), te));
				stmts.add(OKCoUploader.getBaseModel().createStatement(forwarding, OKCoUploader.getBaseModel().getProperty(namespace+"Forwarding_from_Uni_Path_NC"), OKCoUploader.getBaseModel().getIndividual(namespace+rp)));
				stmts.add(OKCoUploader.getBaseModel().createStatement(forwarding, OKCoUploader.getBaseModel().getProperty(namespace+"Forwarding_to_Uni_Path_NC"), OKCoUploader.getBaseModel().getIndividual(namespace+rp_2)));	
			}else{
				forwarding = OKCoUploader.getBaseModel().createIndividual(namespace+rp+"_fw_"+rp_2,OKCoUploader.getBaseModel().getResource(namespace+"AP_Forwarding"));
				te = OKCoUploader.getBaseModel().createIndividual(namespace+rp+"_ate_"+rp_2,OKCoUploader.getBaseModel().getResource(namespace+"Unidirectional_Access_Transport_Entity"));
				stmts = new ArrayList<Statement>();
				stmts.add(OKCoUploader.getBaseModel().createStatement(forwarding, OKCoUploader.getBaseModel().getProperty(namespace+"is_represented_by_Uni_Access_Transport_Entity"), te));
				stmts.add(OKCoUploader.getBaseModel().createStatement(forwarding, OKCoUploader.getBaseModel().getProperty(namespace+"Forwarding_from_Uni_Access_Transport_Entity"), OKCoUploader.getBaseModel().getIndividual(namespace+rp)));
				stmts.add(OKCoUploader.getBaseModel().createStatement(forwarding, OKCoUploader.getBaseModel().getProperty(namespace+"Forwarding_to_Uni_Access_Transport_Entity"), OKCoUploader.getBaseModel().getIndividual(namespace+rp_2)));	
				
			}
			
		}
		stmts.add(OKCoUploader.getBaseModel().createStatement(OKCoUploader.getBaseModel().getIndividual(namespace+rp), OKCoUploader.getBaseModel().getProperty(namespace+"has_forwarding"), OKCoUploader.getBaseModel().getIndividual(namespace+rp_2)));
		OKCoUploader.getBaseModel().add(stmts);
		
		OKCoUploader.substituteInferredModelFromBaseModel(false);
	}

	// type = 0 for output type =1 for input
	public static String getRPFromInterface(String eq_interface, Integer type)
	{
		InfModel inferredModel = OKCoUploader.getInferredModel();		
		String namespace = OKCoUploader.getNamespace();
		
		String value="maps_output";
		String target="Output";
		if(type==1){
			value="maps_input";
			target="Input";
		}

		if(QueryUtil.getIndividualsURIAtObjectPropertyRange(inferredModel, eq_interface, namespace+value, namespace+target).size()>0){
			String port= QueryUtil.getIndividualsURIAtObjectPropertyRange(inferredModel, eq_interface, namespace+value, namespace+target).get(0);
			if(QueryUtil.getIndividualsURIAtObjectPropertyRange(inferredModel, port, namespace+"INV.is_binding", namespace+"Binding").size()>0){
				String binding = QueryUtil.getIndividualsURIAtObjectPropertyRange(inferredModel, port, namespace+"INV.is_binding", namespace+"Binding").get(0);
				if(QueryUtil.getIndividualsURIAtObjectPropertyRange(inferredModel, binding, namespace+"binding_is_represented_by", namespace+"Directly_Bound_Reference_Point").size()>0){
					return QueryUtil.getIndividualsURIAtObjectPropertyRange(inferredModel, binding, namespace+"binding_is_represented_by", namespace+"Directly_Bound_Reference_Point").get(0);					
				}
			}
		}
		return "";
	}

	public static boolean bindsInterfaces(String output, String input)
	{	
		OntModel baseModel = OKCoUploader.getBaseModel();
		Individual ind= baseModel.getIndividual(output);
		return true;
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
	
	public static void bindsSpecific(Individual a, Individual b, String tipo_out,
			String tipo_inp) 
	{		
		OntModel baseModel = OKCoUploader.getBaseModel();
		String namespace = OKCoUploader.getNamespace();
		
		// TODO Auto-generated method stub
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
			stmts.add(OKCoUploader.getBaseModel().createStatement(binding, baseModel.getProperty(namespace+value.get("RP_RELATION")), rp));
			stmts.add(OKCoUploader.getBaseModel().createStatement(binding, baseModel.getProperty(namespace+value.get("RP_BINDING_REL_IN")), b));
			stmts.add(OKCoUploader.getBaseModel().createStatement(binding, baseModel.getProperty(namespace+value.get("RP_BINDING_REL_OUT")), a));
			OKCoUploader.getBaseModel().add(stmts);
		}catch(Exception e){
			e = new Exception("not bound");
		}
	}

	private static String[] getTriplePM(String value, String pm) 
	{
		InfModel inferredModel = OKCoUploader.getInferredModel();		
		String namespace = OKCoUploader.getNamespace();
		
		String[] result = new String[3];

		if(value.equals("input"))
		{
			if(AdvisorQueryUtil.getIndividualsURIAtObjectPropertyRange(pm,RelationEnum.COMPONENTOF, ConceptEnum.PHYSICAL_MEDIA_INPUT).size()>0){
				String port= QueryUtil.getIndividualsURIAtObjectPropertyRange(inferredModel, pm, namespace+"componentOf", namespace+"Physical_Media_Input").get(0);
				result[0]=port;
				if(QueryUtil.getIndividualsURIAtObjectPropertyRange(inferredModel, port, namespace+"INV.binds", namespace+"Termination_Source_Output").size()>0){
					String tf_out= QueryUtil.getIndividualsURIAtObjectPropertyRange(inferredModel, port, namespace+"INV.binds", namespace+"Termination_Source_Output").get(0);
					if(QueryUtil.getIndividualsURIAtObjectPropertyRange(inferredModel, tf_out, namespace+"INV.maps_output", namespace+"Output_Interface").size()>0){
						String out_int= (QueryUtil.getIndividualsURIAtObjectPropertyRange(inferredModel, tf_out, namespace+"INV.maps_output", namespace+"Output_Interface").get(0));
						result[1]=out_int;
						if(QueryUtil.getIndividualsURIAtObjectPropertyRange(inferredModel, out_int, namespace+"INV.componentOf", namespace+"Equipment").size()>0){
							result[2]= (QueryUtil.getIndividualsURIAtObjectPropertyRange(inferredModel, out_int, namespace+"INV.componentOf", namespace+"Equipment").get(0));
						}
					}
				}
			}}else{
				if(QueryUtil.getIndividualsURIAtObjectPropertyRange(inferredModel, pm, namespace+"componentOf", namespace+"Physical_Media_Output").size()>0){
					String port= QueryUtil.getIndividualsURIAtObjectPropertyRange(inferredModel, pm, namespace+"componentOf", namespace+"Physical_Media_Output").get(0);
					result[0]=port;
					if(QueryUtil.getIndividualsURIAtObjectPropertyRange(inferredModel, port, namespace+"binds", namespace+"Termination_Sink_Input").size()>0){
						String tf_in= QueryUtil.getIndividualsURIAtObjectPropertyRange(inferredModel, port, namespace+"binds", namespace+"Termination_Sink_Input").get(0);
						if(QueryUtil.getIndividualsURIAtObjectPropertyRange(inferredModel, tf_in, namespace+"INV.maps_input", namespace+"Input_Interface").size()>0){
							String inp_int= (QueryUtil.getIndividualsURIAtObjectPropertyRange(inferredModel, tf_in, namespace+"INV.maps_input", namespace+"Input_Interface").get(0));
							result[1]=inp_int;
							if(QueryUtil.getIndividualsURIAtObjectPropertyRange(inferredModel, inp_int, namespace+"INV.componentOf", namespace+"Equipment").size()>0){
								result[2]= (QueryUtil.getIndividualsURIAtObjectPropertyRange(inferredModel, inp_int, namespace+"INV.componentOf", namespace+"Equipment").get(0));
							}
						}
					}
				}
			}
		return result ;


	} 


	public static ArrayList<String[]> getAllPhysicalMediaAndBinds()
	{		
		List<String> mediasList = AdvisorQueryUtil.getPhysicalMediasURI();
		
		ArrayList<String[]> triples = new ArrayList<String[]>();

		for (String pm : mediasList) 
		{
			String[] triple = new String[7];
			
			String[] triple_aux = new String[3];
			
			triple_aux= getTriplePM("input", pm);
			
			if(triple_aux[0]!=null)
				triple[1]= triple_aux[0].split("#")[1];
			if(triple_aux[1]!=null)
				triple[0]= triple_aux[1].split("#")[1];
			if(triple_aux[2]!=null)
				triple[2]= triple_aux[2].split("#")[1];

			triple[3]=pm.split("#")[1];

			triple_aux= getTriplePM("output", pm);
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
