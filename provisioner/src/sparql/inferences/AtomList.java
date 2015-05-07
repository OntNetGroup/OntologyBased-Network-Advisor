package sparql.inferences;

import java.util.HashMap;

public class AtomList {
	HashMap<String, Atom> atoms = new HashMap<String, Atom>();
	String type;

	public HashMap<String, Atom> getAtoms() {
		return atoms;
	}
	
	public Atom getAtom(String key) {
		if(!atoms.containsKey(key)){
			addAtom(key);
		}
		return atoms.get(key);
	}
	
	public void addAtom(String key) {
		Atom atom = new Atom();
		this.atoms.put(key, atom);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}	
	
	
}
