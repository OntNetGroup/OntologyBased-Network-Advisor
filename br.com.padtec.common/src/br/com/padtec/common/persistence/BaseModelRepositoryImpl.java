package br.com.padtec.common.persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;

import javax.swing.JFileChooser;

import br.com.padtec.common.queries.OntModelAPI;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

public class BaseModelRepositoryImpl implements BaseModelRepository {

	/** Base ontology model */
	private OntModel baseOntModel;
	
	/** Default namespace of this base ontolgy model */
	private String baseNameSpace;
	
	/** Constructor */
	public BaseModelRepositoryImpl()
	{
		baseOntModel = ModelFactory.createOntologyModel();
		baseNameSpace = baseOntModel.getNsPrefixURI("");	
	}
	
	/**
	 * Read the base OntModel of this repository from a file.
	 * 
	 * @param inputFileName 
	 * 
	 * @author John Guerson
	 */
	public void readBaseOntModel(String inputFileName)
	{
    	InputStream  in= FileManager.get().open(inputFileName);
		if (in == null) throw new IllegalArgumentException("File: " + inputFileName + " not found");				
		baseOntModel.read(in,null);		
		baseNameSpace = baseOntModel.getNsPrefixURI("");
	}
	
	/**
	 * Get he base OntModel of this repository
	 * 
	 * @author John Guerson
	 */
	public OntModel getBaseOntModel()
	{
		return baseOntModel;		
	}
	
	/**
	 * Set he base OntModel of this repository
	 * 
	 * @author John Guerson
	 */
	public void setBaseOntModel(OntModel ontModel)
	{
		baseOntModel = ontModel;
		baseNameSpace = baseOntModel.getNsPrefixURI("");
	}
	
	/**
	 * Read the base OntModel of this repository from a input stream.
	 * 
	 * @param in: InputStream 
	 * 
	 * @author John Guerson
	 */
	public void readBaseOntModel(InputStream  in)
	{
		if (in == null) throw new IllegalArgumentException("File not found");
		baseOntModel.read(in,null);	
		baseNameSpace = baseOntModel.getNsPrefixURI("");
	}
	
	/**
	 * Clone the ontModel passed as argument and replace the existing base model in the repository for the new cloned one. 
	 * 
	 * @param model: OntModel 
	 * 
	 * @author John Guerson
	 */
	public OntModel cloneReplacing(OntModel model)
	{
		OntModel newModel = OntModelAPI.clone(model);
        baseOntModel = newModel;
        return newModel;
	}

	/** 
	 * Get the default name space of the base ontology model.
	 * 
	 * @author John Guerson
	 */
	public String getNameSpace()
	{
		baseNameSpace = baseOntModel.getNsPrefixURI("");
		return baseNameSpace;
	}

	/** 
	 * Save the base ontology model of the repository into a file.
	 * 
	 * @param path: String
	 * 
	 * @author John Guerson
	 */
	public void saveBaseOntModel(String path)
	{		
		if(!path.contains(".owl")) path = path + ".owl";		
		try {
			OutputStream output = new FileOutputStream(path);
			this.baseOntModel.write(output,"RDF/XML");			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * Save the base ontology model of the repository into a file.
	 * 
	 * @author John Guerson
	 */
	public void saveBaseOntModel()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int i= fileChooser.showSaveDialog(null);
        if (i==1){
        	System.out.println("Choose a directory to save the file.\n");
        } else {
        	File file = fileChooser.getSelectedFile();
        	//textArea.setText(arquivo.getPath());        	
        	//Commit
    		this.saveBaseOntModel(file.getPath());    		
    		System.out.println( "Ontology saved in file:\n" + file.getPath() + "\n" );
        }
	}
	
	/** 
	 * Print the base ontology model of the repository in the system out stream.
	 * 
	 * @author John Guerson
	 */
	public void printOutBaseOntModel()
	{		
		baseOntModel.write(System.out, "RDF/XML");
	}
	
	/** 
	 * Get the base ontology model of the repository as string.
	 *  
	 * @author John Guerson
	 */
	public String getBaseOntModelAsString() 
	{
		String syntax = "RDF/XML";
		StringWriter out = new StringWriter();
		baseOntModel.write(out, syntax);
		String result = out.toString();		
		return result;
	}
	
	/**
	 * Erases the repository.
	 * 
	 * @author John Guerson
	 */
	public void clear()
	{
		baseNameSpace="";
		baseOntModel=null;
	}
}
