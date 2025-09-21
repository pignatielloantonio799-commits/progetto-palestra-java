package it.unina.bd.progpoo.gymmanager.controller;

import it.unina.bd.progpoo.gymmanager.dao.AbbonamentoDAO;
import it.unina.bd.progpoo.gymmanager.dao.AccessoDAO;
import it.unina.bd.progpoo.gymmanager.dao.ClienteDAO;
import it.unina.bd.progpoo.gymmanager.model.Abbonamento;
import it.unina.bd.progpoo.gymmanager.model.Accesso;
import it.unina.bd.progpoo.gymmanager.model.Cliente;
import exceptions.AbbonamentoScadutoException;
import java.sql.SQLException;
import java.util.Date;

public class AccessoController {

    private final ClienteDAO clienteDAO;
    private final AccessoDAO accessoDAO;
    private final AbbonamentoDAO abbonamentoDAO; // Aggiunto per la validazione

    // Costruttore che riceve le dipendenze
    public AccessoController(ClienteDAO clienteDAO, AccessoDAO accessoDAO, AbbonamentoDAO abbonamentoDAO) {
        this.clienteDAO = clienteDAO;
        this.accessoDAO = accessoDAO;
        this.abbonamentoDAO = abbonamentoDAO;
    }

    public void registraIngresso(String codiceCliente) throws AbbonamentoScadutoException, SQLException {
        // 1. Cerca il cliente tramite il codice fiscale
        Cliente cliente = clienteDAO.findByCodiceFiscale(codiceCliente);

        if (cliente == null) {
            throw new IllegalArgumentException("Cliente non trovato.");
        }

        // 2. Verifica la validità dell'abbonamento del cliente
        Abbonamento abbonamento = abbonamentoDAO.findByClienteId(cliente.getId());
        if (abbonamento == null || abbonamento.getDataScadenza().before(new Date())) {
            throw new AbbonamentoScadutoException("Accesso negato: abbonamento scaduto.");
        }

        // 3. Crea l'oggetto Accesso e inseriscilo nel database
        Accesso nuovoAccesso = new Accesso(new Date(), cliente);
        accessoDAO.insert(nuovoAccesso);
    }
}