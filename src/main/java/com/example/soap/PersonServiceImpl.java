package com.example.soap;

import com.example.soap.DatabaseConnection;
import com.example.soap.PersonService;
import jakarta.jws.WebService;
import jakarta.xml.soap.SOAPFactory;
import jakarta.xml.soap.SOAPFault;
import jakarta.xml.ws.soap.SOAPFaultException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebService(endpointInterface = "com.example.soap.PersonService")
public class PersonServiceImpl implements PersonService {
    Connection connection = DatabaseConnection.getConnection();

    public PersonServiceImpl() throws SQLException {
    }

    @Override
    public String addPerson(String nom, int age) {
        String query = "INSERT INTO person (nom, age) VALUES (?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nom);
            statement.setInt(2, age);
            statement.executeQuery();
            return nom + "added Seccessfully";
        } catch (SQLException e) {
            throw createSOAPFault("Failed to add personne: " + e.getMessage());
        }

    }

    @Override
    public String getPerson(int id) {
        String query = "SELECT * FROM person WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return "ID: " + resultSet.getInt("id") + ", Name: " + resultSet.getString("nom") + ", Age: " + resultSet.getInt("age");
            } else {
                throw createSOAPFault("Personne not found with ID: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String updatePerson(int id, String nom, int age) {
        String query = "UPDATE person SET nom = ?, age = ? WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nom);
            statement.setInt(2, age);
            statement.setInt(3, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return "Personne updated with ID: " + id;
            } else {
                throw createSOAPFault("Personne not found with ID: " + id);
            }
        } catch (SQLException e) {
            throw createSOAPFault("Failed to update personne: " + e.getMessage());
        }
    }

    @Override
    public String deletePerson(int id) {
        String query = "DELETE FROM person WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return "Personne deleted with ID: " + id;
            } else {
                throw createSOAPFault("Personne not found with ID: " + id);
            }
        } catch (SQLException e) {
            throw createSOAPFault("Failed to delete personne: " + e.getMessage());
        }
    }
    private SOAPFaultException createSOAPFault(String message) {
        try {
            SOAPFault fault = SOAPFactory.newInstance().createFault();
            fault.setFaultString(message);
            return new SOAPFaultException(fault);
        } catch (Exception e) {
            throw new RuntimeException("Error creating SOAP fault", e);
        }
    }
}
