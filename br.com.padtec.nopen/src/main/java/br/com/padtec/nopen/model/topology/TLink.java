package br.com.padtec.nopen.model.topology;



public class TLink {

	String	id;
	TNode 	nodeSource,
			nodeTarget;
	
	public TLink(String id, TNode nodeSource, TNode nodeTarget){
		this.id = id;
		this.nodeSource = nodeSource;
		this.nodeTarget = nodeTarget;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public TNode getNodeSource() {
		return nodeSource;
	}
	public void setNodeSource(TNode nodeSource) {
		this.nodeSource = nodeSource;
	}
	public TNode getNodeTarget() {
		return nodeTarget;
	}
	public void setNodeTarget(TNode nodeTarget) {
		this.nodeTarget = nodeTarget;
	}
	
	
}
