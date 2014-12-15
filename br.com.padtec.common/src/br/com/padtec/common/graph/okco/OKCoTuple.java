package br.com.padtec.common.graph.okco;

public class OKCoTuple{
	
	public String source;
	public boolean isSourceCenterNode = false;
	public String property;
	public String target;
	public boolean isTargetCenterNode = false;

	public OKCoTuple(String src, String prt, String trg, boolean isSourceCenterNode, boolean isTargetCenterNode) {
		source = src;
		property = prt;
		target = trg;
		this.isSourceCenterNode = isSourceCenterNode;
		this.isTargetCenterNode = isTargetCenterNode;
	}

	public boolean isInverse(OKCoTuple tupla){
		if(source.equals(tupla.target) && target.equals(tupla.source)){
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return source.substring(source.indexOf("#")+1)+" "+property+" "+target.substring(target.indexOf("#")+1);
	}
	
	/**
	 * Equals just if (this.source == tupla.source or this.source == tupla.target)
	 * */
	@Override
	public boolean equals(Object tupla) {
		if(source.equals(((OKCoTuple)tupla).source) && target.equals(((OKCoTuple)tupla).target)){
			return true;
		}
		return false;
	}
}
