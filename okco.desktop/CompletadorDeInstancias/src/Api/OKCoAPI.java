package Api;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import org.mindswap.pellet.PelletOptions;
import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.ResourceUtils;

import Business.FactoryInstances;
import Business.FactoryModel;
import Business.ManagerInstances;
import Business.Repository;
import Business.Search;
import Domain.DtoDefinitionClass;
import Domain.IRepository;
import Domain.Instance;

public class OKCoAPI {
	
	public static IRepository Repository;
	private Search Search;
	private String NS;
	private FactoryInstances FactoryInstances;
	private ManagerInstances ManagerInstances;
	private OntModel Model;
	
	public OKCoAPI(OntModel model, String NS)
	{
		this.NS = NS;
		this.Search = new Search(NS);
		this.Model = ModelFactory.createOntologyModel();
		this.FactoryInstances = new FactoryInstances(this.Search);
		this.ManagerInstances = new ManagerInstances(Search, Model);
		FactoryModel Factory = new FactoryModel();
		Repository = Factory.GetRepository();	
	}
	
	public OKCoAPI(File file, String NS)
	{
		String inputFileName = file.getPath();
		InputStream in = FileManager.get().open(inputFileName);
		
		if (in == null) {
		    throw new IllegalArgumentException("File: " + inputFileName + " not found");
		}
		
		this.Model = ModelFactory.createOntologyModel();
		FactoryModel Factory = new FactoryModel();
		Repository = Factory.GetRepository();	
		Model.read(in,null);
		this.NS = NS;//Repository.getNameSpace(Model);
		this.Search = new Search(NS);
		this.FactoryInstances = new FactoryInstances(this.Search);
		this.ManagerInstances = new ManagerInstances(Search, Model);
	}

	/**
	* Retorna as rela��es que definem a classe cls.
	 * O retorno deve ser do tipo: List<String[]>, onde String[0] �
	 * o nome da classe faltante e String[1] a rela��o que liga cls a 
	 * String[0] 
	 * 
	 * @param classe
	 * @return Uma lista com os elementos que est�o faltando para a classe cls
	 */
	public ArrayList<String[]> getComplementsFromClass(String cls){
		
		ArrayList<String[]> list = new ArrayList<String[]>();
		
		ArrayList<DtoDefinitionClass> dtoSomeRelationsList = Search.GetSomeRelations(Model);
		ArrayList<DtoDefinitionClass> dtoMinRelationsList = Search.GetMinRelations(Model);
		ArrayList<DtoDefinitionClass> dtoMaxRelationsList = Search.GetMaxRelations(Model);
		ArrayList<DtoDefinitionClass> dtoExactlyRelationsList = Search.GetExactlyRelations(Model);		
		
		
		for (DtoDefinitionClass dto : dtoSomeRelationsList) {
			if(dto.Source.equals(NS + cls))
			{
				String[] elem = new String[4];
				elem[0] = dto.Relation.split(NS)[1];
				elem[1] = "Some";
				elem[2] = null;
				elem[3] = dto.Target.split(NS)[1];
				list.add(elem);
			}
		}
		
		for (DtoDefinitionClass dto : dtoMinRelationsList) {
			if(dto.Source.equals(NS + cls))
			{
				String[] elem = new String[4];
				elem[0] = dto.Relation.split(NS)[1];
				elem[1] = "Min";
				elem[2] = dto.Cardinality;
				elem[3] = dto.Target.split(NS)[1];
				list.add(elem);
			}
		}
		
		for (DtoDefinitionClass dto : dtoMaxRelationsList) {
			if(dto.Source.equals(NS + cls))
			{
				String[] elem = new String[4];
				elem[0] = dto.Relation.split(NS)[1];
				elem[1] = "Max";
				elem[2] = dto.Cardinality;
				elem[3] = dto.Target.split(NS)[1];
				list.add(elem);
			}
		}
		
		for (DtoDefinitionClass dto : dtoExactlyRelationsList) {
			if(dto.Source.equals(NS + cls))
			{
				String[] elem = new String[4];
				elem[0] = dto.Relation.split(NS)[1];
				elem[1] = "Exactly";
				elem[2] = dto.Cardinality;
				elem[3] = dto.Target.split(NS)[1];
				list.add(elem);
			}
		}
		
		return list;
	}

	/**
	* Se o indiv�duo indv n�o existir, cria-o e diz que ele � diferente de todos
	 * os outros indiv�duos.
	 * Insere o indiv�duo indv na classe cls
	 * @param indv, cls
	 * @return 
	 */
	public void insertIndividualInClass(String indv, String cls){
		
		//Get instance, class
		Individual indInstance = Model.getIndividual(NS + indv);
		OntClass Class = Model.getOntClass(NS + cls);
		
		//Get all instances
		ArrayList<String> listInstances = this.Search.GetAllInstances(Model);
		
		if(indInstance == null)
		{
			//Create individual
			Individual newInstance = Class.createIndividual(NS + indv);
			for (String iName : listInstances) {
				Individual i = Model.getIndividual(iName);
				i.setDifferentFrom(newInstance);	
			}			
		}
	}

	/**
	* Se o invid�duo indv n�o existir, cria-o e diz que ele � diferente de todos
	 * os outros indiv�duos.
	 * Relaciona o indiv�duo indv com a propriedade prop e com a cardinalidade min e max.
	 * Caso min e max sejam null a rela��o � de some.
	 * @param indv, prop, min, max
	 * @return 
	 */
	public void insertProprertyForIndividual(String indvSrc, String prop, String indvDest){

		//Get instance, class
		Individual indS = Model.getIndividual(NS + indvSrc);
		Individual indD = Model.getIndividual(NS + indvDest);
		Property relation = Model.getProperty(NS + prop);
		
		//Add relation
		indS.addProperty(relation, indD);
	}
	
	/**
	* Exclui o indiv�duo indv e as rela��es nas quais ele faz parte.
	 * O reasoner � executado novamente.
	 * @param indv
	 * @return 
	 */
	public void deleteIndividual(String indv){
		
		Individual ind = Model.getIndividual(NS + indv);
		ind.remove();	// remove every statement that mentions this resource as a subject or object of a statement.
	}

	/**
	* Altera o nome do indiv�duo indv para newName. 
	 * Todas as informa��es de indv permanecem.
	 * @param indv, newName
	 * @return 
	 */
	public void changeIndividualName(String indv, String newName){
		
		Individual ind = Model.getIndividual(NS + indv);
		ResourceUtils.renameResource( ind, NS + newName );
	}

	/**
	* Altera a classe que o indiv�duo indv � para classe cls.
	 * O reasoner � executado novamente.
	 * @param indv, newClass
	 * @return 
	 */
	public void changeIndividualClass(String indv, String newClass){
		
		Individual ind = Model.getIndividual(NS + indv);
		OntClass oClass = ind.getOntClass();
		OntClass nClass = Model.getOntClass(NS + newClass);
		
		ind.removeOntClass(oClass);
		ind.addOntClass(nClass);		
	}
	
	/**
	* O reasoner � executado.
	 */
	public void executeReasoner()
	{
		try {
			
			// IMPORTANT: The option to enable tracing should be turned
			// on before the ontology is loaded to the reasoner!
			PelletOptions.USE_TRACING = true;
			
			// create Pellet reasoner
			Reasoner r = PelletReasonerFactory.theInstance().create();
			
			// create an inferencing model using the raw model
			InfModel infModel = ModelFactory.createInfModel(r, Model);
			
		} catch (Exception e) {
			System.out.println("Reaoner problem: " + e.getMessage());
			throw e;
		}
	}
	
	/**
	* Salvar o arquivo
	 */
	public String getOWL()
	{
		//Repository.SaveWithDialog(Model);
		try {
			
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            Model.write(os,"RDF/XML");
            String s = new String(os.toByteArray(),"UTF-8");
            return s;
            
	    } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	            return null;
	    }
	}
}
