package br.ufes.inf.nemo.okco.service;

import java.rmi.RemoteException;

public class ProgramClient {

	public static void main(String[] args) {

		CalculadoraWebService webservice = new CalculadoraWebServiceProxy();
        
        try {
            double x, y, result;
             
            x = 5;
            y = 4;
             
            result = webservice.calcular(x, y);
             
            System.out.println(result);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

	}

}
