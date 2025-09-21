package it.unina.bd.progpoo.gymmanager.dao;

import it.unina.bd.progpoo.gymmanager.model.Abbonamento;
import java.sql.SQLException;

public interface AbbonamentoDAO {
    void insert(Abbonamento abbonamento) throws SQLException;
    Abbonamento findById(int idAbbonamento) throws SQLException;
    Abbonamento findByClienteId(int idCliente) throws SQLException;
    void update(Abbonamento abbonamento) throws SQLException;
    void delete(int idAbbonamento) throws SQLException;

    // 🟢 NUOVO METODO: Cercare un abbonamento usando il codice cliente
    Abbonamento findByCodiceCliente(String codiceCliente) throws SQLException;
}