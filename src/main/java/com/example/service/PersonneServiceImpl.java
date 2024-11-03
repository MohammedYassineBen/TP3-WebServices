package com.example.service;

import javax.jws.WebService;
import javax.xml.ws.soap.SOAPFaultException;
import javax.xml.ws.soap.SOAPBinding;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import java.sql.*;

@WebService(endpointInterface = "com.example.service.PersonneService")
public class PersonneServiceImpl implements PersonneService {

    private static final String URL = "jdbc:mysql://localhost:3306/gestion_personnes";
    private static final String USER = "root";
    private static final String PASSWORD = "votre_password";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String addPersonne(String nom, int age) throws SOAPFaultException {
        String result = "Personne ajoutée avec succès.";
        String sql = "INSERT INTO Personne (nom, age) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nom);
            stmt.setInt(2, age);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw createSOAPFaultException("Erreur lors de l'ajout de la personne: " + e.getMessage());
        }

        return result;
    }

    @Override
    public Personne getPersonne(int id) throws SOAPFaultException {
        String sql = "SELECT * FROM Personne WHERE id = ?";
        Personne personne = null;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                personne = new Personne(rs.getInt("id"), rs.getString("nom"), rs.getInt("age"));
            } else {
                throw createSOAPFaultException("Personne avec l'id " + id + " non trouvée.");
            }

        } catch (SQLException e) {
            throw createSOAPFaultException("Erreur lors de la récupération de la personne: " + e.getMessage());
        }

        return personne;
    }

    @Override
    public String updatePersonne(int id, String nom, int age) throws SOAPFaultException {
        String result = "Personne mise à jour avec succès.";
        String sql = "UPDATE Personne SET nom = ?, age = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nom);
            stmt.setInt(2, age);
            stmt.setInt(3, id);
            int rows = stmt.executeUpdate();

            if (rows == 0) {
                throw createSOAPFaultException("Personne avec l'id " + id + " non trouvée.");
            }

        } catch (SQLException e) {
            throw createSOAPFaultException("Erreur lors de la mise à jour de la personne: " + e.getMessage());
        }

        return result;
    }

    @Override
    public String deletePersonne(int id) throws SOAPFaultException {
        String result = "Personne supprimée avec succès.";
        String sql = "DELETE FROM Personne WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();

            if (rows == 0) {
                throw createSOAPFaultException("Personne avec l'id " + id + " non trouvée.");
            }

        } catch (SQLException e) {
            throw createSOAPFaultException("Erreur lors de la suppression de la personne: " + e.getMessage());
        }

        return result;
    }

    private SOAPFaultException createSOAPFaultException(String message) {
        try {
            SOAPFault fault = SOAPFactory.newInstance().createFault();
            fault.setFaultString(message);
            return new SOAPFaultException(fault);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
