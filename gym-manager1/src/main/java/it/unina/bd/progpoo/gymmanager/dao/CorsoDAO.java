package it.unina.bd.progpoo.gymmanager.dao;

import it.unina.bd.progpoo.gymmanager.model.Corso;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

public interface CorsoDAO {

    int insert(Corso corso) throws SQLException;
    Corso findById(int idCorso) throws SQLException;
    List<Corso> findAll() throws SQLException;
    void update(Corso corso) throws SQLException;
    void delete(int idCorso) throws SQLException;

    // 🟢 Metodo mancante aggiunto
    List<Corso> visualizzaCorsiPerCliente(int idCliente) throws SQLException;
    
        Corso findBySalaAndOrario(String sala, Time orario) throws SQLException;
    }

