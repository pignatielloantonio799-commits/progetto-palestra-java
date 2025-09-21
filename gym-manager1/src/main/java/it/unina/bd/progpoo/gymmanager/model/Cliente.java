package it.unina.bd.progpoo.gymmanager.model;

import java.util.Date;

public class Cliente extends Persona {
    private String codiceFiscale;
    private Date dataNascita;
    private String codiceCliente;
    private Date dataIscrizione;
    private Date dataScadenzaAbbonamento;
    private String tipoAbbonamento;

    public Cliente() {
        super();
    }

    public Cliente(int id, String nome, String cognome, String email, String telefono,
                   String codiceFiscale, Date dataNascita, String codiceCliente,
                   Date dataIscrizione, Date dataScadenzaAbbonamento, String tipoAbbonamento) {
        super(id, nome, cognome, email, telefono);
        this.codiceFiscale = codiceFiscale;
        this.dataNascita = dataNascita;
        this.codiceCliente = codiceCliente;
        this.dataIscrizione = dataIscrizione;
        this.dataScadenzaAbbonamento = dataScadenzaAbbonamento;
        this.tipoAbbonamento = tipoAbbonamento;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }
    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }
    public Date getDataNascita() {
        return dataNascita;
    }
    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }
    public Date getDataIscrizione() {
        return dataIscrizione;
    }
    public void setDataIscrizione(Date dataIscrizione) {
        this.dataIscrizione = dataIscrizione;
    }
    public String getCodiceCliente() {
        return codiceCliente;
    }
    public void setCodiceCliente(String codiceCliente) {
        this.codiceCliente = codiceCliente;
    }
    public Date getDataScadenzaAbbonamento() {
        return dataScadenzaAbbonamento;
    }
    public void setDataScadenzaAbbonamento(Date dataScadenzaAbbonamento) {
        this.dataScadenzaAbbonamento = dataScadenzaAbbonamento;
    }
    public String getTipoAbbonamento() {
        return tipoAbbonamento;
    }
    public void setTipoAbbonamento(String tipoAbbonamento) {
        this.tipoAbbonamento = tipoAbbonamento;
    }
    
    public boolean isAbbonamentoAttivo() {
        return verificaValiditaAbbonamento();
    }

    public long getGiorniRimanenti() {
        if (dataScadenzaAbbonamento == null) {
            return 0;
        }
        long diff = this.dataScadenzaAbbonamento.getTime() - new Date().getTime();
        return diff / (1000 * 60 * 60 * 24);
    }

    public boolean verificaValiditaAbbonamento() {
        if (dataScadenzaAbbonamento == null) {
            return false;
        }
        return this.dataScadenzaAbbonamento.after(new Date());
    }
}