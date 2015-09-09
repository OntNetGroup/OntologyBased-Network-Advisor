package br.com.padtec.nopen.provisioning.service;

import java.util.ArrayList;
import java.util.List;

public class SNC {
	private List<MatrixConnection> matrixConnectionList = new ArrayList<MatrixConnection>();
	private List<Port> portList = new ArrayList<Port>();
	
	public SNC(){}
	
	public SNC(MatrixConnection matrixConnection) {
		addMatrixConnection(matrixConnection);
	}
	
	public SNC(List<MatrixConnection> matrixConnectionList) {
		 this.matrixConnectionList.addAll(matrixConnectionList);
	}
	
	public List<Port> getPortList() {
		return portList;
	}
	
	public void setPortList(List<Port> portList) {
		this.portList = portList;
	}
	
	public List<MatrixConnection> getMatrixConnectionList() {
		return matrixConnectionList;
	}
	
	public void addMatrixConnectionInBegin(MatrixConnection matrixConnection){
		this.matrixConnectionList.add(0, matrixConnection);
	}
	
	public void addMatrixConnection(MatrixConnection matrixConnection){
		this.matrixConnectionList.add(matrixConnection);
	}
	
	public int size(){
		return matrixConnectionList.size();
	}
	
	public void addPort(Port port){
		this.portList.add(port);
	}
	
	public Port getLastPort(){
		return this.portList.get(this.portList.size()-1);
	}
	
	public boolean containsPort(Port port){
		return this.portList.contains(port);
	}
}
