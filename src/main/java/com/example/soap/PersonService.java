package com.example.soap;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

import java.sql.SQLException;

@WebService(targetNamespace = "http://soap.example.com")
public interface PersonService {

    @WebMethod
    String addPerson(String nom, int age) throws SQLException;

    @WebMethod
    String getPerson(int id);

    @WebMethod
    String updatePerson(int id, String nom, int age);

    @WebMethod
    String deletePerson(int id);
}
