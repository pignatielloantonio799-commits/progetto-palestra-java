package it.unina.bd.progpoo.gymmanager.dao.impl;

import it.unina.bd.progpoo.gymmanager.dao.AccessoDAO;
import it.unina.bd.progpoo.gymmanager.dao.ClienteDAO;
import it.unina.bd.progpoo.gymmanager.db.DatabaseConnection;
import it.unina.bd.progpoo.gymmanager.model.Accesso;
import it.unina.bd.progpoo.gymmanager.model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class AccessoDAOImpl implements AccessoDAO {

    private final ClienteDAO clienteDAO;

    public AccessoDAOImpl(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    @Override
    public void insert(Accesso nuovoAccesso) throws SQLException {
        // 1. Validazione: il cliente deve esistere
        Cliente cliente = clienteDAO.findById(nuovoAccesso.getCliente().getId());
        if (cliente == null) {
            throw new SQLException("Errore: Il cliente con ID " + nuovoAccesso.getCliente().getId() + " non esiste.");
        }

        // 2. Controllo: un cliente non può entrare se non è ancora uscito
        Accesso ultimoAccesso = findUltimoAccessoIngresso(nuovoAccesso.getCliente().getId());
        if (ultimoAccesso != null) {
            throw new SQLException("Errore: Il cliente ha già un accesso in corso. Registra prima l'uscita.");
        }

        String query = "INSERT INTO accesso (timestamp_ingresso, id_cliente) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setTimestamp(1, new Timestamp(nuovoAccesso.getTimestampIngresso().getTime()));
            ps.setInt(2, nuovoAccesso.getCliente().getId());
            ps.executeUpdate();
        }
    }
    
    public void updateUscita(int idCliente) throws SQLException {
        // Trova l'ultimo accesso incompleto per quel cliente
        Accesso ultimoAccesso = findUltimoAccessoIngresso(idCliente);
        if (ultimoAccesso == null) {
            throw new SQLException("Errore: Nessun accesso attivo trovato per il cliente " + idCliente + ".");
        }

        String query = "UPDATE accesso SET timestamp_uscita = ? WHERE id_accesso = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            ps.setInt(2, ultimoAccesso.getIdAccesso());
            ps.executeUpdate();
        }
    }

    private Accesso findUltimoAccessoIngresso(int idCliente) throws SQLException {
        String query = "SELECT * FROM accesso WHERE id_cliente = ? AND timestamp_uscita IS NULL ORDER BY timestamp_ingresso DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int idAccesso = rs.getInt("id_accesso");
                    Date timestampIngresso = new Date(rs.getTimestamp("timestamp_ingresso").getTime());
                    Date timestampUscita = rs.getTimestamp("timestamp_uscita"); // Sarà NULL in questo caso
                    
                    // Recupera l'oggetto Cliente usando il DAO iniettato
                    Cliente cliente = clienteDAO.findById(idCliente);

                    // 🆕 Utilizza il costruttore completo che accetta anche il timestampUscita
                    return new Accesso(idAccesso, timestampIngresso, timestampUscita, cliente);
                }
            }
        }
        return null;
    }
}