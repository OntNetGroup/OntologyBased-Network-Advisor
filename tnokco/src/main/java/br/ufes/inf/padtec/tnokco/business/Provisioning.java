package br.ufes.inf.padtec.tnokco.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import arq.remote;
import br.ufes.inf.nemo.okco.model.DtoInstanceRelation;
import br.ufes.inf.padtec.tnokco.controller.HomeController;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.sparql.function.library.e;


public class Provisioning {


	public static HashMap<HashMap<String, String>, HashMap<String,String>> values = new HashMap<HashMap<String,String>, HashMap<String,String>>();
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


		HashMap<String, String> tf1= new HashMap<String, String>();
		HashMap<String, String> hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation_Source_Input");
		tf1.put("OUTPUT", "Termination_Source_Output");
		hashrp.put("RP", "Source_A-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_So_A-FEP");
		hashrp.put("RP_BINDING", "Source_A-FEP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_So_A-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_A-FEP_from");		
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Matrix_Input");
		tf1.put("OUTPUT", "Termination_Source_Output");
		hashrp.put("RP", "Source_M-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_So_M-FEP");
		hashrp.put("RP_BINDING", "Source-M-FEP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_So_M-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_M-FEP-from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Subnetwork_Input");
		tf1.put("OUTPUT", "Termination_Source_Output");
		hashrp.put("RP", "Source_SN-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_So_SN-FEP");
		hashrp.put("RP_BINDING", "Source-SN-FEP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_So_SN-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_SN-FEP_from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Layer_Processor_Source_Input");
		tf1.put("OUTPUT", "Termination_Source_Output");
		hashrp.put("RP", "Source_LPF-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_So_LPF-FEP");
		hashrp.put("RP_BINDING", "Source_LPF-FEP Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_So_LPF-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_LPF-FEP-from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Layer_Processor_Source_Input");
		tf1.put("OUTPUT", "Termination_Source_Output");
		hashrp.put("RP", "Source_PM-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_So_PM-FEP");
		hashrp.put("RP_BINDING", "Source_PM-FEP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_So_PM-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_PM-FEP_from");	
		values.put(tf1, hashrp);

		tf1.put("INPUT", "Adaptation_Sink_Input");
		tf1.put("OUTPUT", "Termination_Sink_Output");
		hashrp.put("RP", "Sink_A-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_A-FEP");
		hashrp.put("RP_BINDING", "Sink_A-FEP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_A-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_A-FEP_from");		
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Matrix_Input");
		tf1.put("OUTPUT", "Termination_Sink_Output");
		hashrp.put("RP", "Sink_M-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_M-FEP");
		hashrp.put("RP_BINDING", "Sink_M-FEP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_M-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_M-FEP_from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Subnetwork_Input");
		tf1.put("OUTPUT", "Termination_Sink_Output");
		hashrp.put("RP", "Sink_SN-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_SN-FEP");
		hashrp.put("RP_BINDING", "Sink_SN-FEP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_SN-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_SN-FEP_from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Layer_Processor_Sink_Input");
		tf1.put("OUTPUT", "Termination_Sink_Output");
		hashrp.put("RP", "Sink_LPF-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_LPF-FEP");
		hashrp.put("RP_BINDING", "Sink_LPF-FEP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_LPF-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_LPF-FEP_from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Layer_Processor_Sink_Input");
		tf1.put("OUTPUT", "Termination_Sink_Output");
		hashrp.put("RP", "Source_PM-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_PM-FEP");
		hashrp.put("RP_BINDING", "Sink_PM-FEP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_PM-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_PM-FEP_from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Termination_Source_Input");
		tf1.put("OUTPUT", "Adaptation_Source_Output");
		hashrp.put("RP", "Source_AP");
		hashrp.put("RP_RELATION", "is_represented_by_So_AP");
		hashrp.put("RP_BINDING", "Source_AP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_So_AP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_AP_from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Termination_Sink_Input");
		tf1.put("OUTPUT", "Adaptation_Sink_Output");
		hashrp.put("RP", "Sink_AP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_AP");
		hashrp.put("RP_BINDING", "Sink_AP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_AP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_AP_from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation_Source_Input");
		tf1.put("OUTPUT", "Layer_Processor_Source_Output");
		hashrp.put("RP", "Source_LP-FP");
		hashrp.put("RP_RELATION", "is_represented_by_So_L-FP");
		hashrp.put("RP_BINDING", "Source_L-FP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_So_L-FP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_L-FP_from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation_Source_Input");
		tf1.put("OUTPUT", "Layer_Processor_Sink_Output");
		hashrp.put("RP", "Sink_LP-FP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_L-FP");
		hashrp.put("RP_BINDING", "Sink_L-FP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_L-FP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_L-FP_from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation_Source_Input");
		tf1.put("OUTPUT", "Adaptation_Source_Output");
		hashrp.put("RP", "Unidirectional_A-FP");
		hashrp.put("RP_RELATION", "is_represented_by_A-FP");
		hashrp.put("RP_BINDING", "A-FP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_A-FP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_A-FP_from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation_Source_Input");
		tf1.put("OUTPUT", "Subnetwork_Output");
		hashrp.put("RP", "Source_SN-FP");
		hashrp.put("RP_RELATION", "is_represented_by_So_SN-FP");
		hashrp.put("RP_BINDING", "Source_SN-FP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_So_SN-FP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_SN-FP_from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation_Sink_Input");
		tf1.put("OUTPUT", "Subnetwork_Output");
		hashrp.put("RP", "Source_SN-FP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_SN-FP");
		hashrp.put("RP_BINDING", "Sink_SN-FP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_SN-FP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_SN-FP_from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation_Source_Input");
		tf1.put("OUTPUT", "Matrix_Output");
		hashrp.put("RP", "Source_SN-FP");
		hashrp.put("RP_RELATION", "is_represented_by_So_M-FP");
		hashrp.put("RP_BINDING", "Source_M-FP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_So_M-FP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_M-FP_from");	
		values.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation_Sink_Input");
		tf1.put("OUTPUT", "Matrix_Output");
		hashrp.put("RP", "Source_M-FP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_M-FP");
		hashrp.put("RP_BINDING", "Sink_M-FP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_M-FP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_M-FP_from");	
		values.put(tf1, hashrp);		
	}



	// get all equipments from specific site
	public static ArrayList<Equipment> getEquipmentsFromSite(String site){
		equipments =  HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, site, HomeController.NS+"has_equipment", HomeController.NS+"Equipment");
		return getEquipmentsConnectionsBinds();
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
		Model = HomeController.Model;
		InfModel = HomeController.InfModel;
		HashMap<String, String> hashInputEquipment= new HashMap<String, String>();
		//equipments = HomeController.Search.GetInstancesFromClass(Model, InfModel, HomeController.NS+"Equipment");
		Equipment e = null;
		Individual individual;
		ArrayList<Equipment> equips= new ArrayList<Equipment>();
		for (String equipment: equipments) {
			Individual indeq= Model.getIndividual(equipment);
			ArrayList<String> inpInt= HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, equipment, HomeController.NS+"componentOf", HomeController.NS+"Input_Interface");
			int i=0;
			InterfaceInput input;
			for (String string : inpInt) {
				Individual ind= Model.getIndividual(string);
				hashInputEquipment.put(ind.getLocalName(), indeq.getLocalName());
			}

		}
		for (String equipment: equipments) {
			ArrayList<String> outInt= HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, equipment, HomeController.NS+"componentOf", HomeController.NS+"Output_Interface");
			int i=0;
			Individual ind= Model.getIndividual(equipment);
			e = new Equipment(ind.getLocalName());
			for (String string : outInt) {
				individual= Model.getIndividual(string);
				InterfaceOutput outputInt = new InterfaceOutput();
				outputInt.setName(individual.getLocalName());
				e.addOut(outputInt);
				String inputcon= null;
				if(!HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, string, HomeController.NS+"interface_binds", HomeController.NS+"Input_Interface").isEmpty()){
					inputcon= HomeController.Search.GetInstancesOfTargetWithRelation(InfModel, string, HomeController.NS+"interface_binds", HomeController.NS+"Input_Interface").get(0);
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
		return equips;
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
		for (String ind : allIndividuals) {
			ArrayList<String> classesFromIndividual= HomeController.Search.GetClassesFrom(ind, InfModel);
			if((classesFromIndividual.contains("Interface") || classesFromIndividual.contains("Site") || classesFromIndividual.contains("Equipment"))){
				allIndividuals.remove(ind);
			}
		}
		setRelationsG800(allIndividuals);
		return allIndividuals;
	}
	public static ArrayList<String> getG800FromEquipment(String equipment){

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
			HashMap<String, String> value = values.get(key);
			OntClass ClassImage = Model.getOntClass(HomeController.NS+value.get("RP"));
			Individual rp = Model.createIndividual(HomeController.NS+a.getLocalName()+"rp"+b.getLocalName(),ClassImage);
			HomeController.Model=Model;
			Individual binding= Model.createIndividual(HomeController.NS+a.getLocalName()+"binding"+b.getLocalName(),Model.getResource(HomeController.NS+value.get("RP_BINDING")));
			ArrayList<Statement> stmts = new ArrayList<Statement>();
			stmts.add(HomeController.Model.createStatement(binding, Model.getProperty(HomeController.NS+HomeController.NS+value.get("RP_RELATION")), rp));
			stmts.add(HomeController.Model.createStatement(binding, Model.getProperty(HomeController.NS+HomeController.NS+value.get("RP_BINDING_REL_IN")), b));
			stmts.add(HomeController.Model.createStatement(binding, Model.getProperty(HomeController.NS+HomeController.NS+value.get("RP_BINDING_REL_OUT")), a));
			HomeController.Model.add(stmts);
		}catch(Exception e){
			e = new Exception("not bound");
		}
	}


}
