package it.unina.bd.progpoo.gymmanager.dao;

import it.unina.bd.progpoo.gymmanager.model.SchedaAllenamento;
import java.sql.SQLException;

public interface SchedaAllenamentoDAO {
    void insert(SchedaAllenamento nuovaScheda) throws SQLException;
    SchedaAllenamento findByClienteId(int idCliente) throws SQLException;
}