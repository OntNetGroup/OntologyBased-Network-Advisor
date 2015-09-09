package br.com.padtec.nopen.provisioning.service;

public class MatrixConnection {
	Port matrixInputPort, matrixOutputPort;
	
	public MatrixConnection(Port matrixInputPort, Port matrixOutputPort) {
		this.matrixInputPort = matrixInputPort;
		this.matrixOutputPort = matrixOutputPort;
	}
}
