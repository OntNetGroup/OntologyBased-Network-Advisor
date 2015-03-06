package br.com.padtec.controller.advisor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ErrorController {
	@RequestMapping(method = RequestMethod.GET, value="/ops")
	public static String ops(HttpServletRequest request) 
	{
		return "ops";
	}	
}
