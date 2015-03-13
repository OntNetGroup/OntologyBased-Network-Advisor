package br.com.padtec.networktopology;

public class Link {

	int id;
	Node 	nodeSource,
			nodeTarget;
	
	public Link(int id, Node nodeSource, Node nodeTarget){
		this.id = id;
		this.nodeSource = nodeSource;
		this.nodeTarget = nodeTarget;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Node getNodeSource() {
		return nodeSource;
	}
	public void setNodeSource(Node nodeSource) {
		this.nodeSource = nodeSource;
	}
	public Node getNodeTarget() {
		return nodeTarget;
	}
	public void setNodeTarget(Node nodeTarget) {
		this.nodeTarget = nodeTarget;
	}
	
	
}
