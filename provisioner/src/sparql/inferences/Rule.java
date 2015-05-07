package sparql.inferences;

import java.util.HashMap;

public class Rule {
	HashMap<String, AtomList> atomLists = new HashMap<String, AtomList>();
	
	public HashMap<String, AtomList> getAtomsList() {
		return atomLists;
	}
	
	public AtomList getAtomList(String key) {
		if(!atomLists.containsKey(key)){
			addAtomList(key);
		}
		return atomLists.get(key);
	}
	
	public void addAtomList(String key) {
		AtomList atomList = new AtomList();
		this.atomLists.put(key, atomList);
	}

	
}
