package it.unina.bd.progpoo.gymmanager.controller;

import it.unina.bd.progpoo.gymmanager.dao.ClienteDAO;
import it.unina.bd.progpoo.gymmanager.dao.IstruttoreDAO;
import it.unina.bd.progpoo.gymmanager.dao.SchedaAllenamentoDAO;
import it.unina.bd.progpoo.gymmanager.model.Cliente;
import it.unina.bd.progpoo.gymmanager.model.Istruttore;
import it.unina.bd.progpoo.gymmanager.model.SchedaAllenamento;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class SchedaAllenamentoController {
    
    private final SchedaAllenamentoDAO schedaDAO;
    private final ClienteDAO clienteDAO;
    private final IstruttoreDAO istruttoreDAO;

    /**
     * Costruttore con Dependency Injection per un accoppiamento debole.
     */
    public SchedaAllenamentoController(SchedaAllenamentoDAO schedaDAO, ClienteDAO clienteDAO, IstruttoreDAO istruttoreDAO) {
        this.schedaDAO = schedaDAO;
        this.clienteDAO = clienteDAO;
        this.istruttoreDAO = istruttoreDAO;
    }

    /**
     * Crea una nuova scheda di allenamento per un cliente.
     *
     * @param codiceFiscale Cliente al quale è associata la scheda.
     * @param matricolaIstruttore Istruttore che crea la scheda.
     * @param dataScadenza Data di scadenza della scheda.
     * @param obiettivo Obiettivo di allenamento.
     * @param esercizi Lista di esercizi.
     * @throws SQLException In caso di errore di accesso al database.
     * @throws IllegalArgumentException Se cliente o istruttore non vengono trovati.
     */
    public void creaScheda(String codiceFiscale, String matricolaIstruttore, Date dataScadenza,
                             String obiettivo, List<String> esercizi) throws SQLException {
        // Cerca cliente e istruttore tramite i loro codici
        Cliente cliente = clienteDAO.findByCodiceFiscale(codiceFiscale);
        Istruttore istruttore = istruttoreDAO.findByMatricola(matricolaIstruttore);

        if (cliente == null || istruttore == null) {
            throw new IllegalArgumentException("Cliente o Istruttore non trovati.");
        }

        // Crea il nuovo oggetto SchedaAllenamento usando il costruttore senza ID
        SchedaAllenamento nuovaScheda = new SchedaAllenamento(new Date(), dataScadenza,
                                                              obiettivo, esercizi, cliente, istruttore);
        schedaDAO.insert(nuovaScheda);
    }

    /**
     * Visualizza la scheda di allenamento di un cliente.
     *
     * @param idCliente ID del cliente.
     * @return L'oggetto SchedaAllenamento.
     * @throws SQLException In caso di errore di accesso al database.
     */
    public SchedaAllenamento visualizzaScheda(int idCliente) throws SQLException {
        SchedaAllenamento scheda = schedaDAO.findByClienteId(idCliente);
        return scheda;
    }
}