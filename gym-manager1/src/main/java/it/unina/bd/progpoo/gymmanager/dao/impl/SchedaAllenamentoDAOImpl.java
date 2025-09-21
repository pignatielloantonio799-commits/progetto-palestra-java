package it.unina.bd.progpoo.gymmanager.dao.impl;

import it.unina.bd.progpoo.gymmanager.dao.SchedaAllenamentoDAO;
import it.unina.bd.progpoo.gymmanager.dao.ClienteDAO;
import it.unina.bd.progpoo.gymmanager.dao.IstruttoreDAO;
import it.unina.bd.progpoo.gymmanager.db.DatabaseConnection;
import it.unina.bd.progpoo.gymmanager.model.SchedaAllenamento;
import it.unina.bd.progpoo.gymmanager.model.Cliente;
import it.unina.bd.progpoo.gymmanager.model.Istruttore;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SchedaAllenamentoDAOImpl implements SchedaAllenamentoDAO {

    private final ClienteDAO clienteDAO;
    private final IstruttoreDAO istruttoreDAO;

    // Aggiunto un costruttore che accetta le dipendenze
    public SchedaAllenamentoDAOImpl(ClienteDAO clienteDAO, IstruttoreDAO istruttoreDAO) {
        this.clienteDAO = clienteDAO;
        this.istruttoreDAO = istruttoreDAO;
    }

    @Override
    public void insert(SchedaAllenamento nuovaScheda) throws SQLException {
        // Logica di validazione: Assicurati che cliente e istruttore esistano
        Cliente cliente = clienteDAO.findById(nuovaScheda.getCliente().getId());
        Istruttore istruttore = istruttoreDAO.findById(nuovaScheda.getIstruttore().getId());

        if (cliente == null) {
            throw new SQLException("Errore: Il cliente specificato non esiste.");
        }
        if (istruttore == null) {
            throw new SQLException("Errore: L'istruttore specificato non esiste.");
        }
        
        String query = "INSERT INTO scheda_allenamento (data_creazione, data_scadenza, obiettivo, esercizi, id_cliente, id_istruttore) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setDate(1, new Date(nuovaScheda.getDataCreazione().getTime()));
            ps.setDate(2, new Date(nuovaScheda.getDataScadenza().getTime()));
            ps.setString(3, nuovaScheda.getObiettivo());
            
            // Creiamo un Array JDBC per gli esercizi
            String[] eserciziArray = nuovaScheda.getEsercizi().toArray(new String[0]);
            java.sql.Array sqlArray = conn.createArrayOf("text", eserciziArray);
            ps.setArray(4, sqlArray);
            
            ps.setInt(5, nuovaScheda.getCliente().getId());
            ps.setInt(6, nuovaScheda.getIstruttore().getId());

            ps.executeUpdate();
        }
    }

    @Override
    public SchedaAllenamento findByClienteId(int idCliente) throws SQLException {
        String query = "SELECT * FROM scheda_allenamento WHERE id_cliente = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractSchedaFromResultSet(rs);
                }
            }
        }
        return null;
    }
    
    private SchedaAllenamento extractSchedaFromResultSet(ResultSet rs) throws SQLException {
        // Recupera gli ID delle chiavi esterne
        int clienteId = rs.getInt("id_cliente");
        int istruttoreId = rs.getInt("id_istruttore");
        
        // Usa i DAO iniettati per recuperare gli oggetti
        Cliente cliente = this.clienteDAO.findById(clienteId);
        Istruttore istruttore = this.istruttoreDAO.findById(istruttoreId);
        
        // Recupera gli altri dati
        Date dataCreazione = rs.getDate("data_creazione");
        Date dataScadenza = rs.getDate("data_scadenza");
        String obiettivo = rs.getString("obiettivo");
        
        List<String> esercizi = new ArrayList<>();
        java.sql.Array sqlArray = rs.getArray("esercizi");
        if (sqlArray != null) {
            String[] arrayEsercizi = (String[]) sqlArray.getArray();
            esercizi = Arrays.asList(arrayEsercizi);
        }

        return new SchedaAllenamento(rs.getInt("id_scheda"), dataCreazione, dataScadenza, obiettivo, esercizi, cliente, istruttore);
    }
}