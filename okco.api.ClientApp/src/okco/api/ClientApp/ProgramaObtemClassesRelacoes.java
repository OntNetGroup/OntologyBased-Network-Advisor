package okco.api.ClientApp;

import java.io.InputStream;
import java.util.List;

import br.com.padtec.common.queries.OntModelAPI;
import br.com.padtec.okco.domain.Search;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

public class ProgramaObtemClassesRelacoes {
	
	public static void main(String[] args) {
		
		String pathOwlFile = "C://Users//fabio_000//Desktop//OntologiasOWL//Unified_Transport_Networkv2.1noRules.owl";
		
		InputStream in = FileManager.get().open(pathOwlFile);
		if (in == null) {
			System.out.println("Arquivo n�o encontrado");
		}
		
		//Create model
		OntModel model = null;
		model = ModelFactory.createOntologyModel();
		
		model.read(in,null);		
		String ns = model.getNsPrefixURI("");		  
		if(ns == null)
		{
			System.out.println("Namespace n�o definido");
		}
		
		Search search = new Search();
		
		List<String> lclasses = OntModelAPI.getClassesURI(model);
		List<String> lpropreties = OntModelAPI.getPropertiesURI(model);
		
		String result1 = "";
		String result2 = "";
		
		for (String cls : lclasses) {
			
			if(cls != null)
				//result1 = result1 + "'" + cls.replace(ns, "") + "' | ";
				result1 = result1 + "instruction.contains(\"" + cls.replace(ns, "") + "\") || ";
		}
		
		for (String prop : lpropreties) {
			
			if(prop != null && ! prop.contains("www.w3.org"))
			{
				//result2 = result2 + "'" + prop.replace(ns, "") + "' | ";
				result2 = result2 + "instruction.contains(\" " + prop.replace(ns, "") + " \") || ";
			}
		}
		
		System.out.println(result1);
		System.out.println("");
		System.out.println(result2);		
	  	
	}

}
