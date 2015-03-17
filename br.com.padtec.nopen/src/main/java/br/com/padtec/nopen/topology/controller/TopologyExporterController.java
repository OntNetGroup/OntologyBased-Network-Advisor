package br.com.padtec.nopen.topology.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.padtec.nopen.topology.service.TopologyExporter;

public class TopologyExporterController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException ,IOException {
		
		TopologyExporter topology = new TopologyExporter();
		topology.exportTopology(request, response);
		
	}
	
}


