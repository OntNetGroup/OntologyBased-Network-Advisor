package br.ufes.inf.padtec.tnokco.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.mindswap.pellet.exceptions.InconsistentOntologyException;

import arq.remote;
import br.ufes.inf.nemo.okco.model.DtoInstanceRelation;
import br.ufes.inf.nemo.okco.model.OKCoExceptionInstanceFormat;
import br.ufes.inf.nemo.padtec.processors.BindsProcessor;
import br.ufes.inf.padtec.tnokco.controller.HomeController;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.sparql.function.library.e;


public class Provisioning {


	//public static HashMap<HashMap<String, String>, HashMap<String,String>> values = new HashMap<HashMap<String,String>, HashMap<String,String>>();
	HashMap<String, HashMap<String, String>> equipmentsReleations = new HashMap<String, HashMap<String,String>>();
	HashMap<String, String> equipmentRP = new HashMap<String, String>();
	HashMap<String, String> equipmentOut = new HashMap<String, String>();
	private static ArrayList<Equipment> equipmentsList= new ArrayList<Equipment>();
	public static OntModel Model= HomeController.Model;
	public static InfModel InfModel = HomeController.InfModel;
	static ArrayList<String> equipments = HomeController.Search.GetInstancesFromClass(Model, HomeController.InfModel, HomeController.NS+"Equipment");
	public static ArrayList<String[]> connections; 
	public static ArrayList<String[]> binds; 
	public static String relation= "site_connects";
	public static HashMap<String, ArrayList<String>> ind_class= new HashMap<String, ArrayList<String>>();
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
		equipments =  HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, site, HomeController.NS+"has_equipment", HomeController.NS+"Equipment");
		return getEquipments();
	}

	public static String nameRelation="";
	public static ArrayList<String[]> siteConnects= new ArrayList<String[]>();
	public static ArrayList<String> getSitesAndRelations(){
		ind_class= new HashMap<String, ArrayList<String>>();
		nameRelation="site_connects";
		ArrayList<String[]> siteConnects= new ArrayList<String[]>();
		ArrayList<String> sites= HomeController.Search.GetInstancesFromClass(Model, InfModel, "Site");
		for (String site : sites) {
			Individual indSite = Model.getIndividual(site);
			ArrayList<String> siteTarget= HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, indSite.getNameSpace(), "site_connects", "Site");
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

		equipments = HomeController.Search.GetInstancesFromClass(Model, HomeController.InfModel, HomeController.NS+"Equipment");
		return getEquipments();
	}

	public static ArrayList<Equipment> getEquipments(){
		Model = HomeController.Model;
		InfModel = HomeController.InfModel;
		HashMap<String, String> hashInputEquipment= new HashMap<String, String>();
		//		inferInterfaceConnections();
		//equipments = HomeController.Search.GetInstancesFromClass(Model, InfModel, HomeController.NS+"Equipment");
		Equipment e = null;
		Individual individual;
		ArrayList<Equipment> equips= new ArrayList<Equipment>();
		for (String equipment: equipments) {
			Individual indeq= Model.getIndividual(equipment);
			ArrayList<String> inpInt= HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, equipment, HomeController.NS+"componentOf", HomeController.NS+"Input_Interface");
			for (String string : inpInt) {
				Individual ind= Model.getIndividual(string);
				hashInputEquipment.put(ind.getLocalName(), indeq.getLocalName());
			}
		}


		for (String equipment: equipments) {
			ArrayList<String> outInts= HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, equipment, HomeController.NS+"componentOf", HomeController.NS+"Output_Interface");
			Individual ind= Model.getIndividual(equipment);
			e = new Equipment(ind.getLocalName());

			for (String out_int : outInts) {
				individual= Model.getIndividual(out_int);
				InterfaceOutput outputInt = new InterfaceOutput();
				outputInt.setName(individual.getLocalName());
				e.addOut(outputInt);
				String inputcon= null;
				if(!HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, out_int, HomeController.NS+"interface_binds", HomeController.NS+"Input_Interface").isEmpty()){
					inputcon= HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, out_int, HomeController.NS+"interface_binds", HomeController.NS+"Input_Interface").get(0);
				}
				if(inputcon!=null){
					outputInt.setConnected(true);
					ArrayList<String> binds = new ArrayList<String>();
					binds.add(individual.getLocalName());
					Individual indiv= Model.getIndividual(inputcon);
					binds.add(indiv.getLocalName());
					Individual equipmentEl = Model.getIndividual(HomeController.NS+hashInputEquipment.get(indiv.getLocalName()));
					Equipment equip = new Equipment(equipmentEl.getLocalName());
					e.putBinds(binds, equip);
				}

			}
			equips.add(e); 
		}

		equipments = HomeController.Search.GetInstancesFromClass(Model, HomeController.InfModel, HomeController.NS+"Equipment");
		return equips;
	}

	public static ArrayList<String[]> getPossibleConnects(String eq_interface){
		//String rp = getRPFromInterface(eq_interface,0);
		ArrayList<String[]> result= new ArrayList<String[]>();
		String[] possibleConnects= new String[2];
		if(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, eq_interface, HomeController.NS+"maps_output", HomeController.NS+"Output").size()>0){
			String port= HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, eq_interface, HomeController.NS+"maps_output", HomeController.NS+"Output").get(0);
			ArrayList<String> classesFromIndividual= HomeController.Search.GetClassesFrom(port, InfModel);	
			if(classesFromIndividual.contains(HomeController.NS+"T_So_Output_-_So_PM-FEP_Bound")){
				if(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, port, HomeController.NS+"binds_So_PM-FEP", HomeController.NS+"PM_Input_-_So_PM-FEP_Bound").size()>0){
					String pm_input=HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, port, HomeController.NS+"binds_So_PM-FEP", HomeController.NS+"PM_Input_-_So_PM-FEP_Bound").get(0);
					if(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, pm_input, HomeController.NS+"INV.componentOf", HomeController.NS+"Physical_Media").size()>0){
						String pm=HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, pm_input, HomeController.NS+"INV.componentOf", HomeController.NS+"Physical_Media").get(0);
						if(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, pm, HomeController.NS+"componentOf", HomeController.NS+"Physical_Media_Output").size()>0){
							String pm_out=HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, pm, HomeController.NS+"componentOf", HomeController.NS+"Physical_Media_Output").get(0);
							if(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, pm_out, HomeController.NS+"binds", HomeController.NS+"T_Sk_Input_-_Sk_PM-FEP_Bound").size()>0){
								String input=HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, pm_out, HomeController.NS+"binds", HomeController.NS+"T_Sk_Input_-_Sk_PM-FEP_Bound").get(0);
								if(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, input, HomeController.NS+"INV.maps_input", HomeController.NS+"Input_Interface").size()>0){			
									possibleConnects[0]=(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, input, HomeController.NS+"INV.maps_in", HomeController.NS+"Input_Interface").get(0));
									possibleConnects[1]="pm_connects";
									result.add(possibleConnects);
								}
							}
						}
					}
				}
			}else{
				if(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, port, HomeController.NS+"binds", HomeController.NS+"Input").size()>0){
					String input=HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, port, HomeController.NS+"binds", HomeController.NS+"Input").get(0);
					if(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, input, HomeController.NS+"INV.componentOf", HomeController.NS+"Termination_Function").size()>0){
						String tf=HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, input, HomeController.NS+"INV.componentOf", HomeController.NS+"Termination_Function").get(0);
						if(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, tf, HomeController.NS+"componentOf", HomeController.NS+"Output").size()>0){
							ArrayList<String> outs=HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, tf, HomeController.NS+"componentOf", HomeController.NS+"Output");
							for (String out : outs) {
								if(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, out, HomeController.NS+"is_binding", HomeController.NS+"Binding").size()>0){
									String binding=HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, out, HomeController.NS+"is_binding", HomeController.NS+"Binding").get(0);
									if(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, binding, HomeController.NS+"binding_is_represented_by", HomeController.NS+"Binding").size()>0){
										String rp=HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, binding, HomeController.NS+"binding_is_represented_by", HomeController.NS+"Binding").get(0);
										if(HomeController.Search.GetClassesFrom(rp, InfModel).contains("Active_Source_AP") || HomeController.Search.GetClassesFrom(rp, InfModel).contains("Active_So_Path-FEP_-_Path_NC_connected")){										
											if(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, rp, HomeController.NS+"has_forwarding", HomeController.NS+"Connected_Reference_Point").size()>0){
												String rp_sk=HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, rp, HomeController.NS+"has_forwarding", HomeController.NS+"Connected_Reference_Point").get(0);
												if(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, rp_sk, HomeController.NS+"INV.binding_is_represented_by", HomeController.NS+"Binding").size()>0){
													String binding_sk=HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, rp_sk, HomeController.NS+"INV.binding_is_represented_by", HomeController.NS+"Binding").get(0);
													if(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, binding_sk, HomeController.NS+"INV.is_binding", HomeController.NS+"Input").size()>0){
														String input_sk=HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, binding_sk, HomeController.NS+"INV.is_binding", HomeController.NS+"Input").get(0);
														if(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, input_sk, HomeController.NS+"INV.componentOf", HomeController.NS+"Termination_Function").size()>0){
															String tf_sk=HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, input_sk, HomeController.NS+"INV.componentOf", HomeController.NS+"Termination_Function").get(0);
															if(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, tf, HomeController.NS+"componentOf", HomeController.NS+"Output").size()>0){
																String out_sk=HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, tf, HomeController.NS+"componentOf", HomeController.NS+"Output").get(0);
																if(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, out_sk, HomeController.NS+"binds", HomeController.NS+"Output").size()>0){
																	String input_sk_not_connected=HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, port, HomeController.NS+"binds", HomeController.NS+"Input").get(0);
																	possibleConnects[0]=(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, input_sk, HomeController.NS+"INV.maps_in", HomeController.NS+"Input_Interface").get(0));
																	if(HomeController.Search.GetClassesFrom(rp_sk, InfModel).contains("Active_Sink_AP")){
																		possibleConnects[1]="nc";
																	}else{
																		possibleConnects[0]="trail";
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
		}
		return result;
	}


	public static void connects(String out_inter, String inp_interface, String type) throws InconsistentOntologyException, OKCoExceptionInstanceFormat{
		String rp=getRPFromInterface(out_inter, 0);
		String rp_2=getRPFromInterface(inp_interface, 1);
		if(type.equals("trail")){
			Individual forwarding = HomeController.Model.createIndividual(rp+"_fw_"+rp_2,HomeController.Model.getResource(HomeController.NS+"AP_Forwarding"));
			Individual trail = HomeController.Model.createIndividual(rp+"_ate_"+rp_2,HomeController.Model.getResource(HomeController.NS+"Unidirectional_Access_Transport_Entity"));
			ArrayList<Statement> stmts = new ArrayList<Statement>();
			stmts.add(HomeController.Model.createStatement(forwarding, HomeController.Model.getProperty(HomeController.NS+"is_represented_by_Uni_Access_Transport_Entity"), trail));
			stmts.add(HomeController.Model.createStatement(HomeController.Model.getIndividual(out_inter), HomeController.Model.getProperty(HomeController.NS+"Forwarding_from_Uni_Access_Transport_Entity"), forwarding));
			stmts.add(HomeController.Model.createStatement(forwarding, HomeController.Model.getProperty(HomeController.NS+"Forwarding_to_Uni_Access_Transport_Entity"), HomeController.Model.getIndividual(inp_interface)));	
			HomeController.Model.add(stmts);
		}else{
			Individual forwarding = HomeController.Model.createIndividual(HomeController.NS+rp.split("#")[1]+"_fw_"+rp_2.split("#")[1],HomeController.Model.getResource(HomeController.NS+"PM_NC_Forwarding"));
			Individual nc = HomeController.Model.createIndividual(HomeController.NS+ rp.split("#")[1]+"_ate_"+rp_2.split("#")[1],HomeController.Model.getResource(HomeController.NS+"Unidirectional_Path_NC"));
			ArrayList<Statement> stmts = new ArrayList<Statement>();
			stmts.add(HomeController.Model.createStatement(forwarding, HomeController.Model.getProperty(HomeController.NS+"is_represented_by_Uni_Path_NC"), nc));
			stmts.add(HomeController.Model.createStatement(HomeController.Model.getIndividual(out_inter), HomeController.Model.getProperty(HomeController.NS+"Forwarding_from_Uni_Path_NC"), forwarding));
			stmts.add(HomeController.Model.createStatement(forwarding, HomeController.Model.getProperty(HomeController.NS+"Forwarding_to_Uni_Access_Transport_Entity"), HomeController.Model.getIndividual(inp_interface)));	
			HomeController.Model.add(stmts);
			System.out.println(forwarding.getNameSpace()+forwarding.getLocalName());
			System.out.println(nc.getNameSpace()+nc.getLocalName());
			HomeController.UpdateAddIntanceInLists(forwarding.getNameSpace()+forwarding.getLocalName());
			HomeController.UpdateAddIntanceInLists(nc.getNameSpace()+nc.getLocalName());
		}

	}

	// type = 0 for output type =1 for input
	public static String getRPFromInterface(String eq_interface, Integer type){
		String value="maps_output";
		String target="Output";
		if(type==1){
			value="maps_input";
			target="Input";
		}

		if(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, eq_interface, HomeController.NS+value, HomeController.NS+target).size()>0){
			String port= HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, eq_interface, HomeController.NS+value, HomeController.NS+target).get(0);
			if(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, port, HomeController.NS+"INV.is_binding", HomeController.NS+"Binding").size()>0){
				String binding = HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, port, HomeController.NS+"INV.is_binding", HomeController.NS+"Binding").get(0);
				if(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, binding, HomeController.NS+"binding_is_represented_by", HomeController.NS+"Directly_Bound_Reference_Point").size()>0){
					return HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, binding, HomeController.NS+"binding_is_represented_by", HomeController.NS+"Directly_Bound_Reference_Point").get(0);					
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

	public static ArrayList<String> getAllSitesAndConnections(){
		connections = new ArrayList<String[]>();
		ArrayList<String> sites = HomeController.Search.GetInstancesFromClass(Model, HomeController.InfModel, HomeController.NS+"Site");
		for (String site : sites) {
			if(!HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, site, HomeController.NS+"site_connects", HomeController.NS+"Site").isEmpty()){
				ArrayList<String>targets=HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, site, HomeController.NS+"site_connects", HomeController.NS+"Site");
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

	public static ArrayList<String> getAllG800(){
		ArrayList<String> allIndividuals= HomeController.Search.GetAllInstances(Model, InfModel);
		ArrayList<String> copy = new ArrayList<String>();

		for (String ind : allIndividuals) {
			ArrayList<String> classesFromIndividual= HomeController.Search.GetClassesFrom(ind, InfModel);
			if((classesFromIndividual.contains(HomeController.NS+"Input_Interface")  || classesFromIndividual.contains(HomeController.NS+"Output_Interface") || classesFromIndividual.contains(HomeController.NS+"Site") || classesFromIndividual.contains(HomeController.NS+"Equipment"))){
				copy.add(ind);
			}
		}
		for (String string : copy) {
			allIndividuals.remove(string);
		}
		setRelationsG800(allIndividuals);
		return allIndividuals;
	}
	public static ArrayList<String> getG800FromEquipment(String equipment){
		Provisioning.triples_g800 =  new ArrayList<String[]>();

		ArrayList<String> g800s  = HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, HomeController.NS+equipment, HomeController.NS+"componentOf", HomeController.NS+"Transport_Function");
		ArrayList<String> outInt = HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, HomeController.NS+equipment, HomeController.NS+"componentOf", HomeController.NS+"Output_Interface");
		ArrayList<String> inpInt = HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, HomeController.NS+equipment, HomeController.NS+"componentOf", HomeController.NS+"Input_Interface");

		for (String interface_out : outInt) {
			try {
				g800s.add(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, HomeController.NS+interface_out, HomeController.NS+interface_out+"maps_output", HomeController.NS+interface_out+"Output").get(0));
			} catch (Exception e) {				
			}			
		}
		for (String interface_inp : inpInt) {
			try {
				g800s.add(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, HomeController.NS+interface_inp, HomeController.NS+"maps_input", HomeController.NS+"Input").get(0));
			} catch (Exception e) {				
			}			
		}
		setRelationsG800(g800s);
		return g800s;
	}
	public static void setRelationsG800(ArrayList<String> g800_elements){

		ind_class = new HashMap<String, ArrayList<String>>();
		ArrayList<String> classesFromIndividual;
		for (String g800 : g800_elements) {
			classesFromIndividual= HomeController.Search.GetClassesFrom(g800, InfModel);
			ind_class.put(g800, classesFromIndividual);
			ArrayList<DtoInstanceRelation> rel= HomeController.Search.GetInstanceRelations(HomeController.InfModel, g800);

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
			OntClass ClassImage = Model.getOntClass(HomeController.NS+value.get("RP"));
			Individual rp = Model.createIndividual(HomeController.NS+a.getLocalName()+"rp"+b.getLocalName(),ClassImage);
			HomeController.Model=Model;
			Individual binding= Model.createIndividual(HomeController.NS+a.getLocalName()+"binding"+b.getLocalName(),Model.getResource(HomeController.NS+value.get("RP_BINDING")));
			ArrayList<Statement> stmts = new ArrayList<Statement>();
			stmts.add(HomeController.Model.createStatement(binding, Model.getProperty(HomeController.NS+value.get("RP_RELATION")), rp));
			stmts.add(HomeController.Model.createStatement(binding, Model.getProperty(HomeController.NS+value.get("RP_BINDING_REL_IN")), b));
			stmts.add(HomeController.Model.createStatement(binding, Model.getProperty(HomeController.NS+value.get("RP_BINDING_REL_OUT")), a));
			HomeController.Model.add(stmts);
		}catch(Exception e){
			e = new Exception("not bound");
		}
	}


	public static void inferInterfaceConnections(){
		HashMap<String, String> int_port = new HashMap<String, String>();
		ArrayList<String> inters = HomeController.Search.GetInstancesFromClass(Model, InfModel, HomeController.NS+"Input_Interface");
		for (String inter : inters) {
			ArrayList<String> port_inp =HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, inter, HomeController.NS+"maps_input", HomeController.NS+"Input");
			if(port_inp.size()>0){
				int_port.put(port_inp.get(0), inter);
			}
		}
		inters = HomeController.Search.GetInstancesFromClass(Model, InfModel, HomeController.NS+"Output_Interface");
		for (String inter : inters) {
			ArrayList<String> port_inp =HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, inter, HomeController.NS+"maps_output", HomeController.NS+"Output");
			if(port_inp.size()>0){
				int_port.put(port_inp.get(0), inter);
			}
		}

		ArrayList<String> outs = HomeController.Search.GetInstancesFromClass(Model, InfModel, HomeController.NS+"Output");
		for (String out : outs) {
			ArrayList<String> inputs  = HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, out, HomeController.NS+"binds", HomeController.NS+"Input");
			if(inputs.size()>0){
				String interfac_input= int_port.get(inputs.get(0));
				String interfac_output= int_port.get(out);
				Individual a = null,b=null;
				if(interfac_input!=null)
					a = HomeController.Model.getIndividual(interfac_input);
				if(interfac_output!=null)
					b = HomeController.Model.getIndividual(interfac_output);
				ObjectProperty rel = HomeController.Model.getObjectProperty(HomeController.NS+"interface_binds");
				if(a!=null && b!=null){
					Statement stmt = HomeController.Model.createStatement(b, rel, a);
					HomeController.Model.add(stmt);
				}
			}
		}
		HomeController.InfModel = HomeController.Model;
	}

	private static String[] getTriplePM(String value, String pm) {
		// TODO Auto-generated method stub

		String[] result = new String[2];

		if(value.equals("input")){
			if(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, pm, HomeController.NS+"componentOf", HomeController.NS+"Physical_Media_Input").size()>0){
				String port= HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, pm, HomeController.NS+"componentOf", HomeController.NS+"Physical_Media_Input").get(0);
				result[0]=port;
				if(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, port, HomeController.NS+"INV.binds_So_PM-FEP", HomeController.NS+"T_So_Output_-_So_PM-FEP_Bound").size()>0){
					String tf_out= HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, port, HomeController.NS+"INV.binds_So_PM-FEP", HomeController.NS+"T_So_Output_-_So_PM-FEP_Bound").get(0);
					if(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, tf_out, HomeController.NS+"INV.maps_output", HomeController.NS+"Output_Interface").size()>0){
						String out_int= (HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, tf_out, HomeController.NS+"INV.maps_output", HomeController.NS+"Output_Interface").get(0));
						if(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, out_int, HomeController.NS+"INV.componentOf", HomeController.NS+"Equipment").size()>0){
							result[1]= (HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, out_int, HomeController.NS+"INV.componentOf", HomeController.NS+"Equipment").get(0));
						}
					}
				}
			}}else{
				if(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, pm, HomeController.NS+"componentOf", HomeController.NS+"Physical_Media_Output").size()>0){
					String port= HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, pm, HomeController.NS+"componentOf", HomeController.NS+"Physical_Media_Output").get(0);
					result[0]=port;
					if(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, port, HomeController.NS+"INV.binds_Sk_PM-FEP", HomeController.NS+"T_Sk_Input_-_Sk_PM-FEP_Bound").size()>0){
						String tf_in= HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, port, HomeController.NS+"INV.binds_Sk_PM-FEP", HomeController.NS+"T_Sk_Input_-_Sk_PM-FEP_Bound").get(0);
						if(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, tf_in, HomeController.NS+"INV.maps_input", HomeController.NS+"Input_Interface").size()>0){
							String out_int= (HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, tf_in, HomeController.NS+"INV.maps_input", HomeController.NS+"Input_Interface").get(0));
							if(HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, out_int, HomeController.NS+"INV.componentOf", HomeController.NS+"Equipment").size()>0){
								result[1]= (HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, out_int, HomeController.NS+"INV.componentOf", HomeController.NS+"Equipment").get(0));
							}
						}
					}
				}
			}
		return result ;


	} 


	public static ArrayList<String[]> getAllPhysicalMediaAndBinds(){

		ArrayList<String> pms = HomeController.Search.GetInstancesFromClass(Model, HomeController.InfModel, HomeController.NS+"Physical_Media");
		ArrayList<String[]> triples = new ArrayList<String[]>();

		for (String pm : pms) {
			String[] triple = new String[5];
			String[] triple_aux = new String[2];	
			triple_aux= getTriplePM("input", pm);
			triple[1]= triple_aux[0].split("#")[1];
			triple[0]= triple_aux[1].split("#")[1];
			triple[2]=pm.split("#")[1];
			triple_aux= getTriplePM("output", pm);
			triple[3]= triple_aux[0].split("#")[1];
			triple[4]= triple_aux[1].split("#")[1];
			triples.add(triple);
		}

		return triples;
	}




}
