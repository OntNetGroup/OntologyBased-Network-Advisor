package br.com.padtec.okco.persistence;

import java.io.InputStream;

import com.hp.hpl.jena.ontology.OntModel;

public interface BaseModelRepository {
	
	/**
	 * Read the base OntModel of this repository from a file.
	 * 
	 * @param inputFileName 
	 * 
	 * @author John Guerson
	 */
	public abstract void readBaseOntModel(String inputFileName);
	
	/**
	 * Read the base OntModel of this repository from a input stream.
	 * 
	 * @param in: InputStream 
	 * 
	 * @author John Guerson
	 */
	public abstract void readBaseOntModel(InputStream in);
	
	/**
	 * Get he base OntModel of this repository
	 * 
	 * @author John Guerson
	 */
	public abstract OntModel getBaseOntModel();
		
	/**
	 * Set he base OntModel of this repository
	 * 
	 * @author John Guerson
	 */
	public abstract void setBaseOntModel(OntModel ontModel);
	
	/** 
	 * Get the base ontology model of the repository as string.
	 *  
	 * @author John Guerson
	 */
	public abstract String getBaseOntModelAsString();
	
	/** 
	 * Get the default name space of the base ontology model.
	 * 
	 * @author John Guerson
	 */
	public abstract String getNameSpace();
	
	/**
	 * Clone the ontModel passed as argument and replace the existing base model in the repository for the new cloned one. 
	 * 
	 * @param model: OntModel 
	 * 
	 * @author John Guerson
	 */
	public abstract OntModel cloneReplacing(OntModel ontModel);	

	/** 
	 * Save the base ontology model of the repository into a file.
	 * 
	 * @param path: String
	 * 
	 * @author John Guerson
	 */
	public abstract void saveBaseOntModel(String path);
	
	/** 
	 * Save the base ontology model of the repository into a file.
	 * 
	 * @author John Guerson
	 */
	public abstract void saveBaseOntModel();
	
	/** 
	 * Print the base ontology model of the repository in the system out stream.
	 * 
	 * @author John Guerson
	 */
	public abstract void printOutBaseOntModel();	
	
	/**
	 * Erases the repository.
	 * 
	 * @author John Guerson
	 */
	public abstract void clear();
}
