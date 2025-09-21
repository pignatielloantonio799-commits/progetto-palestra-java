package it.unina.bd.progpoo.gymmanager.dao.impl;

import it.unina.bd.progpoo.gymmanager.dao.IstruttoreDAO;
import it.unina.bd.progpoo.gymmanager.db.DatabaseConnection;
import it.unina.bd.progpoo.gymmanager.model.Istruttore;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; // Import necessario
import java.util.ArrayList;
import java.util.List;

public class IstruttoreDAOImpl implements IstruttoreDAO {

    @Override
    public int insert(Istruttore istruttore) throws SQLException {
        String query = "INSERT INTO istruttore (nome, cognome, email, telefono, matricola, data_assunzione, stipendio, specializzazione, anni_esperienza) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int generatedId = -1;
        
        // Aggiunto Statement.RETURN_GENERATED_KEYS per ottenere l'ID
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, istruttore.getNome());
            ps.setString(2, istruttore.getCognome());
            ps.setString(3, istruttore.getEmail());
            ps.setString(4, istruttore.getTelefono());
            ps.setString(5, istruttore.getMatricola());
            ps.setDate(6, new java.sql.Date(istruttore.getDataAssunzione().getTime()));
            ps.setDouble(7, istruttore.getStipendio());
            ps.setString(8, istruttore.getSpecializzazione());
            ps.setInt(9, istruttore.getAnniEsperienza());
            
            ps.executeUpdate();

            // Recupera l'ID generato dal database
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1); // Usa l'indice 1 per recuperare il valore
                }
            }
        }
        return generatedId;
    }

    @Override
    public Istruttore findById(int id) throws SQLException {
        // Query corretta: usa 'id' invece di 'id_istruttore'
        String query = "SELECT * FROM istruttore WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractIstruttoreFromResultSet(rs);
                }
            }
        }
        return null;
    }

    @Override
    public Istruttore findByNomeCognome(String nome, String cognome) throws SQLException {
        String query = "SELECT * FROM istruttore WHERE nome = ? AND cognome = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nome);
            ps.setString(2, cognome);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractIstruttoreFromResultSet(rs);
                }
            }
        }
        return null;
    }

    @Override
    public Istruttore findByMatricola(String matricola) throws SQLException {
        String query = "SELECT * FROM istruttore WHERE matricola = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, matricola);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractIstruttoreFromResultSet(rs);
                }
            }
        }
        return null;
    }
    
    @Override
    public List<Istruttore> findAll() throws SQLException {
        List<Istruttore> istruttori = new ArrayList<>();
        String query = "SELECT * FROM istruttore";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                istruttori.add(extractIstruttoreFromResultSet(rs));
            }
        }
        return istruttori;
    }

    @Override
    public void update(Istruttore istruttore) throws SQLException {
        // Query corretta: usa 'id' invece di 'id_istruttore'
        String query = "UPDATE istruttore SET nome = ?, cognome = ?, email = ?, telefono = ?, matricola = ?, data_assunzione = ?, stipendio = ?, specializzazione = ?, anni_esperienza = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, istruttore.getNome());
            ps.setString(2, istruttore.getCognome());
            ps.setString(3, istruttore.getEmail());
            ps.setString(4, istruttore.getTelefono());
            ps.setString(5, istruttore.getMatricola());
            ps.setDate(6, new java.sql.Date(istruttore.getDataAssunzione().getTime()));
            ps.setDouble(7, istruttore.getStipendio());
            ps.setString(8, istruttore.getSpecializzazione());
            ps.setInt(9, istruttore.getAnniEsperienza());
            ps.setInt(10, istruttore.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        // Query corretta: usa 'id' invece di 'id_istruttore'
        String query = "DELETE FROM istruttore WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    
    private Istruttore extractIstruttoreFromResultSet(ResultSet rs) throws SQLException {
        Istruttore istruttore = new Istruttore();
        // Usa il nome di colonna 'id' corretto
        istruttore.setId(rs.getInt("id"));
        istruttore.setNome(rs.getString("nome"));
        istruttore.setCognome(rs.getString("cognome"));
        istruttore.setEmail(rs.getString("email"));
        istruttore.setTelefono(rs.getString("telefono"));
        istruttore.setMatricola(rs.getString("matricola"));
        istruttore.setDataAssunzione(rs.getDate("data_assunzione"));
        istruttore.setStipendio(rs.getDouble("stipendio"));
        istruttore.setSpecializzazione(rs.getString("specializzazione"));
        istruttore.setAnniEsperienza(rs.getInt("anni_esperienza"));
        return istruttore;
    }
}