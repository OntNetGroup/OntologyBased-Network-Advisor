package provisioning;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class Path {
	private List<Interface> interfaceList = new ArrayList<Interface>();
	private int qtDeclared = 0;
	private int qtPossible = 0;
	
	public Path(TreeNode[] treeNodes) {
		for (TreeNode treeNode : treeNodes) {
			DefaultMutableTreeNode mulTreeNode = (DefaultMutableTreeNode) treeNode;
			Interface intfc = (Interface) mulTreeNode.getUserObject();
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
	
	@Override
	public String toString() {
		String ret = "";
		for (Interface intfc : interfaceList) {
			ret += intfc;
			ret += " -> ";
		}
		ret += "\n\t";
		ret += "size (total = ";
		ret += interfaceList.size();
		ret += ", declared = ";
		ret += this.qtDeclared;
		ret += ", possible = ";
		ret += this.qtPossible;
		ret += ")\n";
		
		return ret;
	}
}
