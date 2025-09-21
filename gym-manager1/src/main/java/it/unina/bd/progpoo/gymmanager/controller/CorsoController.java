package it.unina.bd.progpoo.gymmanager.controller;

import it.unina.bd.progpoo.gymmanager.dao.CorsoDAO;
import it.unina.bd.progpoo.gymmanager.dao.IstruttoreDAO;
import it.unina.bd.progpoo.gymmanager.dao.PrenotazioneDAO;
import it.unina.bd.progpoo.gymmanager.dao.impl.CorsoDAOImpl;
import it.unina.bd.progpoo.gymmanager.dao.impl.IstruttoreDAOImpl;
import it.unina.bd.progpoo.gymmanager.dao.impl.PrenotazioneDAOImpl;
import it.unina.bd.progpoo.gymmanager.model.Corso;
import it.unina.bd.progpoo.gymmanager.model.Istruttore;
import it.unina.bd.progpoo.gymmanager.model.Cliente;
import it.unina.bd.progpoo.gymmanager.model.Prenotazione;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.sql.Time;

public class CorsoController {

    private CorsoDAO corsoDAO;
    private IstruttoreDAO istruttoreDAO;
    private PrenotazioneDAO prenotazioneDAO;

    // 🟢 Aggiungi questo costruttore corretto 🟢
    public CorsoController(CorsoDAO corsoDAO, IstruttoreDAO istruttoreDAO, PrenotazioneDAO prenotazioneDAO) {
        this.corsoDAO = corsoDAO;
        this.istruttoreDAO = istruttoreDAO;
        this.prenotazioneDAO = prenotazioneDAO;
    }
    
    // Puoi rimuovere il costruttore vuoto se non lo usi in altre parti del codice
    /*
    public CorsoController() {
        this.corsoDAO = new CorsoDAOImpl();
        this.istruttoreDAO = new IstruttoreDAOImpl();
        this.prenotazioneDAO = new PrenotazioneDAOImpl();
    }
    */
    
    // Metodo per creare un nuovo corso
    public void creaNuovoCorso(String nome, String descrizione, Date orario, int durata,
                               int maxPartecipanti, String sala, String matricolaIstruttore) throws SQLException {
        Istruttore istruttore = istruttoreDAO.findByMatricola(matricolaIstruttore);
        if (istruttore == null) {
            throw new IllegalArgumentException("Errore: Istruttore non trovato con la matricola fornita.");
        }

        Corso nuovoCorso = new Corso();
        nuovoCorso.setNome(nome);
        nuovoCorso.setDescrizione(descrizione);
        nuovoCorso.setOrario(new Time(orario.getTime()));
        nuovoCorso.setDurata(durata);
        nuovoCorso.setMaxPartecipanti(maxPartecipanti);
        nuovoCorso.setSala(sala);
        nuovoCorso.setIstruttore(istruttore);
        nuovoCorso.setPrenotazioniAttuali(0);
        corsoDAO.insert(nuovoCorso);
    }
    
    // Metodo per prenotare un corso
    public void prenotaCorso(Cliente cliente, Corso corso) throws SQLException {
        if (!corso.isDisponibile()) {
            throw new IllegalStateException("Corso al completo. Impossibile prenotare.");
        }
        
        Prenotazione nuovaPrenotazione = new Prenotazione();
        nuovaPrenotazione.setDataPrenotazione(new Date());
        nuovaPrenotazione.setStato("ATTIVA");
        nuovaPrenotazione.setCliente(cliente);
        nuovaPrenotazione.setCorso(corso);
        
        prenotazioneDAO.insert(nuovaPrenotazione);
        
        corso.setPrenotazioniAttuali(corso.getPrenotazioniAttuali() + 1);
        corsoDAO.update(corso);
    }
    
    // Metodo per cancellare una prenotazione logicamente (impostando lo stato)
    public void cancellaPrenotazione(int idPrenotazione) throws SQLException {
        Prenotazione prenotazione = prenotazioneDAO.findById(idPrenotazione);
        if (prenotazione == null) {
            throw new IllegalArgumentException("Prenotazione non trovata.");
        }
        
        prenotazioneDAO.updateStato(idPrenotazione, "ANNULLATA");
        
        Corso corso = prenotazione.getCorso();
        corso.setPrenotazioniAttuali(corso.getPrenotazioniAttuali() - 1);
        corsoDAO.update(corso);
    }
    
    // Metodo per aggiornare un corso esistente
    public void updateCorso(Corso corso) throws SQLException {
        corsoDAO.update(corso);
    }
    
    // Metodo per eliminare un corso
    public void deleteCorso(int idCorso) throws SQLException {
        corsoDAO.delete(idCorso);
    }
    
    // Metodo per trovare un corso per ID
    public Corso findById(int idCorso) throws SQLException {
        return corsoDAO.findById(idCorso);
    }

    // Metodo per visualizzare tutti i corsi disponibili
    public List<Corso> visualizzaTuttiICorsi() throws SQLException {
        return corsoDAO.findAll();
    }
    
    public List<Prenotazione> visualizzaPrenotazioniPerCliente(int idCliente) throws SQLException {
        return prenotazioneDAO.findByCliente(idCliente);
    }
    
    public List<Prenotazione> visualizzaPrenotazioniPerCorso(int idCorso) throws SQLException {
        return prenotazioneDAO.findByCorso(idCorso);
    }
}