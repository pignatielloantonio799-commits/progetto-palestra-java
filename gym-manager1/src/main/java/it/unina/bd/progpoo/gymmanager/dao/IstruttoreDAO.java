package it.unina.bd.progpoo.gymmanager.dao;

import it.unina.bd.progpoo.gymmanager.model.Istruttore;
import java.sql.SQLException;
import java.util.List;

public interface IstruttoreDAO {
    int insert(Istruttore istruttore) throws SQLException;
    Istruttore findById(int id) throws SQLException;
    List<Istruttore> findAll() throws SQLException;
    void update(Istruttore istruttore) throws SQLException;
    void delete(int id) throws SQLException;
    Istruttore findByNomeCognome(String nome, String cognome) throws SQLException;
    // 🟢 Aggiungi questo metodo
    Istruttore findByMatricola(String matricola) throws SQLException;
}