package Business;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JFileChooser;

import Domain.IRepository;
import UserInterface.AplicationOld;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class Repository implements IRepository {

	private OntModel Model;
	public static String NameSpace;
	
	public Repository()
	{
		Model = ModelFactory.createOntologyModel();
	}
	
	public OntModel Open(String inputFileName)
	{
    	InputStream in = FileManager.get().open(inputFileName);
		if (in == null) {
		    throw new IllegalArgumentException("File: " + inputFileName + " not found");
		}
		
		Model.read(in,null);				
		
		return Model;
	}

	public String getNameSpace(OntModel model)
	{
		//Get the base namespace
		NameSpace = model.getNsPrefixURI("");
		/*ExtendedIterator Classes = model.listClasses();
		while (Classes.hasNext()) {
		    Resource cl = (Resource) Classes.next();
		    NameSpace = cl.getNameSpace();
		    break;
		}*/
		
		return NameSpace;
	}

	public void Save(OntModel model, String path)
	{		
		this.Model= model;
		
		if(path.contains(".owl")){
			//OK
		} else {
			path = path + ".owl";
		}			
		
		OutputStream output = null;
		try {
			output = new FileOutputStream(path);
			this.Model.write(output,"RDF/XML");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void SaveWithDialog(OntModel model)
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int i= fileChooser.showSaveDialog(null);
        if (i==1){
        	System.out.println("Escolha um diretório para salvar o arquivo.\n");
        } else {
        	File file = fileChooser.getSelectedFile();
        	//textArea.setText(arquivo.getPath());
        	
        	//Commit
    		this.Save(model, file.getPath());    		
    		System.out.println( "Ontology save in file:\n" + file.getPath() + "\n" );
        }
	}
	
	public void Print(OntModel model) {
		
		model.write(System.out, "RDF/XML");		
	}
}
