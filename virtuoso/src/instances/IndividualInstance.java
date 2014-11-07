package instances;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import virtuoso.SparqlQueries;

public class IndividualInstance {
	private String ns;
	private String name;
	private String iri;
	private List<String> ListClasses;
	private List<ObjectPropertyInstance> objectProperiesAsSource;
	private List<ObjectPropertyInstance> objectProperiesAsTarget;
	private List<DataPropertyInstance> dataProperties;
	
	public IndividualInstance(String iriString) throws Exception {
		this.iri = iriString;
		
		String[] splittedIri = iriString.split("#");
		
		if(splittedIri.length < 2){
			throw new Exception();
		}
		
		this.ns = splittedIri[0];
		this.name = splittedIri[1];
	}

	public void searchForClasses(SparqlQueries sparqlQueries) throws SQLException{
		ResultSet individualClassesResSet = sparqlQueries.getAllIndividualInstanceClasses(this.iri);
		while(individualClassesResSet.next()){
			String individualClass = individualClassesResSet.getString("class");
			
			this.addListClasses(individualClass);
		}
	}
	
	public void searchForObjectPropertiesAsSource(SparqlQueries sparqlQueries) throws Exception{
		ResultSet indInstObjPropAsSourceResSet = sparqlQueries.getAllIndividualInstanceObjPropAsSource(this.iri);
		while(indInstObjPropAsSourceResSet.next()){
			String targetStr = indInstObjPropAsSourceResSet.getString("target");
			String opStr = indInstObjPropAsSourceResSet.getString("objProp");
			
			IndividualInstance target = new IndividualInstance(targetStr);
			
			ObjectPropertyInstance op = new ObjectPropertyInstance(opStr, this, target);
			
			this.addObjectProperiesAsSource(op);
		}
	}
	
	public void searchForObjectPropertiesAsTarget(SparqlQueries sparqlQueries) throws Exception{
		ResultSet indInstObjPropAsTargetResSet = sparqlQueries.getAllIndividualInstanceObjPropAsTarget(this.iri);
		while(indInstObjPropAsTargetResSet.next()){
			String sourceStr = indInstObjPropAsTargetResSet.getString("source");
			String opStr = indInstObjPropAsTargetResSet.getString("objProp");
			
			IndividualInstance source = new IndividualInstance(sourceStr);
			
			ObjectPropertyInstance op = new ObjectPropertyInstance(opStr, source, this);
			
			this.addObjectProperiesAsTarget(op);
		}
	}
	
	public void searchForDataProperties(SparqlQueries sparqlQueries){
		
	}
	
	public String getNs() {
		return this.ns;
	}

	public void setNs(String ns) {
		this.ns = ns;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIri() {
		return iri;
	}

	public void setIri(String iri) {
		this.iri = iri;
	}

	public List<String> getListClasses(SparqlQueries sparqlQueries) throws SQLException {
		if(this.ListClasses == null){
			this.searchForClasses(sparqlQueries);
		}
		return this.ListClasses;
	}

	public void addListClasses(String classIriStr) {
		if(this.ListClasses == null){
			this.ListClasses = new ArrayList<String>();
		}
		String[] splittedIri = classIriStr.split("#");
		if(splittedIri.length == 2){
			this.ListClasses.add(splittedIri[1]);
		}		
	}

	public void addObjectProperiesAsSource(ObjectPropertyInstance opInstance) {
		if(this.objectProperiesAsSource == null){
			this.objectProperiesAsSource = new ArrayList<ObjectPropertyInstance>();
		}		
		
		this.objectProperiesAsSource.add(opInstance);
	}

	public List<ObjectPropertyInstance> getObjectProperiesAsTarget(SparqlQueries sparqlQueries) throws Exception {
		if(this.objectProperiesAsTarget == null){
			this.searchForObjectPropertiesAsTarget(sparqlQueries);
		}
		return this.objectProperiesAsTarget;
	}

	public List<ObjectPropertyInstance> getObjectProperiesAsSource(SparqlQueries sparqlQueries) throws Exception {
		if(this.objectProperiesAsSource == null){
			this.searchForObjectPropertiesAsSource(sparqlQueries);
		}
		return this.objectProperiesAsSource;
	}

	public void addObjectProperiesAsTarget(ObjectPropertyInstance opInstance) {
		if(this.objectProperiesAsTarget == null){
			this.objectProperiesAsTarget = new ArrayList<ObjectPropertyInstance>();
		}		
		this.objectProperiesAsTarget.add(opInstance);
	}

	public List<DataPropertyInstance> getDataProperties(SparqlQueries sparqlQueries) {
		if(this.dataProperties == null){
			this.searchForDataProperties(sparqlQueries);
		}
		return this.dataProperties;
	}

	public void addDataProperties(DataPropertyInstance dpInstance) {
		if(this.dataProperties == null){
			this.dataProperties = new ArrayList<DataPropertyInstance>();
		}
		this.dataProperties.add(dpInstance);
	}
	
	@Override
	public String toString() {
		String out = "";
		out += "\n";
		out += 	this.name;
		out += "\n\tClasses: ";
		out += "\n\t\t";
		if(this.ListClasses != null){
			for (String className : this.ListClasses) {
				out += className + ", ";
			}
		}
		int i = out.lastIndexOf(",");
		if(i < 0){
			i = out.length();
		}
		out = out.substring(0, i);
		out += "\n\tObject Properties: ";
		if(this.objectProperiesAsSource != null){
			for (ObjectPropertyInstance op : this.objectProperiesAsSource) {
				out += "\n\t\t" + op;
			}
		}
		if(this.objectProperiesAsTarget != null){
			for (ObjectPropertyInstance op : this.objectProperiesAsTarget) {
				out += "\n\t\t" + op;
			}
		}
		out += "\n\tData Properties: ";
		if(this.dataProperties != null){
			for (DataPropertyInstance dp : this.dataProperties) {
				out += "\n\t\t" + dp;
			}
		}
		out += "\n";
		return out;
	}
}
