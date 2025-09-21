package it.unina.bd.progpoo.gymmanager.model;

import java.util.Date;

public class Prenotazione {
    private int idPrenotazione;
    private Date dataPrenotazione;
    private String stato;
    private Cliente cliente;
    private Corso corso;

    // Questo è il costruttore completo (per oggetti dal DB)
    public Prenotazione(int idPrenotazione, Date dataPrenotazione, String stato, Cliente cliente, Corso corso) {
        this.idPrenotazione = idPrenotazione;
        this.dataPrenotazione = dataPrenotazione;
        this.stato = stato;
        this.cliente = cliente;
        this.corso = corso;
    }

    // 🟢 NUOVO COSTRUTTORE per le nuove prenotazioni (senza ID)
    public Prenotazione(Date dataPrenotazione, String stato, Cliente cliente, Corso corso) {
        this.dataPrenotazione = dataPrenotazione;
        this.stato = stato;
        this.cliente = cliente;
        this.corso = corso;
    }

    // ... getters e setters
    public int getIdPrenotazione() {
        return idPrenotazione;
    }

    public void setIdPrenotazione(int idPrenotazione) {
        this.idPrenotazione = idPrenotazione;
    }

    public Date getDataPrenotazione() {
        return dataPrenotazione;
    }

    public void setDataPrenotazione(Date dataPrenotazione) {
        this.dataPrenotazione = dataPrenotazione;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Corso getCorso() {
        return corso;
    }

    public void setCorso(Corso corso) {
        this.corso = corso;
    }
}