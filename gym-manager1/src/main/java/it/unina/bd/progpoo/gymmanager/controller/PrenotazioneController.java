package it.unina.bd.progpoo.gymmanager.controller;

import it.unina.bd.progpoo.gymmanager.dao.AbbonamentoDAO;
import it.unina.bd.progpoo.gymmanager.dao.ClienteDAO;
import it.unina.bd.progpoo.gymmanager.dao.CorsoDAO;
import it.unina.bd.progpoo.gymmanager.dao.PrenotazioneDAO;
import exceptions.*;
import it.unina.bd.progpoo.gymmanager.model.Abbonamento;
import it.unina.bd.progpoo.gymmanager.model.Cliente;
import it.unina.bd.progpoo.gymmanager.model.Corso;
import it.unina.bd.progpoo.gymmanager.model.Prenotazione;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class PrenotazioneController {

    private final ClienteDAO clienteDAO;
    private final CorsoDAO corsoDAO;
    private final PrenotazioneDAO prenotazioneDAO;
    private final AbbonamentoDAO abbonamentoDAO;

    public PrenotazioneController(ClienteDAO clienteDAO, CorsoDAO corsoDAO, PrenotazioneDAO prenotazioneDAO, AbbonamentoDAO abbonamentoDAO) {
        this.clienteDAO = clienteDAO;
        this.corsoDAO = corsoDAO;
        this.prenotazioneDAO = prenotazioneDAO;
        this.abbonamentoDAO = abbonamentoDAO;
    }

    public void prenotaCorso(String codiceFiscale, int idCorso) 
            throws AbbonamentoScadutoException, PostiEsauritiException, SQLException {
        
        Cliente cliente = clienteDAO.findByCodiceFiscale(codiceFiscale);
        Corso corso = corsoDAO.findById(idCorso);

        if (cliente == null) {
            throw new IllegalArgumentException("Cliente non trovato.");
        }
        if (corso == null) {
            throw new IllegalArgumentException("Corso non trovato.");
        }

        Abbonamento abbonamento = abbonamentoDAO.findByClienteId(cliente.getId());
        if (abbonamento == null || abbonamento.getDataScadenza().before(new Date())) {
            throw new AbbonamentoScadutoException("Impossibile prenotare: abbonamento scaduto.");
        }

        // Il metodo isDisponibile è un'ottima logica di business.
        if (!corso.isDisponibile()) {
            throw new PostiEsauritiException("Posti esauriti per il corso selezionato.");
        }
        
        // 🟢 CORREZIONE: Crea l'oggetto Prenotazione usando il costruttore completo
        // Questo costruttore non ha l'ID perché verrà assegnato dal database.
        Prenotazione nuovaPrenotazione = new Prenotazione(new Date(), "CONFERMATA", cliente, corso);
        
        prenotazioneDAO.insert(nuovaPrenotazione);

        // La logica di aggiornamento dei posti è già nel metodo insert del PrenotazioneDAO,
        // che abbiamo corretto in precedenza. Quindi queste righe sono ridondanti e vanno rimosse.
        // corso.setPrenotazioniAttuali(corso.getPrenotazioniAttuali() + 1);
        // corsoDAO.update(corso);
    }
    
    public List<Prenotazione> visualizzaPrenotazioniPerCliente(int idCliente) throws SQLException {
        return prenotazioneDAO.findByCliente(idCliente);
    }
    
    public void cancellaPrenotazione(int idPrenotazione) throws SQLException {
        Prenotazione prenotazione = prenotazioneDAO.findById(idPrenotazione);
        
        if (prenotazione == null) {
            throw new IllegalArgumentException("Prenotazione non trovata.");
        }
        
        prenotazioneDAO.delete(idPrenotazione);
    }
}