package br.ufes.inf.padtec.tnokco.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.inf.nemo.padtec.tnokco.TNOKCOGraphPlotting;

@Controller
public class MyProvising2 {

	@RequestMapping(method = RequestMethod.GET, value="/equipmentVisualizer")
	public String equipmentVisualizer(HttpSession session, HttpServletRequest request) {
		TNOKCOGraphPlotting graphPlotting = new TNOKCOGraphPlotting();
		
		String path = "http://localhost:8080/tnokco/Assets/owl/g800.owl"; 

		// Load Model

		HomeController.Model = HomeController.Repository.Open(path);
		HomeController.tmpModel = HomeController.Repository.Open(path);
		HomeController.NS = HomeController.Repository.getNameSpace(HomeController.Model);
		
		String arbor = graphPlotting.getArborStructureFromEquipmentVisualization(HomeController.Model);
		
		return "equipmentVisualizer";
	}
}
