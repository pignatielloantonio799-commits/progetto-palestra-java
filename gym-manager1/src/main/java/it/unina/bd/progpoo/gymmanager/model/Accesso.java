package it.unina.bd.progpoo.gymmanager.model;

import java.util.Date;

public class Accesso {

    private int idAccesso;
    private Date timestampIngresso;
    private Date timestampUscita;
    private Cliente cliente;

    /**
     * Costruttore per registrare un nuovo ingresso in palestra.
     * Questo costruttore non ha l'ID perché viene generato dal database.
     *
     * @param timestampIngresso L'orario di ingresso del cliente.
     * @param cliente L'oggetto Cliente che effettua l'accesso.
     */
    public Accesso(Date timestampIngresso, Cliente cliente) {
        this.timestampIngresso = timestampIngresso;
        this.cliente = cliente;
    }

    /**
     * Costruttore completo per creare un oggetto Accesso già esistente,
     * recuperato dal database (quindi con ID e orario di uscita).
     *
     * @param idAccesso L'ID univoco dell'accesso nel database.
     * @param timestampIngresso L'orario di ingresso.
     * @param timestampUscita L'orario di uscita.
     * @param cliente L'oggetto Cliente associato.
     */
    public Accesso(int idAccesso, Date timestampIngresso, Date timestampUscita, Cliente cliente) {
        this.idAccesso = idAccesso;
        this.timestampIngresso = timestampIngresso;
        this.timestampUscita = timestampUscita;
        this.cliente = cliente;
    }

    public int getIdAccesso() {
        return idAccesso;
    }

    public void setIdAccesso(int idAccesso) {
        this.idAccesso = idAccesso;
    }

    public Date getTimestampIngresso() {
        return timestampIngresso;
    }

    public void setTimestampIngresso(Date timestampIngresso) {
        this.timestampIngresso = timestampIngresso;
    }

    public Date getTimestampUscita() {
        return timestampUscita;
    }

    public void setTimestampUscita(Date timestampUscita) {
        this.timestampUscita = timestampUscita;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}