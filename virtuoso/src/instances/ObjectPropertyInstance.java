package instances;

public class ObjectPropertyInstance {
	String ns, name, iri;
	IndividualInstance source, target;
	
	public ObjectPropertyInstance(String iri, IndividualInstance source, IndividualInstance target) throws Exception {
		this.iri = iri;
		
		String[] splittedIri = iri.split("#");
		if(splittedIri.length < 2){
			throw new Exception();
		}
		
		this.ns = splittedIri[0];
		this.name = splittedIri[1];
		
		this.source = source;
		this.target = target;
	}
	
	@Override
	public String toString() {
		return this.source.getName() + " -> " + this.name + " -> " + this.target.getName();
	}
}
