package Domain;

import com.hp.hpl.jena.ontology.OntModel;

public interface IRepository {
	
	public OntModel Open(String inputFileName);
	public String getNameSpace(OntModel model);
	public void Save(OntModel model, String path);
	public void SaveWithDialog(OntModel model);
	public void Print(OntModel model);
}
