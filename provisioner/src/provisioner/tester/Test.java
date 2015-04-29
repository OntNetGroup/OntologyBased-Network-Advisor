package provisioner.tester;

import provisioner.util.ConsoleUtil;

public class Test {
	int declaredReplications;
	int qtShortPaths;
	int maxPathSize;
	
	public int getDeclaredReplications() {
		return declaredReplications;
	}
	public void setDeclaredReplicationsFromConsole() {
		this.declaredReplications = ConsoleUtil.getOptionFromConsole("Choose the number of layer replications", 2, Integer.MAX_VALUE);
	}
	public int getQtShortPaths() {
		return qtShortPaths;
	}
	public void setQtShortPathsFromConsole() {
		this.qtShortPaths = ConsoleUtil.getOptionFromConsole("Choose the number of paths (enter 0 to show all)", 0, Integer.MAX_VALUE);
	}
	public int getMaxPathSize() {
		return maxPathSize;
	}
	
	public void setMaxPathSizeFromConsole() {
		this.maxPathSize = ConsoleUtil.getOptionFromConsole("Choose the maximum number of interfaces in a path (enter 0 for no limit)", 0, Integer.MAX_VALUE);
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
}
