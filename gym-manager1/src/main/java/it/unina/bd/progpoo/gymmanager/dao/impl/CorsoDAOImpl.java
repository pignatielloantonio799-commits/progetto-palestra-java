package it.unina.bd.progpoo.gymmanager.dao.impl;

import it.unina.bd.progpoo.gymmanager.dao.CorsoDAO;
import it.unina.bd.progpoo.gymmanager.dao.IstruttoreDAO;
import it.unina.bd.progpoo.gymmanager.db.DatabaseConnection;
import it.unina.bd.progpoo.gymmanager.model.Corso;
import it.unina.bd.progpoo.gymmanager.model.Istruttore;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class CorsoDAOImpl implements CorsoDAO {

    private IstruttoreDAO istruttoreDAO;

    // Nuovo costruttore che accetta IstruttoreDAO come parametro
    public CorsoDAOImpl(IstruttoreDAO istruttoreDAO) {
        this.istruttoreDAO = istruttoreDAO;
    }

    @Override
    public int insert(Corso corso) throws SQLException {
        // Logica di validazione per prevenire sovrapposizioni di orario
        if (hasTimeOverlap(corso.getSala(), new java.sql.Time(corso.getOrario().getTime()), corso.getDurata())) {
            throw new SQLException("Errore: Un corso si sovrappone a un corso esistente nella stessa sala.");
        }

        String query = "INSERT INTO corso (nome, orario, durata, max_partecipanti, sala, istruttore_id, prenotazioni_attuali, descrizione) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        int generatedId = -1;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, corso.getNome());
            ps.setTime(2, new java.sql.Time(corso.getOrario().getTime()));
            ps.setInt(3, corso.getDurata());
            ps.setInt(4, corso.getMaxPartecipanti());
            ps.setString(5, corso.getSala());
            
            if (corso.getIstruttore() != null) {
                ps.setInt(6, corso.getIstruttore().getId());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }
            
            ps.setInt(7, corso.getPrenotazioniAttuali());
            ps.setString(8, corso.getDescrizione()); 
            
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }
        }
        return generatedId;
    }

    private boolean hasTimeOverlap(String sala, Time newCourseTime, int newCourseDuration) throws SQLException {
        String query = "SELECT COUNT(*) FROM corso WHERE sala = ? AND (" +
                       "  (orario, orario + (durata || ' minutes')::interval) OVERLAPS (?::time, (? || ' minutes')::interval)" +
                       ")";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, sala);
            ps.setTime(2, newCourseTime);
            ps.setInt(3, newCourseDuration);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    @Override
    public Corso findById(int idCorso) throws SQLException {
        String query = "SELECT * FROM corso WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idCorso);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractCorsoFromResultSet(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Corso> findAll() throws SQLException {
        List<Corso> corsi = new ArrayList<>();
        String query = "SELECT * FROM corso";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                corsi.add(extractCorsoFromResultSet(rs));
            }
        }
        return corsi;
    }

    @Override
    public void update(Corso corso) throws SQLException {
        String query = "UPDATE corso SET nome = ?, orario = ?, durata = ?, max_partecipanti = ?, sala = ?, istruttore_id = ?, prenotazioni_attuali = ?, descrizione = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, corso.getNome());
            ps.setTime(2, new java.sql.Time(corso.getOrario().getTime()));
            ps.setInt(3, corso.getDurata());
            ps.setInt(4, corso.getMaxPartecipanti());
            ps.setString(5, corso.getSala());
            
            if (corso.getIstruttore() != null) {
                ps.setInt(6, corso.getIstruttore().getId());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }
            
            ps.setInt(7, corso.getPrenotazioniAttuali());
            ps.setString(8, corso.getDescrizione());
            ps.setInt(9, corso.getIdCorso());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int idCorso) throws SQLException {
        String query = "DELETE FROM corso WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idCorso);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Corso> visualizzaCorsiPerCliente(int idCliente) throws SQLException {
        List<Corso> corsi = new ArrayList<>();
        String query = "SELECT c.* FROM corso c JOIN prenotazione p ON c.id = p.corso_id WHERE p.cliente_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    corsi.add(extractCorsoFromResultSet(rs));
                }
            }
        }
        return corsi;
    }
    
    @Override
    public Corso findBySalaAndOrario(String sala, Time orario) throws SQLException {
        throw new UnsupportedOperationException("Questo metodo è obsoleto. Si prega di usare la logica di hasTimeOverlap.");
    }

    private Corso extractCorsoFromResultSet(ResultSet rs) throws SQLException {
        Corso corso = new Corso();
        corso.setIdCorso(rs.getInt("id"));
        corso.setNome(rs.getString("nome"));
        corso.setOrario(rs.getTime("orario"));
        corso.setDurata(rs.getInt("durata"));
        corso.setMaxPartecipanti(rs.getInt("max_partecipanti"));
        corso.setSala(rs.getString("sala"));
        corso.setPrenotazioniAttuali(rs.getInt("prenotazioni_attuali"));

        if (hasColumn(rs, "descrizione")) {
            corso.setDescrizione(rs.getString("descrizione"));
        }

        int istruttoreId = rs.getInt("istruttore_id");
        if (istruttoreId > 0) {
            Istruttore istruttore = istruttoreDAO.findById(istruttoreId);
            corso.setIstruttore(istruttore);
        }
        
        return corso;
    }
    
    private boolean hasColumn(ResultSet rs, String columnName) {
        try {
            rs.findColumn(columnName);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}