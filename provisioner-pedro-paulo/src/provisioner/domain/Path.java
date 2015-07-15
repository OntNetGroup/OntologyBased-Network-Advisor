package provisioner.domain;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class Path implements Comparable<Path>{
	private List<Interface> interfaceList = new ArrayList<Interface>();
	private int qtDeclared = 0;
	private int qtPossible = 0;
	
	private int qtBindedInterfaces = 0;
	
	public Path(TreeNode[] treeNodes) {
		for (TreeNode treeNode : treeNodes) {
			DefaultMutableTreeNode mulTreeNode = (DefaultMutableTreeNode) treeNode;
			Interface intfc = (Interface) mulTreeNode.getUserObject();
			addInterface(intfc);
//			interfaceList.add(intfc);
//			if(intfc.isDeclared()){
//				this.qtDeclared++;
//			}else{
//				this.qtPossible++;
//			}
		}		
	}
	
	public Path(List<Interface> interfaceList) {
		 this.interfaceList.addAll(interfaceList);
	}
	
	public Path() {
		
	}
	
	public void addInterface(Interface intf, boolean isBinded){
		addInterface(intf);
		if(isBinded) qtBindedInterfaces++;
	}
	
	public void addInterface(Interface intf){
		this.interfaceList.add(intf);
		if(intf.isDeclared()){
			this.qtDeclared++;
		}else{
			this.qtPossible++;
		}
	}
	
	public void addInterfaceInBegin(Interface intf){
		this.interfaceList.add(0, intf);
	}
	
	public void addInterfaceInBegin(Interface intf, boolean isBinded){
		addInterfaceInBegin(intf);
		if(isBinded) qtBindedInterfaces++;
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
	
	public int newBinds(){
		int newBinds = (interfaceList.size() - this.qtBindedInterfaces - 2)/2;
		if(newBinds < 0) newBinds = 0;
		return newBinds;
	}
	
	public String sizeToString(){
		String ret = "size (interfaces = ";
		ret += interfaceList.size();
		ret += ", new bindings = ";
		ret += newBinds();
		ret += ", declared = ";
		ret += this.qtDeclared;
		ret += ", possible = ";
		ret += this.qtPossible;
		ret += ");";
		
		return ret;
	}
	
	@Override
	public String toString() {
		String ret = "";
		for (Interface intfc : interfaceList) {
			ret += intfc;
			ret += " -> ";
		}
		int last = ret.lastIndexOf(" -> ");
		ret = ret.substring(0, last);
		ret += "\n\t";
		ret += sizeToString();
		ret += "\n";
		
		return ret;
	}

	public int compareTo(Path arg0) {
		return 0;
	}
	public void setQtBindedInterfaces(List<Interface> bindedInterfaces) {
		for (Interface interface1 : interfaceList) {
			if(bindedInterfaces.contains(interface1)){
				this.qtBindedInterfaces++;
			}
		}
	}
}
