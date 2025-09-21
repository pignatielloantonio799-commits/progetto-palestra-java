package it.unina.bd.progpoo.gymmanager.dao;

import it.unina.bd.progpoo.gymmanager.model.Accesso;
import java.sql.SQLException;

public interface AccessoDAO {
    void insert(Accesso nuovoAccesso) throws SQLException;
}
