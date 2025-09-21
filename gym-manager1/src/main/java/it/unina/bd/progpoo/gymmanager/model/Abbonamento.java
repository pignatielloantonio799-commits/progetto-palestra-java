package it.unina.bd.progpoo.gymmanager.model;

import java.util.Date;

public class Abbonamento {
    private int idAbbonamento;
    private String tipoAbbonamento;
    private Date dataInizio;
    private Date dataScadenza;
    private double costo;
    private Cliente cliente;

    // Costruttore predefinito
    public Abbonamento() {
    }

    // Costruttore completo
    public Abbonamento(int idAbbonamento, String tipoAbbonamento, Date dataInizio, Date dataScadenza, double costo, Cliente cliente) {
        
        this.idAbbonamento = idAbbonamento;
        this.tipoAbbonamento = tipoAbbonamento;
        this.dataInizio = dataInizio;
        this.dataScadenza = dataScadenza;
        this.costo = costo;
        this.cliente = cliente;
    }

    // Getter e Setter
    public int getIdAbbonamento() {
        return idAbbonamento;
    }

    public void setIdAbbonamento(int idAbbonamento) {
        this.idAbbonamento = idAbbonamento;
    }

    public String getTipoAbbonamento() {
        return tipoAbbonamento;
    }

    public void setTipoAbbonamento(String tipoAbbonamento) {
        this.tipoAbbonamento = tipoAbbonamento;
    }

    public Date getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(Date dataInizio) {
        this.dataInizio = dataInizio;
    }

    public Date getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(Date dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}