package com.example.service;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.soap.SOAPFaultException;

@WebService
public interface PersonneService {
    
    @WebMethod
    String addPersonne(String nom, int age) throws SOAPFaultException;
    
    @WebMethod
    Personne getPersonne(int id) throws SOAPFaultException;
    
    @WebMethod
    String updatePersonne(int id, String nom, int age) throws SOAPFaultException;
    
    @WebMethod
    String deletePersonne(int id) throws SOAPFaultException;
}
