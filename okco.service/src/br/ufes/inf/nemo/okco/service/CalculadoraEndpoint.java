package br.ufes.inf.nemo.okco.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
 
@WebService(serviceName="CalculadoraEndpoint/calculadoraendpoint")
public class CalculadoraEndpoint implements CalculadoraWebService{
 
    @Override
    @WebMethod
    public double calcular(@WebParam(name = "x") double x,
            @WebParam(name = "y") double y) {
        return x + y;
    }
}