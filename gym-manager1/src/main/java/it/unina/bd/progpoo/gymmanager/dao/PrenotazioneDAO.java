package it.unina.bd.progpoo.gymmanager.dao;

import it.unina.bd.progpoo.gymmanager.model.Prenotazione;
import java.sql.SQLException;
import java.util.List;

public interface PrenotazioneDAO {
    void insert(Prenotazione prenotazione) throws SQLException;
    void delete(int idPrenotazione) throws SQLException;
    void deleteByClienteId(int idCliente) throws SQLException;

    
    // Metodo aggiunto per aggiornare lo stato di una prenotazione
    void updateStato(int idPrenotazione, String nuovoStato) throws SQLException;
    
    Prenotazione findById(int idPrenotazione) throws SQLException;
    List<Prenotazione> findByCliente(int idCliente) throws SQLException;
    List<Prenotazione> findByCorso(int idCorso) throws SQLException;
}