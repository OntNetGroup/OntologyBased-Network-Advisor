package br.com.padtec.okco.application;

import java.util.ArrayList;
import java.util.List;

import br.com.padtec.common.dto.DtoInstanceRelation;
import br.com.padtec.common.exceptions.OKCoNameSpaceException;
import br.com.padtec.common.queries.QueryUtil;
import br.com.padtec.common.util.Instance;
import br.com.padtec.common.util.UploadApp;

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
		List<String> individualsURIList = QueryUtil.getIndividualsURIFromAllClasses(model);
    	for (String indivURI : individualsURIList)
    	{    		
    		if(!indivURI.contains("#")){ throw new OKCoNameSpaceException("Entity namespace problem. The " + indivURI +" have to followed by \"#\"."); }
    		List<String> classesURIList = QueryUtil.getClassesURI(model, indivURI);
    		List<String> diffURIList = QueryUtil.getIndividualsURIDifferentFrom(model, indivURI);
    		List<String> sameAsURIList = QueryUtil.getIndividualsURISameAs(model, indivURI);
    		String nameSpace = indivURI.split("#")[0] + "#";
    		String name = indivURI.split("#")[1];
    		result.add(new Instance(nameSpace, name, classesURIList, diffURIList, sameAsURIList, true));
		}		
		return result;
	}
	
	/** 
	 * Return the a particular individual from the ontology.
	 * It returns also all the classes of an individual as well as all the other individuals different and the same as this one.
	 *  
	 * @return
	 * @throws OKCoNameSpaceException
	 * 
	 * @author John Guerson
	 */
	static public Instance getIndividual(String individualURI)
	{
		InfModel model = UploadApp.getInferredModel();
		if(!individualURI.contains("#")){ throw new OKCoNameSpaceException("Entity namespace problem. The " + individualURI +" have to followed by \"#\"."); }
		List<String> classesURIList = QueryUtil.getClassesURI(model, individualURI);
		List<String> diffURIList = QueryUtil.getIndividualsURIDifferentFrom(model, individualURI);
		List<String> sameAsURIList = QueryUtil.getIndividualsURISameAs(model, individualURI);
		String nameSpace =  individualURI.split("#")[0] + "#";
		String name =  individualURI.split("#")[1];
		return new Instance(nameSpace, name, classesURIList, diffURIList, sameAsURIList, true);
	}
	
	/** 
	 * Return all the relations of a particular individual from the ontology.
	 * It returns also the first range class of the relations.
	 * 
	 * @author John Guerson
	 */
	static public List<DtoInstanceRelation> getRelations(String individualURI)
	{
		List<DtoInstanceRelation> result = new ArrayList<DtoInstanceRelation>();
		List<String> propertiesURIList = QueryUtil.getPropertiesURI(UploadApp.getInferredModel(), individualURI);
		for(String propertyURI: propertiesURIList)
		{
			DtoInstanceRelation dtoItem = new DtoInstanceRelation();
		    dtoItem.Property = propertyURI;
		    List<String> ranges = QueryUtil.getRangeURIs(UploadApp.getInferredModel(), propertyURI);
		    if(ranges.size()>0) dtoItem.Target = ranges.get(0);
		    else dtoItem.Target = "";
		    result.add(dtoItem);
		}
		return result;
	}
}
