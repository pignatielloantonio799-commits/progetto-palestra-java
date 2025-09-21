package it.unina.bd.progpoo.gymmanager.controller;

import it.unina.bd.progpoo.gymmanager.dao.AbbonamentoDAO;
import it.unina.bd.progpoo.gymmanager.dao.ClienteDAO;
import it.unina.bd.progpoo.gymmanager.db.DatabaseConnection;
import it.unina.bd.progpoo.gymmanager.model.Abbonamento;
import it.unina.bd.progpoo.gymmanager.model.Cliente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AbbonamentoController {

    private ClienteDAO clienteDAO;
    private AbbonamentoDAO abbonamentoDAO;

    public AbbonamentoController(ClienteDAO clienteDAO, AbbonamentoDAO abbonamentoDAO) {
        this.clienteDAO = clienteDAO;
        this.abbonamentoDAO = abbonamentoDAO;
    }

    /**
     * Registra un nuovo cliente e il suo primo abbonamento in una singola transazione.
     * @param nome Nome del cliente
     * @param cognome Cognome del cliente
     * @param cf Codice Fiscale del cliente
     * @param dataNascita Data di nascita del cliente
     * @param email Email del cliente
     * @param telefono Telefono del cliente
     * @param codiceCliente Codice univoco del cliente
     * @param tipoAbbonamento Tipo di abbonamento (es. Mensile, Annuale)
     * @throws SQLException Se si verifica un errore SQL durante l'operazione
     */
    public void registraCliente(String nome, String cognome, String cf, Date dataNascita,
                                String email, String telefono, String codiceCliente,
                                String tipoAbbonamento) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // 1. Disabilita l'autocommit per la transazione

            // Crea e inserisce il cliente
            Cliente nuovoCliente = new Cliente();
            nuovoCliente.setNome(nome);
            nuovoCliente.setCognome(cognome);
            nuovoCliente.setCodiceFiscale(cf);
            nuovoCliente.setDataNascita(dataNascita);
            nuovoCliente.setEmail(email);
            nuovoCliente.setTelefono(telefono);
            nuovoCliente.setCodiceCliente(codiceCliente);

            // 🟢 Correzione: Il metodo insert del tuo DAO deve restituire l'ID generato.
            // Il metodo insert deve essere modificato nel DAO per restituire un int.
            int clienteId = clienteDAO.insert(nuovoCliente);
            nuovoCliente.setId(clienteId);

            // Crea e inserisce l'abbonamento per il cliente
            Abbonamento abbonamento = new Abbonamento();
            abbonamento.setTipoAbbonamento(tipoAbbonamento);
            abbonamento.setDataInizio(new Date());
            abbonamento.setDataScadenza(calcolaDataScadenza(tipoAbbonamento));
            abbonamento.setCosto(calcolaCosto(tipoAbbonamento));
            abbonamento.setCliente(nuovoCliente);
            abbonamentoDAO.insert(abbonamento);

            conn.commit(); // 2. Conferma la transazione
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // 3. Annulla la transazione in caso di errore
                } catch (SQLException ex) {
                    throw new SQLException("Errore durante il rollback.", ex);
                }
            }
            throw new SQLException("Registrazione fallita.", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // 4. Ripristina l'autocommit
                    conn.close();
                } catch (SQLException ex) {
                    throw new SQLException("Errore durante la chiusura della connessione.", ex);
                }
            }
        }
    }
    
    /**
     * Aggiorna i dati di un cliente esistente.
     * @param cliente L'oggetto Cliente con i dati aggiornati.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public void updateCliente(Cliente cliente) throws SQLException {
        clienteDAO.update(cliente);
    }
    
    /**
     * Elimina un cliente dal database.
     * @param idCliente L'ID del cliente da eliminare.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public void deleteCliente(int idCliente) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Prima elimina l'abbonamento per mantenere l'integrità referenziale
            Abbonamento abbonamento = abbonamentoDAO.findByClienteId(idCliente);
            if (abbonamento != null) {
                abbonamentoDAO.delete(abbonamento.getIdAbbonamento());
            }

            // Poi elimina il cliente
            clienteDAO.delete(idCliente);
            
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new SQLException("Errore durante il rollback.", ex);
                }
            }
            throw new SQLException("Eliminazione cliente fallita.", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    throw new SQLException("Errore durante la chiusura della connessione.", ex);
                }
            }
        }
    }

    /**
     * Rinnova l'abbonamento di un cliente.
     * @param codiceCliente Codice cliente.
     * @param nuovaDataScadenza Nuova data di scadenza.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public void rinnovaAbbonamento(String codiceCliente, Date nuovaDataScadenza) throws SQLException {
         Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            Abbonamento abbonamento = abbonamentoDAO.findByCodiceCliente(codiceCliente);
            if (abbonamento != null) {
                abbonamento.setDataScadenza(nuovaDataScadenza);
                abbonamentoDAO.update(abbonamento);
                conn.commit();
            } else {
                throw new SQLException("Abbonamento non trovato per il cliente.");
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new SQLException("Errore durante il rollback.", ex);
                }
            }
            throw new SQLException("Rinnovo abbonamento fallito.", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    throw new SQLException("Errore durante la chiusura della connessione.", ex);
                }
            }
        }
    }

    /**
     * Recupera tutti i clienti.
     * @return Una lista di tutti i clienti.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public List<Cliente> getAllClienti() throws SQLException {
        return clienteDAO.findAll();
    }
    
    /**
     * Trova un cliente per ID.
     * @param id L'ID del cliente.
     * @return L'oggetto Cliente trovato, o null se non esiste.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public Cliente findById(int id) throws SQLException {
        return clienteDAO.findById(id);
    }

    /**
     * Trova un cliente per codice fiscale.
     * @param codiceFiscale Il codice fiscale del cliente.
     * @return L'oggetto Cliente trovato, o null se non esiste.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public Cliente findByCodiceFiscale(String codiceFiscale) throws SQLException {
        return clienteDAO.findByCodiceFiscale(codiceFiscale);
    }
    
    // Metodi privati per la logica di business
    public Date calcolaDataScadenza(String tipoAbbonamento) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        if ("Mensile".equalsIgnoreCase(tipoAbbonamento)) {
            cal.add(Calendar.MONTH, 1);
        } else if ("Annuale".equalsIgnoreCase(tipoAbbonamento)) {
            cal.add(Calendar.YEAR, 1);
        }
        return cal.getTime();
    }
    
    public double calcolaCosto(String tipoAbbonamento) {
        if ("Mensile".equalsIgnoreCase(tipoAbbonamento)) {
            return 30.0;
        } else if ("Annuale".equalsIgnoreCase(tipoAbbonamento)) {
            return 250.0;
        }
        return 0.0;
    }
}