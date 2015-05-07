package sparql.inferences;

import java.util.HashMap;

public class RuleList {
	HashMap<String, Rule> ruleList = new HashMap<String, Rule>();
	
	public HashMap<String, Rule> getRuleList() {
		return ruleList;
	}
	
	public Rule getRuleList(String key) {
		if(!ruleList.containsKey(key)){
			addRuleList(key);
		}
		return ruleList.get(key);
	}
	
	public void addRuleList(String key) {
		Rule rule = new Rule();
		this.ruleList.put(key, rule);
	}
}
