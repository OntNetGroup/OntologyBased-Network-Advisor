package virtuoso;

import instances.IndividualInstance;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.shared.JenaException;

import br.com.padtec.nopen.studio.service.StudioInitializer;
import virtuoso.jena.driver.VirtDataSource;
import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtModel;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

public class Main {

	public static void main(String[] args) throws Exception  {
		VirtDataSource vd = new VirtDataSource("jdbc:virtuoso://localhost:1111", "dba", "dba");
		
        ModelMaker maker = ModelFactory.createMemModelMaker();    
        Model m = maker.createModel("", false);
        
        //load owl file in model M
//        FileInputStream  fis = new FileInputStream (getClass().getClassLoader().getResource("file.owl").getFile());
        FileInputStream fis = new FileInputStream (StudioInitializer.class.getResource("/model/EquipStudio.owl").getFile());
        m.read(fis ,null);
        
        fis.close(); 
        String uri = m.getNsPrefixURI("");
        
        //Model m2 = vd.getNamedModel(m.getNsPrefixURI(""));
        Model m2 = getNamedModel(vd, m.getNsPrefixURI(""));
        
        if(m2 == null){
	        vd.addNamedModel(uri, m);
	        vd.setDefaultModel(m);
        }
        
        new KAO(m.getNsPrefixURI("")).getAllIndividuals();
        
		System.out.println();
//		FileInputStream s = new FileInputStream (StudioInitializer.class.getResource("/model/EquipStudio.owl").getFile());
////		FileInputStream s = (FileInputStream) StudioInitializer.class.getResourceAsStream("/model/EquipStudio.owl");
//		
//		String url = ConnectionFactory.getVirtuosourl();
//		VirtGraph graph = new VirtGraph (url, "dba", "dba");
//		
//		VirtuosoUpdateFactory.read(s, graph);
//		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.read(s,graph);
//        vur.exec();
		
//		String ontologyUri = "http://www.semanticweb.org/ontologies/2014/5/ontology.owl";
//		
//		KAO kao = new KAO(ontologyUri);
//		
//		IndividualInstance eq = new IndividualInstance(ontologyUri+"#eq");
//		eq.addListClasses(ontologyUri+"#Equipment");
//		
//		IndividualInstance eq2 = new IndividualInstance(ontologyUri+"#eq2");
//		eq2.addListClasses(ontologyUri+"#Equipment");
//		
//		IndividualInstance in = new IndividualInstance(ontologyUri+"#input");
//		in.addListClasses(ontologyUri+"#Input");
//		
//		IndividualInstance inpint = new IndividualInstance(ontologyUri+"#inpint");
//		inpint.addListClasses(ontologyUri+"#Input_Interface");
//		
//		IndividualInstance out = new IndividualInstance(ontologyUri+"#out");
//		out.addListClasses(ontologyUri+"#Output");
//		
//		IndividualInstance outint = new IndividualInstance(ontologyUri+"#outint");
//		outint.addListClasses(ontologyUri+"#Output_Interface");
//		//outint.addListClasses(ontologyUri+"#Mapped_Output_Interface");
//		
//		eq.addObjectPropertiesAsSource(kao, ontologyUri+"#componentOf", outint, true);
//		eq2.addObjectPropertiesAsSource(kao, ontologyUri+"#componentOf", inpint, true);
//		inpint.addObjectPropertiesAsSource(kao, ontologyUri+"#maps_input", in, true);
//		out.addObjectPropertiesAsSource(kao, ontologyUri+"#binds", in, true);
//		outint.addObjectPropertiesAsSource(kao, ontologyUri+"#maps_output", out, true);
//		outint.addObjectPropertiesAsSource(kao, ontologyUri+"#INV.componentOf", eq, true);
//		
//		kao.insertInstance(eq);
//		kao.insertInstance(eq2);
//		kao.insertInstance(in);
//		kao.insertInstance(inpint);
//		kao.insertInstance(out);
//		kao.insertInstance(outint);
//		
//		ArrayList<IndividualInstance> instances = kao.getAllIndividualInstances(true, true, true);
//		
//		System.out.println(instances);
		
		System.out.println("Start inference!");
        Reasoner reasoner = PelletReasonerFactory.theInstance().create();
        reasoner = reasoner.bindSchema(vd);
        InfModel infModel = ModelFactory.createInfModel(reasoner, m);
        System.out.println("Stop inference!");

        m2 = getNamedModel(vd, m.getNsPrefixURI(""));
        
        System.out.println("instancias "+m2.size());
        System.out.println("instancias "+infModel.size());
        if(m2.containsAll(infModel)){ System.out.print("no hay inferencia ");
        }else{ System.out.print("hay inferencia "); }
        if(m2.containsAny(infModel)){ System.out.print("es correcto ");
        }else{ System.out.print("es incorrecto "); }
        
        Model inferido = infModel.difference(m2);
	    m2.add(inferido);
	}
	
	public static VirtModel getNamedModel(VirtGraph vd, String name){
		try {
        	DataSource _ds = vd.getDataSource();
			if (_ds != null)
				return new VirtModel(new VirtGraph(name, _ds));
			else
				return new VirtModel(new VirtGraph(name, vd.getGraphUrl(),
						vd.getGraphUser(), vd.getGraphPassword()));
		} catch (Exception e) {
			throw new JenaException(e);
		}
	}
	public static void deleteRepository(String ontologyUri){
		/*			STEP 1			*/
		VirtGraph set = new VirtGraph (ConnectionFactory.getVirtuosourl(), "dba", "dba");

		/*			STEP 2			*/
		//System.out.println("\nexecute: CLEAR GRAPH <http://test1>");
        String str = "CLEAR GRAPH <" + ontologyUri + ">";
        VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(str, set);
        vur.exec();     
	}
	
	
}