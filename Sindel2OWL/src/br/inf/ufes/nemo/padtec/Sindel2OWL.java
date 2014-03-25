package br.inf.ufes.nemo.padtec;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.ontology.UnionClass;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class Sindel2OWL {
	public  int cont = 0;
	public  OntModel model; 

	public  String nsG805;

	public  String nsIndv = new String("http://www.semanticweb.org/individuous.owl#");
	public  HashMap<String,String> hashIndividuals = new HashMap<String,String>();

	public  Model modelg805;

	public  String warning = "";
	public  String error = "";


	public static void main(String[] args) {
		String s = "element#input:in-ODUj-Client_AF4,in-ODUj-Client_AF3,in-ODUj-Client_AF2,in-ODUj-Client_AF1,in4-ODUk-ODUj_AF1,in3-ODUk-ODUj_AF1,in2-ODUk-ODUj_AF1,in1-ODUk-ODUj_AF1,in-OTU-ODUk_AF1;af:ODUj-Client_AF4,ODUj-Client_AF3,ODUj-Client_AF2,ODUj-Client_AF1,ODUk-ODUj_AF1,OTU-ODUk_AF1;ttf:ODUj_TTF4,ODUj_TTF3,ODUj_TTF2,ODUj_TTF1,ODUk_TTF1,OTU_TTF1;output:out-ODUj_TTF4,out-ODUj_TTF3,out-ODUj_TTF2,out-ODUj_TTF1,out-ODUk_TTF1,out-OTU_TTF1;sn:ODU_SN;!composition#ODUj-Client_AF1:in-ODUj-Client_AF1;ODUj-Client_AF2:in-ODUj-Client_AF2;ODUj-Client_AF3:in-ODUj-Client_AF3;ODUj-Client_AF4:in-ODUj-Client_AF4;ODUj_TTF1:out-ODUj_TTF1;ODUj_TTF2:out-ODUj_TTF2;ODUj_TTF3:out-ODUj_TTF3;ODUj_TTF4:out-ODUj_TTF4;ODUk-ODUj_AF1:in1-ODUk-ODUj_AF1,in2-ODUk-ODUj_AF1,in3-ODUk-ODUj_AF1,in4-ODUk-ODUj_AF1;ODUk_TTF1:out-ODUk_TTF1;OTU-ODUk_AF1:in-OTU-ODUk_AF1;OTU_TTF1:out-OTU_TTF1;!connects#!binds#0:ODUj-Client_AF1,ODUj_TTF1;1:ODUj-Client_AF2,ODUj_TTF2;2:ODUj-Client_AF3,ODUj_TTF3;3:ODUj-Client_AF4,ODUj_TTF4;4:out-ODUj_TTF1,ODU_SN;5:out-ODUj_TTF2,ODU_SN;6:out-ODUj_TTF3,ODU_SN;7:out-ODUj_TTF4,ODU_SN;8:ODUk-ODUj_AF1,ODUk_TTF1;9:ODU_SN,in1-ODUk-ODUj_AF1;10:ODU_SN,in2-ODUk-ODUj_AF1;11:ODU_SN,in3-ODUk-ODUj_AF1;12:ODU_SN,in4-ODUk-ODUj_AF1;13:OTU-ODUk_AF1,OTU_TTF1;14:out-ODUk_TTF1,in-OTU-ODUk_AF1;!client#!tf_location_str#!tf_location_geo#!ttf_type#!";
		try{
			Sindel2OWL so = new Sindel2OWL();
			so.modelg805 = FileManager.get().loadModel("g805.owl");

			so.run(s, so.modelg805);

			System.out.println("foi");
			File arquivo;   
			arquivo = new File("arquivoSaida.owl");  // Chamou e nomeou o arquivo txt.  
			try{
				FileOutputStream fos = new FileOutputStream(arquivo);  // Perceba que estamos instanciando uma classe aqui. A FileOutputStream. Pesquise sobre ela!  
				so.model.write(fos, "RDF/XML");
				fos.close();  // Fecha o arquivo. Nunca esquecer disso.  
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private  void finish() {
		model.remove(modelg805);

		OntModel g805 = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

		Ontology ontModel = model.createOntology(nsIndv);
		Ontology ontg805 = g805.createOntology(nsG805.replaceAll("#", ""));

		ontModel.addImport(ontg805);
	}

	private  void initialize(Model m805) {

		//lendo a g805
		modelg805 = m805;
		//criando o namespace
		nsG805 = modelg805.getNsPrefixURI("");

		model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		model.add(modelg805);

	}
	
	public  DtoResultSindel run(String s, Model mG805){

		initialize(mG805);

		String[] s_parts;
		s_parts = s.split("!");
		for (String s_elements : s_parts) {
			String[] element = s_elements.split("#");
			if(element.length == 2){
				if(element[0].equalsIgnoreCase("element")){
					processElement(element[1]);
				}else if(element[0].equalsIgnoreCase("composition")){
					processComposition(element[1]);
				}else if(element[0].equalsIgnoreCase("binds")){
					processBinds(element[1]);
				}else if(element[0].equalsIgnoreCase("connects")){
					processConnects(element[1]);
				}else if(element[0].equalsIgnoreCase("client")){
					processClient(element[1]);
				}else if(element[0].equalsIgnoreCase("tf_location_str")){
					processTF_LocationStr(element[1]);
				}else if(element[0].equalsIgnoreCase("tf_location_geo")){
					processTF_LocationGeo(element[1]);
				}else if(element[0].equalsIgnoreCase("ttf_type")){
					processTTF_Type(element[1]);
				}
			}
		}
		
		//Make all individuals differents
		ExtendedIterator<Individual> individuals = model.listIndividuals();
		ArrayList<Resource> lst = new ArrayList<Resource>();

		while (individuals.hasNext()) {
			Resource individual = (Resource) individuals.next();
			if (individual.getNameSpace().equals(nsIndv)){
				lst.add(individual);
			}
		}
		model.createAllDifferent(model.createList(lst.toArray(new RDFNode[]{})));
		
		finish();

		DtoResultSindel ret = new DtoResultSindel();
		ret.warning = warning;
		ret.error = error;
		ret.model = model;
		
		return ret;
	}

	private  void processTTF_Type(String types) {
		Individual x;
		OntProperty y;
		Literal value;
		String stringURI = "http://www.w3.org/2001/XMLSchema#string";

		String[] lin = types.split(";");
		for (String s : lin) {
			String[] vars = s.split(":");
			x  = model.getIndividual(nsIndv+vars[0]);
			y = model.getDatatypeProperty(nsG805+"Trail_Termination_Function.type");
			value = model.createTypedLiteral(vars[1],stringURI);
			x.addProperty(y, value);		    
		}
	}

	private  void processTF_LocationGeo(String locations) {
		Individual x;
		OntProperty dp;
		Literal value;
		String intURI = "http://www.w3.org/2001/XMLSchema#integer";

		String[] lin = locations.split(";");
		for (String s : lin) {
			String[] geos = s.split(":");
			if(hashIndividuals.get(geos[0]).equalsIgnoreCase("pm") || hashIndividuals.get(geos[0]).equalsIgnoreCase("sn")){
				//Exception
				error +="\nLocation attribution aborted: Subnetworks and Physical Medias do not have location;";
			}else{
				x  = model.getIndividual(nsIndv+geos[0]);
				String [] vars = geos[1].split("\\+");
				String [] params = vars[0].split("\\*");

				dp = model.getDatatypeProperty(nsG805+"Defined_Geographical_Element.latitude.degree");
				value = model.createTypedLiteral(params[0],intURI);
				x.addProperty(dp, value);

				dp = model.getDatatypeProperty(nsG805+"Defined_Geographical_Element.latitude.minute");
				value = model.createTypedLiteral(params[1],intURI);
				x.addProperty(dp, value);

				dp = model.getDatatypeProperty(nsG805+"Defined_Geographical_Element.latitude.second");
				value = model.createTypedLiteral(params[2],intURI);
				x.addProperty(dp, value);

				params = vars[1].split("\\*");

				dp = model.getDatatypeProperty(nsG805+"Defined_Geographical_Element.longitude.degree");
				value = model.createTypedLiteral(params[0],intURI);
				x.addProperty(dp, value);

				dp = model.getDatatypeProperty(nsG805+"Defined_Geographical_Element.longitude.minute");
				value = model.createTypedLiteral(params[1],intURI);
				x.addProperty(dp, value);

				dp = model.getDatatypeProperty(nsG805+"Defined_Geographical_Element.longitude.second");
				value = model.createTypedLiteral(params[2],intURI);
				x.addProperty(dp, value);
			}
		}
	}

	private  void processTF_LocationStr(String locations) {
		Individual x;
		OntProperty dp;
		Literal value;
		String stringURI = "http://www.w3.org/2001/XMLSchema#string";

		String[] lin = locations.split(";");
		for (String s : lin) {
			String[] vars = s.split(":");
			if(hashIndividuals.get(vars[0]).equalsIgnoreCase("pm") || hashIndividuals.get(vars[0]).equalsIgnoreCase("sn")){
				//Excpetion
				error += "\nLocation attribution aborted: Subnetworks and Physical Medias do not have location;";
			}else{
				x  = model.getIndividual(nsIndv+vars[0]);
				dp = model.getDatatypeProperty(nsG805+"Geographical_Element_With_Alias.location");
				value = model.createTypedLiteral(vars[1],stringURI);
				x.addProperty(dp, value);
			}
		}
	}

	private  void processClient(String clients) {
		//Variables used in nexts contexts
		Statement stmt;
		ObjectProperty rel;
		Individual a,b;

		String[] lin = clients.split(";");
		for (String s : lin) {
			String[] bind = s.split(":");
			String[] vars = bind[1].split(",");
			a = model.getIndividual(nsIndv+vars[0]);
			b = model.getIndividual(nsIndv+vars[1]);

			rel = model.getObjectProperty(nsG805+"client-server_layer_relationship");
			stmt = model.createStatement(a, rel, b);
			model.add(stmt);
		}

	}
	
	private  void processBinds(String binds) {
		String[] tranFuncs = {"ttf","so-ttf","sk-ttf","bi-tff","af","so-af","sk-af","bi-af","matrix","uni-matrix","so-matrix","sk-matrix","bi-matrix","pm", "sn"};

		String[] lin = binds.split(";");

		//is a common relation used in many parts of the transformation
		ObjectProperty componentOf = model.getObjectProperty(nsG805+"componentOf");

		//Create the individual vars[0] and vars[1]
		Individual a,b,x,k;

		//Variables used in nexts contexts
		Statement stmt;
		ObjectProperty rel = null;

		for (String s : lin) {
			String[] bind = s.split(":");
			String[] vars = bind[1].split(",");

			boolean isTF1 = false, isTF2 = false;//for Transport Function
			boolean isP1 = false, isP2 = false;//for Port

			//Create the individual vars[0] and vars[1]
			a = model.getIndividual(nsIndv+vars[0]);
			b = model.getIndividual(nsIndv+vars[1]);

			String toA = hashIndividuals.get(vars[0]);
			String toB = hashIndividuals.get(vars[1]);

			for (String tf : tranFuncs) {
				if(tf.equals(toA)){
					isTF1 = true;
				}else if(toA.equals("input")){
					isP1 = true;
				}else if(toA.equals("output")){
					isP1 = true;
				}
				if(tf.equals(toB)){
					isTF2 = true;
				}else if(toB.equals("input")){
					isP2 = true;
				}else if(toB.equals("output")){
					isP2 = true;
				}
				//OTIMIZATION if the types of vars[0] and vars[1] was discovered, break
				if((isTF1 || isP1) && (isTF2 || isP2)){
					break;
				}
			}

			if(vars.length == 2){
				//SimpleRelation
				if(isTF1 && isTF2){
					//IF a and b are Transport Functions
					rel = model.getObjectProperty(nsG805+"tf_connection");
					stmt = model.createStatement(a, rel, b);
					model.add(stmt);
				}else if(isTF1 && isP2){
					//IF a is a Transport Function and b is a Port
					//If port is Input, create a Output individual
					String toP = "";
					if(toB.equals("input")){
						toP = "Output";
					}else{//If port is Output, create a Input individual
						toP = "Input";
					}

					OntClass o_class = model.getOntClass(nsG805+toP);

					String indName = "_ind_"+getCont();
					k = o_class.createIndividual(nsIndv+indName);					
					warning += "\nNew individual "+indName+" created of type "+toP;
					hashIndividuals.put(indName, toP);

					stmt = model.createStatement(a, componentOf, k);
					model.add(stmt);

					rel = model.getObjectProperty(nsG805+"binds");
					stmt = model.createStatement(k, rel, b);
					model.add(stmt);	
				}else if(isP1 && isTF2){
					//IF a is a Port and b is a Transport Function
					//If port is Input, create a Output individual
					String toP = "";
					if(toA.equals("input")){
						toP = "Output";
					}else{//If port is Output, create a Input individual
						toP = "Input";
					}

					OntClass o_class = model.getOntClass(nsG805+toP);

					String indName = "_ind_"+getCont();
					k = o_class.createIndividual(nsIndv+indName);					
					warning += "\nNew individual "+indName+" created of type "+toP;
					hashIndividuals.put(indName, toP);

					stmt = model.createStatement(b, componentOf, k);
					model.add(stmt);

					rel = model.getObjectProperty(nsG805+"binds");
					stmt = model.createStatement(a, rel, k);
					model.add(stmt);					
				}else if(isP1 && isP2){
					//IF a and b are Ports
					rel = model.getObjectProperty(nsG805+"binds");
					stmt = model.createStatement(a, rel, b);
					model.add(stmt);					
				}
			}else{
				//AssignableRelation
				if(isP1 && isP2){
					//IF a and b are Ports 
					OntClass o_class = model.getOntClass(nsG805+"Binding");

					String indName = "_ind_"+getCont();
					k = o_class.createIndividual(nsIndv+indName);
					warning += "\nNew individual "+indName+" created of type Binding";
					hashIndividuals.put(indName, "Binding");

					rel = model.getObjectProperty(nsG805+"is_binding");
					stmt = model.createStatement(k, rel, a);
					model.add(stmt);

					stmt = model.createStatement(k, rel, b);
					model.add(stmt);

					x = model.getIndividual(nsIndv+vars[2]);	

					rel = model.getObjectProperty(nsG805+"binding_is_represented_by");
					stmt = model.createStatement(k, rel, x);
					model.add(stmt);
				}else if(isTF1 && isTF2){
					//IF a and b are Transport Functions
					OntClass o_class = model.getOntClass(nsG805+"Binding");

					String indName = "_ind_"+getCont();
					k = o_class.createIndividual(nsIndv+indName);
					warning += "\nNew individual "+indName+" created of type Binding";
					hashIndividuals.put(indName, "Binding");

					o_class = model.getOntClass(nsG805+"Binded_Input/Output");

					indName = "_ind_"+getCont();
					Individual p = o_class.createIndividual(nsIndv+indName);
					warning += "\nNew individual "+indName+" created of type Binded_Input/Output";
					hashIndividuals.put(indName, "Binded_Input/Output");

					indName = "_ind_"+getCont();
					Individual q = o_class.createIndividual(nsIndv+indName);
					warning += "\nNew individual "+indName+" created of type Binded_Input/Output";
					hashIndividuals.put(indName, "Binded_Input/Output");

					stmt = model.createStatement(a, componentOf, p);
					model.add(stmt);

					stmt = model.createStatement(b, componentOf, q);
					model.add(stmt);

					rel = model.getObjectProperty(nsG805+"is_binding");
					stmt = model.createStatement(k, rel, p);
					model.add(stmt);

					stmt = model.createStatement(k, rel, q);
					model.add(stmt);

					x = model.getIndividual(nsIndv+vars[2]);	
					rel = model.getObjectProperty(nsG805+"binding_is_represented_by");
					stmt = model.createStatement(k, rel, x);
					model.add(stmt);
				}else if(isTF1 && isP2){
					//IF a is a Transport Function and b is a Port
					OntClass o_class = model.getOntClass(nsG805+"Binding");

					String indName = "_ind_"+getCont();
					k = o_class.createIndividual(nsIndv+indName);
					warning += "\nNew individual "+indName+" created of type Binding";
					hashIndividuals.put(indName, "Binding");

					o_class = model.getOntClass(nsG805+"Binded_Input/Output");

					indName = "_ind_"+getCont();
					Individual q = o_class.createIndividual(nsIndv+indName);
					warning += "\nNew individual "+indName+" created of type Binded_Input/Output";
					hashIndividuals.put(indName, "Binded_Input/Output");

					stmt = model.createStatement(a, componentOf, q);
					model.add(stmt);

					rel = model.getObjectProperty(nsG805+"is_binding");
					stmt = model.createStatement(k, rel, q);
					model.add(stmt);

					stmt = model.createStatement(k, rel, b);
					model.add(stmt);

					x = model.getIndividual(nsIndv+vars[2]);	
					rel = model.getObjectProperty(nsG805+"binding_is_represented_by");
					stmt = model.createStatement(k, rel, x);
					model.add(stmt);
				}else if(isP1 && isTF2){
					//IF a is a Port and b is a Transport Function
					OntClass o_class = model.getOntClass(nsG805+"Binding");

					String indName = "_ind_"+getCont();
					k = o_class.createIndividual(nsIndv+indName);
					warning += "\nNew individual "+indName+" created of type Binding";
					hashIndividuals.put(indName, "Binding");

					o_class = model.getOntClass(nsG805+"Binded_Input/Output");
					indName = "_ind_"+getCont();
					Individual p = o_class.createIndividual(nsIndv+indName);
					warning += "\nNew individual "+indName+" created of type Binded_Input/Output";
					hashIndividuals.put(indName, "Binded_Input/Output");

					stmt = model.createStatement(b, componentOf, p);
					model.add(stmt);

					rel = model.getObjectProperty(nsG805+"is_binding");
					stmt = model.createStatement(k, rel, a);
					model.add(stmt);

					stmt = model.createStatement(k, rel, p);
					model.add(stmt);

					x = model.getIndividual(nsIndv+vars[2]);	
					rel = model.getObjectProperty(nsG805+"binding_is_represented_by");
					stmt = model.createStatement(k, rel, x);
					model.add(stmt);
				}
			}
		}
	}

	private  void processConnects(String connects) {
		//Variables used in nexts contexts
		Statement stmt;
		ObjectProperty rel;
		Individual a,b;

		String[] lin = connects.split(";");
		for (String s : lin) {
			String[] bind = s.split(":");
			String[] vars = bind[1].split(",");

			a = model.getIndividual(nsIndv+vars[0]);
			b = model.getIndividual(nsIndv+vars[1]);
			if(vars.length == 2){
				//SimpleRelation
				rel = model.getObjectProperty(nsG805+"has_information_transfer");
				stmt = model.createStatement(a, rel, b);
				model.add(stmt);
			}else{
				//Assignable Relation
				OntClass o_class = model.getOntClass(nsG805+"Information_Transfer");
				String indName = "_ind_"+getCont();
				Individual k = o_class.createIndividual(nsIndv+indName);
				warning += "\nNew individual "+indName+" created of type Information_Transfer";
				hashIndividuals.put(indName, "Information_Transfer");


				rel = model.getObjectProperty(nsG805+"is_connecting");
				stmt = model.createStatement(k, rel, a);
				model.add(stmt);

				stmt = model.createStatement(k, rel, b);
				model.add(stmt);

				Individual x = model.getIndividual(nsIndv+vars[2]);	
				rel = model.getObjectProperty(nsG805+"IT_is_represented_by");
				stmt = model.createStatement(k, rel, x);
				model.add(stmt);
			}
		}
	}

	private  void processComposition(String compositions) {
		String[] lin = compositions.split(";");
		ObjectProperty componentOf = model.getObjectProperty(nsG805+"componentOf");
		ObjectProperty invComponentOf = model.getObjectProperty(nsG805+"INV.componentOf");
		for (String s : lin) {
			String[] comp = s.split(":");
			String[] vars = comp[1].split(",");
			Individual src = model.getIndividual(nsIndv+comp[0]);
			for (String var : vars) {
				Individual dst = model.getIndividual(nsIndv+var);
				Statement stmt = model.createStatement(src, componentOf, dst);
				model.add(stmt);

				stmt = model.createStatement(dst, invComponentOf, src);
				model.add(stmt);
			}
		}
	}

	private  void processElement(String elements) {
		String[] lin = elements.split(";");
		ArrayList<OntClass> lst;
		for (String s : lin) {
			String[] elem = s.split(":");
			String[] vars = elem[1].split(",");
			OntClass o_class;
			for (String var : vars) {
				o_class = null;
				if(elem[0].equals("ttf")){
					o_class = model.getOntClass(nsG805+"Trail_Termination_Function");
				}else if(elem[0].equals("so-ttf")){
					o_class = model.getOntClass(nsG805+"Trail_Termination_Source");
				}else if(elem[0].equals("sk-ttf")){
					o_class = model.getOntClass(nsG805+"Trail_Termination_Sink");
				}else if(elem[0].equals("bi-ttf")){
					o_class = model.getOntClass(nsG805+"Bidirectional_Trail_Termination");
				}else if(elem[0].equals("af")){
					o_class = model.getOntClass(nsG805+"Adaptation_Function");
				}else if(elem[0].equals("so-af")){
					o_class = model.getOntClass(nsG805+"Adaptation_Source");
				}else if(elem[0].equals("sk-af")){
					o_class = model.getOntClass(nsG805+"Adaptation_Sink");
				}else if(elem[0].equals("bi-af")){
					o_class = model.getOntClass(nsG805+"Bidirectional_Adaptation");
				}else if(elem[0].equals("matrix")){
					o_class = model.getOntClass(nsG805+"Matrix");
				}else if(elem[0].equals("uni-matrix")){
					o_class = model.getOntClass(nsG805+"Unidirectional_Matrix");
				}else if(elem[0].equals("so-matrix")){
					lst = new ArrayList<OntClass>();

					lst.add(model.getOntClass(nsG805+"Source_Matrix"));
					lst.add(model.getOntClass(nsG805+"Unidirectional_Sink-Source_Matrix"));

					UnionClass unionClass = model.createUnionClass(null, model.createList(lst.toArray(new RDFNode[]{})));
					unionClass.createIndividual(nsIndv+var);
				}else if(elem[0].equals("sk-matrix")){
					lst = new ArrayList<OntClass>();

					lst.add(model.getOntClass(nsG805+"Sink_Matrix"));
					lst.add(model.getOntClass(nsG805+"Unidirectional_Sink-Source_Matrix"));

					UnionClass unionClass = model.createUnionClass(null, model.createList(lst.toArray(new RDFNode[]{})));
					unionClass.createIndividual(nsIndv+var);
				}else if(elem[0].equals("bi-matrix")){
					o_class = model.getOntClass(nsG805+"Bidirectional_Matrix");
				}else if(elem[0].equals("sn")){
					o_class = model.getOntClass(nsG805+"Subnetwork");
				}else if(elem[0].equals("pm")){
					o_class = model.getOntClass(nsG805+"Physical_Media");
				}else if(elem[0].equals("input")){
					o_class = model.getOntClass(nsG805+"Input");
				}else if(elem[0].equals("output")){
					o_class = model.getOntClass(nsG805+"Output");
				}else if(elem[0].equals("rp")){
					o_class = model.getOntClass(nsG805+"Reference_Point");
				}else if(elem[0].equals("tcp")){
					o_class = model.getOntClass(nsG805+"Termination_Connection_Point");
				}else if(elem[0].equals("ap")){
					o_class = model.getOntClass(nsG805+"Access_Point");
				}else if(elem[0].equals("cp")){
					o_class = model.getOntClass(nsG805+"Connection_Point");
				}else if(elem[0].equals("te")){
					o_class = model.getOntClass(nsG805+"Transport_Entity");
				}else if(elem[0].equals("trail")){
					o_class = model.getOntClass(nsG805+"Trail");
				}else if(elem[0].equals("nc")){
					o_class = model.getOntClass(nsG805+"Network_Connection");
				}else if(elem[0].equals("lc")){
					o_class = model.getOntClass(nsG805+"Link_Connection");
				}else if(elem[0].equals("mc")){
					o_class = model.getOntClass(nsG805+"Matrix_Connection");
				}else if(elem[0].equals("snc")){
					o_class = model.getOntClass(nsG805+"Subnetwork_Connection");
				}else if(elem[0].equals("layer")){
					o_class = model.getOntClass(nsG805+"Layer_Network");
				}
				if(o_class != null){
					o_class.createIndividual(nsIndv+var);
				}
				hashIndividuals.put(var,elem[0]);
			}
		}

	}
	
	public  int getCont(){
		return cont++;
	}

}