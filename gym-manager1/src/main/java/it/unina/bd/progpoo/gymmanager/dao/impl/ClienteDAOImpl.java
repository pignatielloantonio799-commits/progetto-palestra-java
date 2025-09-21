package it.unina.bd.progpoo.gymmanager.dao.impl;

import it.unina.bd.progpoo.gymmanager.dao.ClienteDAO;
import it.unina.bd.progpoo.gymmanager.dao.PrenotazioneDAO;
import it.unina.bd.progpoo.gymmanager.db.DatabaseConnection;
import it.unina.bd.progpoo.gymmanager.model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClienteDAOImpl implements ClienteDAO {

    private static final String CF_REGEX = "^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$";
    private static final Pattern CF_PATTERN = Pattern.compile(CF_REGEX);
    private static final String TEL_REGEX = "^[0-9]{10}$";
    private static final Pattern TEL_PATTERN = Pattern.compile(TEL_REGEX);

    private PrenotazioneDAO prenotazioneDAO;

    // Rimuovi il costruttore precedente e usa questo
    public ClienteDAOImpl(PrenotazioneDAO prenotazioneDAO) {
        this.prenotazioneDAO = prenotazioneDAO;
    }

    // Setter per risolvere inizializzazione circolare
    public void setPrenotazioneDAO(PrenotazioneDAO prenotazioneDAO) {
        this.prenotazioneDAO = prenotazioneDAO;
    }


    @Override
    public int insert(Cliente cliente) throws SQLException {
        if (!isValidCodiceFiscale(cliente.getCodiceFiscale())) {
            throw new SQLException("Errore: Il codice fiscale non è valido.");
        }
        if (!isValidTelefono(cliente.getTelefono())) {
            throw new SQLException("Errore: Il numero di telefono non è valido. Deve contenere esattamente 10 cifre.");
        }
        
        // Controllo per evitare duplicati basati sul codice fiscale
        if (findByCodiceFiscale(cliente.getCodiceFiscale()) != null) {
            throw new SQLException("Errore: Un cliente con questo codice fiscale esiste già.");
        }

        Date dataIscrizione = cliente.getDataIscrizione() != null ? cliente.getDataIscrizione() : new Date();
        cliente.setDataIscrizione(dataIscrizione);
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataIscrizione);
        
        switch (cliente.getTipoAbbonamento().toLowerCase()) {
            case "mensile":
                cal.add(Calendar.MONTH, 1);
                break;
            case "trimestrale":
                cal.add(Calendar.MONTH, 3);
                break;
            case "annuale":
                cal.add(Calendar.MONTH, 12);
                break;
            default:
                break;
        }
        cliente.setDataScadenzaAbbonamento(cal.getTime());

        String sql = "INSERT INTO cliente (nome, cognome, codice_fiscale, data_nascita, email, telefono, codice_cliente, data_iscrizione, data_scadenza_abbonamento, tipo_abbonamento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int generatedId = -1;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCognome());
            ps.setString(3, cliente.getCodiceFiscale());
            ps.setDate(4, new java.sql.Date(cliente.getDataNascita().getTime()));
            ps.setString(5, cliente.getEmail());
            ps.setString(6, cliente.getTelefono());
            ps.setString(7, cliente.getCodiceCliente());
            ps.setDate(8, new java.sql.Date(cliente.getDataIscrizione().getTime()));
            ps.setDate(9, new java.sql.Date(cliente.getDataScadenzaAbbonamento().getTime()));
            ps.setString(10, cliente.getTipoAbbonamento());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }
        }
        return generatedId;
    }

    @Override
    public void delete(int idCliente) throws SQLException {
        prenotazioneDAO.deleteByClienteId(idCliente);

        String sql = "DELETE FROM cliente WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            stmt.executeUpdate();
        }
    }

    @Override
    public Cliente findById(int id) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractClienteFromResultSet(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Cliente> findAll() throws SQLException {
        List<Cliente> clienti = new ArrayList<>();
        String sql = "SELECT * FROM cliente";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                clienti.add(extractClienteFromResultSet(rs));
            }
        }
        return clienti;
    }

    @Override
    public void update(Cliente cliente) throws SQLException {
        String sql = "UPDATE cliente SET nome = ?, cognome = ?, codice_fiscale = ?, data_nascita = ?, email = ?, telefono = ?, codice_cliente = ?, data_iscrizione = ?, data_scadenza_abbonamento = ?, tipo_abbonamento = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCognome());
            stmt.setString(3, cliente.getCodiceFiscale());
            stmt.setDate(4, new java.sql.Date(cliente.getDataNascita().getTime()));
            stmt.setString(5, cliente.getEmail());
            stmt.setString(6, cliente.getTelefono());
            stmt.setString(7, cliente.getCodiceCliente());
            stmt.setDate(8, new java.sql.Date(cliente.getDataIscrizione().getTime()));
            stmt.setDate(9, new java.sql.Date(cliente.getDataScadenzaAbbonamento().getTime()));
            stmt.setString(10, cliente.getTipoAbbonamento());
            stmt.setInt(11, cliente.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public Cliente findByCodiceFiscale(String codiceFiscale) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE codice_fiscale = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codiceFiscale);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractClienteFromResultSet(rs);
                }
            }
        }
        return null;
    }

    @Override
    public Cliente findByCodiceCliente(String codiceCliente) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE codice_cliente = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codiceCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractClienteFromResultSet(rs);
                }
            }
        }
        return null;
    }

    private boolean isValidCodiceFiscale(String cf) {
        if (cf == null || cf.length() != 16) {
            return false;
        }
        Matcher matcher = CF_PATTERN.matcher(cf.toUpperCase());
        return matcher.matches();
    }

    private boolean isValidTelefono(String tel) {
        if (tel == null) {
            return false;
        }
        Matcher matcher = TEL_PATTERN.matcher(tel);
        return matcher.matches();
    }

    private Cliente extractClienteFromResultSet(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setCognome(rs.getString("cognome"));
        cliente.setCodiceFiscale(rs.getString("codice_fiscale"));
        cliente.setDataNascita(rs.getDate("data_nascita"));
        cliente.setEmail(rs.getString("email"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setCodiceCliente(rs.getString("codice_cliente"));
        cliente.setDataIscrizione(rs.getDate("data_iscrizione"));
        cliente.setDataScadenzaAbbonamento(rs.getDate("data_scadenza_abbonamento"));
        cliente.setTipoAbbonamento(rs.getString("tipo_abbonamento"));
        return cliente;
    }
}