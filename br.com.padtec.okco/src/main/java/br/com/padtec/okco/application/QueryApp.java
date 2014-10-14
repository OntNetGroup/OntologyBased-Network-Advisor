package br.com.padtec.okco.application;

import java.util.ArrayList;
import java.util.List;

import br.com.padtec.common.queries.InfModelQueryUtil;
import br.com.padtec.okco.domain.Instance;
import br.com.padtec.okco.domain.exceptions.OKCoNameSpaceException;

import com.hp.hpl.jena.rdf.model.InfModel;

public class QueryApp {

	/** 
	 * Return the list of all individuals from the ontology.
	 * It returns also all the classes of an individual as well as all the other individuals different and the same as this one.
	 *  
	 * @return
	 * @throws OKCoNameSpaceException
	 * 
	 * @author John Guerson
	 */
	static public List<Instance> getIndividuals() throws OKCoNameSpaceException 
	{
		List<Instance> result = new ArrayList<Instance>();
		InfModel model = UploadApp.getInferredModel();		
		List<String> individualsURIList = InfModelQueryUtil.getIndividualsURIFromAllClasses(model);
    	for (String indivURI : individualsURIList)
    	{    		
    		if(!indivURI.contains("#")){ throw new OKCoNameSpaceException("Entity namespace problem. The " + indivURI +" have to followed by \"#\"."); }
    		List<String> classesURIList = InfModelQueryUtil.getClassesURI(model, indivURI);
    		List<String> diffURIList = InfModelQueryUtil.getIndividualsURIDifferentFrom(model, indivURI);
    		List<String> sameAsURIList = InfModelQueryUtil.getIndividualsURISameAs(model, indivURI);
    		String nameSpace = indivURI.split("#")[0] + "#";
    		String name = indivURI.split("#")[1];
    		result.add(new Instance(nameSpace, name, classesURIList, diffURIList, sameAsURIList, true));
		}		
		return result;
	}
	
}
