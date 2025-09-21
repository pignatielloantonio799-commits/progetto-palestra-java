package it.unina.bd.progpoo.gymmanager.dao.impl;

import it.unina.bd.progpoo.gymmanager.dao.AbbonamentoDAO;
import it.unina.bd.progpoo.gymmanager.dao.ClienteDAO;
import it.unina.bd.progpoo.gymmanager.db.DatabaseConnection;
import it.unina.bd.progpoo.gymmanager.model.Abbonamento;
import it.unina.bd.progpoo.gymmanager.model.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AbbonamentoDAOImpl implements AbbonamentoDAO {

    private final ClienteDAO clienteDAO;

    /**
     * Costruttore che accetta un'istanza di ClienteDAO.
     * Questo approccio risolve il problema delle dipendenze.
     * @param clienteDAO L'istanza di ClienteDAO da utilizzare per le operazioni sui clienti.
     */
    public AbbonamentoDAOImpl(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }
    
    @Override
    public void insert(Abbonamento abbonamento) throws SQLException {
        String query = "INSERT INTO abbonamento (tipo_abbonamento, data_inizio, data_scadenza, costo, cliente_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, abbonamento.getTipoAbbonamento());
            ps.setDate(2, new java.sql.Date(abbonamento.getDataInizio().getTime()));
            ps.setDate(3, new java.sql.Date(abbonamento.getDataScadenza().getTime()));
            ps.setDouble(4, abbonamento.getCosto());
            ps.setInt(5, abbonamento.getCliente().getId());
            ps.executeUpdate();
        }
    }

    @Override
    public Abbonamento findById(int idAbbonamento) throws SQLException {
        String query = "SELECT * FROM abbonamento WHERE id_abbonamento = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idAbbonamento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractAbbonamentoFromResultSet(rs);
                }
            }
        }
        return null;
    }
    
    @Override
    public Abbonamento findByClienteId(int idCliente) throws SQLException {
        String query = "SELECT * FROM abbonamento WHERE cliente_id = ? ORDER BY data_scadenza DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractAbbonamentoFromResultSet(rs);
                }
            }
        }
        return null;
    }

    @Override
    public Abbonamento findByCodiceCliente(String codiceCliente) throws SQLException {
        Cliente cliente = clienteDAO.findByCodiceCliente(codiceCliente);
        if (cliente == null) {
            return null; // Il cliente non esiste, non c'è un abbonamento da trovare
        }

        String query = "SELECT * FROM abbonamento WHERE cliente_id = ? ORDER BY data_scadenza DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, cliente.getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractAbbonamentoFromResultSet(rs);
                }
            }
        }
        return null;
    }

    @Override
    public void update(Abbonamento abbonamento) throws SQLException {
        String query = "UPDATE abbonamento SET tipo_abbonamento = ?, data_inizio = ?, data_scadenza = ?, costo = ?, cliente_id = ? WHERE id_abbonamento = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, abbonamento.getTipoAbbonamento());
            ps.setDate(2, new java.sql.Date(abbonamento.getDataInizio().getTime()));
            ps.setDate(3, new java.sql.Date(abbonamento.getDataScadenza().getTime()));
            ps.setDouble(4, abbonamento.getCosto());
            ps.setInt(5, abbonamento.getCliente().getId());
            ps.setInt(6, abbonamento.getIdAbbonamento());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int idAbbonamento) throws SQLException {
        String query = "DELETE FROM abbonamento WHERE id_abbonamento = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idAbbonamento);
            ps.executeUpdate();
        }
    }

    private Abbonamento extractAbbonamentoFromResultSet(ResultSet rs) throws SQLException {
        Abbonamento abbonamento = new Abbonamento();
        abbonamento.setIdAbbonamento(rs.getInt("id_abbonamento"));
        abbonamento.setTipoAbbonamento(rs.getString("tipo_abbonamento"));
        abbonamento.setDataInizio(rs.getDate("data_inizio"));
        abbonamento.setDataScadenza(rs.getDate("data_scadenza"));
        abbonamento.setCosto(rs.getDouble("costo"));
        
        int clienteId = rs.getInt("cliente_id");
        if (clienteId > 0) {
            Cliente cliente = clienteDAO.findById(clienteId);
            abbonamento.setCliente(cliente);
        }
        return abbonamento;
    }
}