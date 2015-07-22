package provisioner.tester;

import java.util.ArrayList;

import provisioner.util.ConsoleUtil;

public class Test {
	int declaredReplications;
	int qtShortPaths;
	int maxPathSize;
	int priorityOption; 
	int maxNewBindings; 
	int maxNewPossible;
	
	public void setDeclaredReplicationsFromConsole() {
		this.declaredReplications = ConsoleUtil.getOptionFromConsole("Choose the number of layer replications", 2, Integer.MAX_VALUE,0, false);
	}
	public void setQtShortPathsFromConsole() {
		this.qtShortPaths = ConsoleUtil.getOptionFromConsole("Choose the maximum number of paths (-1 for no limit): ", 1, Integer.MAX_VALUE,0, true);
		if(this.qtShortPaths == -1){
			this.qtShortPaths = Integer.MAX_VALUE;
		}
	}
	public void setMaxPathSizeFromConsole() {
		this.maxPathSize = ConsoleUtil.getOptionFromConsole("Choose the maximum number of interfaces in a path (-1 for no limit): ", 1, Integer.MAX_VALUE,0, true);
		if(this.maxPathSize == -1){
			this.maxPathSize = Integer.MAX_VALUE;
		}
	}
	public void setMaxNewBindingsFromConsole() {
		this.maxNewBindings = ConsoleUtil.getOptionFromConsole("Choose the maximum number of new bindings in a path (-1 for no limit): ", 0, Integer.MAX_VALUE,0, true);
		if(this.maxNewBindings == -1){
			this.maxNewBindings = Integer.MAX_VALUE;
		}
	}
	public void setMaxNewPossibleFromConsole() {
		this.maxNewPossible = ConsoleUtil.getOptionFromConsole("Choose the maximum number of interfaces of possible equipment (-1 for no limit): ", 0, Integer.MAX_VALUE,0, true);
		if(this.maxNewPossible == -1){
			this.maxNewPossible = Integer.MAX_VALUE;
		}
	}
	public void setPriorityOptionFromConsole(String possibleEquipFile) throws Exception {
		ArrayList<String> options = new ArrayList<String>();
		options.add("minimum number of interfaces");
		options.add("minimum number of new bindings");
		if(!possibleEquipFile.equals("")){
			options.add("minimum number of interfaces of possible equipment");
		}
		
		this.priorityOption = ConsoleUtil.chooseOne(options, "Priority", "Choose the Priority:", 0, false, false);
	}
	public int getDeclaredReplications() {
		return declaredReplications;
	}
	public int getQtShortPaths() {
		return qtShortPaths;
	}
	public void setDeclaredReplications(int declaredReplications) {
		this.declaredReplications = declaredReplications;
	}
	public void setQtShortPaths(int qtShortPaths) {
		this.qtShortPaths = qtShortPaths;
	}
	public void setMaxPathSize(int maxPathSize) {
		this.maxPathSize = maxPathSize;
	}	
	public int getMaxNewBindings() {
		return maxNewBindings;
	}
	public int getMaxNewPossible() {
		return maxNewPossible;
	}
	public int getPriorityOption() {
		return priorityOption;
	}
	public int getMaxPathSize() {
		return maxPathSize;
	}
	public void setMaxNewBindings(int maxNewBindings) {
		this.maxNewBindings = maxNewBindings;
	}
	public void setMaxNewPossible(int maxNewPossible) {
		this.maxNewPossible = maxNewPossible;
	}
	public void setPriorityOption(int priorityOption) {
		this.priorityOption = priorityOption;
	}
}
