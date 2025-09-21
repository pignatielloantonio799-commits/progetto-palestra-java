package it.unina.bd.progpoo.gymmanager.dao.impl;

import it.unina.bd.progpoo.gymmanager.dao.ClienteDAO;
import it.unina.bd.progpoo.gymmanager.dao.CorsoDAO;
import it.unina.bd.progpoo.gymmanager.dao.PrenotazioneDAO;
import it.unina.bd.progpoo.gymmanager.db.DatabaseConnection;
import it.unina.bd.progpoo.gymmanager.model.Cliente;
import it.unina.bd.progpoo.gymmanager.model.Corso;
import it.unina.bd.progpoo.gymmanager.model.Prenotazione;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class PrenotazioneDAOImpl implements PrenotazioneDAO {
    
    private final ClienteDAO clienteDAO;
    private final CorsoDAO corsoDAO;

    // Nuovo costruttore che accetta le dipendenze
    public PrenotazioneDAOImpl(ClienteDAO clienteDAO, CorsoDAO corsoDAO) {
        this.clienteDAO = clienteDAO;
        this.corsoDAO = corsoDAO;
    }

    @Override
    public void insert(Prenotazione prenotazione) throws SQLException {
        // Logica di validazione: Assicurati che cliente e corso esistano
        Cliente cliente = clienteDAO.findById(prenotazione.getCliente().getId());
        Corso corso = corsoDAO.findById(prenotazione.getCorso().getIdCorso());
        
        if (cliente == null) {
            throw new SQLException("Errore: Il cliente specificato non esiste.");
        }
        if (corso == null) {
            throw new SQLException("Errore: Il corso specificato non esiste.");
        }
        
        // Logica di business: Verifica disponibilità
        if (corso.getPrenotazioniAttuali() >= corso.getMaxPartecipanti()) {
            throw new SQLException("Errore: Il corso ha raggiunto il numero massimo di prenotazioni.");
        }
        
        String query = "INSERT INTO prenotazione (id_cliente, id_corso, data_prenotazione, stato) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, prenotazione.getCliente().getId());
            ps.setInt(2, prenotazione.getCorso().getIdCorso());
            ps.setDate(3, new Date(prenotazione.getDataPrenotazione().getTime()));
            ps.setString(4, prenotazione.getStato());
            ps.executeUpdate();
            
            // Aggiorna il contatore delle prenotazioni nel corso
            corso.setPrenotazioniAttuali(corso.getPrenotazioniAttuali() + 1);
            corsoDAO.update(corso);
        }
    }

    @Override
    public void delete(int idPrenotazione) throws SQLException {
        Prenotazione prenotazione = findById(idPrenotazione);
        if (prenotazione != null) {
            Corso corso = prenotazione.getCorso();
            if (corso != null) {
                // Diminuisci il contatore delle prenotazioni nel corso
                corso.setPrenotazioniAttuali(corso.getPrenotazioniAttuali() - 1);
                corsoDAO.update(corso);
            }
        }
        
        String query = "DELETE FROM prenotazione WHERE id_prenotazione = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idPrenotazione);
            ps.executeUpdate();
        }
    }
    
    @Override
    public void updateStato(int idPrenotazione, String nuovoStato) throws SQLException {
        String query = "UPDATE prenotazione SET stato = ? WHERE id_prenotazione = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nuovoStato);
            ps.setInt(2, idPrenotazione);
            ps.executeUpdate();
        }
    }

    @Override
    public Prenotazione findById(int idPrenotazione) throws SQLException {
        String query = "SELECT id_prenotazione, id_cliente, id_corso, data_prenotazione, stato FROM prenotazione WHERE id_prenotazione = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idPrenotazione);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractPrenotazioneFromResultSet(rs);
                }
            }
        }
        return null;
    }
    
    @Override
    public void deleteByClienteId(int idCliente) throws SQLException {
        // Logica per aggiornare il contatore delle prenotazioni nei corsi
        List<Prenotazione> prenotazioniDelCliente = findByCliente(idCliente);
        for (Prenotazione p : prenotazioniDelCliente) {
            Corso corso = p.getCorso();
            if (corso != null) {
                corso.setPrenotazioniAttuali(corso.getPrenotazioniAttuali() - 1);
                corsoDAO.update(corso);
            }
        }
        
        String query = "DELETE FROM prenotazione WHERE id_cliente = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idCliente);
            ps.executeUpdate();
        }
    }
    
    @Override
    public List<Prenotazione> findByCliente(int idCliente) throws SQLException {
        List<Prenotazione> prenotazioni = new ArrayList<>();
        String query = "SELECT id_prenotazione, id_cliente, id_corso, data_prenotazione, stato FROM prenotazione WHERE id_cliente = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    prenotazioni.add(extractPrenotazioneFromResultSet(rs));
                }
            }
        }
        return prenotazioni;
    }

    @Override
    public List<Prenotazione> findByCorso(int idCorso) throws SQLException {
        List<Prenotazione> prenotazioni = new ArrayList<>();
        String query = "SELECT id_prenotazione, id_cliente, id_corso, data_prenotazione, stato FROM prenotazione WHERE id_corso = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idCorso);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    prenotazioni.add(extractPrenotazioneFromResultSet(rs));
                }
            }
        }
        return prenotazioni;
    }
    
    private Prenotazione extractPrenotazioneFromResultSet(ResultSet rs) throws SQLException {
        int clienteId = rs.getInt("id_cliente");
        Cliente cliente = clienteDAO.findById(clienteId);
        
        int corsoId = rs.getInt("id_corso");
        Corso corso = corsoDAO.findById(corsoId);
        
        return new Prenotazione(
            rs.getInt("id_prenotazione"),
            rs.getDate("data_prenotazione"),
            rs.getString("stato"),
            cliente,
            corso
        );
    }
}