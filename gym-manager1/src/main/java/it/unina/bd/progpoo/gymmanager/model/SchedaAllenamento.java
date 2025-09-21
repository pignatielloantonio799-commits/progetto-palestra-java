package it.unina.bd.progpoo.gymmanager.model;

import java.util.Date;
import java.util.List;

public class SchedaAllenamento {
    private int idScheda;
    private Date dataCreazione;
    private Date dataScadenza;
    private String obiettivo;
    private List<String> esercizi;
    private Cliente cliente;
    private Istruttore istruttore;
    
    /**
     * Costruttore completo per un oggetto SchedaAllenamento esistente,
     * recuperato dal database.
     */
    public SchedaAllenamento(int idScheda, Date dataCreazione, Date dataScadenza,
                             String obiettivo, List<String> esercizi, Cliente cliente, Istruttore istruttore) {
        this.idScheda = idScheda;
        this.dataCreazione = dataCreazione;
        this.dataScadenza = dataScadenza;
        this.obiettivo = obiettivo;
        this.esercizi = esercizi;
        this.cliente = cliente;
        this.istruttore = istruttore;
    }

    /**
     * Costruttore per creare una nuova Scheda di Allenamento.
     * L'ID non è richiesto perché verrà generato dal database.
     */
    public SchedaAllenamento(Date dataCreazione, Date dataScadenza,
                             String obiettivo, List<String> esercizi, Cliente cliente, Istruttore istruttore) {
        this.dataCreazione = dataCreazione;
        this.dataScadenza = dataScadenza;
        this.obiettivo = obiettivo;
        this.esercizi = esercizi;
        this.cliente = cliente;
        this.istruttore = istruttore;
    }
    
    // Getter e Setter
    
    public int getIdScheda() {
        return idScheda;
    }

    public void setIdScheda(int idScheda) {
        this.idScheda = idScheda;
    }

    public Date getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(Date dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Date getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(Date dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public String getObiettivo() {
        return obiettivo;
    }

    public void setObiettivo(String obiettivo) {
        this.obiettivo = obiettivo;
    }

    public List<String> getEsercizi() {
        return esercizi;
    }

    public void setEsercizi(List<String> esercizi) {
        this.esercizi = esercizi;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Istruttore getIstruttore() {
        return istruttore;
    }

    public void setIstruttore(Istruttore istruttore) {
        this.istruttore = istruttore;
    }

    public boolean isValida() {
        return this.dataScadenza != null && this.dataScadenza.after(new Date());
    }
}