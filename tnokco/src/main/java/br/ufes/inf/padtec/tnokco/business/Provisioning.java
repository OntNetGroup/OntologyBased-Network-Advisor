package br.ufes.inf.padtec.tnokco.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mindswap.pellet.exceptions.InconsistentOntologyException;

import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.okco.core.application.OKCoUploader;
import br.com.padtec.okco.core.exception.OKCoExceptionInstanceFormat;
import br.com.padtec.transformation.sindel.processor.BindsProcessor;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Statement;


public class Provisioning {


	//public static HashMap<HashMap<String, String>, HashMap<String,String>> values = new HashMap<HashMap<String,String>, HashMap<String,String>>();
	HashMap<String, HashMap<String, String>> equipmentsReleations = new HashMap<String, HashMap<String,String>>();
	HashMap<String, String> equipmentRP = new HashMap<String, String>();
	HashMap<String, String> equipmentOut = new HashMap<String, String>();
	private static ArrayList<Equipment> equipmentsList= new ArrayList<Equipment>();
	public static OntModel Model= OKCoUploader.getBaseModel();
	public static InfModel InfModel = OKCoUploader.getInferredModel();
	public static String namespace =  OKCoUploader.getBaseModel().getNsPrefixURI("");
	static List<String> equipments = QueryUtil.getIndividualsURI(OKCoUploader.getInferredModel(), OKCoUploader.getBaseModel().getNsPrefixURI("")+"Equipment");
	public static ArrayList<String[]> connections; 
	public static ArrayList<String[]> binds; 
	public static String relation= "site_connects";
	public static HashMap<String, List<String>> ind_class= new HashMap<String, List<String>>();
	public static ArrayList<String[]> triples_g800 = new ArrayList<String[]>();

	static Provisioning instance = new Provisioning();


	static public Provisioning getInstance(){

		return instance;
	}

	public static void main(String[] args) {
		Provisioning rp= new Provisioning();
	}


	public Provisioning(){
		BindsProcessor.initValues();

		//		HashMap<String, String> tf1= new HashMap<String, String>();
		//		HashMap<String, String> hashrp= new HashMap<String, String>();
		//		tf1.put("INPUT", "Adaptation_Source_Input");
		//		tf1.put("OUTPUT", "Termination_Source_Output");
		//		hashrp.put("RP", "Source_A-FEP");
		//		hashrp.put("RP_RELATION", "is_represented_by_So_A-FEP");
		//		hashrp.put("RP_BINDING", "Source_A-FEP_Binding");
		//		hashrp.put("RP_BINDING_REL_IN", "binds_So_A-FEP_to");
		//		hashrp.put("RP_BINDING_REL_OUT", "binds_So_A-FEP_from");		
		//		values.put(tf1, hashrp);
		//
		//		tf1= new HashMap<String, String>();
		//		hashrp= new HashMap<String, String>();
		//		tf1.put("INPUT", "Matrix_Input");
		//		tf1.put("OUTPUT", "Termination_Source_Output");
		//		hashrp.put("RP", "Source_M-FEP");
		//		hashrp.put("RP_RELATION", "is_represented_by_So_M-FEP");
		//		hashrp.put("RP_BINDING", "Source-M-FEP_Binding");
		//		hashrp.put("RP_BINDING_REL_IN", "binds_So_M-FEP_to");
		//		hashrp.put("RP_BINDING_REL_OUT", "binds_So_M-FEP-from");	
		//		values.put(tf1, hashrp);
		//
		//		tf1= new HashMap<String, String>();
		//		hashrp= new HashMap<String, String>();
		//		tf1.put("INPUT", "Subnetwork_Input");
		//		tf1.put("OUTPUT", "Termination_Source_Output");
		//		hashrp.put("RP", "Source_SN-FEP");
		//		hashrp.put("RP_RELATION", "is_represented_by_So_SN-FEP");
		//		hashrp.put("RP_BINDING", "Source-SN-FEP_Binding");
		//		hashrp.put("RP_BINDING_REL_IN", "binds_So_SN-FEP_to");
		//		hashrp.put("RP_BINDING_REL_OUT", "binds_So_SN-FEP_from");	
		//		values.put(tf1, hashrp);
		//
		//		tf1= new HashMap<String, String>();
		//		hashrp= new HashMap<String, String>();
		//		tf1.put("INPUT", "Layer_Processor_Source_Input");
		//		tf1.put("OUTPUT", "Termination_Source_Output");
		//		hashrp.put("RP", "Source_LPF-FEP");
		//		hashrp.put("RP_RELATION", "is_represented_by_So_LPF-FEP");
		//		hashrp.put("RP_BINDING", "Source_LPF-FEP Binding");
		//		hashrp.put("RP_BINDING_REL_IN", "binds_So_LPF-FEP_to");
		//		hashrp.put("RP_BINDING_REL_OUT", "binds_So_LPF-FEP-from");	
		//		values.put(tf1, hashrp);
		//
		//
		//		tf1.put("INPUT", "Adaptation_Sink_Input");
		//		tf1.put("OUTPUT", "Termination_Sink_Output");
		//		hashrp.put("RP", "Sink_A-FEP");
		//		hashrp.put("RP_RELATION", "is_represented_by_Sk_A-FEP");
		//		hashrp.put("RP_BINDING", "Sink_A-FEP_Binding");
		//		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_A-FEP_to");
		//		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_A-FEP_from");		
		//		values.put(tf1, hashrp);
		//
		//		tf1= new HashMap<String, String>();
		//		hashrp= new HashMap<String, String>();
		//		tf1.put("INPUT", "Matrix_Input");
		//		tf1.put("OUTPUT", "Termination_Sink_Output");
		//		hashrp.put("RP", "Sink_M-FEP");
		//		hashrp.put("RP_RELATION", "is_represented_by_Sk_M-FEP");
		//		hashrp.put("RP_BINDING", "Sink_M-FEP_Binding");
		//		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_M-FEP_to");
		//		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_M-FEP_from");	
		//		values.put(tf1, hashrp);
		//
		//		tf1= new HashMap<String, String>();
		//		hashrp= new HashMap<String, String>();
		//		tf1.put("INPUT", "Subnetwork_Input");
		//		tf1.put("OUTPUT", "Termination_Sink_Output");
		//		hashrp.put("RP", "Sink_SN-FEP");
		//		hashrp.put("RP_RELATION", "is_represented_by_Sk_SN-FEP");
		//		hashrp.put("RP_BINDING", "Sink_SN-FEP_Binding");
		//		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_SN-FEP_to");
		//		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_SN-FEP_from");	
		//		values.put(tf1, hashrp);
		//
		//		tf1= new HashMap<String, String>();
		//		hashrp= new HashMap<String, String>();
		//		tf1.put("INPUT", "Layer_Processor_Sink_Input");
		//		tf1.put("OUTPUT", "Termination_Sink_Output");
		//		hashrp.put("RP", "Sink_LPF-FEP");
		//		hashrp.put("RP_RELATION", "is_represented_by_Sk_LPF-FEP");
		//		hashrp.put("RP_BINDING", "Sink_LPF-FEP_Binding");
		//		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_LPF-FEP_to");
		//		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_LPF-FEP_from");	
		//		values.put(tf1, hashrp);
		//
		//		tf1= new HashMap<String, String>();
		//		hashrp= new HashMap<String, String>();
		//		tf1.put("INPUT", "Physical_Media_Input");
		//		tf1.put("OUTPUT", "Termination_Source_Output");
		//		hashrp.put("RP", "Source_PM-FEP");
		//		hashrp.put("RP_RELATION", "is_represented_by_Sk_PM-FEP");
		//		hashrp.put("RP_BINDING", "Sink_PM-FEP_Binding");
		//		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_PM-FEP_to");
		//		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_PM-FEP_from");	
		//		values.put(tf1, hashrp);
		//
		//		tf1= new HashMap<String, String>();
		//		hashrp= new HashMap<String, String>();
		//		tf1.put("INPUT", "Termination_Source_Input");
		//		tf1.put("OUTPUT", "Adaptation_Source_Output");
		//		hashrp.put("RP", "Source_AP");
		//		hashrp.put("RP_RELATION", "is_represented_by_So_AP");
		//		hashrp.put("RP_BINDING", "Source_AP_Binding");
		//		hashrp.put("RP_BINDING_REL_IN", "binds_So_AP_to");
		//		hashrp.put("RP_BINDING_REL_OUT", "binds_So_AP_from");	
		//		values.put(tf1, hashrp);
		//
		//		tf1= new HashMap<String, String>();
		//		hashrp= new HashMap<String, String>();
		//		tf1.put("INPUT", "Termination_Sink_Input");
		//		tf1.put("OUTPUT", "Adaptation_Sink_Output");
		//		hashrp.put("RP", "Sink_AP");
		//		hashrp.put("RP_RELATION", "is_represented_by_Sk_AP");
		//		hashrp.put("RP_BINDING", "Sink_AP_Binding");
		//		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_AP_to");
		//		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_AP_from");	
		//		values.put(tf1, hashrp);
		//
		//		tf1= new HashMap<String, String>();
		//		hashrp= new HashMap<String, String>();
		//		tf1.put("INPUT", "Adaptation_Source_Input");
		//		tf1.put("OUTPUT", "Layer_Processor_Source_Output");
		//		hashrp.put("RP", "Source_LP-FP");
		//		hashrp.put("RP_RELATION", "is_represented_by_So_L-FP");
		//		hashrp.put("RP_BINDING", "Source_L-FP_Binding");
		//		hashrp.put("RP_BINDING_REL_IN", "binds_So_L-FP_to");
		//		hashrp.put("RP_BINDING_REL_OUT", "binds_So_L-FP_from");	
		//		values.put(tf1, hashrp);
		//
		//		tf1= new HashMap<String, String>();
		//		hashrp= new HashMap<String, String>();
		//		tf1.put("INPUT", "Adaptation_Source_Input");
		//		tf1.put("OUTPUT", "Layer_Processor_Sink_Output");
		//		hashrp.put("RP", "Sink_LP-FP");
		//		hashrp.put("RP_RELATION", "is_represented_by_Sk_L-FP");
		//		hashrp.put("RP_BINDING", "Sink_L-FP_Binding");
		//		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_L-FP_to");
		//		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_L-FP_from");	
		//		values.put(tf1, hashrp);
		//
		//		tf1= new HashMap<String, String>();
		//		hashrp= new HashMap<String, String>();
		//		tf1.put("INPUT", "Adaptation_Source_Input");
		//		tf1.put("OUTPUT", "Adaptation_Source_Output");
		//		hashrp.put("RP", "Unidirectional_A-FP");
		//		hashrp.put("RP_RELATION", "is_represented_by_A-FP");
		//		hashrp.put("RP_BINDING", "A-FP_Binding");
		//		hashrp.put("RP_BINDING_REL_IN", "binds_A-FP_to");
		//		hashrp.put("RP_BINDING_REL_OUT", "binds_A-FP_from");	
		//		values.put(tf1, hashrp);
		//
		//		tf1= new HashMap<String, String>();
		//		hashrp= new HashMap<String, String>();
		//		tf1.put("INPUT", "Adaptation_Source_Input");
		//		tf1.put("OUTPUT", "Subnetwork_Output");
		//		hashrp.put("RP", "Source_SN-FP");
		//		hashrp.put("RP_RELATION", "is_represented_by_So_SN-FP");
		//		hashrp.put("RP_BINDING", "Source_SN-FP_Binding");
		//		hashrp.put("RP_BINDING_REL_IN", "binds_So_SN-FP_to");
		//		hashrp.put("RP_BINDING_REL_OUT", "binds_So_SN-FP_from");	
		//		values.put(tf1, hashrp);
		//
		//		tf1= new HashMap<String, String>();
		//		hashrp= new HashMap<String, String>();
		//		tf1.put("INPUT", "Adaptation_Sink_Input");
		//		tf1.put("OUTPUT", "Subnetwork_Output");
		//		hashrp.put("RP", "Source_SN-FP");
		//		hashrp.put("RP_RELATION", "is_represented_by_Sk_SN-FP");
		//		hashrp.put("RP_BINDING", "Sink_SN-FP_Binding");
		//		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_SN-FP_to");
		//		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_SN-FP_from");	
		//		values.put(tf1, hashrp);
		//
		//		tf1= new HashMap<String, String>();
		//		hashrp= new HashMap<String, String>();
		//		tf1.put("INPUT", "Adaptation_Source_Input");
		//		tf1.put("OUTPUT", "Matrix_Output");
		//		hashrp.put("RP", "Source_SN-FP");
		//		hashrp.put("RP_RELATION", "is_represented_by_So_M-FP");
		//		hashrp.put("RP_BINDING", "Source_M-FP_Binding");
		//		hashrp.put("RP_BINDING_REL_IN", "binds_So_M-FP_to");
		//		hashrp.put("RP_BINDING_REL_OUT", "binds_So_M-FP_from");	
		//		values.put(tf1, hashrp);
		//
		//		tf1= new HashMap<String, String>();
		//		hashrp= new HashMap<String, String>();
		//		tf1.put("INPUT", "Adaptation_Sink_Input");
		//		tf1.put("OUTPUT", "Matrix_Output");
		//		hashrp.put("RP", "Source_M-FP");
		//		hashrp.put("RP_RELATION", "is_represented_by_Sk_M-FP");
		//		hashrp.put("RP_BINDING", "Sink_M-FP_Binding");
		//		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_M-FP_to");
		//		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_M-FP_from");	
		//		values.put(tf1, hashrp);		
	}



	// get all equipments from specific site
	public static ArrayList<Equipment> getEquipmentsFromSite(String site){
		equipments = new ArrayList<String>();
		equipments =  QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, site, namespace+"has_equipment", namespace+"Equipment");
		return getEquipments();
	}

	public static String nameRelation="";
	public static ArrayList<String[]> siteConnects= new ArrayList<String[]>();
	public static List<String> getSitesAndRelations(){
		ind_class= new HashMap<String, List<String>>();
		nameRelation="site_connects";
		ArrayList<String[]> siteConnects= new ArrayList<String[]>();
		List<String> sites= QueryUtil.getIndividualsURI(InfModel, "Site");
		for (String site : sites) {
			Individual indSite = Model.getIndividual(site);
			List<String> siteTarget= QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, indSite.getNameSpace(), "site_connects", "Site");
			for (String target : siteTarget) {
				String[] relation= new String[2];
				relation[0]=site;
				relation[1]=target;
				siteConnects.add(relation);
			}

		}
		return sites;
	}

	public static ArrayList<Equipment> getAllEquipmentsandConnections(){

		equipments = QueryUtil.getIndividualsURI(OKCoUploader.getInferredModel(), namespace+"Equipment");
		return getEquipments();
	}

	public static ArrayList<Equipment> getEquipments(){
		Model = OKCoUploader.getBaseModel();
		InfModel = OKCoUploader.getInferredModel();
		HashMap<String, String> hashInputEquipment= new HashMap<String, String>();
		//		inferInterfaceConnections();
		//equipments = HomeController.Search.GetInstancesFromClass(Model, InfModel, namespace+"Equipment");
		Equipment e = null;
		Individual individual;
		ArrayList<Equipment> equips= new ArrayList<Equipment>();
		for (String equipment: equipments) {
			Individual indeq= Model.getIndividual(equipment);
			List<String> inpInt= QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, equipment, namespace+"componentOf", namespace+"Input_Interface");
			for (String string : inpInt) {
				Individual ind= Model.getIndividual(string);
				hashInputEquipment.put(ind.getLocalName(), indeq.getLocalName());
			}
		}


		for (String equipment: equipments) {
			List<String> outInts= QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, equipment, namespace+"componentOf", namespace+"Output_Interface");
			Individual ind= Model.getIndividual(equipment);
			e = new Equipment(ind.getLocalName());

			for (String out_int : outInts) {
				individual= Model.getIndividual(out_int);
				InterfaceOutput outputInt = new InterfaceOutput();
				outputInt.setName(individual.getLocalName());
				e.addOut(outputInt);
				String inputcon= null;
				if(!QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, out_int, namespace+"interface_binds", namespace+"Input_Interface").isEmpty()){
					inputcon= QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, out_int, namespace+"interface_binds", namespace+"Input_Interface").get(0);
				}
				if(inputcon!=null){
					outputInt.setConnected(true);
					ArrayList<String> binds = new ArrayList<String>();
					binds.add(individual.getLocalName());
					Individual indiv= Model.getIndividual(inputcon);
					binds.add(indiv.getLocalName());
					Individual equipmentEl = Model.getIndividual(namespace+hashInputEquipment.get(indiv.getLocalName()));
					Equipment equip = new Equipment(equipmentEl.getLocalName());
					e.putBinds(binds, equip);
				}

			}
			equips.add(e); 
		}

		equipments = QueryUtil.getIndividualsURI(OKCoUploader.getInferredModel(), namespace+"Equipment");
		return equips;
	}

	public static ArrayList<String[]> getPossibleConnects(String rp){
			InfModel = OKCoUploader.getInferredModel();
			ArrayList<String[]> result = new ArrayList<String[]>();
			
			List<String> classes_from_rp=QueryUtil.getClassesURI(InfModel,namespace+rp);
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
				
				rp_sink = QueryUtil.query_EndOfGraphWithRanges(rp, relations, ranges, InfModel);
				for(int i = 0; i < rp_sink.size(); i++){
					if(!QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, namespace+rp,namespace+"has_forwarding", namespace+"Reference_Point").contains(rp_sink.get(i))){
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
				rp_so = QueryUtil.query_EndOfGraphWithRanges(rp, relations, ranges, InfModel);
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

				rp_sink = QueryUtil.query_EndOfGraphWithRanges(rp, relations, ranges, InfModel);
				
				for(int i = 0; i < rp_sink.size(); i++){
					if(!QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, namespace+rp,namespace+"has_forwarding", namespace+"Reference_Point").contains(rp_sink.get(i))){
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
		/*InfModel = OKCoUploader.getInferredModel();
		ArrayList<String[]> result = new ArrayList<String[]>();
		
		List<String> classes_from_rp=QueryUtil.getClassesURI(InfModel,namespace+rp);
		String binding=null;
		String input = null;
		if(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, namespace+rp, namespace+"INV.binding_is_represented_by", namespace+"Binding").size()>0){
			binding = QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, namespace+rp,namespace+"INV.binding_is_represented_by", namespace+"Binding").get(0);
			if(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, binding,namespace+"is_binding", namespace+"Input").size()>0){
				input = QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, binding,namespace+"is_binding", namespace+"Input").get(0);
			}
			if(classes_from_rp.contains(namespace+"Source_PM-FEP"))
			{
				
				if(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, input,namespace+"INV.componentOf", namespace+"Physical_Media").size()>0){
					String pm = QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, input,namespace+"INV.componentOf", namespace+"Physical_Media").get(0);
					if(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, pm,namespace+"componentOf", namespace+"Output").size()>0){
						String output = QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, pm,namespace+"componentOf", namespace+"Output").get(0);
						if(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, output,namespace+"INV.is_binding", namespace+"Binding").size()>0){
							String binding_sk = QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, output,namespace+"INV.is_binding", namespace+"Binding").get(0);
							if(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, binding_sk,namespace+"binding_is_represented_by", namespace+"Sink_PM-FEP").size()>0){		
								String rp_sink=QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, binding_sk,namespace+"binding_is_represented_by", namespace+"Sink_PM-FEP").get(0);
									if(!QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, namespace+rp,namespace+"has_forwarding", namespace+"Reference_Point").contains(rp_sink)){
									String[] tuple = new String[2];
									tuple[0] =rp_sink;
									tuple[1] = "pm_nc";
									result.add(tuple);
								}
							}
						}
					}
				}


			}else{
				
				// || (HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, namespace+rp,namespace+"Forwarding_from_Uni_Access_Transport_Entity", namespace+"AP_Forwarding").size()>0))
				if(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, input, namespace+"INV.componentOf", namespace+"Transport_Function").size()>0){
					String tf=(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, input, namespace+"INV.componentOf", namespace+"Transport_Function")).get(0);
					if(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, tf, namespace+"componentOf", namespace+"Output").size()>0){
						String output=(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, tf, namespace+"componentOf", namespace+"Output")).get(0);
						if(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, output, namespace+"INV.is_binding", namespace+"Binding").size()>0){
							binding = QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, output,namespace+"INV.is_binding", namespace+"Binding").get(0);
							if(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, binding, namespace+"binding_is_represented_by", namespace+"Reference_Point").size()>0){
								String rp_so = QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, binding,namespace+"binding_is_represented_by", namespace+"Reference_Point").get(0);
								if(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, rp_so, namespace+"has_forwarding", namespace+"Reference_Point").size()>0){
									String rp_sk =QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, rp_so,namespace+"has_forwarding", namespace+"Reference_Point").get(0);
									if(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, rp_sk, namespace+"INV.binding_is_represented_by", namespace+"Binding").size()>0){
										String binding_sk = QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, rp_sk,namespace+"INV.binding_is_represented_by", namespace+"Binding").get(0);
										if(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, binding_sk, namespace+"is_binding", namespace+"Input").size()>0){
											String input_sk = QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, binding_sk,namespace+"is_binding", namespace+"Input").get(0);
											if(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, input_sk, namespace+"INV.componentOf", namespace+"Transport_Function").size()>0){
												String tf_sk = QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, input_sk,namespace+"INV.componentOf", namespace+"Transport_Function").get(0);
												if(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, tf_sk, namespace+"componentOf", namespace+"Output").size()>0){
													String output_sk =QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, tf_sk,namespace+"componentOf", namespace+"Output").get(0);
													if(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, output_sk, namespace+"INV.is_binding", namespace+"Binding").size()>0){
														String binding_2_sk =QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, output_sk,namespace+"INV.is_binding", namespace+"Binding").get(0);
														if(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, binding_2_sk, namespace+"binding_is_represented_by", namespace+"Reference_Point").size()>0){
															String rp_sink =QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, binding_2_sk,namespace+"binding_is_represented_by", namespace+"Reference_Point").get(0);
															if(!QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, namespace+rp,namespace+"has_forwarding", namespace+"Reference_Point").contains(rp_sink)){
																String[] tuple = new String[2];
																tuple[0]= rp_sink;
																if(rp_so.equals(namespace+"Source_PM-FEP")|| rp_so.equals(namespace+"Source_Path_FEP")){
																	tuple[1]="nc";
																}else{
																	tuple[1]="trail";
																	result.add(tuple);
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return result;
*/	}


	public static void connects(String rp, String rp_2, String type) throws InconsistentOntologyException, OKCoExceptionInstanceFormat{
		OKCoUploader.substituteInferredModelFromBaseModel(false);
		
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
	public static String getRPFromInterface(String eq_interface, Integer type){
		String value="maps_output";
		String target="Output";
		if(type==1){
			value="maps_input";
			target="Input";
		}

		if(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, eq_interface, namespace+value, namespace+target).size()>0){
			String port= QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, eq_interface, namespace+value, namespace+target).get(0);
			if(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, port, namespace+"INV.is_binding", namespace+"Binding").size()>0){
				String binding = QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, port, namespace+"INV.is_binding", namespace+"Binding").get(0);
				if(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, binding, namespace+"binding_is_represented_by", namespace+"Directly_Bound_Reference_Point").size()>0){
					return QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, binding, namespace+"binding_is_represented_by", namespace+"Directly_Bound_Reference_Point").get(0);					
				}
			}
		}
		return "";
	}

	public static ArrayList<Equipment> getEquipmentsConnectionsBinds(){
		return getAllEquipmentsandConnections();

	}

	public static boolean bindsInterfaces(String output, String input){
		Individual ind= Model.getIndividual(output);

		return true;
	}

	public static List<String> getAllSitesAndConnections(){
		connections = new ArrayList<String[]>();
		List<String> sites = QueryUtil.getIndividualsURI(OKCoUploader.getInferredModel(), namespace+"Site");
		for (String site : sites) {
			if(!QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, site, namespace+"site_connects", namespace+"Site").isEmpty()){
				List<String>targets=QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, site, namespace+"site_connects", namespace+"Site");
				for (String target : targets) {
					String[] connection = new String[2];
					connection[0]=site;
					connection[1]=target;
					connections.add(connection);
				}
			}
		}

		return sites;
	}

	public static List<String> getAllG800(){
		List<String> allIndividuals=QueryUtil.getIndividualsURIFromAllClasses(InfModel);
		ArrayList<String> copy = new ArrayList<String>();

		for (String ind : allIndividuals) {
			List<String> classesFromIndividual= QueryUtil.getClassesURI(InfModel,ind);
			if((classesFromIndividual.contains(namespace+"Input_Interface")  || classesFromIndividual.contains(namespace+"Output_Interface") || classesFromIndividual.contains(namespace+"Site") || classesFromIndividual.contains(namespace+"Equipment"))){
				copy.add(ind);
			}
		}
		for (String string : copy) {
			allIndividuals.remove(string);
		}
		setRelationsG800(allIndividuals);
		return allIndividuals;
	}
	public static List<String> getG800FromEquipment(String equipment){
		Provisioning.triples_g800 =  new ArrayList<String[]>();

		List<String> g800s  = QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, namespace+equipment, namespace+"componentOf", namespace+"Transport_Function");
		List<String> outInt = QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, namespace+equipment, namespace+"componentOf", namespace+"Output_Interface");
		List<String> inpInt = QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, namespace+equipment, namespace+"componentOf", namespace+"Input_Interface");

		for (String interface_out : outInt) {
			try {
				g800s.add(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, namespace+interface_out, namespace+interface_out+"maps_output", namespace+interface_out+"Output").get(0));
			} catch (Exception e) {				
			}			
		}
		for (String interface_inp : inpInt) {
			try {
				g800s.add(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, namespace+interface_inp, namespace+"maps_input", namespace+"Input").get(0));
			} catch (Exception e) {				
			}			
		}
		setRelationsG800(g800s);
		return g800s;
	}
	public static void setRelationsG800(List<String> g800_elements){

		ind_class = new HashMap<String, List<String>>();
		List<String> classesFromIndividual;
		for (String g800 : g800_elements) {
			classesFromIndividual= QueryUtil.getClassesURI(InfModel,g800);
			ind_class.put(g800, classesFromIndividual);
			
			// Get instance relations
			List<DtoInstanceRelation> rel = new ArrayList<DtoInstanceRelation>();
			List<String> propertiesURIList = QueryUtil.getPropertiesURI(OKCoUploader.getInferredModel(), g800);
			for(String propertyURI: propertiesURIList){
				DtoInstanceRelation dtoItem = new DtoInstanceRelation();
			    dtoItem.Property = propertyURI;
			    List<String> ranges = QueryUtil.getRangeURIs(OKCoUploader.getInferredModel(), propertyURI);
			    if(ranges.size()>0) dtoItem.Target = ranges.get(0);
			    else dtoItem.Target = "";
			    rel.add(dtoItem);
			}
			
			for (DtoInstanceRelation dtoInstanceRelation : rel) {
				String[] triple = new String[3];
				triple[0]=g800;
				triple[1]=dtoInstanceRelation.Property;
				triple[2]=dtoInstanceRelation.Target;
				triples_g800.add(triple);
			}

		}
	}

	public static void bindsSpecific(Individual a, Individual b, String tipo_out,
			String tipo_inp) {
		// TODO Auto-generated method stub
		HashMap<String, String> key = new HashMap<String, String>();
		key.put("INPUT", tipo_inp);
		key.put("OUTPUT", tipo_out);
		try{
			HashMap<String, String> value = BindsProcessor.values.get(key);
			OntClass ClassImage = Model.getOntClass(namespace+value.get("RP"));
			Individual rp = Model.createIndividual(namespace+a.getLocalName()+"rp"+b.getLocalName(),ClassImage);
			//OKCoUploader.getBaseModel()=Model; #### weird construction!
			Individual binding= Model.createIndividual(namespace+a.getLocalName()+"binding"+b.getLocalName(),Model.getResource(namespace+value.get("RP_BINDING")));
			ArrayList<Statement> stmts = new ArrayList<Statement>();
			stmts.add(OKCoUploader.getBaseModel().createStatement(binding, Model.getProperty(namespace+value.get("RP_RELATION")), rp));
			stmts.add(OKCoUploader.getBaseModel().createStatement(binding, Model.getProperty(namespace+value.get("RP_BINDING_REL_IN")), b));
			stmts.add(OKCoUploader.getBaseModel().createStatement(binding, Model.getProperty(namespace+value.get("RP_BINDING_REL_OUT")), a));
			OKCoUploader.getBaseModel().add(stmts);
		}catch(Exception e){
			e = new Exception("not bound");
		}
	}


	public static void inferInterfaceConnections(){
		HashMap<String, String> int_port = new HashMap<String, String>();
		List<String> inters = QueryUtil.getIndividualsURI(InfModel, namespace+"Input_Interface");
		for (String inter : inters) {
			List<String> port_inp =QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, inter, namespace+"maps_input", namespace+"Input");
			if(port_inp.size()>0){
				int_port.put(port_inp.get(0), inter);
			}
		}
		inters = QueryUtil.getIndividualsURI(InfModel, namespace+"Output_Interface");
		for (String inter : inters) {
			List<String> port_inp =QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, inter, namespace+"maps_output", namespace+"Output");
			if(port_inp.size()>0){
				int_port.put(port_inp.get(0), inter);
			}
		}

		List<String> outs = QueryUtil.getIndividualsURI(InfModel, namespace+"Output");
		for (String out : outs) {
			List<String> inputs  = QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, out, namespace+"binds", namespace+"Input");
			if(inputs.size()>0){
				String interfac_input= int_port.get(inputs.get(0));
				String interfac_output= int_port.get(out);
				Individual a = null,b=null;
				if(interfac_input!=null)
					a = OKCoUploader.getBaseModel().getIndividual(interfac_input);
				if(interfac_output!=null)
					b = OKCoUploader.getBaseModel().getIndividual(interfac_output);
				ObjectProperty rel = OKCoUploader.getBaseModel().getObjectProperty(namespace+"interface_binds");
				if(a!=null && b!=null){
					Statement stmt = OKCoUploader.getBaseModel().createStatement(b, rel, a);
					OKCoUploader.getBaseModel().add(stmt);
				}
			}
		}
		OKCoUploader.substituteInferredModelFromBaseModel(false);
	}

	private static String[] getTriplePM(String value, String pm) {
		// TODO Auto-generated method stub

		String[] result = new String[3];

		if(value.equals("input")){
			if(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, pm, namespace+"componentOf", namespace+"Physical_Media_Input").size()>0){
				String port= QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, pm, namespace+"componentOf", namespace+"Physical_Media_Input").get(0);
				result[0]=port;
				if(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, port, namespace+"INV.binds", namespace+"Termination_Source_Output").size()>0){
					String tf_out= QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, port, namespace+"INV.binds", namespace+"Termination_Source_Output").get(0);
					if(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, tf_out, namespace+"INV.maps_output", namespace+"Output_Interface").size()>0){
						String out_int= (QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, tf_out, namespace+"INV.maps_output", namespace+"Output_Interface").get(0));
						result[1]=out_int;
						if(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, out_int, namespace+"INV.componentOf", namespace+"Equipment").size()>0){
							result[2]= (QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, out_int, namespace+"INV.componentOf", namespace+"Equipment").get(0));
						}
					}
				}
			}}else{
				if(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, pm, namespace+"componentOf", namespace+"Physical_Media_Output").size()>0){
					String port= QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, pm, namespace+"componentOf", namespace+"Physical_Media_Output").get(0);
					result[0]=port;
					if(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, port, namespace+"binds", namespace+"Termination_Sink_Input").size()>0){
						String tf_in= QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, port, namespace+"binds", namespace+"Termination_Sink_Input").get(0);
						if(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, tf_in, namespace+"INV.maps_input", namespace+"Input_Interface").size()>0){
							String inp_int= (QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, tf_in, namespace+"INV.maps_input", namespace+"Input_Interface").get(0));
							result[1]=inp_int;
							if(QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, inp_int, namespace+"INV.componentOf", namespace+"Equipment").size()>0){
								result[2]= (QueryUtil.getIndividualsURIAtObjectPropertyRange(InfModel, inp_int, namespace+"INV.componentOf", namespace+"Equipment").get(0));
							}
						}
					}
				}
			}
		return result ;


	} 


	public static ArrayList<String[]> getAllPhysicalMediaAndBinds(){

		List<String> pms = QueryUtil.getIndividualsURI( OKCoUploader.getInferredModel(), namespace+"Physical_Media");
		ArrayList<String[]> triples = new ArrayList<String[]>();

		for (String pm : pms) {
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
