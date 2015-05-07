package sparql.inferences;

public class Atom {
	
	String type = "";
	String predicateType = "";
	String predicate = "";
	String argument1 = "";
	String argument2 = "";
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPredicate() {
		return predicate;
	}
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
	public String getArgument1() {
		return argument1;
	}
	public void setArgument1(String argument1) {
		this.argument1 = argument1;
	}
	public String getArgument2() {
		return argument2;
	}
	public void setArgument2(String argument2) {
		this.argument2 = argument2;
	}
	public String getPredicateType() {
		return predicateType;
	}
	public void setPredicateType(String predicateType) {
		this.predicateType = predicateType;
	}		
}
