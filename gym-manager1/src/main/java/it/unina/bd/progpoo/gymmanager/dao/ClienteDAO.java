package it.unina.bd.progpoo.gymmanager.dao;

import it.unina.bd.progpoo.gymmanager.model.Cliente;
import java.sql.SQLException;
import java.util.List;

public interface ClienteDAO {
    int insert(Cliente cliente) throws SQLException;
    Cliente findById(int idCliente) throws SQLException;
    Cliente findByCodiceFiscale(String codiceFiscale) throws SQLException;
    Cliente findByCodiceCliente(String codiceCliente) throws SQLException;
    void update(Cliente cliente) throws SQLException;
    void delete(int idCliente) throws SQLException;
    List<Cliente> findAll() throws SQLException;
}

