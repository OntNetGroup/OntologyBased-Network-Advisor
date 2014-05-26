package br.ufes.inf.nemo.padtec.processors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import br.ufes.inf.nemo.okco.business.Search;
import br.ufes.inf.nemo.padtec.Sindel2OWL;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Statement;

public class BindsProcessor {
	private static List<String>  transportFunctions =  Arrays.asList("tf","so-tf","sk-tf","bi-tf","af","so-af","sk-af","bi-af","lpf","so-lpf","sk-lpf","bi-lpf","matrix","uni-matrix","so-matrix","sk-matrix","bi-matrix","pm", "sn");
	private static List<String>  ports =  Arrays.asList("input","output");
	private static List<String>  interfaces =  Arrays.asList("in-int","out-int");

	private static HashMap<String, String> simpleBindsHash = new HashMap<>();

	private static OntModel model; 
	private static String ClassNS;
	private static String IndNS;

	//Common used variables
	private static ObjectProperty componentOf;
	private static Individual a,b,x,k,p,q;
	private static Statement stmt;
	private static ObjectProperty rel = null;
	private static String toA;
	private static String toB;
	
	private static ArrayList<ArrayList<Individual>> portsAndRPsToBindsSpecifically = new ArrayList<ArrayList<Individual>>();
	
	public static ArrayList<ArrayList<Individual>> getPortsAndRPsToBindsSpecifically() {
		return portsAndRPsToBindsSpecifically;
	}

	private static Individual createNewIndividual(String type){
		OntClass o_class = model.getOntClass(ClassNS+type);

		String indName = "_ind_"+Sindel2OWL.getCont();
		Individual aux = o_class.createIndividual(IndNS+indName);

		Sindel2OWL.warning += "\nNew individual "+indName+" created of type "+type;
		Sindel2OWL.hashIndividuals.put(indName, type);

		return aux;
	}

	//Simple
	public static void processBinds(OntModel model, String ClassNS, String IndNS, String binds){
		initializeHash();
		String[] declarations = binds.split(",");

		componentOf = model.getObjectProperty(ClassNS+"componentOf");
		BindsProcessor.model = model;
		BindsProcessor.ClassNS = ClassNS;
		BindsProcessor.IndNS = IndNS;

		for (String declaration : declarations) {
			String[] vars = declaration.split(":");

			boolean isTF1 = false, isTF2 = false;//for Transport Function
			boolean isP1 = false, isP2 = false;//for Port
			boolean isI1 = false, isI2 = false;//for Interface

			//Create the individual vars[0] and vars[1]
			if(vars.length == 2){
				a = model.getIndividual(IndNS+vars[0]);
				b = model.getIndividual(IndNS+vars[1]);

				toA = Sindel2OWL.hashIndividuals.get(vars[0]);
				toB = Sindel2OWL.hashIndividuals.get(vars[1]);
			}else{
				a = model.getIndividual(IndNS+vars[1]);
				b = model.getIndividual(IndNS+vars[2]);

				toA = Sindel2OWL.hashIndividuals.get(vars[1]);
				toB = Sindel2OWL.hashIndividuals.get(vars[2]);		
			}

			isTF1 = transportFunctions.contains(toA);
			isP1 = ports.contains(toA);
			isI1 = interfaces.contains(toA);

			isTF2 = transportFunctions.contains(toB);
			isP2 = ports.contains(toB);
			isI2 = interfaces.contains(toB);

			if(vars.length == 2){
				//SimpleRelation
				if(isTF1 && isTF2){
					//IF a and b are Transport Functions
					processSimpleRelation_TFxTF();
				}else if(isTF1 && isP2){
					//IF a is a Transport Function and b is a Port
					processSimpleRelation_TFxPort();
				}else if(isP1 && isTF2){
					//IF a is a Port and b is a Transport Function
					processSimpleRelation_PortxTF();
				}else if(isP1 && isP2){
					//IF a and b are Ports
					processSimpleRelation_PortxPort();
				}else if(isI1 && isI2){
					//IF a and b are Ports
					processSimpleRelation_InterfacexInterface();
				}else if(isI1 && isP2){
					//IF a and b are Ports
					processSimpleRelation_InterfacexPort();
				}
			}else{
				x = model.getIndividual(IndNS+vars[0]);
				//AssignableRelation
				if(isP1 && isP2){
					//IF a and b are Ports
					ArrayList<Individual> portsAndRp = new ArrayList<Individual>();
					portsAndRp.add(x);
					portsAndRp.add(a);
					portsAndRp.add(b);
					portsAndRPsToBindsSpecifically.add(portsAndRp);
					processAssignableRelation_PortxPort();					
				}else if(isTF1 && isTF2){
					//IF a and b are Transport Functions
					processAssignableRelation_TFxTF();
				}else if(isTF1 && isP2){
					//IF a is a Transport Function and b is a Port
					processAssignableRelation_TFxPort();
				}else if(isP1 && isTF2){
					//IF a is a Port and b is a Transport Function
					processAssignableRelation_PortxTF();
				}
			}
		}
	}

	private static void initializeHash() {
		simpleBindsHash.put("Adaptation_Source"+"Input", "Adaptation_Source_Input");
		simpleBindsHash.put("Adaptation_Source"+"Output", "Adaptation_Source_Output");
		simpleBindsHash.put("Adaptation_Sink"+"Input", "Adaptation_Sink_Input");
		simpleBindsHash.put("Adaptation_Sink"+"Output", "Adaptation_Sink_Output");
		simpleBindsHash.put("Termination_Source"+"Input", "Termination_Source_Input");
		simpleBindsHash.put("Termination_Source"+"Output", "Termination_Source_Output");
		simpleBindsHash.put("Termination_Sink"+"Input", "Termination_Sink_Input");
		simpleBindsHash.put("Termination_Sink"+"Output", "Termination_Sink_Output");
		simpleBindsHash.put("Layer_Processor_Function_Source"+"Input", "Layer_Processor_Source_Input");
		simpleBindsHash.put("Layer_Processor_Function_Source"+"Output", "Layer_Processor_Source_Output");
		simpleBindsHash.put("Layer_Processor_Function_Sink"+"Input", "Layer_Processor_Sink_Input");
		simpleBindsHash.put("Layer_Processor_Function_Sink"+"Output", "Layer_Processor_Sink_Output");
		simpleBindsHash.put("Matrix"+"Input", "Marix_Input");
		simpleBindsHash.put("Matrix"+"Output", "Marix_Output");
		simpleBindsHash.put("Subnetwork"+"Input", "Subnetwork_Input");
		simpleBindsHash.put("Subnetwork"+"Output", "Subnetwork_Output");
		simpleBindsHash.put("Physical_Media"+"Input", "Physical_Media_Input");
		simpleBindsHash.put("Physical_Media"+"Output", "Physical_Media_Output");
	}

	private static void processSimpleRelation_InterfacexPort() {
		// TODO VERIFICAR!!!

	}

	private static void processSimpleRelation_InterfacexInterface() {
		//TODO WARNING IN x OUT??
		rel = model.getObjectProperty(ClassNS+"interface_binds");
		stmt = model.createStatement(a, rel, b);
		model.add(stmt);
	}

	private static void processSimpleRelation_TFxTF(){
		rel = model.getObjectProperty(ClassNS+"tf_connection");
		stmt = model.createStatement(a, rel, b);
		model.add(stmt);
	}

	private static void processSimpleRelation_TFxPort(){
		//If port is Input, create a Output individual
		String toP = "";
		if(toB.equals("input")){
			toP = "Output";
		}else{//If port is Output, create a Input individual
			toP = "Input";
		}

		k = createNewIndividual(toP);					

		//TF{new Port}
		stmt = model.createStatement(a, componentOf, k);
		model.add(stmt);

		//k binds a
		rel = model.getObjectProperty(ClassNS+"binds");
		stmt = model.createStatement(k, rel, b);
		model.add(stmt);
	}

	private static void processSimpleRelation_PortxTF(){
		//If port is Input, create a Output individual
		String toP = "";
		if(toA.equals("input")){
			toP = "Output";
		}else{//If port is Output, create a Input individual
			toP = "Input";
		}

		k = createNewIndividual(toP);

		//TF{new Port}
		stmt = model.createStatement(b, componentOf, k);
		model.add(stmt);

		//		if(simpleBindsHash.containsKey(key))
		//		String specificRelation 
		//		
		//a binds new Port
		rel = model.getObjectProperty(ClassNS+"binds");
		stmt = model.createStatement(a, rel, k);
		model.add(stmt);
	}

	private static void processSimpleRelation_PortxPort(){
		rel = model.getObjectProperty(ClassNS+"binds");
		stmt = model.createStatement(a, rel, b);
		model.add(stmt);	
	}

	//Assignable
	private static void processAssignableRelation_TFxTF(){
		k = createNewIndividual("Binding");
		p = createNewIndividual("Binded_Input/Output");
		q = createNewIndividual("Binded_Input/Output");

		stmt = model.createStatement(a, componentOf, p);
		model.add(stmt);

		stmt = model.createStatement(b, componentOf, q);
		model.add(stmt);

		rel = model.getObjectProperty(ClassNS+"is_binding");
		stmt = model.createStatement(k, rel, p);
		model.add(stmt);

		stmt = model.createStatement(k, rel, q);
		model.add(stmt);

		rel = model.getObjectProperty(ClassNS+"binding_is_represented_by");
		stmt = model.createStatement(k, rel, x);
		model.add(stmt);
	}

	private static void processAssignableRelation_TFxPort(){
		k = createNewIndividual("Binding");
		q = createNewIndividual("Binded_Input/Output");

		stmt = model.createStatement(a, componentOf, q);
		model.add(stmt);

		rel = model.getObjectProperty(ClassNS+"is_binding");
		stmt = model.createStatement(k, rel, q);
		model.add(stmt);

		stmt = model.createStatement(k, rel, b);
		model.add(stmt);

		rel = model.getObjectProperty(ClassNS+"binding_is_represented_by");
		stmt = model.createStatement(k, rel, x);
		model.add(stmt);
	}

	private static void processAssignableRelation_PortxTF(){
		k = createNewIndividual("Binding");
		p = createNewIndividual("Binded_Input/Output");

		stmt = model.createStatement(b, componentOf, p);
		model.add(stmt);

		rel = model.getObjectProperty(ClassNS+"is_binding");
		stmt = model.createStatement(k, rel, a);
		model.add(stmt);

		stmt = model.createStatement(k, rel, p);
		model.add(stmt);

		rel = model.getObjectProperty(ClassNS+"binding_is_represented_by");
		stmt = model.createStatement(k, rel, x);
		model.add(stmt);
	}

	private static void processAssignableRelation_PortxPort(){
		k = createNewIndividual("Binding");

		rel = model.getObjectProperty(ClassNS+"is_binding");
		stmt = model.createStatement(k, rel, a);
		model.add(stmt);

		stmt = model.createStatement(k, rel, b);
		model.add(stmt);

		rel = model.getObjectProperty(ClassNS+"binding_is_represented_by");
		stmt = model.createStatement(k, rel, x);
		model.add(stmt);
		
		rel = model.getObjectProperty(ClassNS+"INV.binding_is_represented_by");
		stmt = model.createStatement(x, rel, k);
		model.add(stmt);
		
		//bindPorts(a, b, ClassNS, model);
		
	}
	
	public static void bindPorts(Individual rp, Individual port1, Individual port2, String NS, OntModel ontModel, ArrayList<String> listInstancesCreated){
		if(listInstancesCreated == null){
			listInstancesCreated = new ArrayList<String>();
		}
		initValues();
		Search search = new Search(NS); 
		ArrayList<String> tiposA=search.GetClassesFrom(NS+port1.getLocalName(),ontModel);
		ArrayList<String> tiposB=search.GetClassesFrom(NS+port2.getLocalName(),ontModel);
		tiposA.remove(NS+"Geographical_Element");
		tiposA.remove(NS+"Bound_Input-Output");
		tiposB.remove(NS+"Geographical_Element");
		tiposB.remove(NS+"Bound_Input-Output");
		ObjectProperty rel = ontModel.getObjectProperty(NS+"binds");
		Statement stmt = ontModel.createStatement(port1, rel, port2);
		ontModel.add(stmt);	
		HashMap<String, String> hash = new HashMap<String, String>();
		hash.put("INPUT", tiposB.get(0));
		hash.put("OUTPUT", tiposA.get(0));
		HashMap<String, String>element= values.get(hash);
		bindsSpecific(rp, port1,port2,tiposA.get(0),tiposB.get(0),ontModel,NS, listInstancesCreated);
	}
	
	public static OntModel bindsSpecific(Individual rp, Individual port1, Individual port2, String tipo_out, String tipo_inp, OntModel ontModel, String NS, ArrayList<String> listInstancesCreated) {
		if(listInstancesCreated == null){
			listInstancesCreated = new ArrayList<String>();
		}
		// TODO Auto-generated method stub
		HashMap<String, String> key = new HashMap<String, String>();
		tipo_inp = tipo_inp.replace(NS, "");
		tipo_out = tipo_out.replace(NS, "");
		key.put("INPUT", tipo_inp);
		key.put("OUTPUT", tipo_out);
		try{
			HashMap<String, String> value = values.get(key);
			OntClass ClassImage = ontModel.getOntClass(NS+value.get("RP"));
			if(rp == null){
				rp = ontModel.createIndividual(NS+port1.getLocalName()+"rp"+port2.getLocalName(),ClassImage);
			}
			//Individual rp = ontModel.createIndividual(NS+port1.getLocalName()+"rp"+port2.getLocalName(),ClassImage);
			//HomeController.Model=Model;
			Individual binding= ontModel.createIndividual(NS+port1.getLocalName()+"binding"+port2.getLocalName(),ontModel.getResource(NS+value.get("RP_BINDING")));
			ArrayList<Statement> stmts = new ArrayList<Statement>();
			stmts.add(ontModel.createStatement(binding, ontModel.getProperty(NS+value.get("RP_RELATION")), rp));
			stmts.add(ontModel.createStatement(binding, ontModel.getProperty(NS+value.get("RP_BINDING_REL_IN")), port2));
			stmts.add(ontModel.createStatement(binding, ontModel.getProperty(NS+value.get("RP_BINDING_REL_OUT")), port1));
			ontModel.add(stmts);
			
			listInstancesCreated.add(binding.getNameSpace()+binding.getLocalName());
		}catch(Exception e){
			e = new Exception("not bound");
		}
		
		return ontModel;
	}
	
	public static HashMap<HashMap<String, String>, HashMap<String,String>> values = new HashMap<HashMap<String,String>, HashMap<String,String>>();
	
	public static void initValues(){
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
		tf1.put("INPUT", "Physical_Media_Input");
		tf1.put("OUTPUT", "Termination_Source_Output");
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
		tf1.put("INPUT", "Adaptation_Sink_Output");
		tf1.put("OUTPUT", "Adaptation_Source_Input");
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
}
