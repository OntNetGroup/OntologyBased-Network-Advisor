package provisioning;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.TreeNode;

public class Path {
	private List<Interface> interfaceList = new ArrayList<Interface>();
	private int qtDeclared = 0;
	private int qtPossible = 0;
	
	public Path(TreeNode[] treeNodes) {
		for (TreeNode treeNode : treeNodes) {
			Interface intfc = (Interface) treeNode;
			interfaceList.add(intfc);
			if(intfc.isDeclared()){
				this.qtDeclared++;
			}else{
				this.qtPossible++;
			}
		}		
	}
	
	public int getQtDeclared() {
		return qtDeclared;
	}
	
	public int getQtPossible() {
		return qtPossible;
	}
	
	public List<Interface> getInterfaceList() {
		return interfaceList;
	}
	
	public int size(){
		return interfaceList.size();
	}
}
