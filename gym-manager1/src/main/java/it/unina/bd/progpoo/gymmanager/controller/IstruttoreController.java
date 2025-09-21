package it.unina.bd.progpoo.gymmanager.controller;

import it.unina.bd.progpoo.gymmanager.dao.IstruttoreDAO;
import it.unina.bd.progpoo.gymmanager.model.Istruttore;
import java.sql.SQLException;
import java.util.List;
import java.util.Date;

public class IstruttoreController {

    private IstruttoreDAO istruttoreDAO;

    public IstruttoreController(IstruttoreDAO istruttoreDAO) {
        this.istruttoreDAO = istruttoreDAO;
    }

    public void creaIstruttore(Istruttore istruttore) throws SQLException {
        istruttoreDAO.insert(istruttore);
    }

    public Istruttore getIstruttoreById(int id) throws SQLException {
        return istruttoreDAO.findById(id);
    }

    public Istruttore getIstruttoreByNomeCognome(String nome, String cognome) throws SQLException {
        return istruttoreDAO.findByNomeCognome(nome, cognome);
    }
    
    public List<Istruttore> getAllIstruttori() throws SQLException {
        return istruttoreDAO.findAll();
    }

    public void updateIstruttore(Istruttore istruttore) throws SQLException {
        istruttoreDAO.update(istruttore);
    }

    public void deleteIstruttore(String matricola) throws SQLException {
        Istruttore istruttore = istruttoreDAO.findByMatricola(matricola);
        if (istruttore != null) {
            istruttoreDAO.delete(istruttore.getId());
        } else {
            throw new SQLException("Istruttore non trovato con la matricola: " + matricola);
        }
    }
    
    public Istruttore findByMatricola(String matricola) throws SQLException {
        return istruttoreDAO.findByMatricola(matricola);
    }
}