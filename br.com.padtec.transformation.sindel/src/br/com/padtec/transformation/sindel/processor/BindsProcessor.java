package br.com.padtec.transformation.sindel.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.trasnformation.sindel.Sindel2OWL;

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
	private static ObjectProperty invComponentOf;
	private static Individual a,b,x,k,p,q;
	private static Statement stmt;
	private static ObjectProperty rel = null;
	private static String toA;
	private static String toB;
	
	private static ArrayList<ArrayList<Individual>> portsAndRPsToBindsSpecifically = new ArrayList<ArrayList<Individual>>();
	
	public static ArrayList<ArrayList<Individual>> getPortsAndRPsToBindsSpecifically() {
		return portsAndRPsToBindsSpecifically;
	}
	
	public static void resetPortsAndRPsToBindsSpecifically(){
		portsAndRPsToBindsSpecifically = new ArrayList<ArrayList<Individual>>();
	}
	
	public static void addPortsAndRPsToBindsSpecifically(Individual port1, Individual port2, Individual rp, Individual binding){
		ArrayList<Individual> portsAndRp = new ArrayList<Individual>();
		portsAndRp.add(port1);
		portsAndRp.add(port2);
		portsAndRp.add(rp);
		portsAndRp.add(binding);
		
		if(!portsAndRPsToBindsSpecifically.contains(portsAndRp)){
			Boolean found = false;
			for (ArrayList<Individual> portsAndRpAux : portsAndRPsToBindsSpecifically) {
				if(portsAndRp.get(0).toString().equals(portsAndRpAux.get(0).toString()) && portsAndRp.get(1).toString().equals(portsAndRpAux.get(1).toString()) && portsAndRpAux.get(2) == null && portsAndRpAux.get(3) == null){
					portsAndRPsToBindsSpecifically.remove(portsAndRpAux);
					portsAndRPsToBindsSpecifically.add(portsAndRp);
					found = true;
					break;
				}else if(portsAndRp.get(0).toString().equals(portsAndRpAux.get(0).toString()) && portsAndRp.get(1).toString().equals(portsAndRpAux.get(1).toString()) && portsAndRpAux.get(2).equals(portsAndRpAux.get(2).toString()) && portsAndRpAux.get(3).equals(portsAndRpAux.get(3).toString())){
					found = true;
					break;
				}
				//portsAndRPsToBindsSpecifically.add(portsAndRp);
			}
			if(!found){
				portsAndRPsToBindsSpecifically.add(portsAndRp);
			}
		}
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
		invComponentOf = model.getObjectProperty(ClassNS+"INV.componentOf");
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
					addPortsAndRPsToBindsSpecifically(a, b, null, null);
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
					processAssignableRelation_PortxPort();
					@SuppressWarnings("unused")
					ArrayList<Individual> portsAndRp = new ArrayList<Individual>();
					addPortsAndRPsToBindsSpecifically(a, b, x, k);
					
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
		
		rel = model.getObjectProperty(ClassNS+"INV.interface_binds");
		stmt = model.createStatement(b, rel, a);
		model.add(stmt);
	}

	private static void processSimpleRelation_TFxTF(){
		rel = model.getObjectProperty(ClassNS+"tf_connection");
		stmt = model.createStatement(a, rel, b);
		model.add(stmt);
		
		rel = model.getObjectProperty(ClassNS+"INV.tf_connection");
		stmt = model.createStatement(b, rel, a);
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
		
		stmt = model.createStatement(k, invComponentOf, b);
		model.add(stmt);

		//k binds a
		rel = model.getObjectProperty(ClassNS+"binds");
		stmt = model.createStatement(k, rel, b);
		model.add(stmt);
		
		rel = model.getObjectProperty(ClassNS+"INV.binds");
		stmt = model.createStatement(b, rel, k);
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
		
		stmt = model.createStatement(k, componentOf, b);
		model.add(stmt);

		//		if(simpleBindsHash.containsKey(key))
		//		String specificRelation 
		//		
		//a binds new Port
		rel = model.getObjectProperty(ClassNS+"binds");
		stmt = model.createStatement(a, rel, k);
		model.add(stmt);
		
		rel = model.getObjectProperty(ClassNS+"INV.binds");
		stmt = model.createStatement(k, rel, a);
		model.add(stmt);
	}

	private static void processSimpleRelation_PortxPort(){
		rel = model.getObjectProperty(ClassNS+"binds");
		stmt = model.createStatement(a, rel, b);
		model.add(stmt);
		
		rel = model.getObjectProperty(ClassNS+"INV.binds");
		stmt = model.createStatement(b, rel, a);
		model.add(stmt);
	}

	//Assignable
	private static void processAssignableRelation_TFxTF(){
		k = createNewIndividual("Binding");
		p = createNewIndividual("Binded_Input/Output");
		q = createNewIndividual("Binded_Input/Output");

		stmt = model.createStatement(a, componentOf, p);
		model.add(stmt);
		
		stmt = model.createStatement(p, invComponentOf, a);
		model.add(stmt);

		stmt = model.createStatement(b, componentOf, q);
		model.add(stmt);
		
		stmt = model.createStatement(q, invComponentOf, b);
		model.add(stmt);

		rel = model.getObjectProperty(ClassNS+"is_binding");
		stmt = model.createStatement(k, rel, p);
		model.add(stmt);
		
		rel = model.getObjectProperty(ClassNS+"INV.is_binding");
		stmt = model.createStatement(p, rel, k);
		model.add(stmt);

		rel = model.getObjectProperty(ClassNS+"is_binding");
		stmt = model.createStatement(k, rel, q);
		model.add(stmt);
		
		rel = model.getObjectProperty(ClassNS+"INV.is_binding");
		stmt = model.createStatement(q, rel, k);
		model.add(stmt);

		rel = model.getObjectProperty(ClassNS+"binding_is_represented_by");
		stmt = model.createStatement(k, rel, x);
		model.add(stmt);
		
		rel = model.getObjectProperty(ClassNS+"INV.binding_is_represented_by");
		stmt = model.createStatement(x, rel, k);
		model.add(stmt);
	}

	private static void processAssignableRelation_TFxPort(){
		k = createNewIndividual("Binding");
		q = createNewIndividual("Binded_Input/Output");

		stmt = model.createStatement(a, componentOf, q);
		model.add(stmt);

		stmt = model.createStatement(q, invComponentOf, a);
		model.add(stmt);
		
		rel = model.getObjectProperty(ClassNS+"is_binding");
		stmt = model.createStatement(k, rel, q);
		model.add(stmt);

		rel = model.getObjectProperty(ClassNS+"INV.is_binding");
		stmt = model.createStatement(q, rel, k);
		model.add(stmt);
		
		rel = model.getObjectProperty(ClassNS+"is_binding");
		stmt = model.createStatement(k, rel, b);
		model.add(stmt);
		
		rel = model.getObjectProperty(ClassNS+"INV.is_binding");
		stmt = model.createStatement(b, rel, k);
		model.add(stmt);

		rel = model.getObjectProperty(ClassNS+"binding_is_represented_by");
		stmt = model.createStatement(k, rel, x);
		model.add(stmt);
		
		rel = model.getObjectProperty(ClassNS+"INV.binding_is_represented_by");
		stmt = model.createStatement(x, rel, k);
		model.add(stmt);
	}

	private static void processAssignableRelation_PortxTF(){
		k = createNewIndividual("Binding");
		p = createNewIndividual("Binded_Input/Output");

		stmt = model.createStatement(b, componentOf, p);
		model.add(stmt);
		
		stmt = model.createStatement(p, invComponentOf, b);
		model.add(stmt);

		rel = model.getObjectProperty(ClassNS+"is_binding");
		stmt = model.createStatement(k, rel, a);
		model.add(stmt);
		
		rel = model.getObjectProperty(ClassNS+"INV.is_binding");
		stmt = model.createStatement(a, rel, k);
		model.add(stmt);

		
		rel = model.getObjectProperty(ClassNS+"is_binding");
		stmt = model.createStatement(k, rel, p);
		model.add(stmt);
		
		rel = model.getObjectProperty(ClassNS+"INV.is_binding");
		stmt = model.createStatement(p, rel, k);
		model.add(stmt);

		rel = model.getObjectProperty(ClassNS+"binding_is_represented_by");
		stmt = model.createStatement(k, rel, x);
		model.add(stmt);
		
		rel = model.getObjectProperty(ClassNS+"INV.binding_is_represented_by");
		stmt = model.createStatement(x, rel, k);
		model.add(stmt);
	}

	private static void processAssignableRelation_PortxPort(){
		k = createNewIndividual("Binding");
		
		rel = model.getObjectProperty(ClassNS+"is_binding");
		stmt = model.createStatement(k, rel, a);
		model.add(stmt);
		
		rel = model.getObjectProperty(ClassNS+"INV.is_binding");
		stmt = model.createStatement(a, rel, k);
		model.add(stmt);

		rel = model.getObjectProperty(ClassNS+"is_binding");
		stmt = model.createStatement(k, rel, b);
		model.add(stmt);
		
		rel = model.getObjectProperty(ClassNS+"INV.is_binding");
		stmt = model.createStatement(b, rel, k);
		model.add(stmt);

		rel = model.getObjectProperty(ClassNS+"binding_is_represented_by");
		stmt = model.createStatement(k, rel, x);
		model.add(stmt);
		
		rel = model.getObjectProperty(ClassNS+"INV.binding_is_represented_by");
		stmt = model.createStatement(x, rel, k);
		model.add(stmt);
		
		//bindPorts(a, b, ClassNS, model);
		
	}
	
	@SuppressWarnings("unused")
	public static void bindPorts(Individual rp, Individual port1, Individual port2, Individual binding, String NS, OntModel ontModel, ArrayList<String> listInstancesCreated){
		if(listInstancesCreated == null){
			listInstancesCreated = new ArrayList<String>();
		}
		//initValues();
		List<String> tiposPort1=QueryUtil.getClassesURI(ontModel,NS+port1.getLocalName());
		List<String> tiposPort2=QueryUtil.getClassesURI(ontModel,NS+port2.getLocalName());
		tiposPort1.remove(NS+"Geographical_Element");
		tiposPort1.remove(NS+"Bound_Input-Output");
		tiposPort2.remove(NS+"Geographical_Element");
		tiposPort2.remove(NS+"Bound_Input-Output");
		
		Individual outputPort;
		Individual inputPort;
		List<String> tiposOutputPort;
		List<String> tiposInputPort;
		if(tiposPort1.contains(NS+"Output")){
			outputPort = port1;
			tiposOutputPort = tiposPort1;
			inputPort = port2;
			tiposInputPort = tiposPort2;
		}else{
			outputPort = port2;
			tiposOutputPort = tiposPort2;
			inputPort = port1;
			tiposInputPort = tiposPort1;
		}
		ObjectProperty rel = ontModel.getObjectProperty(NS+"binds");
		Statement stmt = ontModel.createStatement(outputPort, rel, inputPort);
		ontModel.add(stmt);	
		HashMap<String, String> hash = new HashMap<String, String>();
		hash.put("OUTPUT", tiposOutputPort.get(0).replace(NS, ""));
		hash.put("INPUT", tiposInputPort.get(0).replace(NS, ""));
		HashMap<String, String>element= allowedRelationsHash.get(hash);
		bindsSpecific(rp, outputPort,inputPort,binding,tiposOutputPort.get(0),tiposInputPort.get(0),ontModel,NS, listInstancesCreated);
	}
	
	public static OntModel bindsSpecific(Individual rp, Individual outputPort, Individual inputPort, Individual binding, String tipo_out, String tipo_inp, OntModel ontModel, String NS, ArrayList<String> listInstancesCreated) {
		if(listInstancesCreated == null){
			listInstancesCreated = new ArrayList<String>();
		}

		HashMap<String, String> key = new HashMap<String, String>();
		tipo_inp = tipo_inp.replace(NS, "");
		tipo_out = tipo_out.replace(NS, "");
		key.put("INPUT", tipo_inp);
		key.put("OUTPUT", tipo_out);
		try{
			HashMap<String, String> value = allowedRelationsHash.get(key);
			OntClass ClassImage = ontModel.getOntClass(NS+value.get("RP"));
			if(rp == null){
				rp = ontModel.createIndividual(NS+outputPort.getLocalName()+"rp"+inputPort.getLocalName(),ClassImage);
			}
			//Individual rp = ontModel.createIndividual(NS+port1.getLocalName()+"rp"+port2.getLocalName(),ClassImage);
			//HomeController.Model=Model;
			//Individual binding = ontModel.createIndividual(NS+outputPort.getLocalName()+"_binding_"+inputPort.getLocalName(),ontModel.getResource(NS+value.get("RP_BINDING")));
			if(binding == null){
				binding = ontModel.createIndividual(NS+outputPort.getLocalName()+"_binding_"+inputPort.getLocalName(),ontModel.getResource(NS+value.get("RP_BINDING")));
			}
			ArrayList<Statement> stmts = new ArrayList<Statement>();
			stmts.add(ontModel.createStatement(binding, ontModel.getProperty(NS+value.get("RP_RELATION")), rp));
			stmts.add(ontModel.createStatement(binding, ontModel.getProperty(NS+value.get("RP_BINDING_REL_IN")), inputPort));
			stmts.add(ontModel.createStatement(binding, ontModel.getProperty(NS+value.get("RP_BINDING_REL_OUT")), outputPort));
			ontModel.add(stmts);
			
			listInstancesCreated.add(binding.getNameSpace()+binding.getLocalName());
		}catch(Exception e){
			e = new Exception("not bound");
		}
		
		return ontModel;
	}
	
	public static HashMap<HashMap<String, String>, HashMap<String,String>> allowedRelationsHash = initAllowedRelationsHash();
	
	private static HashMap<HashMap<String, String>, HashMap<String, String>> initAllowedRelationsHash(){
		HashMap<HashMap<String, String>, HashMap<String, String>> newAllowedRelationsHash = new HashMap<HashMap<String,String>, HashMap<String,String>>();
		
		HashMap<String, String> tf1= new HashMap<String, String>();
		HashMap<String, String> hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation_Source_Input");//the input port
		tf1.put("OUTPUT", "Termination_Source_Output");//the output port
		hashrp.put("RP", "Source_A-FEP");//the RP created
		hashrp.put("RP_RELATION", "is_represented_by_So_A-FEP");//the relation between RP and binding
		hashrp.put("RP_BINDING", "Source_A-FEP_Binding");//binding type
		hashrp.put("RP_BINDING_REL_IN", "binds_So_A-FEP_to");//relation bind and input
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_A-FEP_from");//relation bind and output		
		newAllowedRelationsHash.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Matrix_Input");
		tf1.put("OUTPUT", "Termination_Source_Output");
		hashrp.put("RP", "Source_M-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_So_M-FEP");
		hashrp.put("RP_BINDING", "Source-M-FEP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_So_M-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_M-FEP-from");	
		newAllowedRelationsHash.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Subnetwork_Input");
		tf1.put("OUTPUT", "Termination_Source_Output");
		hashrp.put("RP", "Source_SN-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_So_SN-FEP");
		hashrp.put("RP_BINDING", "Source-SN-FEP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_So_SN-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_SN-FEP_from");	
		newAllowedRelationsHash.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Layer_Processor_Source_Input");
		tf1.put("OUTPUT", "Termination_Source_Output");
		hashrp.put("RP", "Source_LPF-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_So_LPF-FEP");
		hashrp.put("RP_BINDING", "Source_LPF-FEP Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_So_LPF-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_LPF-FEP-from");	
		newAllowedRelationsHash.put(tf1, hashrp);


		tf1.put("INPUT", "Termination_Sink_Input");
		tf1.put("OUTPUT", "Adaptation_Sink_Output");
		hashrp.put("RP", "Sink_A-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_A-FEP");
		hashrp.put("RP_BINDING", "Sink_A-FEP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_A-FEP_from");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_A-FEP_to");		
		newAllowedRelationsHash.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Matrix_Input");
		tf1.put("OUTPUT", "Termination_Sink_Output");
		hashrp.put("RP", "Sink_M-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_M-FEP");
		hashrp.put("RP_BINDING", "Sink_M-FEP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_M-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_M-FEP_from");	
		newAllowedRelationsHash.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Subnetwork_Input");
		tf1.put("OUTPUT", "Termination_Sink_Output");
		hashrp.put("RP", "Sink_SN-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_SN-FEP");
		hashrp.put("RP_BINDING", "Sink_SN-FEP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_SN-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_SN-FEP_from");	
		newAllowedRelationsHash.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Layer_Processor_Sink_Input");
		tf1.put("OUTPUT", "Termination_Sink_Output");
		hashrp.put("RP", "Sink_LPF-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_LPF-FEP");
		hashrp.put("RP_BINDING", "Sink_LPF-FEP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_LPF-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_LPF-FEP_from");	
		newAllowedRelationsHash.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Physical_Media_Input");
		tf1.put("OUTPUT", "Termination_Source_Output");
		hashrp.put("RP", "Source_PM-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_So_PM-FEP");
		hashrp.put("RP_BINDING", "Source_PM-FEP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_So_PM-FEP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_PM-FEP_from");	
		newAllowedRelationsHash.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Termination_Sink_Input");
		tf1.put("OUTPUT", "Physical_Media_Output");
		hashrp.put("RP", "Sink_PM-FEP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_PM-FEP");
		hashrp.put("RP_BINDING", "Sink_PM-FEP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_PM-FEP_from");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_PM-FEP_to");	
		newAllowedRelationsHash.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Termination_Source_Input");
		tf1.put("OUTPUT", "Adaptation_Source_Output");
		hashrp.put("RP", "Source_AP");
		hashrp.put("RP_RELATION", "is_represented_by_So_AP");
		hashrp.put("RP_BINDING", "Source_AP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_So_AP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_AP_from");	
		newAllowedRelationsHash.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation_Sink_Input");
		tf1.put("OUTPUT", "Termination_Sink_Output");
		hashrp.put("RP", "Sink_AP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_AP");
		hashrp.put("RP_BINDING", "Sink_AP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_AP_from");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_AP_to");	
		newAllowedRelationsHash.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation_Source_Input");
		tf1.put("OUTPUT", "Layer_Processor_Source_Output");
		hashrp.put("RP", "Source_LP-FP");
		hashrp.put("RP_RELATION", "is_represented_by_So_L-FP");
		hashrp.put("RP_BINDING", "Source_L-FP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_So_L-FP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_L-FP_from");	
		newAllowedRelationsHash.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation_Source_Input");
		tf1.put("OUTPUT", "Layer_Processor_Sink_Output");
		hashrp.put("RP", "Sink_LP-FP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_L-FP");
		hashrp.put("RP_BINDING", "Sink_L-FP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_L-FP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_L-FP_from");	
		newAllowedRelationsHash.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation_Sink_Output");
		tf1.put("OUTPUT", "Adaptation_Source_Input");
		hashrp.put("RP", "Unidirectional_A-FP");
		hashrp.put("RP_RELATION", "is_represented_by_A-FP");
		hashrp.put("RP_BINDING", "A-FP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_A-FP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_A-FP_from");	
		newAllowedRelationsHash.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation_Source_Input");
		tf1.put("OUTPUT", "Subnetwork_Output");
		hashrp.put("RP", "Source_SN-FP");
		hashrp.put("RP_RELATION", "is_represented_by_So_SN-FP");
		hashrp.put("RP_BINDING", "Source_SN-FP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_So_SN-FP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_SN-FP_from");	
		newAllowedRelationsHash.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation_Sink_Input");
		tf1.put("OUTPUT", "Subnetwork_Output");
		hashrp.put("RP", "Source_SN-FP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_SN-FP");
		hashrp.put("RP_BINDING", "Sink_SN-FP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_SN-FP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_SN-FP_from");	
		newAllowedRelationsHash.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation_Source_Input");
		tf1.put("OUTPUT", "Matrix_Output");
		hashrp.put("RP", "Source_SN-FP");
		hashrp.put("RP_RELATION", "is_represented_by_So_M-FP");
		hashrp.put("RP_BINDING", "Source_M-FP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_So_M-FP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_So_M-FP_from");	
		newAllowedRelationsHash.put(tf1, hashrp);

		tf1= new HashMap<String, String>();
		hashrp= new HashMap<String, String>();
		tf1.put("INPUT", "Adaptation_Sink_Input");
		tf1.put("OUTPUT", "Matrix_Output");
		hashrp.put("RP", "Source_M-FP");
		hashrp.put("RP_RELATION", "is_represented_by_Sk_M-FP");
		hashrp.put("RP_BINDING", "Sink_M-FP_Binding");
		hashrp.put("RP_BINDING_REL_IN", "binds_Sk_M-FP_to");
		hashrp.put("RP_BINDING_REL_OUT", "binds_Sk_M-FP_from");	
		newAllowedRelationsHash.put(tf1, hashrp);
		
		return newAllowedRelationsHash;
	}
}
