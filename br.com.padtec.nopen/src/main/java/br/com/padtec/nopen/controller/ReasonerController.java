package br.com.padtec.nopen.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ReasonerController {

	@RequestMapping("/reasoner")
	public String reasonerRequest(HttpServletRequest request) 
	{
		return "reasoner";
	}
}
